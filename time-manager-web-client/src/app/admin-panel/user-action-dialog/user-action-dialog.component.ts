import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {ActionMode} from '../../_helpers/action.mode';
import {User} from '../../_models/user';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {TaskActionDialogData} from '../../task-action-dialog/task-action-dialog.component';
import {PositionService} from '../../_services/position.service';
import {RoleService} from '../../_services/role.service';
import {UserService} from '../../_services/user.service';
import {take} from 'rxjs/operators';
import {MatSelect} from '@angular/material/select';

export interface UserActionDialogData {
  user: User;
  mode: ActionMode;
}

@Component({
  selector: 'app-user-action-dialog',
  templateUrl: './user-action-dialog.component.html',
  styleUrls: ['./user-action-dialog.component.scss']
})
export class UserActionDialogComponent implements OnInit {

  actionMode = ActionMode;
  @ViewChild('roleSelect') roleSelect: MatSelect;
  @ViewChild('positionSelect') positionSelect: MatSelect;

  constructor(public dialogRef: MatDialogRef<UserActionDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: UserActionDialogData,
              public positionService: PositionService,
              public roleService: RoleService,
              private userService: UserService
  ) { }

  ngOnInit(): void {
  }

  submit() {
    switch (this.data.mode) {

      case ActionMode.edit:
        this.userService.updateUser(this.data.user).pipe(take(1)).subscribe(x => {
          this.dialogRef.close(this.data);
        });
        break;

      case ActionMode.create:
        this.userService.registerUser(this.data.user).pipe(take(1)).subscribe(x => {
          this.dialogRef.close(this.data);
        });
        break;
    }
  }

  cancel() {
    this.dialogRef.close(this.data);
  }
}
