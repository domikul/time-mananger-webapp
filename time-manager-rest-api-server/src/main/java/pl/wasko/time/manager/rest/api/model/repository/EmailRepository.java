package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pl.wasko.time.manager.rest.api.model.entity.Email;
import pl.wasko.time.manager.rest.api.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Integer> {

    Optional<Email> findByEmailName(String emailName);
    Boolean existsByIdEmail(Integer id);

}
