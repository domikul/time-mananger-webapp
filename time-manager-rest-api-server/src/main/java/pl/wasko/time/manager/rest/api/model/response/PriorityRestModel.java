package pl.wasko.time.manager.rest.api.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wasko.time.manager.rest.api.model.entity.Priority;

import java.util.Date;

@Data
@NoArgsConstructor
public class PriorityRestModel {

    private Integer idPriority;

    private String priority;

    private String notificationTime;

    public PriorityRestModel(Priority priority) {
        this.idPriority = priority.getIdPriority();
        this.priority = priority.getPriority();
        this.notificationTime = priority.getNotificationTime();
    }
}
