import { Injectable } from '@angular/core';
import { Post } from '../../model/Post';
import { HttpResponse } from '../../model/HttpResponse';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Like } from '../../model/Like';
import { Comment } from '../../model/Comment';
import { AuthenticationService } from '../authentication/authentication.service';
import { LikeStatus } from '../../model/LikeStatus';

@Injectable({
  providedIn: 'root',
})
export class LikeService {
  LIKE = 'Like';
  DISLIKE = 'Dislike';
  POST = 'Post';
  COMMENT = 'Comment';

  constructor(
    private httpClient: HttpClient,
    private authService: AuthenticationService
  ) {}

  deleteLike(like: Like): Promise<HttpResponse> {
    return this.httpClient
      .delete<HttpResponse>(
        `${environment.resourceServerUrl}/like/deleteLike/${like.id}`
      )
      .toPromise();
  }

  async like(
    object: string,
    id: number,
    likeStatusString: string
  ): Promise<Like> {
    const like: Like = new Like();
    like.user = this.authService.getUserFromLocalCache();
    if (object == null) {
      return null;
    }
    if (object === this.POST) {
      const post: Post = new Post();
      post.id = id;
      like.post = post;
    }
    if (object === this.COMMENT) {
      const comment: Comment = new Comment();
      comment.id = id;
      like.comment = comment;
    }
    await this.getLikeStatusByName(likeStatusString).then((likeStatus) => {
      like.likeStatus = likeStatus;
    });
    return this.httpClient
      .post<Like>(environment.resourceServerUrl + '/like/save', like)
      .toPromise();
  }

  getLikeStatusByName(likeStatus: string): Promise<LikeStatus> {
    return this.httpClient
      .get<LikeStatus>(
        environment.resourceServerUrl + '/comment/like-status/' + likeStatus
      )
      .toPromise();
  }

  async updateLike(like: Like, likeStatusString: string): Promise<any> {
    await this.getLikeStatusByName(likeStatusString).then((likeStatus) => {
      like.likeStatus = likeStatus;
    });
    return this.httpClient
      .post<Like>(environment.resourceServerUrl + '/like/save', like)
      .toPromise();
  }
}
