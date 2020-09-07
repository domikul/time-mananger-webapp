package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wasko.time.manager.rest.api.model.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
