package pl.wasko.time.manager.rest.api.model.entity;

import pl.wasko.time.manager.rest.api.model.enumeration.HistoryElementEnum;
import pl.wasko.time.manager.rest.api.model.enumeration.OperationTypeEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="operation_types")
public class OperationType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id_operation_type", nullable=false)
    private Integer    idOperationType    ;

    @Column(name="operation_type_name", nullable=false, length=255)
    @Enumerated(EnumType.STRING)
    private OperationTypeEnum operationTypeName  ;

    @OneToMany(mappedBy="operationType", targetEntity=History.class)
    private List<History> listOfHistory;

    public OperationType() {
        super();
    }

    public Integer getIdOperationType() {
        return this.idOperationType;
    }

    public void setIdOperationType(Integer idOperationType) {
        this.idOperationType = idOperationType;
    }

    public OperationTypeEnum getOperationTypeName() {
        return this.operationTypeName;
    }

    public void setOperationTypeName(OperationTypeEnum operationTypeName) {
        this.operationTypeName = operationTypeName;
    }

    public List<History> getListOfHistory() {
        return this.listOfHistory;
    }

    public void setListOfHistory(List<History> listOfHistory) {
        this.listOfHistory = listOfHistory;
    }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(idOperationType);
        sb.append("]:");
        sb.append(operationTypeName);
        return sb.toString();
    }

}
