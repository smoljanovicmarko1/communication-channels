import { Component, OnDestroy, OnInit } from '@angular/core';
import { Channel } from '../../model/Channel';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { ChannelService } from '../../service/channel/channel.service';
import { AuthenticationService } from '../../service/authentication/authentication.service';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-channels',
  templateUrl: './channels.component.html',
  styleUrls: ['./channels.component.css'],
})
export class ChannelsComponent implements OnInit, OnDestroy {
  channels: Channel[];
  channelsError = false;
  channelLoading = true;
  subs: Subscription[] = [];

  constructor(
    private router: Router,
    private channelService: ChannelService,
    private authenticationService: AuthenticationService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.subs.push(
      this.channelService
        .getChannelsForUser(
          this.authenticationService.getUserFromLocalCache().id
        )
        .subscribe(
          (value) => {
            this.channels = value;
            this.channels?.forEach((channel) =>
              this.subs.push(
                this.channelService
                  .getProfilePictureById(channel.id)
                  .subscribe((data) => {
                    const objectURL = URL.createObjectURL(data);
                    channel.profilePicture =
                      this.sanitizer.bypassSecurityTrustUrl(objectURL);
                    this.channelLoading = false;
                  })
              )
            );
            this.channelLoading = false;
          },
          (error) => {
            this.channelsError = true;
            this.channelLoading = false;
          }
        )
    );
  }

  ngOnDestroy(): void {
    this.subs.forEach((value) => value.unsubscribe());
  }
}
