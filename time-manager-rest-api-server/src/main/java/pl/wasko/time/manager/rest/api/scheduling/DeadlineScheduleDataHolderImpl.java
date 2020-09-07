package pl.wasko.time.manager.rest.api.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.wasko.time.manager.rest.api.model.entity.Status;
import pl.wasko.time.manager.rest.api.model.entity.Task;
import pl.wasko.time.manager.rest.api.model.enumeration.StatusEnum;
import pl.wasko.time.manager.rest.api.model.repository.StatusRepository;
import pl.wasko.time.manager.rest.api.model.repository.TaskRepository;
import pl.wasko.time.manager.rest.api.model.service.TaskFinisher;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class DeadlineScheduleDataHolderImpl implements DeadlineScheduleDataHolder {

    private final Map<Integer, Date> deadlines = new ConcurrentHashMap<>();
    private final TaskFinisher taskFinisher;
    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;
    private Status finishedStatus;

    public DeadlineScheduleDataHolderImpl(TaskFinisher taskFinisher, TaskRepository taskRepository, StatusRepository statusRepository) {

        this.taskFinisher = taskFinisher;
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;
    }

    @PostConstruct
    void populateData() {
        finishedStatus = statusRepository.findByStatusName(StatusEnum.FINISHED);
        taskRepository.findAll().forEach(task -> {
            Status taskStatus = task.getStatus();
            if(!taskStatus.getIdStatus().equals(finishedStatus.getIdStatus()))
                deadlines.put(task.getIdTask(), task.getDeadline());
        });
    }

    @Override
    public void onTaskAdded(Task task) {
        deadlines.put(task.getIdTask(), task.getDeadline());
    }

    @Override
    public void onTaskUpdated(Task task) {
        deadlines.put(task.getIdTask(), task.getDeadline());
    }

    @Override
    public void onTaskDeleting(Task task) {
        deadlines.remove(task.getIdTask());
    }

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void checkDeadlines() {

        Date currentDate = new Date();

        List<Integer> tasksIds = deadlines.entrySet().stream()
                .filter(it -> it.getValue().before(currentDate))
                .map(Map.Entry::getKey).collect(Collectors.toList());

        if(!tasksIds.isEmpty()) {
            List<Task> tasks = taskRepository.findAllById(tasksIds);

            for(Task task : tasks) {
                task.setStatus(finishedStatus);
                taskFinisher.onTaskFinished(task);
                deadlines.remove(task.getIdTask());
            }
            taskRepository.saveAll(tasks);
        }
    }
}
