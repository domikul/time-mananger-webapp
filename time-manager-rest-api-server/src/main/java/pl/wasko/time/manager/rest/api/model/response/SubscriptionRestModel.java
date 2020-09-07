package pl.wasko.time.manager.rest.api.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wasko.time.manager.rest.api.model.entity.Subscription;

@Data
@NoArgsConstructor
public class SubscriptionRestModel {

    private Integer idSub;

    private Integer taskId;

    private Integer emailId;

    public SubscriptionRestModel(Subscription subscription) {
        this.idSub = subscription.getIdSub();
        this.emailId = subscription.getEmail().getIdEmail();
        this.taskId = subscription.getTask().getIdTask();
    }
}
