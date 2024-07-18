import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";

@Injectable({
  providedIn: 'root'
})
export class AuthRepositoryService {
  host:string ="http://localhost:5000/api/auth/signin";

  constructor(private http : HttpClient, private appState : AppStateService) { }

  async login(username:string, password:string) :Promise<any> {
    try {
      let data:any = await this.http.post(this.host, {
        username: username,
        password: password
      }).toPromise();
      if (data.token){
        this.appState.setAuthState({
          isAuthenticated : true,
          username : data.username,
          id:data.id,
          email:data.email,
          roles : data.roles,
          token :data.token
        })
        return Promise.resolve(true);
      } else {
        return Promise.reject("Bad Credentials");
      }
    } catch (e) {
      return Promise.reject("Network error");
    }
  }

  async logout() {
    this.appState.setAuthState({
      isAuthenticated : false,
      username : "",
      id:"",
      email:"",
      roles : [],
      token :""
    })
  }
}
