package pl.wasko.time.manager.rest.api.model.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.wasko.time.manager.rest.api.model.entity.*;
import pl.wasko.time.manager.rest.api.model.enumeration.HistoryElementEnum;
import pl.wasko.time.manager.rest.api.model.enumeration.OperationTypeEnum;
import pl.wasko.time.manager.rest.api.model.repository.HistoryElementRepository;
import pl.wasko.time.manager.rest.api.model.repository.HistoryRepository;
import pl.wasko.time.manager.rest.api.model.repository.OperationTypeRepository;
import pl.wasko.time.manager.rest.api.model.response.HistoryRestModel;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HistoryService {

    HistoryRepository historyRepository;
    HistoryElementRepository historyElementRepository;
    OperationTypeRepository operationTypeRepository;

    public void saveToHistory(Archivable archivable, User modifier, OperationTypeEnum operationTypeEnum) {
        HistoryElement historyElement = historyElementRepository.findByElementName(archivable.getHistoryElement());
        OperationType operationType = operationTypeRepository.findByOperationTypeName(operationTypeEnum);
        History history = new History();
        history.setHistoryElement(historyElement);
        history.setUser(modifier);
        history.setModifiedElementId(archivable.getArchivableId());
        history.setModificationTime(new Date());
        history.setOperationType(operationType);
        historyRepository.save(history);
    }

        public List<HistoryRestModel> getFromHistory(Archivable archivable, HistoryElementEnum element){

        HistoryElement historyElement = historyElementRepository.findByElementName(element);

        return historyRepository.findAllByModifiedElementIdAndHistoryElement(archivable.getArchivableId(), historyElement).stream()
                    .map(HistoryRestModel::new)
                    .collect(Collectors.toList());

        }

}
