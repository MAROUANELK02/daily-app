import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {catchError, Observable, tap, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ChangePasswordRepositoryService {
  host : string = "http://localhost:5000/api/auth/";

  constructor(private httpClient : HttpClient,
              private appState : AppStateService) { }

  sendEmail(): Observable<any> {
    return this.httpClient.post(this.host + "forgot-password", {}, {
      params: {
        email: this.appState.changePasswordState.email
      }
    }).pipe(
      tap((response: any) => {
        if (response.message === 'OTP sent to email') {
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

  resetPassword(password: string, confirmedPassword: string, code: string): Observable<any> {
    return this.httpClient.post(this.host + "reset-password", {
      email: this.appState.changePasswordState.email,
      otpCode: code,
      password: password,
      confirmedPassword: confirmedPassword
    }).pipe(
      tap((response: any) => {
        if (response.message === 'Password reset successful') {
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



}
