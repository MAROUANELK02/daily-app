import {Component, OnInit} from '@angular/core';
import {AppStateService} from "../services/app-state.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ChangePasswordRepositoryService} from "../services/change-password.repository.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent implements OnInit{
  form!: FormGroup;
  openResetPassword: boolean = false;

  constructor(private appState: AppStateService,
            private changePasswordRepository: ChangePasswordRepositoryService,
              private fb : FormBuilder,
              private router : Router) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
      confirmedPassword: ['', Validators.required],
      code: ['', Validators.required]
    })
  }

  sendEmail() {
    this.appState.setChangePasswordState({ email: this.form.value.email });
    this.changePasswordRepository.sendEmail().subscribe(
      (response: any) => {
        if (this.appState.changePasswordState.status === 'success') {
          this.openResetPassword = !this.openResetPassword;
          window.alert("Email sent successfully");
        } else {
          window.alert(`Error: ${this.appState.changePasswordState.errorMessage}`);
        }
      },
      (error: any) => {
        const errorMessage = error.error?.error || 'An error occurred';
        window.alert(`Error: ${errorMessage}`);
      }
    );
  }

  resetPassword() {
    this.changePasswordRepository.resetPassword(
      this.form.value.password,
      this.form.value.confirmedPassword,
      this.form.value.code
    );
  }


}
