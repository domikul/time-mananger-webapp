import {Component, Input, OnDestroy, OnInit, Output, EventEmitter} from '@angular/core';
import {UserService} from '../../_services/user.service';
import {TimerWrapper} from '../timer-wrapper';
import {Observable, Subject, Subscription} from 'rxjs';
import {Task} from '../../_models/task';
import {TimerService} from '../../_services/timer.service';

@Component({
  selector: 'app-timer',
  templateUrl: './timer.component.html',
  styleUrls: ['./timer.component.scss']
})
export class TimerComponent implements OnInit, OnDestroy {

  @Input() timer: TimerWrapper;
  @Input() own: boolean;
  @Input() second$: Observable<number>;
  @Input() suspendNotifier: Subject<boolean>;
  @Input() stopNotifier: Subject<any>;
  @Input() sureSuspended = false;
  @Output() incremented = new EventEmitter();
  second: Subscription = new Subscription();

  constructor(public userService: UserService,
              private timerService: TimerService) { }

  ngOnInit(): void {
    if (!this.timer.isSuspended)
      this.doSubscribe();

    this.suspendNotifier.subscribe(isSuspending => {
      if(isSuspending) {
        if (!this.timer.isSuspended)
          this.timer.makeSuspended();
        this.second.unsubscribe();
      }
      this.sureSuspended = isSuspending;
    });

    this.stopNotifier?.subscribe(x => {
      this.timer.makeEmpty();
      this.second.unsubscribe();
    });

  }

  private doSubscribe() {
    this.second = this.second$.subscribe(x => {
      this.timer.timerTime += 1;
      this.incremented.emit();
    });
  }

  startTimer() {
    if (this.timer.isEmpty)
      this.timerService.startTimer(this.timer).subscribe(x => {
        this.timer = new TimerWrapper(x);
        this.doSubscribe();
      });
    else if (this.timer.isSuspended)
      this.timerService.resumeTimer(this.timer.idTimer).subscribe(x => {
        this.timer = new TimerWrapper(x);
        this.doSubscribe();
      });
  }

  suspendTimer() {
    if(!this.timer.isSuspended)
      this.timerService.suspendTimer(this.timer.idTimer).subscribe(x => {
        this.timer = new TimerWrapper(x);
        this.second.unsubscribe();
      });

  }

  ngOnDestroy(): void {
    if(!this.second.closed)
      this.second.unsubscribe();

    this.suspendNotifier.unsubscribe();
    this.stopNotifier?.unsubscribe();
  }

  stopTimer() {
    this.timerService.stopTimer(this.timer.idTimer).subscribe(x => {
      this.timer.makeEmpty();
      this.second.unsubscribe();
    });

  }
}
