import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {environment} from '../../environments/environment';
import {Status} from '../_models/status';
import {map, skipWhile} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StatusService {

  private statuses: BehaviorSubject<Status[]> = new BehaviorSubject<Status[]>([]);
  private statuses$: Observable<Status[]> = this.statuses.asObservable();

  constructor(private http: HttpClient) { }

  getStatuses(): Observable<Status[]> {
    if(this.statuses.value.length === 0)
      this.http.get<Status[]>(`${environment.apiUrl}/status`).subscribe(x => {
        this.statuses.next(x);
      });

    return this.statuses$;
  }

  getStatus(idStatus: number): Observable<Status> {
    this.getStatuses();
    return this.statuses$.pipe(skipWhile(x => x.length === 0)).pipe(map(x => {
      return x.find(s => s.idStatus === idStatus);
    }));


  }

}
