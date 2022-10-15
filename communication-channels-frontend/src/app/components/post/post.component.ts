// noinspection DuplicatedCode

import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { Subscription, throwError } from 'rxjs';
import { Post } from '../../model/Post';
import { PostService } from '../../service/post/post.service';
import { User } from '../../model/User';
import { AuthenticationService } from '../../service/authentication/authentication.service';
import { Like } from '../../model/Like';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Attachment } from '../../model/Attachment';
import { HttpEventType } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { UserService } from '../../service/user/user.service';
import { LikeService } from '../../service/like/like.service';
import {
  faCloudUploadAlt,
  faPaperclip,
} from '@fortawesome/free-solid-svg-icons';
import { AttachmentService } from '../../service/attachment/attachment.service';
import { Comment } from '../../model/Comment';
import { CommentService } from '../../service/comment/comment.service';
import { DomSanitizer } from '@angular/platform-browser';
import { DialogService } from '../../service/dialog/dialog.service';
import { MatDialog } from '@angular/material/dialog';
import { PostEditComponent } from './post-edit/post-edit.component';
import { POST_DELETE } from '../../constants/permission';
import { Router } from '@angular/router';
import { Channel } from '../../model/Channel';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
})
export class PostComponent implements OnInit, OnDestroy {
  @Input() post: Post;
  @Input() channel: Channel;
  @Output() favouriteClick = new EventEmitter<Post>();
  subs: Subscription[] = [];
  isCommentOpen = false;
  isLiked = false;
  isDisliked = false;
  loggedUser: User;
  fileList: FileList;
  fileForm: FormGroup;
  filesToUploadPost = [];
  filesToUploadComment = [];
  faUpload = faPaperclip;
  isFavourite = false;
  faCommitUpload = faCloudUploadAlt;

  constructor(
    private postService: PostService,
    private commentService: CommentService,
    private attachmentService: AttachmentService,
    private authService: AuthenticationService,
    private userService: UserService,
    private likeService: LikeService,
    private fb: FormBuilder,
    private sanitizer: DomSanitizer,
    private dialogService: DialogService,
    private matDialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loggedUser = this.authService.getUserFromLocalCache();

    if (
      this.post.filesToUpload !== undefined &&
      this.post.filesToUpload.length > 0
    ) {
      this.filesToUploadPost = this.post.filesToUpload;
      // if, when post was created, files for uploading were selected, puts that files in filesToUpload queue
    }
    // kreiranje forme za slanje fajla
    this.fileForm = this.fb.group({
      file: [''],
    });

    this.subs.push(
      this.postService.getById(this.post.id).subscribe((post) => {
        this.post = post;
        this.onUpload(); // emits the event onUpload in case there are files to be uploaded
        this.getProfilePictureByUserId(this.post.user.id);
        // prolazi da vidi da li je taj post lajkovan kod ulogovanog usera da bi obelezio slova
        for (const like of this.post.likes) {
          if (like.user.id === this.loggedUser.id) {
            if (like.likeStatus.name === this.postService.LIKE) {
              this.isLiked = true;
              this.isDisliked = false;
              break;
            }
            if (like.likeStatus.name === this.postService.DISLIKE) {
              this.isLiked = false;
              this.isDisliked = true;
              break;
            }
          }
        }
        this.isFavourite =
          this.loggedUser.favorites.find(
            (post1) => post1.id === this.post.id
          ) !== undefined;
      })
    );
  }

  ngOnDestroy(): void {
    this.subs.forEach((value) => value.unsubscribe());
  }

  commentButtonClicked(): void {
    this.isCommentOpen = !this.isCommentOpen;
  }

  async postComment(commentText: string): Promise<void> {
    if (commentText == null || commentText.length === 0) {
      return;
    }
    const comment: Comment = new Comment();
    comment.text = commentText;
    comment.user = this.authService.getUserFromLocalCache();
    comment.post = this.post;

    await this.commentService
      .getCommentStatusByName(this.commentService.ORIGINAL)
      .then((recevedStatus) => {
        comment.commentStatus = recevedStatus;
      });

    this.subs.push(
      this.commentService.save(comment).subscribe((data) => {
        data.filesToUpload = this.filesToUploadComment;
        this.post.comments.push(data);
        this.isCommentOpen = true; // to ensure that files will be uploaded
      })
    );
  }

