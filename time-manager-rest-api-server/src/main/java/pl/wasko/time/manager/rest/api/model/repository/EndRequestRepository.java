package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wasko.time.manager.rest.api.model.entity.EndRequest;
import pl.wasko.time.manager.rest.api.model.entity.EndRequestKey;
import pl.wasko.time.manager.rest.api.model.entity.Task;
import pl.wasko.time.manager.rest.api.model.entity.User;

import java.util.Collection;
import java.util.List;

public interface EndRequestRepository extends JpaRepository<EndRequest, EndRequestKey> {

    Boolean existsByUserAndTask(User user, Task task);

    List<EndRequest> findAllByTaskIsInAndUser(Collection<Task> tasks, User user);

}
