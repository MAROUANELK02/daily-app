import {Component, OnInit} from '@angular/core';
import {AppStateService} from "../services/app-state.service";
import {ColleaguesRepositoryService} from "../services/colleagues.repository.service";
import {catchError, forkJoin, Observable, of, switchMap} from "rxjs";
import {User} from "../models/user.model";
import {TasksRepositoryService} from "../services/tasks.repository.service";
import {Task} from "../models/task.model";

@Component({
  selector: 'app-colleagues',
  templateUrl: './colleagues.component.html',
  styleUrls: ['./colleagues.component.css']
})
export class ColleaguesComponent implements OnInit {
  userImageUrls: { [userId: number]: string } = {};
  searchQuery: string = '';
  selectedUser: any;
  selectedTaskForDetails: any;

  constructor(public appState: AppStateService,
              private colleaguesRepositoryService: ColleaguesRepositoryService,
              private tasksRepositoryService: TasksRepositoryService) {
  }

  searchUsers(): Observable<any> {
    return this.colleaguesRepositoryService.getColleagues({
      keyword: this.searchQuery,
      currentPage: this.appState.usersState.currentPage,
      size: this.appState.usersState.pageSize
    });
  }

  ngOnInit(): void {
    this.fetchUsersAndImages();
  }

  fetchUsersAndImages(): void {
    this.searchUsers().pipe(
      switchMap(() => {
        const imageRequests = this.appState.usersState.users.map((user: { userId: number }) =>
          this.colleaguesRepositoryService.getUserImageById(user.userId)
        );
        return forkJoin(imageRequests);
      })
    ).subscribe((imageUrls) => {
      const typedImageUrls = imageUrls as string[];
      this.appState.usersState.users.forEach((user: { userId: number }, index: number) => {
        this.userImageUrls[user.userId] = typedImageUrls[index];
      });
    });
  }

  getUserCardClass(tasksCount: number): string {
    if (tasksCount <= 2) {
      return 'bg-green-200'; // Background green for low load
    } else if (tasksCount <= 3) {
      return 'bg-yellow-200'; // Background yellow for moderate load
    } else if (tasksCount <= 4) {
      return 'bg-orange-200'; // Background orange for high load
    } else {
      return 'bg-red-200'; // Background red for critical load
    }
  }

  prevPage() {
    if (this.appState.usersState.currentPage > 0) {
      this.appState.usersState.currentPage--;
      this.fetchUsersAndImages();
    }
  }

  nextPage() {
    if (this.appState.usersState.currentPage < this.appState.usersState.totalPages-1) {
      this.appState.usersState.currentPage++;
      this.fetchUsersAndImages();
    }
  }

  goToPage(index: number) {
    this.appState.usersState.currentPage = index;
    this.fetchUsersAndImages();
  }

  initiateSearch() {
    this.appState.usersState.currentPage = 0;
    this.appState.usersState.keyword = this.searchQuery;
    this.fetchUsersAndImages();
  }

  openModal(user: User) {
    this.selectedUser = user;
    this.tasksRepositoryService.getInProgressTasksByUserId(user.userId, {}).subscribe({
      next: (response) => {},
      error: (error) => {
        console.error('Failed to fetch in-progress tasks for user', error);
      }
    });
  }

  closeModal() {
    this.selectedUser = null;
  }

  getPriorityLabel(priority: string): string {
    switch (priority) {
      case 'HIGH':
        return 'Urgente';
      case 'MEDIUM':
        return 'Moyenne';
      case 'LOW':
        return 'Normale';
      default:
        return priority;
    }
  }

  openDetailModal(task: Task) {
    this.selectedTaskForDetails = task;
  }

  closeDetailModal() {
    this.selectedTaskForDetails = null;
  }
}
