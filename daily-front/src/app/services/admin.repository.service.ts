import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {User} from "../models/user.model";

@Injectable({
  providedIn: 'root'
})
export class AdminRepositoryService {
  host : string = "http://localhost:5000/api/admin";

  constructor(private http : HttpClient, private appState : AppStateService) { }

  addUser(user: User) {
    this.http.post(this.host + "/addUser", user)
      .subscribe({
        next: (data) => {
          window.alert("User added successfully");
        },
        error: (err) => {
          window.alert(err.message());
        }
      })
  }


}
