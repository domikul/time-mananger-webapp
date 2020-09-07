package pl.wasko.time.manager.rest.api.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.wasko.time.manager.rest.api.exception.NotAuthorizedActionException;
import pl.wasko.time.manager.rest.api.exception.ResourceNotFoundException;
import pl.wasko.time.manager.rest.api.exception.WrongRequestException;
import pl.wasko.time.manager.rest.api.helper.JwtHelper;
import pl.wasko.time.manager.rest.api.helper.StatusHelper;
import pl.wasko.time.manager.rest.api.helper.TimeHelper;
import pl.wasko.time.manager.rest.api.model.entity.*;
import pl.wasko.time.manager.rest.api.model.entity.Timer;
import pl.wasko.time.manager.rest.api.model.enumeration.HistoryElementEnum;
import pl.wasko.time.manager.rest.api.model.enumeration.OperationTypeEnum;
import pl.wasko.time.manager.rest.api.model.enumeration.StatusEnum;
import pl.wasko.time.manager.rest.api.model.repository.*;
import pl.wasko.time.manager.rest.api.model.response.HistoryRestModel;
import pl.wasko.time.manager.rest.api.model.response.ShareRestModel;
import pl.wasko.time.manager.rest.api.model.response.TaskRestModel;
import pl.wasko.time.manager.rest.api.model.response.UserRestModel;
import pl.wasko.time.manager.rest.api.scheduling.DeadlineScheduleDataHolder;
import pl.wasko.time.manager.rest.api.scheduling.MailScheduleDataHolder;

import java.util.*;
import java.util.stream.Collectors;

import static pl.wasko.time.manager.rest.api.exception.ExceptionMessage.*;

@Component
@AllArgsConstructor
public class TaskService implements TaskDeleter, ShareDeleter {

    private final TaskRepository taskRepository;
    private final BucketRepository bucketRepository;
    private final PriorityRepository priorityRepository;
    private final StatusRepository statusRepository;
    private final ShareRepository shareRepository;
    private final JwtHelper jwtHelper;
    private final UserRepository userRepository;
    private final TimerDeleter timerDeleter;
    private final EndRequestDeleter endRequestDeleter;
    private final SubscriptionManager subscriptionManager;
    private final HistoryService historyService;
    private final MailScheduleDataHolder mailScheduleDataHolder;
    private final DeadlineScheduleDataHolder deadlineScheduleDataHolder;
    private final TaskFinisher taskFinisher;


    @Override
    public void deleteUserTimer(Task task, User user) {
        timerDeleter.deleteTimers(task.getListOfTimers().stream()
                .filter(it -> it.getUser().equals(user)).collect(Collectors.toList()));

        mailScheduleDataHolder.onSingleUserTimerDeleting(task, user);
    }

    @Override
    public void deleteTasks(List<Task> tasksToDelete, User user) {

        List<Share> sharesToDelete = new ArrayList<>();

        for(Task task : tasksToDelete) {
            List<Timer> timers = task.getListOfTimers();
            timerDeleter.deleteTimers(timers);

            List<Share> shares = task.getListOfShares().stream().peek(share -> share.setDeleted(true))
                    .collect(Collectors.toList());

            sharesToDelete.addAll(shares);
            task.setDeleted(true);

            List<Subscription> taskSubscriptions = task.getListOfSubscriptions();
            task.setListOfSubscriptions(Collections.emptyList());
            subscriptionManager.deleteSubscriptions(taskSubscriptions);
            endRequestDeleter.deleteEndRequests(task.getListOfEndRequests());
            task.setListOfEndRequests(Collections.emptyList());

            mailScheduleDataHolder.onTaskDeleting(task);
            deadlineScheduleDataHolder.onTaskDeleting(task);
            historyService.saveToHistory(task, user, OperationTypeEnum.DELETE);

        }

        shareRepository.saveAll(sharesToDelete);
        taskRepository.saveAll(tasksToDelete);


    }

    @Override
    public void onBucketCoOwnerDeleted(CoOwner coOwner) {
        for(Task task : coOwner.getBucket().getListOfTasks())
            deleteUserTimer(task, coOwner.getUser());

        endRequestDeleter.deleteSingleCoOwnerEndRequests(coOwner);
    }

    @Override
    public void deleteUserShares(User user) {
        List<Share> shares = user.getListOfShares().stream().peek(share -> {
            share.setDeleted(true);
            deleteUserTimer(share.getTask(), user);
            endRequestDeleter.deleteSingleShareEndRequests(share);
        }).collect(Collectors.toList());

        shareRepository.saveAll(shares);
    }

