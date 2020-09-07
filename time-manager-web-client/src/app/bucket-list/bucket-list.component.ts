import {Component, EventEmitter, Inject, Input, OnInit, Output} from '@angular/core';
import {Bucket} from '../_models/bucket';
import {BucketService} from '../_services/bucket.service';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA, MatDialogConfig} from '@angular/material/dialog';
import {NewBucketDialogComponent} from '../new-bucket-dialog/new-bucket-dialog.component';
import {AlertDialogComponent} from '../alert-dialog/alert-dialog.component';
import {filter} from 'rxjs/operators';

@Component({
  selector: 'app-bucket-list',
  templateUrl: './bucket-list.component.html',
  styleUrls: ['./bucket-list.component.scss']
})
export class BucketListComponent implements OnInit {

  @Input() buckets: Bucket[];
  @Input() buttonsAllow: boolean;
  @Input() showNewBucketButton: boolean;
  @Output() showClicked = new EventEmitter<Bucket>();
  @Output() deleteClicked = new EventEmitter<Bucket>();
  newDialog: MatDialogRef<NewBucketDialogComponent>;

  constructor(private bucketService: BucketService, private dialog: MatDialog) { }

  ngOnInit(): void {
    if(this.showNewBucketButton === undefined)
      this.showNewBucketButton = this.buttonsAllow;
  }

  show(bucket) {
    this.showClicked.emit(bucket);
  }

  openDialog(): void {

    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.height = '400px';
    dialogConfig.width = '700px';
    dialogConfig.data = {
        type: true,
        sendButtonsAllow: true
    }

    this.newDialog = this.dialog.open(NewBucketDialogComponent, dialogConfig);

      this.newDialog.afterClosed().subscribe(bucket => {
        if (bucket != undefined)
          this.buckets.push(bucket);

      })
  }

  openAlert(bucket): void {

    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.height = '215px';
    dialogConfig.width = '415px';

    this.dialog.open(AlertDialogComponent, dialogConfig).afterClosed().subscribe((result: boolean) =>
    {
      if(result)
        this.bucketService.deleteBucket(bucket.idBucket).subscribe(y => {
          const deletedBucket = this.buckets.find(x => x.idBucket === bucket.idBucket);
          this.buckets.splice(this.buckets.indexOf(deletedBucket), 1);
        });

    });

  }

}

