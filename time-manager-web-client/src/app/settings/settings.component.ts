import { Component, OnInit } from '@angular/core';
import {User} from '../_models/user';
import {AuthenticationService} from '../_services/authentication.service';
import {UserService} from '../_services/user.service';
import {Router} from '@angular/router';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {LogoutAlertDialogComponent} from './logout-alert-dialog/logout-alert-dialog.component';
import {DialogConfigFactory} from '../_helpers/dialog-config-factory';
import {MatSnackBar} from '@angular/material/snack-bar';
import {HistoryComponent} from "../history/history.component";
import {PositionService} from '../_services/position.service';
import {RoleService} from '../_services/role.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {

  currentUser: User;
  private oldEmail: string;

  constructor(
    private authenticationService: AuthenticationService,
    private userService: UserService,
    public positionService: PositionService,
    public roleService: RoleService,
    private router: Router,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.currentUser = User.makeCopy(this.authenticationService.currentUserValue);
    this.oldEmail = String(this.currentUser.email);
  }

  submit() {
    this.userService.updateUser(this.currentUser).subscribe(x => {

      this.snackBar.open('Account updated', 'OK', {duration: 5000, panelClass: 'success-snackbar'});

      if(this.oldEmail !== x.email)
        this.dialog.open(LogoutAlertDialogComponent, DialogConfigFactory.for(LogoutAlertDialogComponent))
          .afterClosed().subscribe(y => {
            this.authenticationService.logout();
            this.router.navigate(['/login']);
          });
      else
        this.authenticationService.onUserUpdated(x);
    });

  }

  showHistory(){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.maxHeight = '600px';
    dialogConfig.width = '700px';
    dialogConfig.data = {
      inputUser: this.currentUser,
      elementType: 3
    }
    this.dialog.open(HistoryComponent, dialogConfig);
  }
}
