package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.repository.CrudRepository;
import pl.wasko.time.manager.rest.api.model.entity.Share;
import pl.wasko.time.manager.rest.api.model.entity.ShareKey;
import pl.wasko.time.manager.rest.api.model.entity.User;

import java.util.List;

public interface ShareRepository extends CrudRepository<Share, ShareKey> {

    List<Share> findAllByUserAndDeletedFalse(User user);

}
