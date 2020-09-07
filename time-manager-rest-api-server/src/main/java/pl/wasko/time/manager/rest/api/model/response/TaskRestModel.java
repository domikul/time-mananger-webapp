package pl.wasko.time.manager.rest.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wasko.time.manager.rest.api.model.entity.Task;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class TaskRestModel {

    private Integer idTask;

    private Integer bucketId;

    private Integer userId;

    private Integer statusId;

    private Integer priorityId;

    private String description;

    private String taskName;

    private Date deadline;

    private Date beginTime;

    private String estimatedEndTime;

    private String totalTime;

    private List<TimerRestModel> timers;

    public TaskRestModel(Task task) {
        this.idTask = task.getIdTask();
        this.bucketId = task.getBucket().getIdBucket();
        this.userId = task.getUser().getIdUser();
        this.statusId = task.getStatus().getIdStatus();
        this.priorityId = task.getPriority().getIdPriority();
        this.taskName = task.getTaskName();
        this.description = task.getDescription();
        this.beginTime = task.getBeginTime();
        this.deadline = task.getDeadline();
        this.estimatedEndTime = task.getEstimatedEndTime();
        this.totalTime = task.getTotalTime();
        if(task.getListOfTimers() != null)
            this.timers = task.getListOfTimers().stream().map(TimerRestModel::new).collect(Collectors.toList());
        else
            this.timers = Collections.emptyList();
    }

}
