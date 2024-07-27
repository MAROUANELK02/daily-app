import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {HttpAppInterceptor} from "./services/http-app.interceptor";
import {provideAnimationsAsync} from "@angular/platform-browser/animations/async";
import { NavbarComponent } from './navbar/navbar.component';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';
import { ColleaguesComponent } from './colleagues/colleagues.component';
import { TasksComponent } from './tasks/tasks.component';
import { AddTaskComponent } from './add-task/add-task.component';
import { TasksHistoryComponent } from './tasks-history/tasks-history.component';
import { AddColleagueComponent } from './add-colleague/add-colleague.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import {QuillModule} from "ngx-quill";
import { ProfileComponent } from './profile/profile.component';
import { SidebarComponent } from './sidebar/sidebar.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NavbarComponent,
    NotAuthorizedComponent,
    ColleaguesComponent,
    TasksComponent,
    AddTaskComponent,
    TasksHistoryComponent,
    AddColleagueComponent,
    ForgotPasswordComponent,
    ProfileComponent,
    SidebarComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    QuillModule.forRoot(),
    FormsModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: HttpAppInterceptor, multi: true},
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
