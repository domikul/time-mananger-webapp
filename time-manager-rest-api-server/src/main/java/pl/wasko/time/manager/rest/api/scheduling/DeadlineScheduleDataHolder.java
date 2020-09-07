package pl.wasko.time.manager.rest.api.scheduling;

import pl.wasko.time.manager.rest.api.model.entity.Task;

public interface DeadlineScheduleDataHolder {

    void onTaskAdded(Task task);

    void onTaskUpdated(Task task);

    void onTaskDeleting(Task task);

}
