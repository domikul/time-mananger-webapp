package pl.wasko.time.manager.rest.api.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wasko.time.manager.rest.api.model.entity.EndRequest;

@Data
@NoArgsConstructor
public class EndRequestRestModel {

    private Integer userId;

    private Integer taskId;

    public EndRequestRestModel(EndRequest endRequest) {
        this.taskId = endRequest.getTask().getIdTask();
        this.userId = endRequest.getUser().getIdUser();
    }
}
