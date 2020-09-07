package pl.wasko.time.manager.rest.api.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wasko.time.manager.rest.api.exception.NotAuthorizedActionException;
import pl.wasko.time.manager.rest.api.exception.ResourceNotFoundException;
import pl.wasko.time.manager.rest.api.exception.WrongRequestException;
import pl.wasko.time.manager.rest.api.helper.JwtHelper;
import pl.wasko.time.manager.rest.api.model.entity.*;
import pl.wasko.time.manager.rest.api.model.repository.EmailRepository;
import pl.wasko.time.manager.rest.api.model.repository.SubscriptionRepository;
import pl.wasko.time.manager.rest.api.model.repository.TaskRepository;
import pl.wasko.time.manager.rest.api.model.response.SubscriptionRestModel;
import pl.wasko.time.manager.rest.api.model.response.DetailedSubscriptionRestModel;
import pl.wasko.time.manager.rest.api.scheduling.MailScheduleDataHolder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static pl.wasko.time.manager.rest.api.exception.ExceptionMessage.*;

@Component
@AllArgsConstructor
public class SubscriptionService implements SubscriptionManager {

    private final SubscriptionRepository subscriptionRepository;
    private final EmailRepository emailRepository;
    private final TaskRepository taskRepository;
    private final JwtHelper jwtHelper;
    private final MailScheduleDataHolder mailScheduleDataHolder;

    @Override
    public void deleteSubscriptions(List<Subscription> subscriptionsToDelete) {

        if(subscriptionsToDelete.size() == 1) //singletonList case
            mailScheduleDataHolder.onSingleSubscriptionDeleting(subscriptionsToDelete.get(0));

        List<Subscription> subscriptions = subscriptionsToDelete.stream()
                .peek(sub -> {
                    Task task = sub.getTask();
                    List<Subscription> ss = task.getListOfSubscriptions();
                    ss.remove(sub);
                })
                .collect(Collectors.toList());
        subscriptionRepository.deleteAll(subscriptions);
    }

    @Override
    public void onEmailUpdated(Email oldEmail, Email newEmail) {
        List<Subscription> subscriptions = oldEmail.getListOfSubscriptions().stream()
                .filter(it -> it.getUser() == null)
                .peek(it -> {
                    it.setEmail(newEmail);
                    mailScheduleDataHolder.onSubscriptionEmailUpdated(it, oldEmail);
                })
                .collect(Collectors.toList());

        subscriptionRepository.saveAll(subscriptions);

    }

    @Override
    public void onTimerAdded(Timer timer) {
        try {
            doAddSubscription(timer.getTask(), timer.getUser().getEmail(), null); //auto subscription
        } catch (WrongRequestException e) { //user already had a subscription to this task
            Subscription subscription = subscriptionRepository.findByEmailAndTask(timer.getUser().getEmail(), timer.getTask());
            subscription.setUser(null); //make that subscription 'auto'
            subscriptionRepository.save(subscription);
        }
    }

    @Override
    public void onSingleTimerDeleting(Timer timer) {

        timer.getTask().getListOfSubscriptions()
                .stream()
                .filter(it -> it.getEmail().equals(timer.getUser().getEmail()))
                .findFirst().ifPresent(value -> deleteSubscriptions(Collections.singletonList(value)));
    }

    private Subscription doAddSubscription(Task task, Email email, User user) {
        if(subscriptionRepository.existsByEmailAndTask(email, task))
            throw new WrongRequestException(ALREADY_SUBSCRIBED.getMessage());

        Subscription subscription = new Subscription();
        subscription.setTask(task);
        subscription.setEmail(email);
        subscription.setUser(user);
        mailScheduleDataHolder.onSubscriptionAdded(subscription);
        return subscriptionRepository.save(subscription);
    }

    public SubscriptionRestModel addSubscription(SubscriptionRestModel subscriptionRestModel, String token) {

        Task task = taskRepository.findById(subscriptionRestModel.getTaskId()).orElseThrow(() ->
                new ResourceNotFoundException(TASK_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);
        if(!task.isUserCoOwner(currentUser))
            throw new NotAuthorizedActionException(NOT_ALLOWED_ADD_SUBSCRIPTION.getMessage());

        Email email = emailRepository.findById(subscriptionRestModel.getEmailId()).orElseThrow(() ->
                new ResourceNotFoundException(EMAIL_NOT_FOUND.getMessage()));

        return new SubscriptionRestModel(doAddSubscription(task, email, currentUser));
    }

    public void deleteSubscription(Integer idSub, String token) {
        Subscription subscription = subscriptionRepository.findByIdSubAndUserNotNull(idSub).orElseThrow(() ->
                new ResourceNotFoundException(SUBSCRIPTION_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);
        if (!subscription.isUserOwner(currentUser) && !subscription.getEmail().equals(currentUser.getEmail()))
            throw new NotAuthorizedActionException(NOT_ALLOWED_DELETE_SUBSCRIPTION.getMessage());

        deleteSubscriptions(Collections.singletonList(subscription));

    }

    public List<SubscriptionRestModel> getTaskSubscriptions(Integer taskId, String token) {
        Task task = taskRepository.findById(taskId).orElseThrow(() ->
                new ResourceNotFoundException(TASK_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);
        if(!task.isUserCoOwner(currentUser))
            throw new NotAuthorizedActionException(NOT_AUTHORIZED_TO_TASK.getMessage());

        return task.getListOfSubscriptions().stream().map(SubscriptionRestModel::new).collect(Collectors.toList());
    }

    public List<DetailedSubscriptionRestModel> getOwnSubscriptions(String token) {
        User currentUser = jwtHelper.getUserFromToken(token);

        return currentUser.getListOfSubscriptions().stream()
                .map(DetailedSubscriptionRestModel::new)
                .collect(Collectors.toList());
    }

    public List<DetailedSubscriptionRestModel> getSharedSubscriptions(String token) {
        User currentUser = jwtHelper.getUserFromToken(token);

        return currentUser.getEmail().getListOfSubscriptions().stream()
                .filter(it -> it.getUser() != null)
                .map(DetailedSubscriptionRestModel::new)
                .collect(Collectors.toList());
    }
}
