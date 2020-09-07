package pl.wasko.time.manager.rest.api.model.response;

import pl.wasko.time.manager.rest.api.model.entity.CoOwner;

public class CoOwnerRestModel {

    private Integer bucketId;

    private Integer userId;

    public CoOwnerRestModel(Integer bucket, Integer user) {
        this.bucketId = bucket;
        this.userId = user;
    }

    public CoOwnerRestModel(CoOwner coOwner) {
        this.bucketId = coOwner.getBucketId();
        this.userId = coOwner.getUserId();
    }

    public Integer getBucketId() {
        return bucketId;
    }

    public void setBucketId(Integer bucketId) {
        this.bucketId = bucketId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
