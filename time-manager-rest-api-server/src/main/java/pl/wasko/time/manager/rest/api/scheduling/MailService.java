package pl.wasko.time.manager.rest.api.scheduling;

import pl.wasko.time.manager.rest.api.model.entity.Task;
import pl.wasko.time.manager.rest.api.model.entity.User;

public interface MailService {

    void sendEndRequestNotification(Task task, User user);

}
