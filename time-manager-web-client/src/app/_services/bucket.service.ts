import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { Observable, throwError } from 'rxjs';
import {Bucket} from "../_models/bucket";
import {environment} from "../../environments/environment";
import {ShareableService} from './shareable-service';
import {Share} from '../_models/share';
import {TaskShare} from '../_models/task-share';
import {History} from "../_models/history";

@Injectable({
  providedIn: 'root'
})
export class BucketService implements ShareableService {

  constructor(private http: HttpClient) {}

  getOwnBuckets(): Observable<Bucket[]> {
    return this.http.get<Bucket[]>(`${environment.apiUrl}/bucket/own`);
  }

  getSharedBuckets(): Observable<Bucket[]> {
    return this.http.get<Bucket[]>(`${environment.apiUrl}/bucket/shared`);
  }

  getBucketHistory(idBucket: number): Observable<History[]> {
    return this.http.get<History[]>(`${environment.apiUrl}/bucket/history/` + idBucket);
  }

  createBucket(bucket: Bucket): Observable<Bucket> {
    return this.http.post<Bucket>(`${environment.apiUrl}/bucket`, bucket);
  }

  deleteBucket(idBucket: number) : Observable<any> {
    return this.http.delete<any>(`${environment.apiUrl}/bucket/` + idBucket);
  }

  updateBucket(inputBucket: number, bucket: Bucket) : Observable<Bucket> {
    return this.http.put<Bucket>(`${environment.apiUrl}/bucket/` + inputBucket, bucket);
  }

  getShares(shareableId: number): Observable<Share[]> {
    return this.http.get<TaskShare[]>(`${environment.apiUrl}/bucket/share/` + shareableId);
  }

  addNewShares(users: { idUser: number }[], shareableId: number): Observable<Share[]> {
    return this.http.post<Share[]>(`${environment.apiUrl}/bucket/share/` + shareableId, users);
  }

  deleteShare(userId: number, shareableId: number): Observable<any> {
    return this.http.delete(`${environment.apiUrl}/bucket/share/` + shareableId + `?userId=` + userId);
  }

  getInactiveUserBuckets(userId: number): Observable<Bucket[]> {
    return this.http.get<Bucket[]>(`${environment.apiUrl}/bucket/inactive-user?userId=` + userId);
  }

}
