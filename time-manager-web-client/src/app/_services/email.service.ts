import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {map, shareReplay, skipWhile, take} from 'rxjs/operators';
import {Email} from '../_models/email';
import {User} from "../_models/user";

@Injectable({
  providedIn: 'root'
})
export class EmailService {

  private emails: BehaviorSubject<Email[]> = new BehaviorSubject<Email[]>([]);
  private emails$: Observable<Email[]> = this.emails.asObservable();
  private pending = false;

  constructor(private http: HttpClient) { }

  getEmails(): Observable<Email[]> {
    if(this.emails.value.length === 0 && !this.pending) {
      this.pending = true;
      this.http.get<Email[]>(`${environment.apiUrl}/email`).subscribe(x => {
        this.emails.next(x);
        this.pending = false;
      });

    }

    return this.emails$.pipe(skipWhile(x => x.length === 0));
  }

  refreshEmails() {

      this.pending = true;
      this.http.get<Email[]>(`${environment.apiUrl}/email`).subscribe(x => {
        this.emails.next(x);
        this.pending = false;
      });


  }

  private addEmailToCache(email: Email) {
    const currentEmails = this.emails.value;
    currentEmails.push(email);
    this.emails.next(currentEmails);
  }

  getEmail(idEmail: number): Observable<Email> {
    this.getEmails();
    return this.emails$.pipe(skipWhile(x => x.length === 0)).pipe(map(x => {
      return x.find(s => s.idEmail === idEmail);
  }));

  }

  deleteEmail(email: Email): Observable<any> {
    const deletion$ = this.http.delete(`${environment.apiUrl}/email/` + email.idEmail).pipe(shareReplay());
    deletion$.pipe(take(1)).subscribe(x => {
      const currentEmails = this.emails.value;
      currentEmails.splice(currentEmails.findIndex(y => y.idEmail === email.idEmail), 1);
      this.emails.next(currentEmails);
    });
    return deletion$;
  }

  addEmail(email: Email): Observable<Email> {
    const email$ = this.http.post<Email>(`${environment.apiUrl}/email`, email).pipe(shareReplay());
    email$.pipe(take(1)).subscribe(x => {
      const currentEmails = this.emails.value;
      currentEmails.unshift(x);
      this.emails.next(currentEmails);
    });
    return email$;
  }

  getFreeEmails(): Observable<Email[]> {
    return this.http.get<Email[]>(`${environment.apiUrl}/email/free`);
  }
}
