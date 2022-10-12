import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Searchable } from '../../model/Searchable';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { AuthenticationService } from '../authentication/authentication.service';

@Injectable({
  providedIn: 'root',
})
export class SearchableService {
  constructor(
    private httpClient: HttpClient,
    private authService: AuthenticationService
  ) {}

  getChannelsAndPosts(filterValue: string): Observable<Searchable[]> {
    const userId = this.authService.getUserFromLocalCache().id;
    return this.httpClient.get<Searchable[]>(
      `${environment.resourceServerUrl}/searchable/channels-and-posts?filterValue=${filterValue}&userId=${userId}`
    );
  }
}
