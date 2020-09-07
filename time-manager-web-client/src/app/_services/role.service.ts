import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {environment} from '../../environments/environment';
import {map, skipWhile} from 'rxjs/operators';
import {Position} from '../_models/position';
import {Role} from '../_models/role';
import {Status} from '../_models/status';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  private roles: BehaviorSubject<Role[]> = new BehaviorSubject<Role[]>([]);
  private roles$: Observable<Role[]> = this.roles.asObservable();
  private pending = false;

  constructor(private http: HttpClient) { }

  getRoles(): Observable<Role[]> {
    if(this.roles.value.length === 0 && !this.pending) {
      this.pending = true;
      this.http.get<Role[]>(`${environment.apiUrl}/role`).subscribe(x => {
        this.roles.next(x);
        this.pending = false;
      });
    }

    return this.roles$;
  }

  getRole(idRole: number): Observable<Role> {
    this.getRoles();
    return this.roles$.pipe(skipWhile(x => x.length === 0)).pipe(map(x => {
      return x.find(s => s.idRole === idRole);
    }));
  }

}
