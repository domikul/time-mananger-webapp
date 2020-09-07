package pl.wasko.time.manager.rest.api.model.response;

import lombok.NoArgsConstructor;
import pl.wasko.time.manager.rest.api.model.entity.Share;

import lombok.Data;


@Data
@NoArgsConstructor
public class ShareRestModel {

    Integer taskId;

    Integer userId;

    public ShareRestModel(Share share) {
        this.taskId = share.getTaskId();
        this.userId = share.getUserId();
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
