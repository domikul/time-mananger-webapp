import {Component, Input, OnInit, Output, EventEmitter, OnDestroy} from '@angular/core';
import {Task} from '../_models/task';
import {Status} from '../_models/status';
import {StatusService} from '../_services/status.service';
import {take} from 'rxjs/operators';
import {Priority} from '../_models/priority';
import {PriorityService} from '../_services/priority.service';
import {Timer} from '../_models/timer';
import {TimerWrapper} from './timer-wrapper';
import {AuthenticationService} from '../_services/authentication.service';
import {User} from '../_models/user';
import {TimeStringPipe, TransformDirection} from '../_helpers/time-string.pipe';
import {Observable, Subject, Subscription} from 'rxjs';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {TaskActionDialogComponent, TaskActionDialogData} from '../task-action-dialog/task-action-dialog.component';
import {AlertDialogComponent} from '../alert-dialog/alert-dialog.component';
import {TaskService} from '../_services/task.service';
import {SharesDialogComponent, SharesDialogData} from '../shares-dialog/shares-dialog.component';
import {DialogConfigFactory} from '../_helpers/dialog-config-factory';
import {environment} from '../../environments/environment';
import {Share} from '../_models/share';
import {ActionMode} from '../_helpers/action.mode';
import {HistoryComponent} from "../history/history.component";
import {NewSubscriptionDialogComponent} from './new-subscription-dialog/new-subscription-dialog.component';
import {EndRequestDialogComponent} from './end-request-dialog/end-request-dialog.component';
import {UserService} from '../_services/user.service';

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.scss']
})
export class TaskComponent implements OnInit, OnDestroy {

  transformDirection = TransformDirection;
  @Input() task: Task;
  @Input() sureOwn;
  @Input() second$: Observable<number>;
  second = new Subscription();
  @Output() deleted = new EventEmitter<Task>();
  suspendNotifier = new Subject<boolean>();
  stopNotifier = new Subject<any>();
  status: Status;
  priority: Priority;
  currentUser: User;
  totalSeconds = 0;
  timerWrappers: TimerWrapper[];
  expanded = false;

  constructor(private statusService: StatusService,
              private priorityService: PriorityService,
              private authenticationService: AuthenticationService,
              private taskService: TaskService,
              public userService: UserService,
              private dialog: MatDialog) { }

  ngOnInit(): void {
    this.setStatus();
    this.setPriority();
    this.currentUser = this.authenticationService.currentUserValue;

    if(!this.isAdministrated)
      this.prepareOwnTimer();
    this.totalSeconds = new TimeStringPipe().transform(this.task.totalTime, TransformDirection.toSeconds);
    this.timerWrappers = this.task.timers.map(timer => this.timerWrapper(timer));

    this.second = this.second$.subscribe(x => {
      if(new Date().getTime() > new Date(this.task.deadline).getTime()) {
        this.task.statusId = 4;
        this.setStatus();
        this.second.unsubscribe();
      }
    });
  }

  private prepareOwnTimer() {
    const userTimerIndex = this.task.timers.findIndex(x => x.userId === this.currentUser.idUser);
    if(userTimerIndex !== -1)
      this.task.timers.unshift(this.task.timers.splice(userTimerIndex, 1)[0]);
    else {
      const timer = new Timer();
      timer.userId = this.currentUser.idUser;
      timer.idTimer = -1;
      timer.taskId = this.task.idTask;
      this.task.timers.unshift(timer);
    }

  }

  private setStatus() {
    this.statusService.getStatus(this.task.statusId).pipe(take(1)).subscribe(x => {
      this.status = x;
    });
  }

  private setPriority() {
    this.priorityService.getPriority(this.task.priorityId).pipe(take(1)).subscribe(x => {
      this.priority = x;
    });
  }

  onTimerIncremented() {
    if(this.status.idStatus === 1) {
      this.task.statusId = 2; //change to in progress
      this.setStatus();
    }

    this.totalSeconds++;
  }

  get isAdministrated(): boolean {
    return !this.userService.isUserActive(this.task.userId) && this.currentUser.roleId === 1;
  }

  get isStrictOwn(): boolean {
    return this.task.userId === this.currentUser.idUser || this.isAdministrated;
  }

  get isOwn(): boolean {
    return this.sureOwn || this.isStrictOwn;
  }

  private timerWrapper(timer: Timer) {
    const wrapper = new TimerWrapper(timer);
    this.totalSeconds += wrapper.timerTime;
    return wrapper;
  }

  launchEdit() {
    this.dialog.open(TaskActionDialogComponent, DialogConfigFactory.for(TaskActionDialogComponent,
      {
        task: Task.makeCopy(this.task),
        own: this.isOwn,
        mode: ActionMode.edit,
        didAction: false
    })).afterClosed().subscribe((x: TaskActionDialogData) => {
      if(x.didAction) {
        this.task = x.task;
        this.setStatus();
        this.setPriority();

        if(this.status.idStatus === 3)
          this.suspendNotifier.next(true);
        else
          this.suspendNotifier.next(false);
      }

    });
  }

  deleteTask() {

    this.dialog.open(AlertDialogComponent, DialogConfigFactory.for(AlertDialogComponent))
      .afterClosed().subscribe((result: boolean) => {
      if(result)
        this.taskService.deleteTask(this.task.idTask).subscribe(x => {
          this.deleted.emit(this.task);
        });
    });
  }

  launchShares() {

    let deleteSub: Subscription;
    const dialogRef = this.dialog.open(SharesDialogComponent, DialogConfigFactory.for(SharesDialogComponent,
      {
        shareable: Task.makeCopy(this.task),
        shareableService: this.taskService
      }));

    dialogRef.afterOpened().subscribe(x => {

        deleteSub = dialogRef.componentInstance.deletionNotifier.subscribe((share: Share) => {
          const deletedTimerWrapper = this.timerWrappers.find(y => y.userId === share.userId);
          if(deletedTimerWrapper !== undefined)
            this.timerWrappers.splice(this.timerWrappers.indexOf(deletedTimerWrapper), 1);
        });
    });

    dialogRef.afterClosed().subscribe(x => {
      deleteSub.unsubscribe();
    });

  }

  launchNewSubscription() {
    this.dialog.open(NewSubscriptionDialogComponent, DialogConfigFactory.for(NewSubscriptionDialogComponent, {
      task: this.task
    }));
  }

  launchEndRequest() {
    this.dialog.open(EndRequestDialogComponent, DialogConfigFactory.for(EndRequestDialogComponent, {
      task: this.task,
      own: this.isStrictOwn
    })).afterClosed().subscribe((didEndRequested: boolean) => {
      if(didEndRequested)
        this.stopNotifier.next();

    });
  }


  showHistory(){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.maxHeight = '600px';
    dialogConfig.width = '700px';
    dialogConfig.data = {
      inputTask: this.task,
      elementType: 2
    }
    this.dialog.open(HistoryComponent, dialogConfig);

  }

  ngOnDestroy(): void {
    if(!this.second.closed)
      this.second.unsubscribe();
  }

}
