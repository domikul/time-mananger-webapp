import {Component, Input, OnInit} from '@angular/core';
import {Bucket} from "../_models/bucket";
import {BucketService} from "../_services/bucket.service";

@Component({
  selector: 'app-own-buckets',
  templateUrl: './own-buckets.component.html',
  styleUrls: ['./own-buckets.component.scss']
})
export class OwnBucketsComponent implements OnInit {

  showButtons: boolean = true;
  selectedBucket: Bucket = null;
  @Input() showButtonClick: Bucket;
  @Input() backButtonClick: boolean;
  @Input() saveButtonClick: Bucket;
  buckets: Bucket[];

  constructor(private bucketService: BucketService) { }

  ngOnInit(): void {
    this.bucketService.getOwnBuckets().subscribe(x => {
      this.buckets = x;
    });
  }

  onBack(backButtonClick){
    this.backButtonClick = backButtonClick;
    this.selectedBucket=null;
  }

  onShow(showButtonClick){
    this.selectedBucket = showButtonClick;
  }

  onSave(saveButtonClick){

    if(saveButtonClick != null){
      const index = this.buckets.findIndex(x=> x.idBucket === saveButtonClick.idBucket);
      this.buckets[index] = saveButtonClick;
    }
  }


}
