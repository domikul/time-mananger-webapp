import {Timer} from '../_models/timer';

export class TimerWrapper extends Timer {
  timerTime: number;

  constructor(timer: Timer) {
    super();
    this.idTimer = timer.idTimer;
    this.startTime = timer.startTime;
    this.endTime = timer.endTime;
    this.userId = timer.userId;
    this.taskId = timer.taskId;
    this.timerSteps = timer.timerSteps;

    if(this.isEmpty) {
      this.timerTime = 0;
      return;
    }

    let endDate: Date;
    if(timer.endTime != null)
       endDate = new Date(timer.endTime);
    else
      endDate = new Date();

    let time = endDate.getTime() - new Date(timer.startTime).getTime();

    for(const timerStep of timer.timerSteps) {
      if(timerStep.endTime != null)
        endDate = new Date(timerStep.endTime);
      else
        endDate = new Date();
      time += endDate.getTime() - new Date(timerStep.startTime).getTime();
    }


    this.timerTime = Math.floor(time / 1000);
  }

  public get isEmpty(): boolean {
    return this.idTimer === -1;
  }

  public get isSuspended(): boolean {
    if(this.isEmpty)
      return true;

    if(this.timerSteps.length === 0)
      return this.endTime != null;
    else
      return this.timerSteps[this.timerSteps.length - 1].endTime != null;
  }

  public makeEmpty() {
    this.idTimer = -1;
    this.timerTime = 0;
  }

  makeSuspended() {
    if(this.timerSteps.length === 0)
      return this.endTime = new Date();
    else
      return this.timerSteps[this.timerSteps.length - 1].endTime = new Date();
  }
}
