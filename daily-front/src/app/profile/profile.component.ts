import {Component, OnInit} from '@angular/core';
import {AppStateService} from "../services/app-state.service";
import {ColleaguesRepositoryService} from "../services/colleagues.repository.service";
import {User} from "../models/user.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Observable} from "rxjs";
import {AuthRepositoryService} from "../services/auth.repository.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  user !: User;
  form !: FormGroup;

  constructor(public appState: AppStateService,
              public fb: FormBuilder,
              private authService : AuthRepositoryService,
              private route : Router,
              private userService: ColleaguesRepositoryService) {
    this.form = this.fb.group({
      password: this.fb.control(""),
      newPassword: this.fb.control(""),
      confirmedPassword: this.fb.control(""),
      firstname: this.fb.control(""),
      lastname: this.fb.control(""),
      email: this.fb.control(""),
      username: this.fb.control(""),
      jobTitle: this.fb.control(""),
    });
  }

  ngOnInit() {
    if(this.appState.authState.isAuthenticated) {
      this.appState.getCurrentUserImage();
    }
    this.fetchCurrentUser();
  }

  private fetchCurrentUser() {
    this.userService.getUserById(this.appState.authState.id).subscribe(
      (data) => {
        this.user = data;
        this.formInitialization();
      }, (err) => {
        console.log(err);
      });
  }

  private formInitialization() {
    this.form.patchValue({
      firstname: this.user.firstname,
      lastname: this.user.lastname,
      email: this.user.email,
      username: this.user.username,
      jobTitle: this.user.jobTitle
    });
  }

  changePassword() {
    if (window.confirm("Etes-vous sûr de vouloir changer votre mot de passe ?")) {
      this.userService.changePassword(this.appState.authState.id, this.form.value.password,
        this.form.value.newPassword, this.form.value.confirmedPassword).subscribe({
        next: (data) => {
          window.alert(data.message);
          this.formInitialization();
        },
        error: (error) => {
          if (error.error[0]?.includes("Le mot de passe")) {
            window.alert(error.error[0])
          } else {
            const errorMessage = error.error?.error || 'An error occurred';
            window.alert(`Error: ${errorMessage}`);
          }
        }
      });
    }
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
          this.appState.getCurrentUserImage();
            }
          });
      }
    }

  onDeleteImage() {
    if (window.confirm("Êtes vous sûr de vouloir supprimer votre photo de profil ?")) {
      this.userService.deleteImage(this.appState.authState.id).subscribe({
        next: () => {
          this.appState.getCurrentUserImage();
          },
      });
    }
  }

  editProfile() {
    if (window.confirm("Êtes vous sûr de vouloir modifier vos coordonnées ?")) {
      this.userService.editProfile(this.user.userId, this.form.value.firstname, this.form.value.lastname,
        this.form.value.email, this.form.value.username, this.form.value.jobTitle).subscribe({
        next: (data) => {
          window.alert(data.message);
          if(this.user.username !== this.form.value.username) {
            this.authService.logout().finally(() => {
              this.route.navigateByUrl("/login");
            });
          }
          this.fetchCurrentUser();
        },
        error: (err) => {
          window.alert(err.error[0])
        }
      });
    }
  }
}