  onDislike(): void {
    const like: Like = this.didUserAlreadyLikedOrDisliked();
    if (like == null) {
      this.likeService
        .like(this.likeService.POST, this.post.id, this.likeService.DISLIKE)
        .then((value) => {
          this.post.likes.push(value);
          this.isLiked = false;
          this.isDisliked = true;
        });
    } else {
      // ovaj if je slucaj kada kliknes na like koji je vec kliknut
      if (like.likeStatus.name === this.postService.DISLIKE) {
        this.likeService.deleteLike(like).then((httpResponse) => {
          this.isDisliked = false;
          this.post.likes = this.post.likes.filter(
            (value) => value.id !== like.id
          );
        });
      } else {
        // update like
        this.likeService
          .updateLike(like, this.likeService.DISLIKE)
          .then((value) => {
            this.post.likes = this.post.likes.filter(
              (like1) => like1.id !== like.id
            );
            this.post.likes.push(value);
            this.isLiked = false;
            this.isDisliked = true;
          });
      }
    }
  }

  onLike(): void {
    const like: Like = this.didUserAlreadyLikedOrDisliked();
    if (like == null) {
      this.likeService
        .like(this.likeService.POST, this.post.id, this.likeService.LIKE)
        .then((value) => {
          // this.post = post;
          this.post.likes.push(value);
          this.isLiked = true;
          this.isDisliked = false;
        });
    } else {
      // ovaj if je slucaj kada kliknes na like koji je vec kliknut
      if (like.likeStatus.name === this.postService.LIKE) {
        this.likeService.deleteLike(like).then((httpResponse) => {
          this.isLiked = false;
          this.post.likes = this.post.likes.filter(
            (value) => value.id !== like.id
          );
        });
      } else {
        // update like
        this.likeService
          .updateLike(like, this.likeService.LIKE)
          .then((value) => {
            this.post.likes = this.post.likes.filter(
              (like1) => like1.id !== like.id
            );
            this.post.likes.push(value);
            this.isLiked = true;
            this.isDisliked = false;
          });
      }
    }
  }

  uploadFile(file): void {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('userId', String(this.loggedUser.id));
    formData.append('attachmentParentId', String(this.post.id));
    formData.append('attachmentParentName', 'POST');

    const dummyAttachment: Attachment = new Attachment();
    dummyAttachment.uploadComplete = false;
    dummyAttachment.originalName = file.name;
    dummyAttachment.uploadProgress = 0;
    this.post.attachments.push(dummyAttachment);
    this.attachmentService
      .add(formData)
      .pipe(
        map((event: any) => {
          if (dummyAttachment.uploadAborted) {
            throw new Error('UPLOAD_ABORTED');
          }
          if (event.type === HttpEventType.UploadProgress) {
            dummyAttachment.uploadProgress = Math.round(
              (100 / event.total) * event.loaded
            );
          } else if (event.type === HttpEventType.Response) {
            this.post.attachments = this.post.attachments.filter(
              (att) => att.originalName !== file.name
            );
            const uploadedAttachment: Attachment = event.body;
            uploadedAttachment.uploadComplete = true;
            this.post.attachments.push(uploadedAttachment);
          }
        }),
        catchError((err: any) => {
          let dummyAttachmentRemoveName: string;
          if (dummyAttachment.uploadAborted) {
            dummyAttachmentRemoveName = err.message;
          } else {
            dummyAttachmentRemoveName = err.error;
            dummyAttachment.uploadProgress = 0;
            dummyAttachment.uploadError = true;
          }
          dummyAttachment.originalName = dummyAttachmentRemoveName;
          setTimeout(() => {
            this.post.attachments = this.post.attachments.filter(
              (att) => att.originalName !== dummyAttachmentRemoveName
            );
          }, 3000);
          this.dialogService.openDialogError(
            'Error occurred while uploading files'
          );
          return throwError(err.message);
        })
      )
      .toPromise()
      .then(
        (value) => {},
        (error) => {}
      );
  }

  detectFiles(event): void {
    this.filesToUploadPost = [];
    if (event.target.files.length > 0) {
      for (const file of event.target.files) {
        this.filesToUploadPost.push(file);
      }
    }
  }

  detectFilesComment(event): void {
    this.filesToUploadComment = [];
    if (event.target.files.length > 0) {
      for (const file of event.target.files) {
        this.filesToUploadComment.push(file);
      }
    }
  }

  onDeleteUpload(attachment: Attachment): void {
    this.postService
      .removeAttachment(this.post, attachment)
      .subscribe((data) => {});
  }

