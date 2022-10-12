import { Component, Inject, OnInit } from '@angular/core';
import { UsersPaginationResponse } from '../../../http/response/UsersPaginationResponse';
import { PageEvent } from '@angular/material/paginator';
import { UserService } from '../../../service/user/user.service';
import { Channel } from '../../../model/Channel';
import { Subscription } from 'rxjs';
import { AuthenticationService } from '../../../service/authentication/authentication.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ChannelService } from '../../../service/channel/channel.service';
import { DialogService } from '../../../service/dialog/dialog.service';

@Component({
  selector: 'app-remove-users',
  templateUrl: './remove-users.component.html',
  styleUrls: ['./remove-users.component.css'],
})
export class RemoveUsersComponent implements OnInit {
  dataSource: UsersPaginationResponse = null;
  pageEvent: PageEvent;
  columnsToDisplay: string[] = ['firstName', 'lastName', 'email'];

  channel: Channel;
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

  onPaginateChange($event: PageEvent): void {
    const page = $event.pageIndex;
    const size = $event.pageSize;
    this.userService
      .findAllUsersInChannel(
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

  removeUsersFromChannel(): void {
    for (const id of this.usersId) {
      this.channelService.deleteUserChannel(id, this.channelId).subscribe(
        (value) => {
          this.dialogService.openDialogSuccess(
            `Users were successfully removed from the channel`
          );
        },
        (error) => {
          this.dialogService.openDialogError(
            `An error occurred while removing users from the channel`
          );
        }
      );
    }
  }

  private initDataSource(): void {
    this.userService
      .findAllUsersInChannel(
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
}
