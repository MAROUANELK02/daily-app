import {Component, OnInit} from '@angular/core';
import {AuthRepositoryService} from "../services/auth.repository.service";
import {Router} from "@angular/router";
import {AppStateService} from "../services/app-state.service";
import {TasksRepositoryService} from "../services/tasks.repository.service";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit{
  constructor(private authService : AuthRepositoryService,
              public appState: AppStateService,
              private taskService : TasksRepositoryService,
              private router: Router) {
  }

  ngOnInit(): void {
    if(this.appState.authState.isAuthenticated) {
      this.appState.getCurrentUserImage();
    }
    this.taskService.fetchTasksCount();
  }

  handleLogOut() {
    if (window.confirm('Êtes-vous sure de vouloir se déconnecter ?')) {
      this.authService.logout();
      this.router.navigateByUrl("/login");
    }
  }
}
