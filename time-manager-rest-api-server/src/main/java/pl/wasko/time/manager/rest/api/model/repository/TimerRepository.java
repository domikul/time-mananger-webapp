package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.repository.CrudRepository;
import pl.wasko.time.manager.rest.api.model.entity.Timer;

public interface TimerRepository extends CrudRepository<Timer, Integer> {
}
