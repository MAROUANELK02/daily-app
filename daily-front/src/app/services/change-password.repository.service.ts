import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {catchError, Observable, tap, throwError} from "rxjs";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class ChangePasswordRepositoryService {
  host : string = "http://localhost:5000/api/auth/";

  constructor(private httpClient : HttpClient,
              private appState : AppStateService,
              private router : Router) { }

  sendEmail(): Observable<any> {
    return this.httpClient.post(this.host + "forgot-password", {}, {
      params: {
        email: this.appState.changePasswordState.email
      }
    }).pipe(
      tap((response: any) => {
        if (response.message.includes('Code OTP de vérification envoyé')) {
          this.appState.setChangePasswordState({ status: 'success', errorMessage: '' });
        } else {
          this.appState.setChangePasswordState({ status: 'failed', errorMessage: response.error });
        }
      }),
      catchError((error) => {
        this.appState.setChangePasswordState({ status: 'failed', errorMessage: error.error?.error || 'An error occurred' });
        return throwError(error);
      })
    );
  }

  resetPassword(password: string, confirmedPassword: string, code: string) {
    return this.httpClient.post(this.host + "reset-password", {
      email: this.appState.changePasswordState.email,
      otpCode: code,
      password: password,
      confirmedPassword: confirmedPassword
    }).subscribe(
      (response: any) => {
        if (response.message.includes("Password reset successful")) {
          window.alert("Password reset successfully");
          this.router.navigate(['/login']);
        } else {
          this.appState.setChangePasswordState({ status: 'failed', errorMessage: response.error });
        }
      },(error: any) => {
        if(error.error[0]?.includes("Le mot de passe")) {
          window.alert(error.error[0])
        }else {
          const errorMessage = error.error?.error || 'An error occurred';
          window.alert(`Error: ${errorMessage}`);
        }
      }
    );
  }



}
