package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wasko.time.manager.rest.api.model.entity.Position;

public interface PositionRepository extends JpaRepository<Position, Integer> {
}
