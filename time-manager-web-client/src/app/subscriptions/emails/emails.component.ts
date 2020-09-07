import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subject} from 'rxjs';
import {EmailService} from '../../_services/email.service';
import {Email} from '../../_models/email';
import {take} from 'rxjs/operators';

@Component({
  selector: 'app-emails',
  templateUrl: './emails.component.html',
  styleUrls: ['./emails.component.scss']
})
export class EmailsComponent implements OnInit {

  newEmail = new Email();
  freeEmails: Email[];

  constructor(
    public emailService: EmailService
  ) { }

  ngOnInit(): void {
    this.emailService.getFreeEmails().subscribe(x => {
      this.freeEmails = x;
    });
  }

  deleteEmail(email: Email) {
    this.emailService.deleteEmail(email).pipe(take(1)).subscribe(x => {
      this.freeEmails.splice(this.freeEmails.findIndex(y => y.idEmail === email.idEmail), 1);
    });
  }

  addNewEmail() {
    if(this.newEmail.emailName !== undefined && this.newEmail.emailName !== '')
      this.emailService.addEmail(this.newEmail).pipe(take(1)).subscribe(x => {
        this.freeEmails.unshift(x);
      });
  }
}
