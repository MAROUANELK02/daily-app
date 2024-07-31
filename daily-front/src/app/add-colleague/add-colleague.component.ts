import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User} from "../models/user.model";
import {AdminRepositoryService} from "../services/admin.repository.service";
import {AppStateService} from "../services/app-state.service";

@Component({
  selector: 'app-add-colleague',
  templateUrl: './add-colleague.component.html',
  styleUrl: './add-colleague.component.css'
})
export class AddColleagueComponent implements OnInit{
  form!: FormGroup;

  constructor(private adminService : AdminRepositoryService,
              private appState : AppStateService,
              private fb : FormBuilder) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', Validators.required],
      confirmedPassword: ['', Validators.required],
      jobTitle: ['', Validators.required]
    });
    if(this.appState.authState.isAuthenticated) {
      this.appState.getCurrentUserImage();
    }
  };

  handleCreate() {
    if(this.form.value.password === this.form.value.confirmedPassword) {
      let user : User = new User();
      user.firstname = this.form.value.firstName;
      user.lastname = this.form.value.lastName;
      user.email = this.form.value.email;
      user.username = this.form.value.username;
      user.password = this.form.value.password;
      user.jobTitle = this.form.value.jobTitle;
      this.adminService.addUser(user);
    }else {
      window.alert("Passwords do not match");
    }
  }
}
