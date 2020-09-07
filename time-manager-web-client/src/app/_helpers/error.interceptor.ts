import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Injectable} from '@angular/core';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private snackBar: MatSnackBar) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    request = request.clone();
    return next.handle(request)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          let errorMsg = '';
          if (error.error instanceof ErrorEvent) {
            errorMsg = `Error: ${error.error.message}`;
          }
          else {
            if(error.status !== 500)
              this.snackBar.open(error.error.message, 'OK', {duration: 5000, panelClass: 'error-snackbar'});
            else
              this.snackBar.open('Server Error', 'OK', {duration: 5000, panelClass: 'error-snackbar'});

            errorMsg = `Error Code: ${error.status},  Message: ${error.error.message}`;
          }

          return throwError(errorMsg);
        })
      );
  }
}
