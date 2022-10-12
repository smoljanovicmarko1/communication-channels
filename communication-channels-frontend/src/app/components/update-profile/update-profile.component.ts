// noinspection DuplicatedCode

import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, Subscription } from 'rxjs';
import { UserService } from '../../service/user/user.service';
import { User } from '../../model/User';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthenticationService } from '../../service/authentication/authentication.service';
import { faPaperclip } from '@fortawesome/free-solid-svg-icons';
import { DomSanitizer } from '@angular/platform-browser';
import { HttpResponse } from '../../model/HttpResponse';

@Component({
  selector: 'app-update-profile',
  templateUrl: './update-profile.component.html',
  styleUrls: ['./update-profile.component.css'],
})
export class UpdateProfileComponent implements OnInit, OnDestroy {
  public form: FormGroup;
  public profileImage;
  public profileImageUpload: any;
  faUpload = faPaperclip;
  private subs: Subscription[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private authenticationService: AuthenticationService,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      firstName: [
        this.authenticationService.getUserFromLocalCache().firstName,
        [Validators.required],
      ],
      lastName: [
        this.authenticationService.getUserFromLocalCache().lastName,
        [Validators.required],
      ],
      username: [
        this.authenticationService.getUserFromLocalCache().username,
        [Validators.required],
      ],
      phone: [this.authenticationService.getUserFromLocalCache().phone],
    });
    this.userService
      .getProfilePictureById(
        this.authenticationService.getUserFromLocalCache().id
      )
      .subscribe((data) => {
        this.profileImageUpload = new File([data], 'profile-image');
        // in case user does not change picture the same picture will be uploaded
        const objectURL = URL.createObjectURL(data);
        const img = this.sanitizer.bypassSecurityTrustUrl(objectURL);
        this.profileImage = img;
      });
  }

  updateUser(): void {
    const user: User = this.authenticationService.getUserFromLocalCache();
    user.firstName = this.form.get('firstName').value;
    user.lastName = this.form.get('lastName').value;
    user.username = this.form.get('username').value;
    user.phone = this.form.get('phone').value;
    this.subs.push(
      this.userService.updateProfile(user).subscribe(
        (response: User) => {
          this.authenticationService.saveUserToLocalCache(user);
        },
        (errorResponse: HttpErrorResponse) => {}
      )
    );
    this.subs.push(this.onChangeProfilePicture().subscribe((data) => {}));
  }

  ngOnDestroy(): void {
    this.subs.forEach((value) => value.unsubscribe());
  }

  detectFile(event): void {
    if (event.target.files.length > 0) {
      const reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);
      reader.onload = (readerEvent) => {
        this.profileImage = reader.result;
      };
      this.profileImageUpload = event.target.files[0];
    }
  }

  onChangeProfilePicture(): Observable<HttpResponse> {
    if (this.profileImageUpload === undefined) {
      return null;
    }
    const formData = new FormData();
    formData.append('profileImage', this.profileImageUpload);
    formData.append(
      'id',
      String(this.authenticationService.getUserFromLocalCache().id)
    );
    return this.userService.uploadProfileImage(formData);
  }
}
