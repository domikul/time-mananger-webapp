package pl.wasko.time.manager.rest.api.model.service;

import org.springframework.stereotype.Service;
import pl.wasko.time.manager.rest.api.model.repository.HistoryElementRepository;
import pl.wasko.time.manager.rest.api.model.repository.OperationTypeRepository;
import pl.wasko.time.manager.rest.api.model.response.HistoryElementRestModel;
import pl.wasko.time.manager.rest.api.model.response.OperationTypeRestModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperationTypeService {

    private final OperationTypeRepository operationTypeRepository;

    public OperationTypeService(OperationTypeRepository operationTypeRepository) {
        this.operationTypeRepository = operationTypeRepository;
    }

    public List<OperationTypeRestModel> getAllOperationTypes() {
        return operationTypeRepository.findAll().stream()
                .map(OperationTypeRestModel::new)
                .collect(Collectors.toList());
    }

}
