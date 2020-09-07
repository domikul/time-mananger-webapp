import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {environment} from '../../environments/environment';
import {map, skipWhile} from 'rxjs/operators';
import {Priority} from '../_models/priority';

@Injectable({
  providedIn: 'root'
})
export class PriorityService {

  private priorities: BehaviorSubject<Priority[]> = new BehaviorSubject<Priority[]>([]);
  private priorities$: Observable<Priority[]> = this.priorities.asObservable();

  constructor(private http: HttpClient) { }

  getPriorities(): Observable<Priority[]> {
    if(this.priorities.value.length === 0)
      this.http.get<Priority[]>(`${environment.apiUrl}/priority`).subscribe(x => {
        this.priorities.next(x);
      });

    return this.priorities$;
  }

  getPriority(idPriority: number): Observable<Priority> {
    this.getPriorities();
    return this.priorities$.pipe(skipWhile(x => x.length === 0)).pipe(map(x => {
      return x.find(s => s.idPriority === idPriority);
    }));


  }

}
