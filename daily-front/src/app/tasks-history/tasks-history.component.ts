import {Component, OnInit} from '@angular/core';
import {Task} from "../models/task.model";
import {AppStateService} from "../services/app-state.service";
import {ColleaguesRepositoryService} from "../services/colleagues.repository.service";
import {TasksRepositoryService} from "../services/tasks.repository.service";
import {forkJoin, Observable, switchMap} from "rxjs";

@Component({
  selector: 'app-tasks-history',
  templateUrl: './tasks-history.component.html',
  styleUrl: './tasks-history.component.css'
})
export class TasksHistoryComponent implements OnInit{
  tasks!: Task[];
  openClose: boolean = false;
  userImageUrls: { [userId: number]: string } = {};
  selectedTaskForDetails!: any;
  searchQuery: string = '';

  ngOnInit() {
    if(this.appState.authState.isAuthenticated) {
      this.appState.getCurrentUserImage();
    }
    this.fetchTasksUsersAndImages();
  }

  constructor(public appState : AppStateService,
              private taskService : TasksRepositoryService,
              private colleaguesRepositoryService : ColleaguesRepositoryService) { }

  fetchTasksUsersAndImages(): void {
    this.getTasks().pipe(
      switchMap(() => {
        const imageRequests = this.appState.tasksHistoryState.tasks.map((task: Task) =>
          this.colleaguesRepositoryService.getUserImageById(task.userDTO.userId)
        );
        return forkJoin(imageRequests);
      })
    ).subscribe((imageUrls) => {
      const typedImageUrls = imageUrls as string[];
      this.appState.tasksHistoryState.tasks.forEach((task: Task, index: number) => {
        this.userImageUrls[task.userDTO.userId] = typedImageUrls[index];
      });
    });
  }

  getTasks(): Observable<any> {
    return this.taskService.getTasksHistory({});
  }

  prevPage() {
    if (this.appState.tasksHistoryState.currentPage > 0) {
      this.appState.tasksHistoryState.currentPage--;
      this.fetchTasksUsersAndImages();
    }
  }

  goToPage(index: number) {
    this.appState.tasksHistoryState.currentPage = index;
    this.fetchTasksUsersAndImages();
  }

  nextPage() {
    if (this.appState.tasksHistoryState.currentPage < this.appState.tasksHistoryState.totalPages - 1) {
      this.appState.tasksHistoryState.currentPage++;
      this.fetchTasksUsersAndImages();
    }
  }

  openDropdown(task: Task) {
    this.selectedTaskForDetails = task;
    this.openClose = !this.openClose;
  }

  closeDropdown() {
    this.selectedTaskForDetails = null;
    this.openClose = !this.openClose;
  }

  initiateSearch() {
    this.appState.tasksHistoryState.currentPage = 0;
    this.appState.tasksHistoryState.keyword = this.searchQuery;
    this.fetchTasksUsersAndImages();
  }
}
