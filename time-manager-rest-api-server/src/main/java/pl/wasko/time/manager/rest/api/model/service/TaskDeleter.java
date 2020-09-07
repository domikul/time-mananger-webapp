package pl.wasko.time.manager.rest.api.model.service;

import pl.wasko.time.manager.rest.api.model.entity.CoOwner;
import pl.wasko.time.manager.rest.api.model.entity.Task;
import pl.wasko.time.manager.rest.api.model.entity.User;

import java.util.List;

public interface TaskDeleter {

    void deleteUserTimer(Task task, User user);

    void deleteTasks(List<Task> tasksToDelete, User user);

    void onBucketCoOwnerDeleted(CoOwner coOwner);
}
