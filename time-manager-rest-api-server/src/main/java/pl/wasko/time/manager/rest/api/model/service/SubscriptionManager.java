package pl.wasko.time.manager.rest.api.model.service;

import pl.wasko.time.manager.rest.api.model.entity.Email;
import pl.wasko.time.manager.rest.api.model.entity.Subscription;
import pl.wasko.time.manager.rest.api.model.entity.Timer;

import java.util.List;

public interface SubscriptionManager {

    void deleteSubscriptions(List<Subscription> subscriptionsToDelete);

    void onEmailUpdated(Email oldEmail, Email newEmail);

    void onTimerAdded(Timer timer);

    void onSingleTimerDeleting(Timer timer);

}
