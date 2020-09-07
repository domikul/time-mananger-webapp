package pl.wasko.time.manager.rest.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wasko.time.manager.rest.api.model.entity.HistoryElement;
import pl.wasko.time.manager.rest.api.model.entity.OperationType;
import pl.wasko.time.manager.rest.api.model.enumeration.HistoryElementEnum;
import pl.wasko.time.manager.rest.api.model.enumeration.OperationTypeEnum;

public interface OperationTypeRepository extends JpaRepository<OperationType, Integer> {

    OperationType findByOperationTypeName(OperationTypeEnum elementName);

}
