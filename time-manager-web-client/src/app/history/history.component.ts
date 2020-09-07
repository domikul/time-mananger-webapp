import {Component, Inject, Input, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {History} from "../_models/history";
import {BucketService} from "../_services/bucket.service";
import {Bucket} from "../_models/bucket";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {OperationTypeService} from "../_services/operation-type.service";
import {UserService} from "../_services/user.service";
import {DatePipe} from "@angular/common";
import { Task } from '../_models/task';
import {TaskService} from "../_services/task.service";
import {User} from "../_models/user";
import {async} from "@angular/core/testing";


@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.scss']
})
export class HistoryComponent implements OnInit {

  dataTable: MatTableDataSource<History> = new MatTableDataSource<History>([]);
  bucket: Bucket;
  elementNumber: number;

  constructor(private bucketService: BucketService,
              private taskService: TaskService,
              public userService: UserService,
              private dialogRef: MatDialogRef<HistoryComponent>,
              public operationTypeService: OperationTypeService,
              @Inject(MAT_DIALOG_DATA) public data:{
                inputBucket: Bucket,
                inputTask: Task,
                inputUser: User,
                elementType: number
  }) { }

  ngOnInit(): void {
    this.bucket = this.data.inputBucket;
    this.elementNumber = this.data.elementType;
    console.log(this.elementNumber);
    if(this.elementNumber == 1) {
      this.bucketService.getBucketHistory(this.bucket.idBucket).subscribe(x => {
        this.dataTable.data = x;
      });
    }else if(this.elementNumber == 2){
      this.taskService.getTaskHistory(this.data.inputTask.idTask).subscribe(x => {
        this.dataTable.data = x;
      });
    } else if(this.elementNumber == 3){
      this.userService.getUserHistory(this.data.inputUser.idUser).subscribe(x => {
        this.dataTable.data = x;
      });
    }
  }


  displayedColumns: string[] = ['idHistory', 'modificatorId', 'modificationTime', 'operationType'];
  dataSource = this.dataTable;

  close() {
    this.dialogRef.close();
  }
}
