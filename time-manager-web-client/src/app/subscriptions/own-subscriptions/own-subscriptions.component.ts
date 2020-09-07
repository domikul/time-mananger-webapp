import { Component, OnInit } from '@angular/core';
import {SubscriptionService} from '../../_services/subscription.service';
import {DetailedSubscription} from '../../_models/detailed-subscription';
import {EmailService} from '../../_services/email.service';

@Component({
  selector: 'app-own-subscriptions',
  templateUrl: './own-subscriptions.component.html',
  styleUrls: ['./own-subscriptions.component.scss']
})
export class OwnSubscriptionsComponent implements OnInit {

  subscriptions: DetailedSubscription[];

  constructor(
    private subscriptionService: SubscriptionService
  ) { }

  ngOnInit(): void {
    this.subscriptionService.getOwnSubscriptions().subscribe(x => {
      this.subscriptions = x;
    });
  }

}
