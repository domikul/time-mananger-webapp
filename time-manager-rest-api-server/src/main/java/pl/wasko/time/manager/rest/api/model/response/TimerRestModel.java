package pl.wasko.time.manager.rest.api.model.response;

import java.util.Collections;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wasko.time.manager.rest.api.model.entity.Timer;
import pl.wasko.time.manager.rest.api.model.entity.TimerStep;

import java.util.Date;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class TimerRestModel {

    private Integer idTimer;

    private Date startTime;

    private Date endTime;

    private Integer userId;

    private Integer taskId;

    private List<TimerStepRestModel> timerSteps;

    public TimerRestModel(Timer timer) {
        this.idTimer = timer.getIdTimer();
        this.startTime = timer.getStartTime();
        this.endTime = timer.getEndTime();
        this.userId = timer.getUser().getIdUser();
        this.taskId = timer.getTask().getIdTask();
        if(timer.getListOfTimerSteps() != null)
            this.timerSteps = timer.getListOfTimerSteps().stream().map(TimerStepRestModel::new).collect(Collectors.toList());
        else
            this.timerSteps = Collections.emptyList();
    }
}
