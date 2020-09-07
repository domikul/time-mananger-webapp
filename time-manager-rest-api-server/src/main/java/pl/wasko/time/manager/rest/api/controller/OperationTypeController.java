package pl.wasko.time.manager.rest.api.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wasko.time.manager.rest.api.model.entity.OperationType;
import pl.wasko.time.manager.rest.api.model.response.HistoryElementRestModel;
import pl.wasko.time.manager.rest.api.model.response.OperationTypeRestModel;
import pl.wasko.time.manager.rest.api.model.service.OperationTypeService;

import java.util.List;

@RestController
@RequestMapping(path = "/operation")
@AllArgsConstructor
public class OperationTypeController {

    private final OperationTypeService operationTypeService;

    @GetMapping
    public ResponseEntity<List<OperationTypeRestModel>> getOperationTypes() {
        final List<OperationTypeRestModel> operationTypesList = operationTypeService.getAllOperationTypes();
        return ResponseEntity.ok(operationTypesList);
    }

}