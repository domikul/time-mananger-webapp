import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {map, shareReplay, skipWhile, take} from 'rxjs/operators';
import {User} from '../_models/user';
import {History} from "../_models/history";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private users: BehaviorSubject<User[]> = new BehaviorSubject<User[]>([]);
  private users$: Observable<User[]> = this.users.asObservable();
  private pending = false;

  constructor(private http: HttpClient) { }

  getUsers(): Observable<User[]> {
    if(this.users.value.length === 0 && !this.pending) {
      this.pending = true;
      this.http.get<User[]>(`${environment.apiUrl}/user`).subscribe(x => {
        this.users.next(x);
        this.pending = false;
      });

    }

    return this.users$.pipe(skipWhile(x => x.length === 0));
  }

  refreshUsers() {

      this.pending = true;
      this.http.get<User[]>(`${environment.apiUrl}/user`).subscribe(x => {
        this.users.next(x);
        this.pending = false;
      });


  }

  getUser(idUser: number): Observable<User> {
    this.getUsers();
    return this.users$.pipe(skipWhile(x => x.length === 0)).pipe(map(x => {
      return x.find(s => s.idUser === idUser);
    }));
  }

  registerUser(user: User): Observable<User> {
    const user$ = this.http.post<User>(`${environment.apiUrl}/user`, user).pipe(shareReplay());
    user$.pipe(take(1)).subscribe(x => {
      const users = this.users.value;
      users.unshift(x);
      this.users.next(users);
    });

    return user$;
  }

  updateUser(user: User): Observable<User> {
    const user$ = this.http.put<User>(`${environment.apiUrl}/user/` + user.idUser, user).pipe(shareReplay());
    user$.pipe(take(1)).subscribe(x => {
      const users = this.users.value;
      const editedUser = users.find(y => y.idUser === user.idUser);
      users[users.indexOf(editedUser)] = user;
      this.users.next(users);
    });

    return user$;

  }

  deleteUser(idUser: number) {
    const deletion$ = this.http.delete<User>(`${environment.apiUrl}/user/` + idUser);
    deletion$.subscribe(x => {
      const users = this.users.value;
      const deletedUser = users.find(y => y.idUser === idUser);
      users.splice(users.indexOf(deletedUser), 1);
      this.users.next(users);
    });

  }

  getUserHistory(idUser: number): Observable<History[]> {
    return this.http.get<History[]>(`${environment.apiUrl}/user/history/` + idUser);
  }

  getInactiveUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${environment.apiUrl}/user/inactive`);
  }

  isUserActive(idUser: number): boolean {
    this.getUsers();
    return this.users.value.find(x => x.idUser === idUser)?.active ?? true;
  }

}
