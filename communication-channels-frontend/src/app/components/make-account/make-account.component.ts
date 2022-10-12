// noinspection DuplicatedCode

import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Subscription } from 'rxjs';
import { UserService } from '../../service/user/user.service';
import { AuthenticationService } from '../../service/authentication/authentication.service';
import { DomSanitizer } from '@angular/platform-browser';
import { User } from '../../model/User';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component';
import * as bcrypt from 'bcryptjs';

@Component({
  selector: 'app-make-account',
  templateUrl: './make-account.component.html',
  styleUrls: ['./make-account.component.css'],
})
export class MakeAccountComponent implements OnInit, OnDestroy {
  public form: FormGroup;
  public user: User;
  public token: string;
  public showDetails = false;
  public passwordStrength = 0;
  private subs: Subscription[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private authenticationService: AuthenticationService,
    private sanitizer: DomSanitizer,
    private activatedRoute: ActivatedRoute,
    private dialogService: MatDialog,
    private router: Router
  ) {}

  private static strongPasswordValidator(
    control: AbstractControl
  ): { [key: string]: boolean } | null {
    const password = control.value;
    if (
      password.match('[a-z]')?.length > 0 &&
      password.match('[0-9]')?.length > 0 &&
      password.match('[a-z]')?.length > 0 &&
      password.match('[!@#$%^&*(),.?":{}|<>]')?.length > 0 &&
      password.length >= 8
    ) {
      return null;
    }
    return { weak: true };
  }

  ngOnInit(): void {
    this.subs.push(
      this.activatedRoute.paramMap.subscribe((paramMap) => {
        this.token = String(paramMap.get('hash'));
        const id = Number(paramMap.get('id'));
        this.authenticationService
          .verifyActivationToken(this.token, id)
          .subscribe(
            (user) => {
              this.user = user;
              this.initAccountActivation();
            },
            (error) => {
              this.router.navigateByUrl('/error').then();
            }
          );
      })
    );
  }

  public initAccountActivation(): void {
    this.form = this.formBuilder.group({
      firstName: [this.user.firstName, [Validators.required]],
      lastName: [this.user.lastName, [Validators.required]],
      username: [this.user.username, [Validators.required]],
      phone: [this.user.phone],
      password: [
        '',
        [Validators.required, MakeAccountComponent.strongPasswordValidator],
      ],
      confirm_password: ['', [Validators.required]],
    });
  }

  updateUser(): void {
    const dialogRef = this.dialogService.open(ConfirmDialogComponent, {
      data: 'You are about to save your account. Proceed?',
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        const user: User = this.user;
        user.firstName = this.form.get('firstName').value;
        user.lastName = this.form.get('lastName').value;
        user.username = this.form.get('username').value;
        user.phone = this.form.get('phone').value;

        const salt = bcrypt.genSaltSync(10);
        user.password =
          '{bcrypt}' + bcrypt.hashSync(this.form.get('password').value, salt);

        this.subs.push(
          this.userService.makeAccount(user, this.token).subscribe(
            (response: User) => {
              this.router.navigateByUrl('').then();
            },
            (errorResponse: HttpErrorResponse) => {
              this.router.navigateByUrl('error').then();
            }
          )
        );
      }
    });
  }

  ngOnDestroy(): void {
    this.subs.forEach((value) => value.unsubscribe());
  }

  onStrengthChanged($event: number): void {}

  passwordMismatch(): boolean {
    return (
      this.form.get('password').value !==
      this.form.get('confirm_password').value
    );
  }
}
