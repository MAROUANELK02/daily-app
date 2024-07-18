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

  public setAuthState(state:any){
    this.authState={...this.authState, ...state};
  }

  constructor() { }
}
