import { AfterViewChecked, Component, OnDestroy, OnInit } from '@angular/core';
import { ChannelService } from '../../../service/channel/channel.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Channel } from '../../../model/Channel';
import { switchMap } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { PostNewComponent } from '../../post/post-new/post-new.component';
import { Post } from '../../../model/Post';
import { PostService } from '../../../service/post/post.service';
import { AuthenticationService } from '../../../service/authentication/authentication.service';
import { SuccessDialogComponent } from '../../success-dialog/success-dialog.component';
import { CHANNEL_DELETE } from '../../../constants/permission';
import { DialogService } from '../../../service/dialog/dialog.service';

@Component({
  selector: 'app-channel',
  templateUrl: './channel.component.html',
  styleUrls: ['./channel.component.css'],
})
export class ChannelComponent implements OnInit, OnDestroy, AfterViewChecked {
  channel: Channel;
  subChannels: Channel[] = [];
  subs: Subscription[] = [];

  constructor(
    private channelService: ChannelService,
    private postService: PostService,
    private authService: AuthenticationService,
    private route: ActivatedRoute,
    private router: Router,
    private matDialog: MatDialog,
    private dialogService: DialogService
  ) {}

  ngOnInit(): void {
    this.subs.push(
      this.route.paramMap
        .pipe(
          switchMap((params) => {
            const id = params.get('id');
            return this.channelService.getById(parseInt(id, 10));
          })
        )
        .subscribe(
          (value) => {
            value.posts.sort((postA, postB) =>
              postA.dateCreated > postB.dateCreated ? -1 : 1
            );
            this.channel = value;
          },
          (error) => {
            console.log(error);
          }
        )
    );
    this.subs.push(
      this.route.paramMap
        .pipe(
          switchMap((params) => {
            const id = params.get('id');
            return this.channelService.getSubChannelsForUser(
              parseInt(id, 10),
              this.authService.getUserFromLocalCache().id
            );
          })
        )
        .subscribe((value) => {
          this.subChannels = value;
        })
    );
  }

  ngAfterViewChecked(): void {
    setTimeout(() => {
      const postIdForNavigation = this.postService.getPostIdForNavigation();
      if (postIdForNavigation > 0) {
        document.getElementById(String(postIdForNavigation)).scrollIntoView();
        this.postService.savePostIdForNavigation(-1);
      }
    }, 1000);
  }

  openNewPostDialog(): void {
    const refDialog = this.matDialog.open(PostNewComponent, {
      height: '500px',
      width: '400px',
    });

    refDialog.afterClosed().subscribe(
      (result) => {
        if (result === undefined) {
          return;
        }
        const post: Post = new Post();
        post.title = result.title;
        post.body = result.body;
        this.subs.push(
          this.postService.savePost(this.channel, post).subscribe(
            (savedPost) => {
              savedPost.filesToUpload = result.files; // files that were chosen while new post is created
              this.channel.posts.unshift(savedPost);
              this.openDialog('Post Created Successfully');
            },
            (error) => {
              this.openDialog('An error occurred while creating the post');
            }
          )
        );
      },
      (error) => {}
    );
  }

  openDialog(message: string): void {
    const dialogRef = this.matDialog.open(SuccessDialogComponent, {
      width: '500px',
    });

    dialogRef.componentInstance.message = message;

    dialogRef.afterClosed().subscribe(() => {});
  }

  ngOnDestroy(): void {
    this.subs.forEach((value) => value.unsubscribe());
  }

  isPostingAllowed(): boolean {
    if (
      this.channel.communicationDirection.name ===
      this.channelService.BIDIRECTIONAL
    ) {
      return true;
    }
    const user = this.authService.getUserFromLocalCache();
    const userChannelOwner = this.channel.userChannels.find(
      (value) =>
        value.user.id === user.id &&
        value.channelRole.name === this.channelService.OWNER
    );
    return userChannelOwner !== undefined;
  }

  isHisChannel(): boolean {
    const user = this.authService.getUserFromLocalCache();
    const userChannelOwner = this.channel.userChannels.find(
      (value) =>
        value.user.id === user.id &&
        value.channelRole.name === this.channelService.OWNER
    );
    return userChannelOwner !== undefined;
  }

  deleteChannel(id: number): void {
    this.subs.push(
      this.channelService.deleteChannel(id).subscribe(
        (value) => {
          this.dialogService.openDialogSuccess(
            `Channel ${value.name} has been deleted successfully`
          );
          this.router.navigate([`/channels`]).then();
        },
        (error) => {
          this.dialogService.openDialogError(
            'An error occurred while deleting channel'
          );
        }
      )
    );
  }

  hasChannelDeletePermission(): boolean {
    return this.authService.hasPermission(CHANNEL_DELETE);
  }
}
