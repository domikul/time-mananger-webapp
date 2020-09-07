import {Component, Input, OnInit} from '@angular/core';
import {DetailedSubscription} from '../../_models/detailed-subscription';
import {EmailService} from '../../_services/email.service';
import {SubscriptionService} from '../../_services/subscription.service';
import {UserService} from '../../_services/user.service';

@Component({
  selector: 'app-subscriptions-list',
  templateUrl: './subscriptions-list.component.html',
  styleUrls: ['./subscriptions-list.component.scss']
})
export class SubscriptionsListComponent implements OnInit {

  @Input() subscriptions: DetailedSubscription[];
  @Input() mode: string;

  constructor(
    public emailService: EmailService,
    public userService: UserService,
    private subscriptionService: SubscriptionService
  ) { }

  ngOnInit(): void {
  }

  deleteSubscription(subscription: DetailedSubscription) {
    this.subscriptionService.deleteSubscription(subscription.idSub).subscribe(x => {
      this.subscriptions.splice(this.subscriptions.indexOf(subscription), 1);
    });
  }
}