    public TaskRestModel createTask(TaskRestModel taskRestModel, String token) {
        User currentUser = jwtHelper.getUserFromToken(token);
        Task task = new Task();

        task.setUser(currentUser);
        Bucket bucket = bucketRepository.findById(taskRestModel.getBucketId()).orElseThrow(() ->
                new ResourceNotFoundException(BUCKET_NOT_FOUND.getMessage())
        );

        if(taskRestModel.getDeadline().before(new Date()))
            throw new WrongRequestException(DEADLINE_BEFORE_CURRENT.getMessage());

        if(taskRestModel.getEstimatedEndTime().startsWith("-"))
            throw new WrongRequestException(NEGATIVE_ESTIMATED_END_TIME.getMessage());

        if(bucket.getMaxTasks() <= bucket.getListOfTasks().size())
            throw new WrongRequestException(CANNOT_ADD_TASKS.getMessage());

        if(bucket.isUserOwner(currentUser) || bucket.isUserCoOwner(currentUser))
            task.setBucket(bucket);
        else
            throw new NotAuthorizedActionException(NOT_AUTHORIZED_TO_BUCKET.getMessage());

        task.setPriority(priorityRepository.findById(taskRestModel.getPriorityId()).orElseThrow(() ->
                new ResourceNotFoundException(PRIORITY_NOT_FOUND.getMessage())
        ));

        task.setStatus(statusRepository.findByStatusName(StatusEnum.NEW));
        task.setDescription(taskRestModel.getDescription());
        task.setTaskName(taskRestModel.getTaskName());
        task.setBeginTime(new Date());
        task.setDeadline(taskRestModel.getDeadline());
        task.setTotalTime(TimeHelper.secondsAsTime(0));
        task.setEstimatedEndTime(taskRestModel.getEstimatedEndTime());
        task.setDeleted(false);

        TaskRestModel actualTask = new TaskRestModel(taskRepository.save(task));
        historyService.saveToHistory(task,currentUser, OperationTypeEnum.CREATE);
        mailScheduleDataHolder.onTaskAdded(task);
        deadlineScheduleDataHolder.onTaskAdded(task);
        return actualTask;
    }

    public TaskRestModel updateTask(TaskRestModel taskRestModel, String token) {

        Task task = taskRepository.findById(taskRestModel.getIdTask()).orElseThrow(() ->
            new ResourceNotFoundException(TASK_NOT_FOUND.getMessage()));

        Status newStatus = statusRepository.findById(taskRestModel.getStatusId()).orElseThrow(() ->
                new ResourceNotFoundException(STATUS_NOT_FOUND.getMessage()));

        Status currentStatus = task.getStatus();
        if(currentStatus.getStatusName().equals(StatusEnum.FINISHED))
            throw new NotAuthorizedActionException(NOT_ALLOWED_EDIT_FINISHED_TASK.getMessage());

        User currentUser = jwtHelper.getUserFromToken(token);

        if(task.isUserOwner(currentUser) || task.getBucket().isUserOwner(currentUser)) {

            if(taskRestModel.getDeadline().before(new Date()))
                throw new WrongRequestException(DEADLINE_BEFORE_CURRENT.getMessage());

            if(taskRestModel.getEstimatedEndTime().startsWith("-"))
                throw new WrongRequestException(NEGATIVE_ESTIMATED_END_TIME.getMessage());

            task.setTaskName(taskRestModel.getTaskName());
            task.setDescription(taskRestModel.getDescription());
            task.setDeadline(taskRestModel.getDeadline());
            task.setEstimatedEndTime(taskRestModel.getEstimatedEndTime());
            task.setPriority(priorityRepository.findById(taskRestModel.getPriorityId()).orElseThrow(() ->
                new ResourceNotFoundException(PRIORITY_NOT_FOUND.getMessage())
            ));

            if(StatusHelper.getChildStatusesFromStatusName(currentStatus.getStatusName())
                    .contains(newStatus.getStatusName())) {

                task.setStatus(newStatus);
                if(newStatus.getStatusName().equals(StatusEnum.SUSPENDED))
                    timerDeleter.suspendTimers(task.getListOfTimers());
                else if(newStatus.getStatusName().equals(StatusEnum.FINISHED))
                    taskFinisher.onTaskFinished(task);

            }
            else
                throw new WrongRequestException(WRONG_STATUS_SELECTED.getMessage());

        }
        else if(task.isUserCoOwner(currentUser)) {

            task.setTaskName(taskRestModel.getTaskName());
            task.setDescription(taskRestModel.getDescription());
        }
        else
            throw new NotAuthorizedActionException(NOT_ALLOWED_EDIT_TASK.getMessage());

        TaskRestModel actualTask = new TaskRestModel(taskRepository.save(task));
        mailScheduleDataHolder.onTaskUpdated(task);
        deadlineScheduleDataHolder.onTaskUpdated(task);
        historyService.saveToHistory(task, currentUser, OperationTypeEnum.UPDATE);
        return actualTask;
    }

    public List<TaskRestModel> getBucketTasks(Integer bucketId, String token) {
        Bucket bucket = bucketRepository.findById(bucketId).orElseThrow(() ->
            new ResourceNotFoundException(BUCKET_NOT_FOUND.getMessage())
        );

        User currentUser = jwtHelper.getUserFromToken(token);
        if(bucket.isUserOwner(currentUser) || bucket.isUserCoOwner(currentUser))
            return taskRepository.findAllByBucket(bucket).stream().map(TaskRestModel::new).collect(Collectors.toList());
        else
            throw new NotAuthorizedActionException(NOT_AUTHORIZED_TO_BUCKET.getMessage());

    }

