import {Component, OnInit} from '@angular/core';
import {AppStateService} from "../services/app-state.service";
import {TasksRepositoryService} from "../services/tasks.repository.service";
import {Task} from "../models/task.model";

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrl: './tasks.component.css'
})
export class TasksComponent implements OnInit{
  tasks!: Task[];
  completedTasks!: Task[];

  constructor(public appState : AppStateService,
              private taskService : TasksRepositoryService) {}

  ngOnInit(): void {
    this.taskService.getInProgressTasksByUserId(this.appState.authState.id, {}).subscribe(
      tasks => this.tasks = this.appState.tasksState.tasks
    );
    this.taskService.getCompletedTasksByUserId(this.appState.authState.id, {}).subscribe(
      tasks => this.completedTasks = this.appState.completedTasksState.tasks
    );
  }

  prevPage() {
    if (this.appState.tasksState.currentPage > 0) {
      this.appState.tasksState.currentPage--;
      this.taskService.getInProgressTasksByUserId(this.appState.authState.id, {}).subscribe(
        tasks => this.tasks = this.appState.tasksState.tasks
      );
    }
  }

  goToPage(index: number) {
    this.appState.tasksState.currentPage = index;
    this.taskService.getInProgressTasksByUserId(this.appState.authState.id, {}).subscribe(
      tasks => this.tasks = this.appState.tasksState.tasks
    );
  }

  nextPage() {
    if (this.appState.tasksState.currentPage < this.appState.tasksState.totalPages-1) {
      this.appState.tasksState.currentPage++;
      this.taskService.getInProgressTasksByUserId(this.appState.authState.id, {}).subscribe(
        tasks => this.tasks = this.appState.tasksState.tasks
      );
    }
  }

  completedPrevPage() {
    if (this.appState.completedTasksState.currentPage > 0) {
      this.appState.completedTasksState.currentPage--;
      this.taskService.getCompletedTasksByUserId(this.appState.authState.id, {}).subscribe(
        tasks => this.completedTasks = this.appState.completedTasksState.tasks
      );
    }
  }

  completedGoToPage(index: number) {
    this.appState.completedTasksState.currentPage = index;
    this.taskService.getCompletedTasksByUserId(this.appState.authState.id, {}).subscribe(
      tasks => this.completedTasks = this.appState.completedTasksState.tasks
    );
  }

  completedNextPage() {
    if (this.appState.completedTasksState.currentPage < this.appState.completedTasksState.totalPages-1) {
      this.appState.completedTasksState.currentPage++;
      this.taskService.getCompletedTasksByUserId(this.appState.authState.id, {}).subscribe(
        tasks => this.completedTasks = this.appState.completedTasksState.tasks
      );
    }
  }
}
