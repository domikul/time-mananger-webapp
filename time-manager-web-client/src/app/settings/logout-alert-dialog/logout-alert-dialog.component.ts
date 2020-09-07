import { Component, OnInit } from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-logout-alert-dialog',
  template: `
    <mat-dialog-content>
      <h2 fxLayoutAlign="center center">Log out</h2>
      <h3>You need to log in again after changing your email.</h3>
    </mat-dialog-content>
    <mat-dialog-actions fxLayoutAlign="center">
      <button mat-flat-button color="primary" (click)="close()">OK</button>
    </mat-dialog-actions>
  `,
  styles: [
  ]
})
export class LogoutAlertDialogComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<LogoutAlertDialogComponent>) { }

  ngOnInit(): void {
  }

  close() {
    this.dialogRef.close();
  }
}
