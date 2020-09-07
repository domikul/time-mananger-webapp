package pl.wasko.time.manager.rest.api.scheduling;

import pl.wasko.time.manager.rest.api.model.entity.Email;
import pl.wasko.time.manager.rest.api.model.entity.Subscription;
import pl.wasko.time.manager.rest.api.model.entity.Task;
import pl.wasko.time.manager.rest.api.model.entity.User;

public interface MailScheduleDataHolder {

    void onTaskAdded(Task task);

    void onTaskUpdated(Task task);

    void onTaskDeleting(Task task);

    void onSubscriptionAdded(Subscription subscription);

    void onSubscriptionEmailUpdated(Subscription subscription, Email oldEmail);

    void onSingleSubscriptionDeleting(Subscription subscription);

    void onSingleUserTimerDeleting(Task task, User user);

    void onEmailChanged(Email email);

    void onEmailDeleting(Email email);

    void onEmailAdded(Email email);

}
