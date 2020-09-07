package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pl.wasko.time.manager.rest.api.model.entity.History;
import pl.wasko.time.manager.rest.api.model.entity.HistoryElement;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Integer> {

   List<History> findAllByModifiedElementId(Integer id);
   List<History> findAllByModifiedElementIdAndHistoryElement(Integer id, HistoryElement historyElement);

}
