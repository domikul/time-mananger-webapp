package pl.wasko.time.manager.rest.api.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.wasko.time.manager.rest.api.helper.TimeHelper;
import pl.wasko.time.manager.rest.api.model.entity.Email;
import pl.wasko.time.manager.rest.api.model.entity.Subscription;
import pl.wasko.time.manager.rest.api.model.entity.Task;
import pl.wasko.time.manager.rest.api.model.entity.User;
import pl.wasko.time.manager.rest.api.model.repository.EmailRepository;
import pl.wasko.time.manager.rest.api.model.repository.TaskRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MailScheduleDataHolderImpl implements MailScheduleDataHolder {

    private final Map<Integer, String> emails = new ConcurrentHashMap<>();
    private final Map<Integer, MailScheduleData> taskSubsData = new ConcurrentHashMap<>();
    private final TaskRepository taskRepository;
    private final EmailRepository emailRepository;
    private final MailServiceImpl mailServiceImpl;

    private Date getSendDate(Task task) {
        return TimeHelper.addToDate(task.getDeadline(), "-" + task.getPriority().getNotificationTime());
    }

    //insert into cache if needed
    private void populateTaskSubscriptions(Task task, Date sendDate) {
        List<Subscription> taskSubscriptions = task.getListOfSubscriptions();

        if(!taskSubscriptions.isEmpty() && sendDate.after(new Date())) {

            taskSubsData.put(task.getIdTask(), new MailScheduleData(
                    sendDate,
                    taskSubscriptions.stream()
                            .map(subscription -> subscription.getEmail().getIdEmail())
                            .collect(Collectors.toList())
            ));

        }
    }

    @PostConstruct
    private void populateData() {

        taskRepository.findAll().forEach(task -> {
            Date sendDate = getSendDate(task);
            populateTaskSubscriptions(task, sendDate);
        });

        emails.putAll(emailRepository.findAll().stream().collect(Collectors.toConcurrentMap(Email::getIdEmail, Email::getEmailName)));
    }

    public MailScheduleDataHolderImpl(TaskRepository taskRepository, EmailRepository emailRepository, MailServiceImpl mailServiceImpl) {
        this.taskRepository = taskRepository;
        this.emailRepository = emailRepository;
        this.mailServiceImpl = mailServiceImpl;
    }

    private void addEmailToData(Task task, Email email) {
        Integer idTask = task.getIdTask();
        MailScheduleData taskMailScheduleData = taskSubsData.get(idTask);
        if(taskMailScheduleData != null)
            taskMailScheduleData.getEmailsIds().add(email.getIdEmail());
    }

    private void deleteEmailFromData(Task task, Email email) {
        Integer idTask = task.getIdTask();
        MailScheduleData taskMailScheduleData = taskSubsData.get(idTask);
        if(taskMailScheduleData != null)
            taskMailScheduleData.getEmailsIds().remove(email.getIdEmail());
    }

    @Override
    public void onTaskAdded(Task task) {
        taskSubsData.put(task.getIdTask(), new MailScheduleData(
                getSendDate(task),
                new ArrayList<>()
        ));
    }

    @Override
    public void onTaskUpdated(Task task) {
        Integer idTask = task.getIdTask();
        MailScheduleData taskMailScheduleData = taskSubsData.get(idTask);
        Date newSendDate = getSendDate(task);
        if(taskMailScheduleData != null)
            taskMailScheduleData.setSendDate(newSendDate);
         else
            populateTaskSubscriptions(task, newSendDate);

    }

    @Override
    public void onTaskDeleting(Task task) {
        taskSubsData.remove(task.getIdTask());
    }

    @Override
    public void onSubscriptionAdded(Subscription subscription) {
        addEmailToData(subscription.getTask(), subscription.getEmail());
    }

    @Override
    public void onSubscriptionEmailUpdated(Subscription subscription, Email oldEmail) {
        Email newEmail = subscription.getEmail();
        MailScheduleData taskMailScheduleData = taskSubsData.get(subscription.getTask().getIdTask());
        if(taskMailScheduleData != null) {
            taskMailScheduleData.setEmailsIds(taskMailScheduleData.getEmailsIds().stream()
                    .map(it -> {
                        if(it.equals(oldEmail.getIdEmail()))
                            it = newEmail.getIdEmail();
                        return it;
                    }).collect(Collectors.toList()));
        }

    }

    @Override
    public void onSingleSubscriptionDeleting(Subscription subscription) {
            deleteEmailFromData(subscription.getTask(), subscription.getEmail());
    }

    @Override
    public void onSingleUserTimerDeleting(Task task, User user) {
        MailScheduleData taskMailScheduleData = taskSubsData.get(task.getIdTask());
        if(taskMailScheduleData != null) {
            taskMailScheduleData.getEmailsIds().remove(user.getEmail().getIdEmail());
        }
    }

    @Override
    public void onEmailChanged(Email email) {
        emails.put(email.getIdEmail(), email.getEmailName());
    }

    @Override
    public void onEmailDeleting(Email email) {
        emails.remove(email.getIdEmail());
    }

    @Override
    public void onEmailAdded(Email email) {
        emails.put(email.getIdEmail(), email.getEmailName());
    }

    @Scheduled(fixedRate = 5000)
    public void checkSchedule() {
        Date currentDate = new Date();

        List<Integer> tasksIds = taskSubsData.entrySet().stream()
                .filter(it -> it.getValue().getSendDate().before(currentDate))
                .map(Map.Entry::getKey).collect(Collectors.toList());

        if(!tasksIds.isEmpty()) {

            new Thread(() -> {

                List<Task> tasks = taskRepository.findAllById(tasksIds);
                for (Task task : tasks) {
                    List<String> emails = taskSubsData.get(task.getIdTask()).getEmailsIds()
                            .stream().map(this.emails::get).collect(Collectors.toList());

                    if(emails.size() > 0) {
                        try {
                            mailServiceImpl.sendPriorityNotifications(task, emails);
                            taskSubsData.remove(task.getIdTask());
                        } catch (RuntimeException e) { //TODO logging
                            System.out.println("Send exception" + e.getMessage());
                        }
                    }
                }
            }).start();

        }
    }
}
