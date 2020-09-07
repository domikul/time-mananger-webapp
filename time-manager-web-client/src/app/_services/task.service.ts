import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Task} from '../_models/task';
import {environment} from '../../environments/environment';
import {ShareableService} from './shareable-service';
import {TaskShare} from '../_models/task-share';
import {Share} from '../_models/share';
import {User} from '../_models/user';
import {History} from "../_models/history";

@Injectable({
  providedIn: 'root'
})
export class TaskService implements ShareableService {

  constructor(private http: HttpClient) { }

  getSharedTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(`${environment.apiUrl}/task/shared`);
  }

  getBucketTasks(bucketId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${environment.apiUrl}/task?bucketId=` + bucketId);
  }

  getTaskHistory(idTask: number): Observable<History[]> {
    return this.http.get<History[]>(`${environment.apiUrl}/task/history/` + idTask);
  }

  updateTask(task: Task): Observable<Task> {
    return this.http.put<Task>(`${environment.apiUrl}/task/` + task.idTask, task);
  }

  createTask(task: Task): Observable<Task> {
    return this.http.post<Task>(`${environment.apiUrl}/task`, task);
  }

  deleteTask(idTask: number): Observable<any> {
    return this.http.delete(`${environment.apiUrl}/task/` + idTask);
  }

  getShares(shareableId: number): Observable<Share[]> {
    return this.http.get<TaskShare[]>(`${environment.apiUrl}/task/share/` + shareableId);
  }

  addNewShares(users: { idUser: number }[], shareableId: number): Observable<Share[]> {
    return this.http.post<Share[]>(`${environment.apiUrl}/task/share/` + shareableId, users);
  }

  deleteShare(userId: number, shareableId: number): Observable<any> {
    return this.http.delete(`${environment.apiUrl}/task/share/` + shareableId + `?userId=` + userId);
  }
}


