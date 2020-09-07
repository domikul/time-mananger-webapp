import {Component, Input, OnInit} from '@angular/core';
import { Task } from '../_models/task';
import {interval} from 'rxjs';
import {share, shareReplay} from 'rxjs/operators';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss']
})
export class TaskListComponent implements OnInit {

  @Input() tasks: Task[];
  @Input() sureOwn: boolean;
  second$ = interval(1000).pipe(share());

  constructor() { }

  onTaskDeleted(task: Task) {
    const deletedTask = this.tasks.find(x => x.idTask === task.idTask);
    this.tasks.splice(this.tasks.indexOf(deletedTask), 1);
  }

  ngOnInit(): void {

  }

}
