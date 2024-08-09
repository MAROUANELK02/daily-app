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
  openClose: boolean = false;
  selectedTaskForDetails!: any;

  constructor(public appState: AppStateService,
              private taskService: TasksRepositoryService) {}

  ngOnInit(): void {
    if(this.appState.authState.isAuthenticated) {
      this.appState.getCurrentUserImage();
    }
    this.fetchTasksByUserId(this.appState.authState.id);
  }

  prevPage() {
    if (this.appState.tasksState.currentPage > 0) {
      this.appState.tasksState.currentPage--;
      this.fetchInProgressTasksByUserId(this.appState.authState.id);
    }
  }

  goToPage(index: number) {
    this.appState.tasksState.currentPage = index;
    this.fetchInProgressTasksByUserId(this.appState.authState.id);
  }

  nextPage() {
    if (this.appState.tasksState.currentPage < this.appState.tasksState.totalPages - 1) {
      this.appState.tasksState.currentPage++;
      this.fetchInProgressTasksByUserId(this.appState.authState.id);
    }
  }

  completedPrevPage() {
    if (this.appState.completedTasksState.currentPage > 0) {
      this.appState.completedTasksState.currentPage--;
      this.fetchCompletedTasksByUserId(this.appState.authState.id);
    }
  }

  completedGoToPage(index: number) {
    this.appState.completedTasksState.currentPage = index;
    this.fetchCompletedTasksByUserId(this.appState.authState.id);
  }

  completedNextPage() {
    if (this.appState.completedTasksState.currentPage < this.appState.completedTasksState.totalPages - 1) {
      this.appState.completedTasksState.currentPage++;
      this.fetchCompletedTasksByUserId(this.appState.authState.id);
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
        next: (data) => {
          this.fetchTasksByUserId(this.appState.authState.id);
          window.alert(data.message)
        },
        error: (err) => {
          window.alert(err.error.error);
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
        next: (data) => {
          this.taskService.fetchTasksCount();
          this.fetchTasksByUserId(this.appState.authState.id);
          window.alert(data.message)
        },
        error: (err) => {
          window.alert(err.error.error);
        }
      });
    }
  }

  reactiveTask(task: Task) {
    if(window.confirm("Êtes-vous sûr de vouloir reprendre cette tâche ?")) {
      task.status = "IN_PROGRESS";
      this.taskService.changeStatus(task).subscribe({
        next: (data) => {
          this.taskService.fetchTasksCount();
          this.fetchTasksByUserId(this.appState.authState.id);
          window.alert(data.message)
        },
        error: (err) => {
          window.alert(err.error.error);
        }
      });
    }
  }

  deleteTask(task: Task) {
    if(window.confirm("Êtes-vous sûr de vouloir supprimer cette tâche ?")) {
      this.taskService.deleteTask(task).subscribe({
        next: (data) => {
          this.taskService.fetchTasksCount();
          this.fetchTasksByUserId(this.appState.authState.id);
          window.alert(data.message)
        },
        error: (err) => {
          window.alert(err.error.error);
        }
      });
    }
  }

  openDetailModal(task: Task) {
    this.selectedTaskForDetails = task;
    this.openClose = !this.openClose;
  }

  closeDetailModal() {
    this.selectedTaskForDetails = null;
    this.openClose = !this.openClose;
  }

  private fetchTasksByUserId(userId: number) {
    this.fetchInProgressTasksByUserId(userId);
    this.fetchCompletedTasksByUserId(userId);
  }

  private fetchCompletedTasksByUserId(userId: number) {
    this.taskService.getCompletedTasksByUserId(userId, {}).subscribe(
      tasks => this.completedTasks = this.appState.completedTasksState.tasks
    );
  }

  private fetchInProgressTasksByUserId(userId: number) {
    this.taskService.getInProgressTasksByUserId(userId, {}).subscribe(
      tasks => this.tasks = this.appState.tasksState.tasks
    );
  }

}
