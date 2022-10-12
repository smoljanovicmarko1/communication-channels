import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { AuthenticationService } from '../../service/authentication/authentication.service';
import { ChannelService } from '../../service/channel/channel.service';
import { MatDialog } from '@angular/material/dialog';
import { ErrorDialogComponent } from '../../components/error-dialog/error-dialog.component';

@Injectable({
  providedIn: 'root',
})
export class RoleGuard implements CanActivate {
  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private channelService: ChannelService,
    private matDialog: MatDialog
  ) {}

  async canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<any> {
    if (
      next.url.map((value) => value.path).includes('add-user') &&
      this.authService
        .getUserFromLocalCache()
        .role.userPermissions.map((value) => value.name)
        .includes('user:write')
    ) {
      return true;
    }

    if (
      (next.url.map((value) => value.path).includes('make-channel') ||
        next.url.map((value) => value.path).includes('edit-channel')) &&
      this.authService
        .getUserFromLocalCache()
        .role.userPermissions.map((value) => value.name)
        .includes('channel:write')
    ) {
      return true;
    }

    if (next.url.map((value) => value.path).includes('channel')) {
      let flag = false;
      await this.channelService
        .isUserInChannel(Number(next.url[1].path))
        .then((value) => {
          if (value === false) {
            this.router.navigate(['/home']).then();
            return false;
          }
          if (value === true) {
            flag = value;
          }
        })
        .catch((reason) => {
          this.openDialog('Could not load channel');
        });
      return flag;
    }
    this.router.navigate(['/home']).then();
    return false;
  }

  openDialog(message: string): void {
    const dialogRef = this.matDialog.open(ErrorDialogComponent, {
      width: '500px',
    });

    dialogRef.componentInstance.message = message;

    dialogRef.afterClosed().subscribe(() => {});
  }
}
