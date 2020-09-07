package pl.wasko.time.manager.rest.api.model.response;

import pl.wasko.time.manager.rest.api.model.entity.HistoryElement;
import pl.wasko.time.manager.rest.api.model.entity.OperationType;

public class OperationTypeRestModel {

    private Integer idOperationType;
    private String operationTypeName;

    public OperationTypeRestModel(OperationType operationType) {
        this.idOperationType = operationType.getIdOperationType();
        this.operationTypeName = operationType.getOperationTypeName().name();
    }

    public Integer getIdOperationType() {
        return idOperationType;
    }

    public void setIdOperationType(Integer idOperationType) {
        this.idOperationType = idOperationType;
    }

    public String getOperationTypeName() {
        return operationTypeName;
    }

    public void setOperationTypeName(String operationTypeName) {
        this.operationTypeName = operationTypeName;
    }
}
