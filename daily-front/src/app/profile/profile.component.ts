import {Component, OnInit} from '@angular/core';
import {AppStateService} from "../services/app-state.service";
import {ColleaguesRepositoryService} from "../services/colleagues.repository.service";
import {User} from "../models/user.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  user !: User;
  form !: FormGroup;
  private host : string = "http://localhost:5000/api/users";

  constructor(public appState: AppStateService,
              private http: HttpClient,
              public fb: FormBuilder,
              private userService : ColleaguesRepositoryService) { }

  ngOnInit() {
    this.form = this.fb.group({
      password: this.fb.control(""),
      newPassword: this.fb.control(""),
      confirmedPassword: this.fb.control("")
    });
    this.userService.getUserById(this.appState.authState.id).subscribe(
      (data) => {
        this.user = data;
      }, (err) => {
        console.log(err);
      });
  }

  changePassword() {
    if(window.confirm("Are you sure you want to change your password?")) {
      this.userService.changePassword(this.appState.authState.id,this.form.value.password,
        this.form.value.newPassword, this.form.value.confirmedPassword).subscribe({
        next: (data) => {
          window.alert(data.message);
          this.form.reset();
        },
        error: (err) => {
          window.alert(err.error.error);
        }
      });
    }
  }

  private getUserImageById(userId: number): Observable<string> {
    return this.http.get(this.host + `/image/${userId}`, { responseType: 'blob' }).pipe(
      map((imageBlob: Blob) => {
        return window.URL.createObjectURL(imageBlob);
      }),
      catchError(() => {
        return of('/profile.jpg');
      })
    );
  }

  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      const formData = new FormData();
      formData.append('image', file);

      const userId = this.appState.authState.id;
      let imageUpload$: Observable<any>;

      if (this.appState.authState.imageUri === '/profile.jpg') {
        imageUpload$ = this.userService.postImage(userId, formData);
      } else {
        imageUpload$ = this.userService.putImage(userId, formData);
      }

      imageUpload$.subscribe({
        next: () => {
          this.getUserImageById(userId).subscribe({
            next: (imageUri: string) => {
              const authState = {
                isAuthenticated: this.appState.authState.isAuthenticated,
                username: this.appState.authState.username,
                id: this.appState.authState.id,
                email: this.appState.authState.email,
                roles: this.appState.authState.roles,
                token: this.appState.authState.token
              };
              const updatedAuthState = {
                ...authState,
                imageUri: imageUri
              };
              this.appState.setAuthState(updatedAuthState);
              localStorage.setItem('authState', JSON.stringify(updatedAuthState));
            },
            error: (error) => {
              console.error('Error getting image:', error);
            }
          });
        },
        error: (error) => {
          console.error('Error uploading image:', error);
        }
      });
    }
  }

}
