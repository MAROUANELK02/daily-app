import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {User} from "../models/user.model";
import {Router} from "@angular/router";
import {Observable} from "rxjs";
import { environment } from "../../../environments/environment.prod";

@Injectable({
  providedIn: 'root'
})
export class AdminRepositoryService {
  host : string = `${environment.apiUrl}/admin`;

  constructor(private http : HttpClient, private appState : AppStateService,
              private router : Router) { }

  addUser(user: User) {
    this.http.post(this.host + "/addUser", user)
      .subscribe({
        next: (data) => {
          if (data) {
            this.router.navigateByUrl("/colleagues");
            window.alert("Utilisateur ajouté avec succès");
          }
        },
        error: (err) => {
          if (err.status === 400) {
            console.log(err)
            if(err.error[0]?.includes("Le mot de passe")) {
              window.alert(err.error[0])
            }else{
              const errorMessages = this.extractValidationMessages(err.error.error);
              window.alert("Une erreur s'est produite:\n" + errorMessages.join('\n'));
            }
          } else {
            console.log(err)
            window.alert("An unexpected error occurred: " + err.message);
          }
        }
      });
  }

  private extractValidationMessages(error: string): string[] {
    const messages = [];
    const regex = /interpolatedMessage='([^']+)'/g;
    let match;
    while ((match = regex.exec(error)) !== null) {
      messages.push(match[1]);
    }
    return messages;
  }

  deleteUser(userId : number): Observable<any> {
    return this.http.delete(`${this.host}/deleteUser/${userId}`)
  }

}
