package pl.wasko.time.manager.rest.api.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wasko.time.manager.rest.api.model.entity.TimerStep;

import java.util.Date;

@Data
@NoArgsConstructor
public class TimerStepRestModel {

    private Integer idTimerStep;

    private Date startTime;

    private Date endTime;

    public TimerStepRestModel(TimerStep timerStep) {
        this.idTimerStep = timerStep.getIdTimerStep();
        this.startTime = timerStep.getStartTime();
        this.endTime = timerStep.getEndTime();
    }
}
