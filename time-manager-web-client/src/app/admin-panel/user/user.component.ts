import {Component, Input, OnInit} from '@angular/core';
import {User} from '../../_models/user';
import {PositionService} from '../../_services/position.service';
import {UserActionDialogComponent, UserActionDialogData} from '../user-action-dialog/user-action-dialog.component';
import {DialogConfigFactory} from '../../_helpers/dialog-config-factory';
import {ActionMode} from '../../_helpers/action.mode';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {AlertDialogComponent} from '../../alert-dialog/alert-dialog.component';
import {UserService} from '../../_services/user.service';
import {HistoryComponent} from "../../history/history.component";

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {

  @Input() user: User;

  constructor(
    public positionService: PositionService,
    private userService: UserService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
  }

  launchEdit() {
    this.dialog.open(UserActionDialogComponent, DialogConfigFactory.for(UserActionDialogComponent, {
      user: User.makeCopy(this.user),
      mode: ActionMode.edit
    }));
  }

  deleteUser() {
    this.dialog.open(AlertDialogComponent, DialogConfigFactory.for(AlertDialogComponent)).afterClosed().subscribe((result: boolean) => {
      if(result)
        this.userService.deleteUser(this.user.idUser);

    });
  }

  showHistory(){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.maxHeight = '600px';
    dialogConfig.width = '700px';
    dialogConfig.data = {
      inputUser: this.user,
      elementType: 3
    }
    this.dialog.open(HistoryComponent, dialogConfig);
  }

}