    public List<HistoryRestModel> getHistory(String token, Integer idTask) {

        Task task = taskRepository.findById(idTask).orElseThrow(() ->
                new ResourceNotFoundException(TASK_NOT_FOUND.getMessage())
        );

        User currentUser = jwtHelper.getUserFromToken(token);

        if(task.isUserOwner(currentUser))
            return historyService.getFromHistory(task, HistoryElementEnum.TASK);
        else
            throw new NotAuthorizedActionException(NOT_AUTHORIZED_TO_TASK.getMessage());

    }

    public Set<ShareRestModel> shareTask(List<UserRestModel> userRestModels, Integer idTask, String token) {
        Task task = taskRepository.findById(idTask).orElseThrow(() ->
                new ResourceNotFoundException(TASK_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);
        if (!task.isUserOwner(currentUser))
            throw new NotAuthorizedActionException(NOT_ALLOWED_SHARE_TASK.getMessage());

        Set<Integer> ids = userRestModels.stream().map(UserRestModel::getIdUser).collect(Collectors.toSet());
        if(ids.contains(currentUser.getIdUser()))
            throw new WrongRequestException(CANNOT_SHARE_TASK_TO_YOURSELF.getMessage());

        List<User> newSharedUsers = userRepository.findAllByIdUserIsIn(ids);
        if(newSharedUsers.size() != ids.size())
            throw new WrongRequestException(WRONG_USER_IDS.getMessage());

        List<Share> taskShares = task.getListOfShares();

        List<Share> listOfNewShares = taskShares.stream()
                .filter(it ->
                        it.getDeleted().equals(true) &&
                        newSharedUsers.stream()
                        .anyMatch(it2 -> it.getUser().equals(it2))
                )
                .peek(it -> it.setDeleted(false)).collect(Collectors.toList());

        newSharedUsers.stream().filter(it -> taskShares.stream().noneMatch(it2 -> it2.getUser().equals(it)))
                .forEach(newSharedUser -> {
                    Share share = new Share();
                    share.setDeleted(false);
                    share.setUser(newSharedUser);
                    share.setTask(task);
                    listOfNewShares.add(share);
                });


        shareRepository.saveAll(listOfNewShares);

        return listOfNewShares.stream()
                .map(ShareRestModel::new)
                .collect(Collectors.toSet());
        
    }

    public List<TaskRestModel> getSharedTasks(String token) {

        User currentUser = jwtHelper.getUserFromToken(token);

        List<Task> taskList = shareRepository.findAllByUserAndDeletedFalse(currentUser)
                .stream()
                .map(Share::getTask)
                .collect(Collectors.toList());

        return taskList.stream()
                .map(TaskRestModel::new)
                .collect(Collectors.toList());

    }

    public void deleteTask(Integer idTask, String token) {
        User currentUser = jwtHelper.getUserFromToken(token);

        Task task = taskRepository.findById(idTask).orElseThrow(() ->
            new ResourceNotFoundException(TASK_NOT_FOUND.getMessage())
        );

        if(task.isUserOwner(currentUser) || task.getBucket().isUserOwner(currentUser))
            deleteTasks(Collections.singletonList(task), currentUser);
        else
            throw new NotAuthorizedActionException(NOT_ALLOWED_DELETE_TASK.getMessage());

    }

    public void deleteTaskShare(Integer idTask, Integer userId, String token) {
        User currentUser = jwtHelper.getUserFromToken(token);

        Task task = taskRepository.findById(idTask).orElseThrow(() ->
                new ResourceNotFoundException(TASK_NOT_FOUND.getMessage())
        );

        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(USER_NOT_FOUND.getMessage())
        );

        if(task.isUserOwner(currentUser)) {

            Share share = task.getListOfShares().stream().filter(it -> it.getUser().equals(user))
                    .findFirst().orElseThrow(() ->
                        new WrongRequestException(USER_NOT_SHARED_TASK.getMessage())
                    );

            share.setDeleted(true);
            deleteUserTimer(task, user);
            endRequestDeleter.deleteSingleShareEndRequests(share);
            shareRepository.save(share);
        }
        else
            throw new NotAuthorizedActionException(NOT_ALLOWED_EDIT_TASK_SHARES.getMessage());
    }

    public List<ShareRestModel> getTaskShares(Integer idTask, String token) {
        Task task = taskRepository.findById(idTask).orElseThrow(() ->
                new ResourceNotFoundException(TASK_NOT_FOUND.getMessage()));

        User currentUser = jwtHelper.getUserFromToken(token);
        if (!task.isUserOwner(currentUser))
            throw new NotAuthorizedActionException(NOT_ALLOWED_GET_TASK_SHARES.getMessage());

        return task.getListOfShares().stream().filter(it -> it.getDeleted().equals(false)).map(ShareRestModel::new).collect(Collectors.toList());
    }



}

