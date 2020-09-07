import {Component, Inject, OnInit} from '@angular/core';
import {Task} from '../../_models/task';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {UserService} from '../../_services/user.service';
import {EndRequestService} from '../../_services/end-request.service';
import {EndRequest} from '../../_models/end-request';
import {MatSnackBar} from '@angular/material/snack-bar';

export interface EndRequestDialogData {
  task: Task;
  own: boolean;
}

@Component({
  selector: 'app-end-request-dialog',
  templateUrl: './end-request-dialog.component.html',
  styleUrls: ['./end-request-dialog.component.scss']
})
export class EndRequestDialogComponent implements OnInit {

  endRequests: EndRequest[];

  constructor(
    private dialogRef: MatDialogRef<EndRequestDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: EndRequestDialogData,
    public userService: UserService,
    private endRequestService: EndRequestService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    if(this.data.own)
      this.endRequestService.getTaskEndRequests(this.data.task.idTask).subscribe(x => {
        this.endRequests = x;
      });
  }

  close() {
    this.dialogRef.close(false);
  }

  sendEndRequest() {
    const endRequest = new EndRequest();
    endRequest.taskId = this.data.task.idTask;
    this.endRequestService.sendEndRequest(endRequest).subscribe(x => {
      this.snackBar.open('End request sent', 'OK', {duration: 5000, panelClass: 'success-snackbar'});
      this.dialogRef.close(true);
    });
  }
}
