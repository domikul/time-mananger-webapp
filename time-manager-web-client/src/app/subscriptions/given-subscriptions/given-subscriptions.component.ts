import { Component, OnInit } from '@angular/core';
import {DetailedSubscription} from '../../_models/detailed-subscription';
import {SubscriptionService} from '../../_services/subscription.service';

@Component({
  selector: 'app-given-subscriptions',
  templateUrl: './given-subscriptions.component.html',
  styleUrls: ['./given-subscriptions.component.scss']
})
export class GivenSubscriptionsComponent implements OnInit {

  subscriptions: DetailedSubscription[];

  constructor(
    private subscriptionService: SubscriptionService
  ) { }

  ngOnInit(): void {
    this.subscriptionService.getSharedSubscriptions().subscribe(x => {
      this.subscriptions = x;
    });
  }

}
