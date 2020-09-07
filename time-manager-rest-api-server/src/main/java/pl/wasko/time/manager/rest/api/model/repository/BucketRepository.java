package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wasko.time.manager.rest.api.model.entity.Bucket;
import pl.wasko.time.manager.rest.api.model.entity.CoOwner;
import pl.wasko.time.manager.rest.api.model.entity.User;
import pl.wasko.time.manager.rest.api.model.response.CoOwnerRestModel;

import java.util.List;

public interface BucketRepository extends JpaRepository<Bucket, Integer>{

    List<Bucket> findAllByUser(User user);

}
