import { Component, ElementRef, HostListener } from '@angular/core';
import { AppStateService } from "../services/app-state.service";
import { Router } from "@angular/router";
import { AuthRepositoryService } from "../services/auth.repository.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  openClose: boolean = false;

  constructor(
    public appState: AppStateService,
    private router: Router,
    private authService: AuthRepositoryService,
    private elRef: ElementRef
  ) { }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    // Check if the click was inside the navbar or the dropdown menu
    if (this.openClose && !this.elRef.nativeElement.contains(target)) {
      this.openClose = false;
    }
  }

  handleLogOut() {
    if (window.confirm('Êtes-vous sure de vouloir se déconnecter ?')) {
      this.authService.logout();
      this.openDropDown();
      this.router.navigateByUrl("/login");
    }else{
      this.openDropDown();
    }
  }

  handleLogin() {
    this.router.navigateByUrl("/login");
  }

  openDropDown() {
    this.openClose = !this.openClose;
  }
}
