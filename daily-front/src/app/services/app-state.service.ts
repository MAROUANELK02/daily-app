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
    imageUri:"",
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
