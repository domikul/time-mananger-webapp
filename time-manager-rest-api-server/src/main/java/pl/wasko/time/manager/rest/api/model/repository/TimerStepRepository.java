package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.repository.CrudRepository;
import pl.wasko.time.manager.rest.api.model.entity.TimerStep;

public interface TimerStepRepository extends CrudRepository<TimerStep, Integer> {
}
