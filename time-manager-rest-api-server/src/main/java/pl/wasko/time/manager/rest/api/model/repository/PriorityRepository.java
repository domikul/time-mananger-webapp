package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wasko.time.manager.rest.api.model.entity.Priority;

public interface PriorityRepository extends JpaRepository<Priority, Integer> {
}
