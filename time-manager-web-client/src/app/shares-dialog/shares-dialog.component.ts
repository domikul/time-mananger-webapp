import {Component, Inject, OnInit, EventEmitter} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Shareable} from '../_models/shareable';
import {ShareableService} from '../_services/shareable-service';
import {Share} from '../_models/share';
import {UserService} from '../_services/user.service';
import {User} from '../_models/user';
import {EmailService} from "../_services/email.service";

export interface SharesDialogData {

  shareable: Shareable;

  shareableService: ShareableService;

}

@Component({
  selector: 'app-shares-dialog',
  templateUrl: './shares-dialog.component.html',
  styleUrls: ['./shares-dialog.component.scss']
})
export class SharesDialogComponent implements OnInit {

  shares: Share[];
  showNewShare = false;
  newSharedUsers: User[] = [];
  deletionNotifier = new EventEmitter<Share>();

  constructor(private dialogRef: MatDialogRef<SharesDialogComponent>,
              public userService: UserService,
              @Inject(MAT_DIALOG_DATA) public data: SharesDialogData) { }

  ngOnInit(): void {

    this.data.shareableService.getShares(this.data.shareable.getShareableId()).subscribe(x => {
      this.shares = x;
    });
  }

  close() {
    this.dialogRef.close();
  }

  deleteShare(idUser: number) {

    this.data.shareableService.deleteShare(idUser, this.data.shareable.getShareableId()).subscribe(x => {
      const deletedShare = this.shares.find(y => y.userId === idUser);
      this.shares.splice(this.shares.indexOf(deletedShare), 1);
      this.deletionNotifier.emit(deletedShare);
    });
  }

  newShare() {
    this.showNewShare = true;
  }

  submitNewShares() {

    this.data.shareableService.addNewShares(this.newSharedUsers.map(user => {
      return { idUser: user.idUser };
      }),
      this.data.shareable.getShareableId()
    ).subscribe(newShares => {

      newShares.forEach(share => {
        this.shares.unshift(share);
      });

    });
  }
}
