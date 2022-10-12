// noinspection DuplicatedCode

import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ChannelService } from '../../../../service/channel/channel.service';
import { Channel } from '../../../../model/Channel';
import { Category } from '../../../../model/Category';
import { CategoryService } from '../../../../service/category/category.service';
import { HttpClient } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { UserService } from '../../../../service/user/user.service';
import { CommunicationDirection } from '../../../../model/CommunicationDirection';
import { CommunicationDirectionService } from '../../../../service/communication-direction/communication-direction.service';
import { ChannelStatus } from '../../../../model/ChannelStatus';
import { ActivatedRoute, Router } from '@angular/router';
import { faImage } from '@fortawesome/free-solid-svg-icons';
import { UserChannel } from '../../../../model/UserChannel';
import { ChannelRole } from '../../../../model/ChannelRole';
import { User } from '../../../../model/User';
import { AuthenticationService } from '../../../../service/authentication/authentication.service';
import {
  Message,
  ValidationFailedResponse,
} from '../../../../http/response/ValidationFailedResponse';
import { DialogService } from '../../../../service/dialog/dialog.service';

@Component({
  selector: 'app-make-channel',
  templateUrl: './make-channel.component.html',
  styleUrls: ['./make-channel.component.css'],
})
export class MakeChannelComponent implements OnInit, OnDestroy {
  public form: FormGroup;
  channels: Channel[];
  categories: Category[];
  communicationDirections: CommunicationDirection[];
  parentChannelId: number;
  public profileImage;
  public profileImageUpload;
  faUpload = faImage;
  nameValidationFailedResponseMessage = '';
  private subs: Subscription[] = [];

  constructor(
    private formBuilder: FormBuilder,
    private channelService: ChannelService,
    private categoryService: CategoryService,
    private http: HttpClient,
    private userService: UserService,
    private communicationDirectionService: CommunicationDirectionService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService,
    private dialogService: DialogService
  ) {}

  private static getIdByName(list: any[], value: any): number {
    for (const item of list) {
      if (item.name === value) {
        return item.id;
      }
    }
  }

  private static findByName(error: Message[], name: string): string {
    for (const item of error) {
      if (item.type === name) {
        return item.message;
      }
    }
    return '';
  }

  ngOnInit(): void {
    this.initForm();
    this.initDefaultProfilePicture();
    this.initParentChannelId();
    this.initCategories();
    this.initCommunicationDirections();
  }

  ngOnDestroy(): void {
    this.subs.forEach((value) => value.unsubscribe());
  }

  makeChannel(): void {
    const channel: Channel = new Channel();
    const category: Category = new Category();
    const communicationDirection: CommunicationDirection =
      new CommunicationDirection();
    const channelStatus: ChannelStatus = new ChannelStatus();

    if (this.parentChannelId !== -1) {
      const parentChannel: Channel = new Channel();
      parentChannel.id = this.parentChannelId;
      channel.parentChannel = parentChannel;
    }

    category.id = MakeChannelComponent.getIdByName(
      this.categories,
      this.form.get('category').value
    );
    communicationDirection.id = MakeChannelComponent.getIdByName(
      this.communicationDirections,
      this.form.get('communicationDirection').value
    );
    channelStatus.id = 1;
    channel.name = this.form.get('name').value;
    channel.category = category;
    channel.communicationDirection = communicationDirection;
    channel.channelStatus = channelStatus;

    const userChannel: UserChannel = new UserChannel();
    const channelRole: ChannelRole = new ChannelRole();
    channelRole.id = 2;
    userChannel.channelRole = channelRole;
    const user: User = new User();
    user.id = this.authenticationService.getUserFromLocalCache().id;
    userChannel.user = user;
    channel.userChannels.push(userChannel);

    this.subs.push(
      this.channelService.saveChannel(channel).subscribe(
        (response: Channel) => {
          const formData = new FormData();
          formData.append('id', String(response.id));
          formData.append('profileImage', this.profileImageUpload);
          this.dialogService.openDialogSuccess('Channel Created Successfully');
          this.channelService.uploadProfileImage(formData).subscribe((data) => {
            this.router.navigate([`/channel/${response.id}`]).then();
          });
        },
        (error: ValidationFailedResponse) => {
          this.nameValidationFailedResponseMessage =
            MakeChannelComponent.findByName(error.error.error, 'name');
        }
      )
    );
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

  private initForm(): void {
    this.form = this.formBuilder.group({
      name: ['', [Validators.required]],
      category: ['', [Validators.required]],
      communicationDirection: ['', [Validators.required]],
    });
  }

  private initParentChannelId(): void {
    // this.subs.push(this.activatedRoute.paramMap.pipe(
    //   switchMap((params) => {
    //     return params.get('id');
    //   })
    // ).subscribe((value) => {
    //   this.parentChannelId = Number(value);
    // }));

    this.subs.push(
      this.activatedRoute.paramMap.subscribe((paramMap) => {
        this.parentChannelId = Number(paramMap.get('id'));
      })
    );
  }

  private initCategories(): void {
    this.categoryService.getCategories().subscribe((value) => {
      this.categories = value;
    });
  }

  private initCommunicationDirections(): void {
    this.communicationDirectionService
      .getCommunicationDirections()
      .subscribe((value) => (this.communicationDirections = value));
  }

  private initDefaultProfilePicture(): void {
    this.profileImage = this.channelService.getDefaultPictureUrl();
    this.channelService.getDefaultPicture().subscribe((data) => {
      this.profileImageUpload = new File([data], 'channel-default-image.png');
    });
  }
}
