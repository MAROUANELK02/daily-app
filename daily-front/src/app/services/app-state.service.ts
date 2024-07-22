import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AppStateService {

  public authState:any={
    isAuthenticated :false,
    username:"",
    id:"",
    email:"",
    roles :[],
    token :"",
    status :"",
    errorMessage :"",
  }

  public usersState :any={
    users:[],
    totalPages:0,
    keyword:"",
    currentPage : 0,
    pageSize: 6,
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

  public setAuthState(state:any){
    this.authState={...this.authState, ...state};
  }

  public setUsersState(state:any){
    this.usersState={...this.usersState, ...state};
  }

  public setTasksState(state:any){
    this.tasksState={...this.tasksState, ...state};
  }

  constructor() { }
}
