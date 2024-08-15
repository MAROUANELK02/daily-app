import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";
import { environment } from "../../../environments/environment.prod";

@Injectable({
  providedIn: 'root'
})
export class AppStateService {
  completedTasksCount: number = 0;
  inProgressTasksCount: number = 0;
  host : string = `${environment.apiUrl}`;

  constructor(private http : HttpClient) {
  }

  public authState:any={
    isAuthenticated :false,
    username:"",
    id:"",
    email:"",
    imageUri:"",
    roles :[],
    token :"",
    status :"",
    errorMessage :"",
  }

  public setTasksCount(inProgress: number, completed: number) {
    this.inProgressTasksCount = inProgress;
    this.completedTasksCount = completed;
  }

  private getImageUri(): Observable<string> {
    return this.http.get(`${this.host}/users/image/${this.authState.id}`,
      { responseType: 'blob' }).pipe(
      map((imageBlob: Blob) => {
        return window.URL.createObjectURL(imageBlob);
      }),
      catchError(() => {
        return of('/profile.jpg');
      })
    );
  }

  public getCurrentUserImage() : void {
    this.getImageUri().subscribe(
      (imageUri) => {
        const updatedAuthState = {
          ...this.authState,
          imageUri: imageUri
        };
        this.setAuthState(updatedAuthState);
        localStorage.removeItem('authState');
        localStorage.setItem('authState', JSON.stringify(updatedAuthState));
      }
    )
  }

  public usersState :any={
    users:[],
    totalPages:0,
    keyword:"",
    currentPage : 0,
    pageSize: 8,
    status :"",
    errorMessage :""
  }

  public changePasswordState :any={
    email:"",
    status :"",
    errorMessage :""
  }

  public tasksState :any={
    tasks:[],
    totalPages:0,
    currentPage : 0,
    pageSize: 6,
    status :"",
    errorMessage :""
  }

  public completedTasksState :any={
    tasks:[],
    totalPages:0,
    currentPage : 0,
    pageSize: 6,
    status :"",
    errorMessage :""
  }

  public tasksHistoryState: any={
    tasks:[],
    totalPages:0,
    currentPage : 0,
    keyword:"",
    pageSize: 6,
    status :"",
    errorMessage :""
  }

  public statistics: Map<string,number> = new Map();

  public inProgressStatistics: Map<string,number> = new Map();

  setTasksHistoryState(state: any) {
    this.tasksHistoryState = { ...this.tasksHistoryState, ...state };
  }

  public setAuthState(state:any){
    this.authState={...this.authState, ...state};
  }

  public setUsersState(state:any){
    this.usersState={...this.usersState, ...state};
  }

  public setTasksState(state:any){
    this.tasksState={...this.tasksState, ...state};
  }

  public setCompletedTasksState(state:any){
    this.completedTasksState={...this.completedTasksState, ...state};
  }

  public setChangePasswordState(state:any) {
    this.changePasswordState={...this.changePasswordState, ...state};
  }

}
