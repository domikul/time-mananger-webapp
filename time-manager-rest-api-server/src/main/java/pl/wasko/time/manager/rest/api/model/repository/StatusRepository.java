package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pl.wasko.time.manager.rest.api.model.entity.Status;
import pl.wasko.time.manager.rest.api.model.enumeration.StatusEnum;

public interface StatusRepository extends JpaRepository<Status, Integer> {

    Status findByStatusName(StatusEnum statusName);
}
