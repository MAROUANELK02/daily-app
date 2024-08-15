import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {ColleaguesRepositoryService} from "./colleagues.repository.service";
import { environment } from "../../../environments/environment.prod";

@Injectable({
  providedIn: 'root'
})
export class AuthRepositoryService {
  host : string = `${environment.apiUrl}/auth/signin`;

  constructor(private http : HttpClient, private appState : AppStateService,
              private userService : ColleaguesRepositoryService) {
    this.loadAuthState();
  }

  async login(username: string, password: string): Promise<any> {
    try {
      let data: any = await this.http.post(this.host, {
        username: username,
        password: password
      }).toPromise();
      if (data.token) {
        const authState = {
          isAuthenticated: true,
          username: data.username,
          id: data.id,
          email: data.email,
          roles: data.roles,
          token: data.token
        };
        this.appState.setAuthState(authState);
        this.userService.getUserImageById(authState.id).subscribe(
          imageUri => {
            const updatedAuthState = {
              ...authState,
              imageUri: imageUri
            };
            this.appState.setAuthState(updatedAuthState);
            localStorage.setItem('authState', JSON.stringify(updatedAuthState));
          });
        return Promise.resolve(true);
      } else {
        return Promise.reject("Bad Credentials");
      }
    } catch (e) {
      return Promise.reject(e);
    }
  }

  async logout() {
    this.appState.setAuthState({
      isAuthenticated: false,
      username: "",
      id: "",
      email: "",
      imageUri: "",
      roles: [],
      token: ""
    });
    localStorage.removeItem('authState');
  }

  private loadAuthState() {
    const authState = localStorage.getItem('authState');
    if (authState) {
      this.appState.setAuthState(JSON.parse(authState));
    }
  }

}
