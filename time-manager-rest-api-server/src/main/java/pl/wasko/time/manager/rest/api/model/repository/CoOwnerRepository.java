package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wasko.time.manager.rest.api.model.entity.Bucket;
import pl.wasko.time.manager.rest.api.model.entity.CoOwner;
import pl.wasko.time.manager.rest.api.model.entity.CoOwnerKey;
import pl.wasko.time.manager.rest.api.model.entity.User;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface CoOwnerRepository extends JpaRepository<CoOwner, CoOwnerKey> {

    List<CoOwner> findAllByUserAndDeletedFalse(User user);
    List<CoOwner> findAllByBucket(Bucket bucket);
}
