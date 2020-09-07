package pl.wasko.time.manager.rest.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.wasko.time.manager.rest.api.model.entity.Subscription;

@EqualsAndHashCode(callSuper = true)
@Data
public class DetailedSubscriptionRestModel extends SubscriptionRestModel {

    private String taskName;

    private Integer userId;

    private String bucketName;

    public DetailedSubscriptionRestModel(Subscription subscription) {
        super(subscription);
        this.taskName = subscription.getTask().getTaskName();
        this.userId = subscription.getUser().getIdUser();
        this.bucketName = subscription.getTask().getBucket().getBucketName();
    }

}
