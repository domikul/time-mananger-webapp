import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import { Task } from 'src/app/_models/task';
import {EmailService} from '../../_services/email.service';
import {SubscriptionService} from '../../_services/subscription.service';
import {Email} from '../../_models/email';
import {take} from 'rxjs/operators';
import {Subscription} from '../../_models/subscription';

@Component({
  selector: 'app-new-subscription-dialog',
  templateUrl: './new-subscription-dialog.component.html',
  styleUrls: ['./new-subscription-dialog.component.scss']
})
export class NewSubscriptionDialogComponent implements OnInit {

  emails: Email[];

  constructor(
    private dialogRef: MatDialogRef<NewSubscriptionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { task: Task },
    private emailService: EmailService,
    private subscriptionService: SubscriptionService
    ) { }

  ngOnInit(): void {
    this.emailService.getEmails().pipe(take(1)).subscribe(emails => {
      this.subscriptionService.getTaskSubscriptions(this.data.task.idTask).subscribe(taskSubs => {
        this.emails = emails.filter(x => !taskSubs.some(y => x.idEmail === y.emailId));
      });
    });
  }

  close() {
    this.dialogRef.close();
  }

  addSubscription(email: Email) {
    const subscription = new Subscription();
    subscription.emailId = email.idEmail;
    subscription.taskId = this.data.task.idTask;
    this.subscriptionService.addSubscription(subscription).subscribe(x => {
      this.emails.splice(this.emails.indexOf(email), 1);
    });
  }
}
