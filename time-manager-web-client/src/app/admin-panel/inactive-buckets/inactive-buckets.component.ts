import { Component, OnInit } from '@angular/core';
import {UserService} from '../../_services/user.service';
import {User} from '../../_models/user';
import {Bucket} from '../../_models/bucket';
import {BucketService} from '../../_services/bucket.service';



@Component({
  selector: 'app-inactive-buckets',
  templateUrl: './inactive-buckets.component.html',
  styleUrls: ['./inactive-buckets.component.scss']
})
export class InactiveBucketsComponent implements OnInit {

  inactiveUsers: User[];

  constructor(
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.userService.getInactiveUsers().subscribe(x => {
      this.inactiveUsers = x;
    });
  }


}
