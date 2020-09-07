import {Component, Input, OnInit} from '@angular/core';
import {User} from '../../../_models/user';
import {BucketService} from '../../../_services/bucket.service';
import {Bucket} from '../../../_models/bucket';

@Component({
  selector: 'app-inactive-user-holder',
  templateUrl: './inactive-user-holder.component.html',
  styleUrls: ['./inactive-user-holder.component.scss']
})
export class InactiveUserHolderComponent implements OnInit {

  @Input() user: User;
  buckets: Bucket[];
  selectedBucket: Bucket = null;

  constructor(
    private bucketService: BucketService
  ) { }

  ngOnInit(): void {

  }

  loadBuckets() {
    this.bucketService.getInactiveUserBuckets(this.user.idUser).subscribe(x => {
      this.buckets = x;
    });
  }

  onBucketSelected(bucket: Bucket) {
    this.selectedBucket = bucket;
  }

  onBucketSaved(bucket: Bucket) {
    if(bucket !== null) {
      const index = this.buckets.findIndex(x => x.idBucket === bucket.idBucket);
      this.buckets[index] = bucket;
    }
  }
}
