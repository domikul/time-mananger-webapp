import {Timer} from './timer';
import {Shareable} from './shareable';

export class Task implements Shareable {

  idTask: number;

  bucketId: number;

  userId: number;

  statusId: number;

  priorityId: number;

  description: string;

  taskName: string;

  deadline: Date;

  beginTime: Date;

  estimatedEndTime: string;

  totalTime: string;

  timers: Timer[];

  public static makeCopy(task: Task): Task {
    const copied = new Task();

    copied.idTask = task.idTask;
    copied.bucketId = task.bucketId;
    copied.userId = task.userId;
    copied.statusId = task.statusId;
    copied.priorityId = task.priorityId;
    copied.taskName = task.taskName;
    copied.description = task.description;
    copied.deadline = task.deadline;
    copied.beginTime = task.beginTime;
    copied.totalTime = task.totalTime;
    copied.estimatedEndTime = task.estimatedEndTime;
    copied.timers = task.timers;

    return copied;
  }

  getShareableId() {
    return this.idTask;
  }

  getShareableName() {
    return this.taskName;
  }

}