  onDeleteAttachment(attachment: Attachment): void {
    this.post.attachments = this.post.attachments.filter(
      (value) => value.id !== attachment.id
    );
  }

  onUpload(): void {
    if (this.filesToUploadPost.length > 0) {
      this.filesToUploadPost.forEach((file) => this.uploadFile(file));
    }
  }

  favourite(): void {
    if (this.isFavourite) {
      this.subs.push(
        this.userService
          .addFavourite(this.post, this.userService.REMOVE)
          .subscribe(
            (user) => {
              this.loggedUser = user;
              this.authService.saveUserToLocalCache(user);
              this.isFavourite = !this.isFavourite;
              this.favouriteClick.emit(this.post);
              this.dialogService.openDialogSuccess(
                'Post was successfully removed from favorites'
              );
            },
            (error) => {
              this.dialogService.openDialogError(
                'Error occurred while removing post from favorites'
              );
            }
          )
      );
    } else {
      this.subs.push(
        this.userService
          .addFavourite(this.post, this.userService.ADD)
          .subscribe(
            (user) => {
              this.authService.saveUserToLocalCache(user);
              this.loggedUser = user;
              this.isFavourite = !this.isFavourite;
              this.favouriteClick.emit(this.post);
              this.dialogService.openDialogSuccess(
                'Post was successfully added to favorites'
              );
            },
            (error) => {
              this.dialogService.openDialogError(
                'Error occurred while adding post to favorites'
              );
            }
          )
      );
    }
  }

  getLikesNumber(): number {
    return this.post.likes.filter(
      (value) => value.likeStatus.name === this.likeService.LIKE
    ).length;
  }

  getDislikeNumber(): number {
    return this.post.likes.filter(
      (value) => value.likeStatus.name === this.likeService.DISLIKE
    ).length;
  }

  getSelectedFileNames(filesToUpload): string {
    let result = '';
    filesToUpload.forEach((file) => (result += file.name + ' \n'));
    return result;
  }

  getProfilePictureByUserId(id: number): void {
    this.userService.getProfilePictureById(id).subscribe((data) => {
      const objectURL = URL.createObjectURL(data);
      this.post.user.profilePicture =
        this.sanitizer.bypassSecurityTrustUrl(objectURL);
    });
  }

  onUploadFiles($event: Event): void {
    this.detectFiles($event);
    this.onUpload();
  }

  /**
   * like objekat: ako postoji lajk sa tim user-om
   * null: ako ne postoji lajk sa tim user-om
   */
  private didUserAlreadyLikedOrDisliked(): Like {
    for (const like of this.post.likes) {
      if (like.user.id === this.loggedUser.id) {
        return like;
      }
    }
    return null;
  }

  openEditPostDialog(post): void {
    const refDialog = this.matDialog.open(PostEditComponent, {
      height: '500px',
      width: '400px',
      data: post,
    });

    refDialog.afterClosed().subscribe(
      (result) => {
        if (!result) {
          return;
        }
        // @ts-ignore
        const editedPost: Post = { ...this.post };
        editedPost.title = result.title;
        editedPost.body = result.body;
        editedPost.edited = true;
        this.subs.push(
          this.postService.update(editedPost).subscribe(
            (response) => {
              this.post.title = response.title;
              this.post.body = response.body;
              this.post.edited = response.edited;
              this.dialogService.openDialogSuccess('Post Updated Successfully');
            },
            (error) => {
              this.dialogService.openDialogError(
                'An error occurred while updating the post'
              );
            }
          )
        );
      },
      (error) => {}
    );
  }

  isEditingAllowed(): boolean {
    return this.authService.getUserFromLocalCache().id === this.post.user.id;
  }

  deletePost(id: number): void {
    this.subs.push(
      this.postService.deletePost(id).subscribe(
        (value) => {
          this.dialogService.openDialogSuccess(
            `Post has been deleted successfully`
          );
          this.channel.posts.splice(
            this.channel.posts.findIndex((post) => post.id === id),
            1
          );
        },
        (error) => {
          this.dialogService.openDialogError(
            'An error occurred while deleting post'
          );
        }
      )
    );
  }

  isDeletingAllowed(): boolean {
    return this.hasPostDeletePermission() || this.isHisChannel();
  }

  hasPostDeletePermission(): boolean {
    return this.authService.hasPermission(POST_DELETE);
  }

  isHisChannel(): boolean {
    const user = this.authService.getUserFromLocalCache();
    return user.id === this.post.user.id;
  }
}
