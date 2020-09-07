import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {DetailedSubscription} from '../_models/detailed-subscription';
import {Subscription} from '../_models/subscription';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {

  constructor(private http: HttpClient) { }

  getOwnSubscriptions(): Observable<DetailedSubscription[]> {
    return this.http.get<DetailedSubscription[]>(`${environment.apiUrl}/subscription/own`);
  }

  getSharedSubscriptions(): Observable<DetailedSubscription[]> {
    return this.http.get<DetailedSubscription[]>(`${environment.apiUrl}/subscription/shared`);
  }

  getTaskSubscriptions(taskId: number): Observable<Subscription[]> {
    return this.http.get<Subscription[]>(`${environment.apiUrl}/subscription?taskId=` + taskId);
  }

  addSubscription(subscription: Subscription): Observable<Subscription> {
    return this.http.post<Subscription>(`${environment.apiUrl}/subscription`, subscription);
  }

  deleteSubscription(idSub: number): Observable<any> {
    return this.http.delete<any>(`${environment.apiUrl}/subscription/` + idSub);
  }

}
