import { Component, OnInit } from '@angular/core';
import { AppStateService } from "../services/app-state.service";
import { TasksRepositoryService } from "../services/tasks.repository.service";
import { Task } from "../models/task.model";

@Component({
  selector: 'app-tasks',
  templateUrl: './tasks.component.html',
  styleUrls: ['./tasks.component.css']
})
export class TasksComponent implements OnInit {
  tasks!: Task[];
  completedTasks!: Task[];

  constructor(public appState: AppStateService,
              private taskService: TasksRepositoryService) {}

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
    if (this.appState.tasksState.currentPage < this.appState.tasksState.totalPages - 1) {
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
    if (this.appState.completedTasksState.currentPage < this.appState.completedTasksState.totalPages - 1) {
      this.appState.completedTasksState.currentPage++;
      this.taskService.getCompletedTasksByUserId(this.appState.authState.id, {}).subscribe(
        tasks => this.completedTasks = this.appState.completedTasksState.tasks
      );
    }
  }

  toggleDropdown(task: Task) {
    task.dropdownOpen = !task.dropdownOpen;
  }

  changePriority(task: Task, newPriority: string) {
    if(window.confirm("Êtes-vous sûr de vouloir changer la priorité de cette tâche ?")) {
      task.priority = newPriority;
      task.dropdownOpen = false;
      this.taskService.changePriority(task).subscribe({
        next: () => {
          this.taskService.getInProgressTasksByUserId(this.appState.authState.id, {}).subscribe(
            tasks => this.tasks = this.appState.tasksState.tasks
          );
          this.taskService.getCompletedTasksByUserId(this.appState.authState.id, {}).subscribe(
            tasks => this.completedTasks = this.appState.completedTasksState.tasks
          );
        },
        error: (err) => {
          this.appState.setTasksState({ status: "ERROR", errorMessage: err.statusText });
          window.alert("Une erreur s'est produite lors du changement de priorité.");
        }
      });
    }else{
      task.dropdownOpen = false;
    }
  }

  completeTask(task: Task) {
    if(window.confirm("Êtes-vous sûr de vouloir compléter cette tâche ?")) {
      task.status = "DONE";
      this.taskService.changeStatus(task).subscribe({
        next: () => {
          this.taskService.getInProgressTasksByUserId(this.appState.authState.id, {}).subscribe(
            tasks => this.tasks = this.appState.tasksState.tasks
          );
          this.taskService.getCompletedTasksByUserId(this.appState.authState.id, {}).subscribe(
            tasks => this.completedTasks = this.appState.completedTasksState.tasks
          );
        },
        error: (err) => {
          this.appState.setTasksState({ status: "ERROR", errorMessage: err.statusText });
          window.alert("Une erreur s'est produite lors de la complétion de la tâche.");
        }
      });
    }
  }

  reopenTask(task: Task) {
    if(window.confirm("Êtes-vous sûr de vouloir reprendre cette tâche ?")) {
      task.status = "IN_PROGRESS";
      this.taskService.changeStatus(task).subscribe({
        next: () => {
          this.taskService.getInProgressTasksByUserId(this.appState.authState.id, {}).subscribe(
            tasks => this.tasks = this.appState.tasksState.tasks
          );
          this.taskService.getCompletedTasksByUserId(this.appState.authState.id, {}).subscribe(
            tasks => this.completedTasks = this.appState.completedTasksState.tasks
          );
        },
        error: (err) => {
          this.appState.setTasksState({ status: "ERROR", errorMessage: err.statusText });
          window.alert("Une erreur s'est produite lors de la reprise de la tâche.");
        }
      });
    }
  }
}
