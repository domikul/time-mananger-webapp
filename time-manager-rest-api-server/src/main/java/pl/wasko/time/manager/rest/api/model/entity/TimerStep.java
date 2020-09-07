package pl.wasko.time.manager.rest.api.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="timer_steps")
public class TimerStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id_timer_step", nullable=false)
    private Integer    idTimerStep      ;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="start_time", nullable=false)
    private Date startTime    ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="end_time")
    private Date       endTime      ;


    @ManyToOne
    @JoinColumn(name="timer_id", referencedColumnName="id_timer")
    private Timer timer;

    public Integer getIdTimerStep() {
        return idTimerStep;
    }

    public void setIdTimerStep(Integer idTimerStep) {
        this.idTimerStep = idTimerStep;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
