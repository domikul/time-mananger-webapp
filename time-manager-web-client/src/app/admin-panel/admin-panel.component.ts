import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserService} from '../_services/user.service';
import {User} from '../_models/user';
import {take, takeUntil} from 'rxjs/operators';
import {AuthenticationService} from '../_services/authentication.service';
import {MatDialog} from '@angular/material/dialog';
import {UserActionDialogComponent, UserActionDialogData} from './user-action-dialog/user-action-dialog.component';
import {DialogConfigFactory} from '../_helpers/dialog-config-factory';
import {ActionMode} from '../_helpers/action.mode';
import {Subject} from 'rxjs';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss']
})
export class AdminPanelComponent implements OnInit, OnDestroy {

  users: User[];
  teamMembers: User[];
  managers: User[];
  admins: User[];
  unsubscribe$ = new Subject();

  constructor(
    private userService: UserService,
    private authenticationService: AuthenticationService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.userService.getUsers().pipe(takeUntil(this.unsubscribe$)).subscribe(x => {
      this.users = x;
      this.admins = x.filter(y => y.roleId === 1 && y.idUser !== this.authenticationService.currentUserValue.idUser);
      this.managers = x.filter(y => y.roleId === 2);
      this.teamMembers = x.filter(y => y.roleId === 3);
    });
  }

  launchNewUser() {
    this.dialog.open(UserActionDialogComponent, DialogConfigFactory.for(UserActionDialogComponent, {
      user: new User(),
      mode: ActionMode.create
    }));
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }
}
