package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.wasko.time.manager.rest.api.model.entity.Email;
import pl.wasko.time.manager.rest.api.model.entity.User;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import pl.wasko.time.manager.rest.api.model.entity.User;

import java.util.List;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {

    Boolean existsUserByEmail(Email email);
    Optional<User> findByEmail(Email email);
    List<User> findAllByIdUserIsIn(Set<Integer> idUser);
    Boolean existsUserByIdUser(Integer idUser);

  //  @Query("select count(u)>0 from User u where u.emailId = :idEmail")
    @Query(value = "SELECT CASE  WHEN count(*)> 0 THEN 1 ELSE 0 END FROM users u  where u.email_id = :idEmail", nativeQuery=true)
    BigInteger existsUserByEmail(@Param("idEmail") Integer idEmail);
}
