package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wasko.time.manager.rest.api.model.entity.HistoryElement;
import pl.wasko.time.manager.rest.api.model.enumeration.HistoryElementEnum;

public interface HistoryElementRepository extends JpaRepository<HistoryElement, Integer> {

    HistoryElement findByElementName(HistoryElementEnum elementName);

}
