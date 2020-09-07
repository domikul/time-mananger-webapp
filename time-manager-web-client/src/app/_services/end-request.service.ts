import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Timer} from '../_models/timer';
import {EndRequest} from '../_models/end-request';

@Injectable({
  providedIn: 'root'
})
export class EndRequestService {

  constructor(private http: HttpClient) { }

  sendEndRequest(endRequest: EndRequest): Observable<EndRequest> {
    return this.http.post<EndRequest>(`${environment.apiUrl}/end-request`, endRequest);
  }

  getTaskEndRequests(taskId: number): Observable<EndRequest[]> {
    return this.http.get<EndRequest[]>(`${environment.apiUrl}/end-request?taskId=` + taskId);
  }

}
