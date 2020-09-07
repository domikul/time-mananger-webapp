package pl.wasko.time.manager.rest.api.model.response;

import pl.wasko.time.manager.rest.api.model.entity.Bucket;

import java.util.Date;

public class BucketRestModel {

    private Integer idBucket;
    private String bucketName;
    private String description;
    private Date creationDate ;
    private Integer userId;
    private Integer  maxTasks;

    public BucketRestModel() {
    }

    public BucketRestModel(Integer idBucket, String bucketName, String description, Date creationDate, Integer userId, Integer maxTasks) {
        this.idBucket = idBucket;
        this.bucketName = bucketName;
        this.description = description;
        this.creationDate = creationDate;
        this.userId = userId;
        this.maxTasks = maxTasks;
    }


    public BucketRestModel(Bucket bucket) {
        this.idBucket = bucket.getIdBucket();
        this.bucketName = bucket.getBucketName();
        this.description = bucket.getDescription();
        this.creationDate = bucket.getCreationDate();
        this.userId = bucket.getUser().getIdUser();
        this.maxTasks = bucket.getMaxTasks();

    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date setAndReturnCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this.creationDate;
    }

    public Integer getMaxTasks() {
        return maxTasks;
    }

    public void setMaxTasks(Integer maxTasks) {
        this.maxTasks = maxTasks;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIdBucket() {
        return idBucket;
    }

    public void setIdBucket(Integer idBucket) {
        this.idBucket = idBucket;
    }

}
