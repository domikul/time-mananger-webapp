import {Component, EventEmitter, Inject, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BucketService} from "../_services/bucket.service";
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {error} from "@angular/compiler/src/util";
import {Bucket} from "../_models/bucket";


@Component({
  selector: 'app-new-bucket-dialog',
  templateUrl: './new-bucket-dialog.component.html',
  styleUrls: ['./new-bucket-dialog.component.scss']
})
export class NewBucketDialogComponent implements OnInit {

  type: boolean;
  sendButtonsAllow: boolean;
  bucket: Bucket;

  constructor(
    private bucketService: BucketService,
    private dialogRef: MatDialogRef<NewBucketDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data:{
      inputBucket: Bucket,
      type: boolean,
      sendButtonsAllow: boolean
    }
  ) { }


  ngOnInit(): void {
    this.bucket = new Bucket();
    this.type = this.data.type;
    if(this.type == false){
      this.bucket = this.data.inputBucket;
    }
    this.sendButtonsAllow = this.data.sendButtonsAllow;
  }

  save() {
    if(this.data.type == true){
      this.bucketService.createBucket(this.bucket).subscribe(x => {
        this.dialogRef.close(x);
      });
    } else {
      this.bucketService.updateBucket(this.data.inputBucket.idBucket, this.bucket).subscribe(x => {
        this.dialogRef.close(x);
      })
    }
  }

  close() {
    this.dialogRef.close();
  }

}
