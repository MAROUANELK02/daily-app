import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AppStateService {

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

  tasksCount : number = 0;

  public setTasksCount(count: number) {
    this.tasksCount = count;
  }

  private getImageUri(): Observable<string> {
    return this.http.get(`http://localhost:5000/api/users/image/${this.authState.id}`,
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
