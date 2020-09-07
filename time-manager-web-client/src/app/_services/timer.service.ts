import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Timer} from '../_models/timer';

@Injectable({
  providedIn: 'root'
})
export class TimerService {

  constructor(private http: HttpClient) { }

  startTimer(timer: Timer): Observable<Timer> {
    return this.http.post<Timer>(`${environment.apiUrl}/timer`, timer);
  }

  suspendTimer(idTimer: number): Observable<Timer> {
    return this.http.patch<Timer>(`${environment.apiUrl}/timer/suspend/` + idTimer, {});
  }

  resumeTimer(idTimer: number): Observable<Timer> {
    return this.http.patch<Timer>(`${environment.apiUrl}/timer/resume/` + idTimer, {});
  }

  stopTimer(idTimer: number): Observable<any> {
    return this.http.delete<any>(`${environment.apiUrl}/timer/` + idTimer, {});
  }

}
