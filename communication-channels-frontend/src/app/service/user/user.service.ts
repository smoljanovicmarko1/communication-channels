// noinspection DuplicatedCode

import { Injectable } from '@angular/core';
import { User } from '../../model/User';
import { Observable } from 'rxjs';
import {
  HttpClient,
  HttpErrorResponse,
  HttpParams,
} from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Post } from '../../model/Post';
import { AuthenticationService } from '../authentication/authentication.service';
import { HttpResponse } from '../../model/HttpResponse';
import { UsersPaginationResponse } from '../../http/response/UsersPaginationResponse';
import { MakeAccountRequest } from '../../model/MakeAccountRequest';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  REMOVE = 'remove';
  ADD = 'add';

  public host = environment.resourceServerUrl;

  constructor(
    private http: HttpClient,
    private authService: AuthenticationService
  ) {}

  findAll(page: number, size: number): Observable<UsersPaginationResponse> {
    let params = new HttpParams();
    params = params.append('page', String(page));
    params = params.append('size', String(size));
    return this.http.get<UsersPaginationResponse>(
      environment.resourceServerUrl + '/user/all-pagination',
      { params }
    );
  }

  findById(id: number): Observable<User> {
    return this.http.get<User>(`${environment.resourceServerUrl}/user/${id}`);
  }

  findAllUsersNotInChannel(
    channelId: number,
    loggedUserId: number,
    page: number,
    size: number
  ): Observable<UsersPaginationResponse> {
    let params = new HttpParams();
    params = params.append('channelId', String(channelId));
    params = params.append('loggedUserId', String(loggedUserId));
    params = params.append('page', String(page));
    params = params.append('size', String(size));
    return this.http.get<UsersPaginationResponse>(
      environment.resourceServerUrl +
        '/user/all-pagination-users-not-in-channel',
      { params }
    );
  }

  findAllUsersInChannel(
    channelId: number,
    loggedUserId: number,
    page: number,
    size: number
  ): Observable<UsersPaginationResponse> {
    let params = new HttpParams();
    params = params.append('channelId', String(channelId));
    params = params.append('loggedUserId', String(loggedUserId));
    params = params.append('page', String(page));
    params = params.append('size', String(size));
    return this.http.get<UsersPaginationResponse>(
      environment.resourceServerUrl + '/user/all-pagination-users-in-channel',
      { params }
    );
  }

  findByUsername(username: string): Promise<User> {
    return this.http
      .get<User>(`${this.host}/user/find-by-username?username=${username}`)
      .toPromise();
  }

  addUser(user: User): Observable<User | HttpErrorResponse> {
    return this.http.post<User | HttpErrorResponse>(
      `${this.host}/user/add`,
      user
    );
  }

  updateProfile(user: User): Observable<User | HttpErrorResponse> {
    return this.http.post<User | HttpErrorResponse>(
      `${this.host}/user/save`,
      user
    );
  }

  addFavourite(post: Post, addOrRemove: string): Observable<User> {
    const loggedUser = this.authService.getUserFromLocalCache();
    if (addOrRemove === this.ADD) {
      loggedUser.favorites.push(post);
    }
    if (addOrRemove === this.REMOVE) {
      loggedUser.favorites = loggedUser.favorites.filter(
        (value) => value.id !== post.id
      );
    }
    return this.http.post<User>(
      `${environment.resourceServerUrl}/user/save`,
      loggedUser
    );
  }

  getProfilePictureById(id: number): Observable<Blob> {
    return this.http.get(
      `${environment.resourceServerUrl}/user/${id}/profile-picture`,
      {
        responseType: 'blob',
      }
    );
  }

  uploadProfileImage(formData: FormData): Observable<HttpResponse> {
    return this.http.post<HttpResponse>(
      `${environment.resourceServerUrl}/user/upload-profile-image`,
      formData
    );
  }

  makeAccount(user: User, token: string): Observable<User> {
    const makeAccountRequest: MakeAccountRequest = new MakeAccountRequest();
    makeAccountRequest.token = token;
    makeAccountRequest.user = user;
    return this.http.post<User>(
      `${environment.resourceServerUrl}/user/make-account`,
      makeAccountRequest
    );
  }
}
