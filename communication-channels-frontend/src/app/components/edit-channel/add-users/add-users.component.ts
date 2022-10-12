import { Component, Inject, OnInit } from '@angular/core';
import { UsersPaginationResponse } from '../../../http/response/UsersPaginationResponse';
import { PageEvent } from '@angular/material/paginator';
import { UserService } from '../../../service/user/user.service';
import { Channel } from '../../../model/Channel';
import { Subscription } from 'rxjs';
import { AuthenticationService } from '../../../service/authentication/authentication.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { UserChannel } from '../../../model/UserChannel';
import { ChannelRole } from '../../../model/ChannelRole';
import { User } from '../../../model/User';
import { ChannelService } from '../../../service/channel/channel.service';
import { DialogService } from '../../../service/dialog/dialog.service';

@Component({
  selector: 'app-add-users',
  templateUrl: './add-users.component.html',
  styleUrls: ['./add-users.component.css'],
})
export class AddUsersComponent implements OnInit {
  dataSource: UsersPaginationResponse = null;
  pageEvent: PageEvent;
  columnsToDisplay: string[] = ['firstName', 'lastName', 'email'];

  subs: Subscription[] = [];

  usersId: Set<number> = new Set<number>();

  constructor(
    private userService: UserService,
    private authenticationService: AuthenticationService,
    private channelService: ChannelService,
    private dialogService: DialogService,
    @Inject(MAT_DIALOG_DATA) private channelId
  ) {}

  ngOnInit(): void {
    this.initDataSource();
  }

  initDataSource(): void {
    this.userService
      .findAllUsersNotInChannel(
        this.channelId,
        this.authenticationService.getUserFromLocalCache().id,
        0,
        10
      )
      .subscribe(
        (value: UsersPaginationResponse) => {
          this.dataSource = value;
        },
        (error) => {}
      );
  }

  onPaginateChange($event: PageEvent): void {
    const page = $event.pageIndex;
    const size = $event.pageSize;
    this.userService
      .findAllUsersNotInChannel(
        this.channelId,
        this.authenticationService.getUserFromLocalCache().id,
        page,
        size
      )
      .subscribe(
        (value: UsersPaginationResponse) => {
          this.dataSource = value;
        },
        (error) => {}
      );
  }

  addUser(row: any): void {
    if (this.usersId.has(row.id)) {
      this.usersId.delete(row.id);
    } else {
      this.usersId.add(row.id);
    }
  }

  addUsersToChannel(): void {
    const userChannel: UserChannel = new UserChannel();
    const channelRole: ChannelRole = new ChannelRole();
    channelRole.id = 1;
    userChannel.channelRole = channelRole;
    for (const id of this.usersId) {
      const user: User = new User();
      const channel: Channel = new Channel();
      user.id = id;
      userChannel.user = user;
      channel.id = this.channelId;
      userChannel.channel = channel;
      this.channelService.saveUserChannel(userChannel).subscribe(
        (value) => {
          this.dialogService.openDialogSuccess(
            `Users were successfully added to the channel`
          );
        },
        (error) => {
          this.dialogService.openDialogError(
            `An error occurred while adding users to the channel`
          );
        }
      );
    }
  }
}
