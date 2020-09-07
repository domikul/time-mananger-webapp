package pl.wasko.time.manager.rest.api.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wasko.time.manager.rest.api.exception.NotAuthorizedActionException;
import pl.wasko.time.manager.rest.api.exception.ResourceNotFoundException;
import pl.wasko.time.manager.rest.api.exception.WrongRequestException;
import pl.wasko.time.manager.rest.api.helper.JwtHelper;
import pl.wasko.time.manager.rest.api.helper.TimeHelper;
import pl.wasko.time.manager.rest.api.model.entity.*;
import pl.wasko.time.manager.rest.api.model.entity.Timer;
import pl.wasko.time.manager.rest.api.model.enumeration.StatusEnum;
import pl.wasko.time.manager.rest.api.model.repository.*;
import pl.wasko.time.manager.rest.api.model.response.TimerRestModel;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static pl.wasko.time.manager.rest.api.exception.ExceptionMessage.*;

@Component
@AllArgsConstructor
public class TimerService implements TimerDeleter {

    private final TimerRepository timerRepository;
    private final TimerStepRepository timerStepRepository;
    private final TaskRepository taskRepository;
    private final JwtHelper jwtHelper;
    private final StatusRepository statusRepository;
    private final SubscriptionManager subscriptionManager;

    // must be timers from the same task
    @Override
    public void deleteTimers(List<Timer> timersToDelete) {

        if(timersToDelete.isEmpty())
            return;

        // always the same task for whole list
        Task task = timersToDelete.get(0).getTask();
        final Duration[] timersDuration = {Duration.ZERO};
        List<Timer> timers = timersToDelete.stream().peek(timer -> {

            // already sorted
            List<TimerStep> timerSteps = timer.getListOfTimerSteps();

            if(timer.getEndTime() == null)
                timer.setEndTime(new Date());
            else if(!timerSteps.isEmpty()){
               TimerStep lastStep = timerSteps.get(timerSteps.size() - 1);
               if(lastStep.getEndTime() == null) {
                   lastStep.setEndTime(new Date());
                   timerStepRepository.save(lastStep);
               }
            }
            timersDuration[0] = timersDuration[0].plus(Duration.between(timer.getStartTime().toInstant(),
                    timer.getEndTime().toInstant()));

            for(TimerStep step : timerSteps) {
                timersDuration[0] = timersDuration[0].plus(Duration.between(step.getStartTime().toInstant(),
                        step.getEndTime().toInstant()));
            }


            timer.setDeleted(true);


        }).collect(Collectors.toList());


        Duration totalTime = Duration.ofSeconds(
                TimeHelper.timeAsSeconds(task.getTotalTime()))
                .plus(timersDuration[0]);

        task.setTotalTime(TimeHelper.secondsAsTime(totalTime.getSeconds()));
        taskRepository.save(task);
        if(timersToDelete.size() == 1) //singletonList case
            subscriptionManager.onSingleTimerDeleting(timers.get(0));

        timerRepository.saveAll(timers);
    }

    @Override
    public void suspendTimers(List<Timer> timersToSuspend) {

        List<TimerStep> timerStepsToSave = new ArrayList<>();
        timersToSuspend = timersToSuspend.stream().peek(timer -> {

            if(timer.getEndTime() == null) {
                timer.setEndTime(new Date());
            }
            else {
                List<TimerStep> timerSteps = timer.getListOfTimerSteps();
                if(!timerSteps.isEmpty()) {
                    TimerStep lastStep = timerSteps.get(timerSteps.size() - 1);
                    if (lastStep.getEndTime() == null) {
                        lastStep.setEndTime(new Date());
                        timerStepsToSave.add(lastStep);
                    }
                }
            }
        }).collect(Collectors.toList());

        timerStepRepository.saveAll(timerStepsToSave);
        timerRepository.saveAll(timersToSuspend);
    }

    public TimerRestModel startNewTimer(TimerRestModel timerRestModel, String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        Task task = taskRepository.findById(timerRestModel.getTaskId()).orElseThrow(() ->
            new ResourceNotFoundException(TASK_NOT_FOUND.getMessage())
        );

        if((task.isUserOwner(currentUser) || task.isUserCoOwner(currentUser)) && task.isTaskActive()) {

            if(task.getListOfTimers().stream().map(Timer::getUser).anyMatch(it -> it.equals(currentUser)))
                throw new WrongRequestException(CANNOT_ADD_MORE_THAN_ONE_TIMER.getMessage());

            if(task.getStatus().getStatusName().equals(StatusEnum.NEW))
                task.setStatus(statusRepository.findByStatusName(StatusEnum.IN_PROGRESS));
            Timer timer = new Timer();
            timer.setStartTime(new Date());
            timer.setTask(task);
            timer.setUser(currentUser);
            timer.setDeleted(false);
            subscriptionManager.onTimerAdded(timer);
            return new TimerRestModel(timerRepository.save(timer));
        }
        else
            throw new NotAuthorizedActionException(NOT_ALLOWED_START_TIMER.getMessage());

    }

    public void stopTimer(Integer idTimer, String token) {
        Timer timer = timerRepository.findById(idTimer).orElseThrow(() ->
                new ResourceNotFoundException(TIMER_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);

        if(timer.isUserOwner(currentUser)) {
            deleteTimers(Collections.singletonList(timer));
        }
        else
            throw new NotAuthorizedActionException(NOT_ALLOWED_STOP_TIMER.getMessage());
    }

    public TimerRestModel suspendTimer(Integer idTimer, String token) {
        Timer timer = timerRepository.findById(idTimer).orElseThrow(() ->
                new ResourceNotFoundException(TIMER_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);
        if(timer.isUserOwner(currentUser)) {
            suspendTimers(Collections.singletonList(timer));
            return new TimerRestModel(timer);
        }
        else
            throw new NotAuthorizedActionException(NOT_ALLOWED_SUSPEND_TIMER.getMessage());
    }

    public TimerRestModel resumeTimer(Integer idTimer, String token) {
        Timer timer = timerRepository.findById(idTimer).orElseThrow(() ->
                new ResourceNotFoundException(TIMER_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);
        if(timer.isUserOwner(currentUser)) {
            List<TimerStep> timerSteps = timer.getListOfTimerSteps();
            if((timerSteps.isEmpty() && timer.getEndTime() != null) || timerSteps.get(timerSteps.size() - 1).getEndTime() != null) {
                TimerStep timerStep = new TimerStep();
                timerStep.setTimer(timer);
                timerStep.setStartTime(new Date());
                timerSteps.add(timerStep);
                timerStepRepository.save(timerStep);
            }
            return new TimerRestModel(timer);
        }
        else
            throw new NotAuthorizedActionException(NOT_ALLOWED_RESUME_TIMER.getMessage());
    }
}
