import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {Task} from "../models/task.model";
import {ApiResponse} from "../models/api-response.model";
import {Observable, tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TasksRepositoryService {
  host : string = "http://localhost:5000/api/users";

  constructor(private http : HttpClient, private appState : AppStateService) { }

  createTask(task : Task) {
    this.appState.usersState.status = "LOADING";
    this.http.post<Task>(this.host + "/task/" + this.appState.authState.id, task).subscribe({
      next : ()=>{
        this.appState.setTasksState({status:"SUCCESS", errorMessage:""});
      },
      error : (err)=>{
        this.appState.setTasksState({status:"ERROR", errorMessage:err.statusText});
      }
    });
  }

  getInProgressTasksByUserId(
    userId: number,
    {
      currentPage = this.appState.tasksState.currentPage,
      size = this.appState.tasksState.pageSize
    }): Observable<ApiResponse<Task>> {
    this.appState.tasksState.status = "LOADING";
    return this.http.get<ApiResponse<Task>>(this.host + "/" + userId + "/tasks/inProgress", {
      params: {
        page: currentPage,
        size: size
      }
    }).pipe(
      tap({
        next: (data: ApiResponse<Task>) => {
          let tasks = data.content;
          let totalPages = data.totalPages;
          this.appState.setTasksState({ tasks, totalPages, currentPage, status: "LOADED", errorMessage: "" });
        },
        error: (err) => {
          this.appState.setTasksState({ status: "ERROR", errorMessage: err.statusText });
        }
      })
    );
  }

  getCompletedTasksByUserId(
    userId: number,
    {
      currentPage = this.appState.completedTasksState.currentPage,
      size = this.appState.completedTasksState.pageSize
    }
  ): Observable<ApiResponse<Task>> {
    this.appState.completedTasksState.status = "LOADING";
    return this.http.get<ApiResponse<Task>>(this.host + "/" + userId + "/tasks/completed", {
      params: {
        page: currentPage,
        size: size
      }
    }).pipe(
      tap({
        next: (data: ApiResponse<Task>) => {
          let tasks = data.content;
          let totalPages = data.totalPages;
          this.appState.setCompletedTasksState({tasks, totalPages, currentPage, status: "LOADED", errorMessage: ""});
        },
        error: (err) => {
          this.appState.setCompletedTasksState({status: "ERROR", errorMessage: err.statusText});
        }
      })
    );
  }

}
