package pl.wasko.time.manager.rest.api.model.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "end_requests")
public class EndRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private EndRequestKey compositeKey;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id_user", insertable=false, updatable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="task_id", referencedColumnName="id_task", insertable=false, updatable=false)
    private Task task;

    public EndRequest() {
        super();
        this.compositeKey = new EndRequestKey();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.compositeKey.setUserId(user.getIdUser());
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
        this.compositeKey.setTaskId(task.getIdTask());
    }
}
