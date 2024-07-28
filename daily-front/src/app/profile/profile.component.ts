import {Component, OnInit} from '@angular/core';
import {AppStateService} from "../services/app-state.service";
import {ColleaguesRepositoryService} from "../services/colleagues.repository.service";
import {User} from "../models/user.model";
import {data} from "autoprefixer";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  user !: User;
  form !: FormGroup;

  constructor(public appState: AppStateService,
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



}
