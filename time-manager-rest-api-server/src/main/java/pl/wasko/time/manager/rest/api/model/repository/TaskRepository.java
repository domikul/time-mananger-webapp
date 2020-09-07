package pl.wasko.time.manager.rest.api.model.repository;

import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.wasko.time.manager.rest.api.model.entity.Bucket;
import pl.wasko.time.manager.rest.api.model.entity.Task;
import pl.wasko.time.manager.rest.api.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @EntityGraph(value = "Task.subsStatusPriority")
    List<Task> findAll();

    List<Task> findAllByBucket(Bucket bucket);

    List<Task> findAllByUser(User user);

}
