import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SuccessDialogComponent } from '../../components/success-dialog/success-dialog.component';
import { ErrorDialogComponent } from '../../components/error-dialog/error-dialog.component';

@Injectable({
  providedIn: 'root',
})
export class DialogService {
  constructor(private matDialog: MatDialog) {}

  openDialogSuccess(message: string): void {
    const dialogRef = this.matDialog.open(SuccessDialogComponent, {
      width: '500px',
    });
    dialogRef.componentInstance.message = message;
    dialogRef.afterClosed().subscribe(() => {});
  }

  openDialogError(message: string): void {
    const dialogRef = this.matDialog.open(ErrorDialogComponent, {
      width: '500px',
    });
    dialogRef.componentInstance.message = message;
    dialogRef.afterClosed().subscribe(() => {});
  }
}
