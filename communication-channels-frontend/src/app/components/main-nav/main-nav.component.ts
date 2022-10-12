import { Component, OnDestroy, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable, Subscription } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { User } from '../../model/User';
import { AuthenticationService } from '../../service/authentication/authentication.service';
import { Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { Searchable } from '../../model/Searchable';
import { SearchableService } from '../../service/searchable/searchable.service';
import { Post } from '../../model/Post';
import { Channel } from '../../model/Channel';
import { ChannelService } from '../../service/channel/channel.service';
import { PostService } from '../../service/post/post.service';

export interface State {
  flag: string;
  name: string;
  population: string;
}

@Component({
  selector: 'app-main-nav',
  templateUrl: './main-nav.component.html',
  styleUrls: ['./main-nav.component.css'],
})
export class MainNavComponent implements OnInit, OnDestroy {
  user: User;

  searchableCtrl = new FormControl();
  filteredSearchable$: Observable<Searchable[]>;
  searchables: Searchable[] = [];
  subs: Subscription[] = [];

  opened = true;
  isHandset$: Observable<boolean> = this.breakpointObserver
    .observe(Breakpoints.Handset)
    .pipe(
      map((result) => result.matches),
      shareReplay()
    );

  constructor(
    private breakpointObserver: BreakpointObserver,
    private authenticationService: AuthenticationService,
    private router: Router,
    private searchableService: SearchableService,
    private channelService: ChannelService,
    private postService: PostService
  ) {}

  ngOnInit(): void {
    this.user = this.authenticationService.getUserFromLocalCache();
  }

  logout(): void {
    this.authenticationService.logout();
    this.router.navigate(['/login']).then();
  }

  letterEntered(filterValue: string): void {
    if (filterValue.length === 0) {
      this.searchables = [];
      return;
    }
    this.subs.push(
      this.searchableService.getChannelsAndPosts(filterValue).subscribe(
        (searchableList) => {
          this.searchables = searchableList.map((value) => {
            if ('name' in value) {
              return Object.assign(new Channel(), value);
            }
            if ('title' in value) {
              return Object.assign(new Post(), value);
            }
          });
        },
        (error) => console.dir(error)
      )
    );
  }

  ngOnDestroy(): void {
    this.subs.forEach((value) => value.unsubscribe());
  }

  search(searchable: Searchable): void {
    if ('name' in searchable) {
      // @ts-ignore
      this.router.navigate([`/channel/${searchable.getId()}`]).then();
      this.searchables = [];
      const autocompleteField = (document.getElementById(
        'autocompleteField'
        // @ts-ignore
      ).value = '');
    }
    if ('title' in searchable) {
      this.channelService
        // @ts-ignore
        .getChannelIdByPostId(searchable.getId())
        .then((id) => {
          // @ts-ignore
          this.postService.savePostIdForNavigation(searchable.getId());
          this.router.navigate([`/channel/${id}`]).then();
          this.searchables = [];
          // @ts-ignore
          document.getElementById('autocompleteField').value = '';
        });
    }
  }

  isAddUserAllowed(): boolean {
    return this.user.role.userPermissions
      .map((value) => value.name)
      .includes('user:write');
  }

  isMakeChannelAllowed(): boolean {
    return this.user.role.userPermissions
      .map((value) => value.name)
      .includes('channel:write');
  }
}
