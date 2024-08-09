import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {AppStateService} from "./app-state.service";
import {Task} from "../models/task.model";
import {ApiResponse} from "../models/api-response.model";
import {Observable, tap} from "rxjs";
import {Router} from "@angular/router";
import {data} from "autoprefixer";

@Injectable({
  providedIn: 'root'
})
export class TasksRepositoryService {
  host : string = "http://localhost:5000/api/users";

  constructor(private http : HttpClient, private appState : AppStateService,private router: Router) { }

  createTask(task : Task) {
    this.http.post<Task>(this.host + "/task/" + this.appState.authState.id, task).subscribe({
      next : ()=>{
        this.fetchTasksCount();
        window.alert("Tâche ajoutée");
        this.router.navigateByUrl("/tasks");
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

  changePriority(task: Task) : Observable<any> {
    return this.http.patch(`${this.host}/task/${task.taskId}/priority`, {}, {
      params: {
        priority: task.priority
      }
    });
  }

  changeStatus(task: Task) : Observable<any> {
    return this.http.patch(`${this.host}/task/${task.taskId}/status`, {}, {
      params: {
        status: task.status
      }
    });
  }

  getTasksHistory({
    currentPage = this.appState.tasksHistoryState.currentPage,
    size = this.appState.tasksHistoryState.pageSize,
    keyword = this.appState.tasksHistoryState.keyword,
                  }) : Observable<any> {
    this.appState.tasksHistoryState.status = "LOADING";
    return this.http.get<ApiResponse<Task>>(this.host + "/tasksHistory", {
      params: {
        page: currentPage,
        size: size,
        keyword: keyword
      }
    }).pipe(
      tap({
        next: (data: ApiResponse<Task>) => {
          let tasks = data.content;
          let totalPages = data.totalPages;
          this.appState.setTasksHistoryState({ tasks, totalPages, currentPage, status: "LOADED", errorMessage: "" });
        },
        error: (err) => {
          this.appState.setTasksHistoryState({ status: "ERROR", errorMessage: err.statusText });
        }
      })
    );
  }

  deleteTask(task: Task) :Observable<any>{
    return this.http.delete(`${this.host}/task/${task.taskId}`);
  }

  public fetchTasksCount() {
    this.http.get<any>(`${this.host}/user/${this.appState.authState.id}/tasksCount`).subscribe(
      data => { this.appState.setTasksCount(data.inProgressTasksCount, data.completedTasksCount) }
    )
  }

  public fetchCompletedStatistics(userId: number, size: number = 6): Observable<any> {
    return this.http.get<{ [key: string]: number }>(`${this.host}/statistics/${userId}/completed`, {
      params: {
        size: size
      }
    }).pipe(
      tap(data => {
        if(this.appState.statistics != null) {
          this.appState.statistics.clear();
        }
        Object.keys(data).forEach(key => {
          this.appState.statistics.set(key, data[key]);
        });
      })
    );
  }

  public fetchInProgressStatistics(userId: number, size: number = 6): Observable<any> {
    return this.http.get<{ [key: string]: number }>(`${this.host}/statistics/${userId}/inProgress`, {
      params: {
        size: size
      }
    }).pipe(
      tap(data => {
        if(this.appState.inProgressStatistics != null) {
          this.appState.inProgressStatistics.clear();
        }
        Object.keys(data).forEach(key => {
          this.appState.inProgressStatistics.set(key, data[key]);
        });
      })
    );
  }

}
