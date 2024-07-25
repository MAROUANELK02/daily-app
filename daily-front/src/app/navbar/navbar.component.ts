import {Component} from '@angular/core';
import {AppStateService} from "../services/app-state.service";
import {Router} from "@angular/router";
import {AuthRepositoryService} from "../services/auth.repository.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  openClose : boolean = false;

  constructor(public appState : AppStateService,
              private router : Router,
              private authService : AuthRepositoryService) { }

  handleLogOut() {
    this.authService.logout();
    this.openDropDown();
    this.router.navigateByUrl("/login");
  }

  handleLogin() {
    this.router.navigateByUrl("/login")
  }

  openDropDown() {
    this.openClose = !this.openClose;
  }
}
