import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from '../../_models/user';
import {Subject} from 'rxjs';
import {UserService} from '../../_services/user.service';
import {AuthenticationService} from '../../_services/authentication.service';
import {MatDialog} from '@angular/material/dialog';
import {takeUntil} from 'rxjs/operators';
import {UserActionDialogComponent} from '../user-action-dialog/user-action-dialog.component';
import {DialogConfigFactory} from '../../_helpers/dialog-config-factory';
import {ActionMode} from '../../_helpers/action.mode';

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss']
})
export class UsersListComponent implements OnInit, OnDestroy {

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
