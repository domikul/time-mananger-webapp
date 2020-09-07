import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {environment} from '../../environments/environment';
import {map, skipWhile} from 'rxjs/operators';
import {Position} from '../_models/position';

@Injectable({
  providedIn: 'root'
})
export class PositionService {

  private positions: BehaviorSubject<Position[]> = new BehaviorSubject<Position[]>([]);
  private positions$: Observable<Position[]> = this.positions.asObservable();
  private pending = false;

  constructor(private http: HttpClient) { }

  getPositions(): Observable<Position[]> {
    if(this.positions.value.length === 0 && !this.pending) {
      this.pending = true;
      this.http.get<Position[]>(`${environment.apiUrl}/position`).subscribe(x => {
        this.positions.next(x);
        this.pending = false;
      });
    }

    return this.positions$;
  }

  getPosition(idPosition: number): Observable<Position> {
    this.getPositions();
    return this.positions$.pipe(skipWhile(x => x.length === 0)).pipe(map(x => {
      return x.find(s => s.idPosition === idPosition);
    }));


  }

}
