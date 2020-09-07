import { Component, OnInit } from '@angular/core';
import {Task} from '../_models/task';
import {TaskService} from '../_services/task.service';

@Component({
  selector: 'app-shared-tasks',
  templateUrl: './shared-tasks.component.html',
  styleUrls: ['./shared-tasks.component.scss']
})
export class SharedTasksComponent implements OnInit {

  tasks: Task[];

  constructor(private taskService: TaskService) { }

  ngOnInit(): void {
    this.taskService.getSharedTasks().subscribe(x => {
      this.tasks = x;
    });
  }

}
