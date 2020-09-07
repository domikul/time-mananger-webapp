import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Task} from '../_models/task';
import {DatePipe} from '@angular/common';
import {StatusService} from '../_services/status.service';
import {PriorityService} from '../_services/priority.service';
import {Status} from '../_models/status';
import {Observable} from 'rxjs';
import {Priority} from '../_models/priority';
import {TaskService} from '../_services/task.service';
import {ActionMode} from '../_helpers/action.mode';
import {MatSelect} from '@angular/material/select';

export interface TaskActionDialogData {
  task: Task;
  own: boolean;
  mode: ActionMode;
  didAction: boolean;
}

@Component({
  selector: 'app-edit-task-dialog',
  templateUrl: './task-action-dialog.component.html',
  styleUrls: ['./task-action-dialog.component.scss']
})
export class TaskActionDialogComponent implements OnInit {

  taskActionMode = ActionMode;
  formDeadline: string;
  statuses$: Observable<Status[]>;
  priorities$: Observable<Priority[]>;
  @ViewChild('prioritySelect') prioritySelect: MatSelect;

  constructor(public dialogRef: MatDialogRef<TaskActionDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: TaskActionDialogData,
              private statusService: StatusService,
              private priorityService: PriorityService,
              private taskService: TaskService) { }

  ngOnInit(): void {
    if(this.data.mode === ActionMode.edit)
      this.formDeadline = new DatePipe('en-US').transform(this.data.task.deadline, 'yyyy-MM-ddTHH:mm');
    else
      this.formDeadline = new DatePipe('en-US').transform(new Date(), 'yyyy-MM-ddTHH:mm');

    this.statuses$ = this.statusService.getStatuses();
    this.priorities$ = this.priorityService.getPriorities();
  }

  private finish(task: Task) {
    this.data.didAction = true;
    this.data.task = task;
    this.dialogRef.close(this.data);
  }

  submit() {
    this.data.task.deadline = new Date(new Date(this.formDeadline).toISOString());

    switch(this.data.mode) {
      case ActionMode.edit:
        this.taskService.updateTask(this.data.task).subscribe(x => {
          this.finish(x);
        });
        break;

      case ActionMode.create:
        this.taskService.createTask(this.data.task).subscribe(x => {
          this.finish(x);
        });
        break;
    }

  }

  cancel() {
    this.dialogRef.close(this.data);
  }
}
