import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post } from '../../model/Post';
import { Comment } from '../../model/Comment';
import { HttpClient, HttpErrorResponse, HttpEvent } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { AuthenticationService } from '../authentication/authentication.service';
import { CommentService } from '../comment/comment.service';
import { Channel } from '../../model/Channel';
import { Like } from '../../model/Like';
import { LikeStatus } from '../../model/LikeStatus';
import { User } from '../../model/User';
import { Attachment } from '../../model/Attachment';
import { HttpResponse } from '../../model/HttpResponse';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  LIKE = 'Like';
  DISLIKE = 'Dislike';
  postIdForNavigation = -1;
  public host = environment.resourceServerUrl;

  constructor(
    private httpClient: HttpClient,
    private authService: AuthenticationService,
    private commentService: CommentService
  ) {}

  getById(id: number): Observable<Post> {
    return this.httpClient.get<Post>(
      environment.resourceServerUrl + '/post/' + id
    );
  }

  async postComment(post: Post, commentText: string): Promise<Post> {
    const comment: Comment = new Comment();
    comment.text = commentText;
    comment.user = this.authService.getUserFromLocalCache();
    await this.commentService
      .getCommentStatusByName(this.commentService.ORIGINAL)
      .then((receivedStatus) => {
        comment.commentStatus = receivedStatus;
      });
    post.comments.push(comment);

    return this.httpClient
      .post<Post>(environment.resourceServerUrl + '/post/addComment', post)
      .toPromise();
  }

  savePost(channel: Channel, post: Post): Observable<Post> {
    post.channel = channel;
    post.user = this.authService.getUserFromLocalCache();
    return this.httpClient.post<Post>(
      environment.resourceServerUrl + '/post/save',
      post
    );
  }

  update(post: Post): Observable<Post> {
    return this.httpClient.put<Post>(
      environment.resourceServerUrl + '/post',
      post
    );
  }

  async like(post: Post, likeStatusString: string): Promise<Post> {
    const like: Like = new Like();
    // user.favorites = null;//namestam da je null da ne bi ulazilo u beskonacnu petlju negde
    like.user = this.authService.getUserFromLocalCache();
    const postWithId: Post = new Post();
    postWithId.id = post.id;
    like.post = postWithId;
    await this.getLikeStatusByName(likeStatusString).then((likeStatus) => {
      like.likeStatus = likeStatus;
    });
    post.likes.push(like);
    return this.httpClient
      .post<Post>(environment.resourceServerUrl + '/post/addLike', post)
      .toPromise();
  }

  async updateLike(
    post: Post,
    likeStatusString: string,
    like: Like
  ): Promise<Post> {
    const user: User = this.authService.getUserFromLocalCache();
    // user.favorites = null;//namestam da je null da ne bi ulazilo u beskonacnu petlju negde
    await this.getLikeStatusByName(likeStatusString).then((likeStatus) => {
      like.likeStatus = likeStatus;
    });
    return this.httpClient
      .post<Post>(environment.resourceServerUrl + '/post/addLike', post)
      .toPromise();
  }

  getLikeStatusByName(likeStatus: string): Promise<LikeStatus> {
    return this.httpClient
      .get<LikeStatus>(
        environment.resourceServerUrl + '/comment/like-status/' + likeStatus
      )
      .toPromise();
  }

  addAttachment(formData: FormData): Observable<HttpEvent<Attachment>> {
    return this.httpClient.post<Attachment>(
      `${environment.resourceServerUrl}/post/addAttachment`,
      formData,
      {
        observe: 'events',
        reportProgress: true,
      }
    );
  }

  downloadAttachment(post: Post, attachment: Attachment): Observable<Blob> {
    return this.httpClient.get(
      `${environment.resourceServerUrl}/post/${post.id}/file/${attachment.originalName}`,
      {
        responseType: 'blob',
      }
    );
  }

  removeAttachment(
    post: Post,
    attachment: Attachment
  ): Observable<HttpResponse> {
    return this.httpClient.delete<HttpResponse>(
      `${environment.resourceServerUrl}/post/${post.id}/attachment/${attachment.id}/delete`
    );
  }

  savePostIdForNavigation(postIdForNavigation: number): void {
    this.postIdForNavigation = postIdForNavigation;
  }

  getPostIdForNavigation(): number {
    return this.postIdForNavigation;
  }

  deletePost(id: number): Observable<Post | HttpErrorResponse> {
    return this.httpClient.delete<Post | HttpErrorResponse>(
      `${this.host}/post/${id}`
    );
  }
}
