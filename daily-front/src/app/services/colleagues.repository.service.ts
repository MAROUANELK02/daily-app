import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {User} from "../models/user.model";
import {ApiResponse} from "../models/api-response.model";
import {catchError, map, Observable, of, tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ColleaguesRepositoryService implements OnInit{
  host : string = "http://localhost:5000/api/users";

  constructor(private http : HttpClient, private appState : AppStateService) { }

  ngOnInit(): void {
    this.getColleagues({});
  }

  getColleagues({keyword = this.appState.usersState.keyword,
                  currentPage = this.appState.usersState.currentPage,
                  size = this.appState.usersState.pageSize}): Observable<ApiResponse<User>> {
    this.appState.usersState.status = "LOADING";
    return this.http.get<ApiResponse<User>>(this.host + "/all", {
      params: {
        query : keyword,
        page : currentPage,
        size : size
      }
    }).pipe(
      tap({
        next: (data: ApiResponse<User>) => {
          let authUserId = this.appState.authState.id;
          let users = data.content.filter(user => user.userId !== authUserId);
          let totalPages = data.totalPages;
          this.appState.setUsersState({ users, totalPages, currentPage, keyword, status: "LOADED", errorMessage: "" });
        },
        error: (err) => {
          this.appState.setUsersState({ status: "ERROR", errorMessage: err.statusText });
        }
      })
    );
  }

  getUserImageById(userId: number): Observable<string> {
    return this.http.get(this.host + `/image/${userId}`, { responseType: 'blob' }).pipe(
      map((imageBlob: Blob) => {
        return window.URL.createObjectURL(imageBlob);
      }),
      catchError(() => {
        return of('/profile.jpg');
      })
    );
  }

  getUserById(userId: number): Observable<User> {
    return this.http.get<User>(this.host + `/${userId}`);
  }

  changePassword(userId: number, oldPassword: string, newPassword: string, confirmedPassword: string): Observable<any> {
    return this.http.patch(this.host + `/${userId}/changePassword`, {
      oldPassword: oldPassword,
      newPassword: newPassword,
      confirmedPassword: confirmedPassword
    });
  }

  postImage(userId: number, formData: FormData): Observable<any> {
    return this.http.post(this.host + `/image/${userId}`, formData);
  }

  putImage(userId: number, formData: FormData): Observable<any> {
    return this.http.put(this.host + `/image/${userId}`, formData);
  }

  deleteImage(userId: number): Observable<any> {
    return this.http.delete(this.host + `/image/${userId}`);
  }


}
