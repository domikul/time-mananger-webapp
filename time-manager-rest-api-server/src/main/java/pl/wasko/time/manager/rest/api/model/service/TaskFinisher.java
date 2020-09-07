package pl.wasko.time.manager.rest.api.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wasko.time.manager.rest.api.model.entity.Task;
import pl.wasko.time.manager.rest.api.scheduling.MailScheduleDataHolder;

import java.util.Collections;

@Component
@AllArgsConstructor
public class TaskFinisher {

    private final TimerDeleter timerDeleter;
    private final SubscriptionManager subscriptionManager;
    private final MailScheduleDataHolder mailScheduleDataHolder;

    public void onTaskFinished(Task task) {
        timerDeleter.deleteTimers(task.getListOfTimers());
        task.setListOfTimers(Collections.emptyList());
        subscriptionManager.deleteSubscriptions(task.getListOfSubscriptions());
        task.setListOfSubscriptions(Collections.emptyList());
        mailScheduleDataHolder.onTaskDeleting(task);
    }
}
