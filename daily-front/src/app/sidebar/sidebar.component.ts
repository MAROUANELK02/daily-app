import {Component, OnInit} from '@angular/core';
import {AuthRepositoryService} from "../services/auth.repository.service";
import {Router} from "@angular/router";
import {AppStateService} from "../services/app-state.service";
import {TasksRepositoryService} from "../services/tasks.repository.service";
import {Task} from "../models/task.model";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit{
  tasks!: Task[];

  constructor(private authService : AuthRepositoryService,
              public appState: AppStateService,
              private taskService : TasksRepositoryService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.taskService.getInProgressTasksByUserId(this.appState.authState.id, {}).subscribe(
      tasks => this.tasks = this.appState.tasksState.tasks
    );
    }

  handleLogOut() {
    if (window.confirm('Êtes-vous sure de vouloir se déconnecter ?')) {
      this.authService.logout();
      this.router.navigateByUrl("/login");
    }
  }
}
