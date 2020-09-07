import {Component, Input, OnInit, Output, EventEmitter} from '@angular/core';
import {Bucket} from '../_models/bucket';
import {TaskService} from '../_services/task.service';
import { Task } from '../_models/task';
import {AuthenticationService} from '../_services/authentication.service';
import {TaskActionDialogComponent, TaskActionDialogData} from '../task-action-dialog/task-action-dialog.component';
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {NewBucketDialogComponent} from "../new-bucket-dialog/new-bucket-dialog.component";
import {DialogConfigFactory} from '../_helpers/dialog-config-factory';
import {SharesDialogComponent} from '../shares-dialog/shares-dialog.component';
import {BucketService} from '../_services/bucket.service';
import {ActionMode} from '../_helpers/action.mode';
import {HistoryComponent} from "../history/history.component";
import {User} from "../_models/user";
import {UserService} from "../_services/user.service";
import {Observable} from "rxjs";



@Component({
  selector: 'app-bucket-details',
  templateUrl: './bucket-details.component.html',
  styleUrls: ['./bucket-details.component.scss']
})
export class BucketDetailsComponent implements OnInit {

  @Input() buttonsAllow: boolean;
  @Input() inputBucket: Bucket;
  @Output() backClicked = new EventEmitter<boolean>();
  @Output() saveClicked = new EventEmitter<Bucket>();
  tasks: Task[];

  backButtonClick(){
    this.backClicked.emit(true);
  }

  constructor(private taskService: TaskService,
              public userService: UserService,
              private authenticationService: AuthenticationService,
              private bucketService: BucketService,
              private dialog: MatDialog) { }


  ngOnInit(): void {

    this.taskService.getBucketTasks(this.inputBucket.idBucket).subscribe(x => {
      this.tasks = x;
    });
  }

  public get isAdministrated(): boolean {
    return !this.userService.isUserActive(this.inputBucket.userId) && this.authenticationService.currentUserValue.roleId === 1;
  }

  public get isOwn(): boolean {
    return this.authenticationService.currentUserValue.idUser === this.inputBucket.userId;
  }

  openDialog() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.height = '400px';
    dialogConfig.width = '700px';
    dialogConfig.data = {
      inputBucket: Bucket.makeCopy(this.inputBucket),
      type: false,
      sendButtonsAllow: this.buttonsAllow
    }
    this.dialog.open(NewBucketDialogComponent, dialogConfig).afterClosed().subscribe(bucket => {
        if (bucket != undefined){
          this.inputBucket = bucket;
          this.saveClicked.emit(this.inputBucket);
        }
    });

  }

  launchNewTask() {
    const task = new Task();
    task.bucketId = this.inputBucket.idBucket;

    this.dialog.open(TaskActionDialogComponent, DialogConfigFactory.for(TaskActionDialogComponent,
     {
      task,
      own: true,
      mode: ActionMode.create,
      didAction: false
    })).afterClosed().subscribe((x: TaskActionDialogData) => {

      if(x.didAction)
        this.tasks.push(x.task);

    });
  }

  launchShares() {
    this.dialog.open(SharesDialogComponent, DialogConfigFactory.for(SharesDialogComponent,
    {
      shareable: Bucket.makeCopy(this.inputBucket),
      shareableService: this.bucketService
    }));

    //TODO deleting timers or tasks
  }

  showHistory(){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.maxHeight = '600px';
    dialogConfig.width = '700px';
    dialogConfig.data = {
      inputBucket: this.inputBucket,
      elementType: 1
    }
    this.dialog.open(HistoryComponent, dialogConfig);

  }


}
