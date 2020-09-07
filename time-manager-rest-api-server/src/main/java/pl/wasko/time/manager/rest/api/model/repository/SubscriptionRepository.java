package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wasko.time.manager.rest.api.model.entity.Email;
import pl.wasko.time.manager.rest.api.model.entity.Subscription;
import pl.wasko.time.manager.rest.api.model.entity.Task;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    Optional<Subscription> findByIdSubAndUserNotNull(Integer idSub);

    Boolean existsByEmailAndTask(Email email, Task task);

    Boolean existsByEmail(Email email);

    Subscription findByEmailAndTask(Email email, Task task);

}
