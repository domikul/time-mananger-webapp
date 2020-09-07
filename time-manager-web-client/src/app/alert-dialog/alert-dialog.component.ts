import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BucketService} from "../_services/bucket.service";
import {Bucket} from "../_models/bucket";

@Component({
  selector: 'app-alert-dialog',
  templateUrl: './alert-dialog.component.html',
  styleUrls: ['./alert-dialog.component.scss']
})
export class AlertDialogComponent implements OnInit {

  constructor(
    private bucketService: BucketService,
    private dialogRef: MatDialogRef<AlertDialogComponent>,
    ) { }

  ngOnInit(): void {
  }

  close() {
    this.dialogRef.close(false);
  }

  doDelete(){
    this.dialogRef.close(true);
  }

}
