import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {NotAuthorizedComponent} from "./not-authorized/not-authorized.component";
import {ColleaguesComponent} from "./colleagues/colleagues.component";
import {TasksComponent} from "./tasks/tasks.component";
import {AddTaskComponent} from "./add-task/add-task.component";
import {TasksHistoryComponent} from "./tasks-history/tasks-history.component";
import {AddColleagueComponent} from "./add-colleague/add-colleague.component";
import {ForgotPasswordComponent} from "./forgot-password/forgot-password.component";
import {AuthenticationGuard} from "./guards/authentication.guard";
import {UserAuthorizationGuard} from "./guards/user-authorization.guard";
import {AdminAuthorizationGuard} from "./guards/admin-authorization.guard";

const routes: Routes = [
  {path: "login", component: LoginComponent},
  {path: "", redirectTo: "/login", pathMatch: "full"},
  {path: "notAuthorized", component: NotAuthorizedComponent},
  {path: "colleagues", component: ColleaguesComponent, canActivate : [AuthenticationGuard, UserAuthorizationGuard]},
  {path: "tasks", component: TasksComponent, canActivate : [AuthenticationGuard, UserAuthorizationGuard]},
  {path: "addTask", component: AddTaskComponent, canActivate : [AuthenticationGuard, UserAuthorizationGuard]},
  {path: "tasksHistory", component: TasksHistoryComponent, canActivate : [AuthenticationGuard, UserAuthorizationGuard]},
  {path: "addColleague", component: AddColleagueComponent, canActivate : [AuthenticationGuard, AdminAuthorizationGuard]},
  {path: "forgot-password", component: ForgotPasswordComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
