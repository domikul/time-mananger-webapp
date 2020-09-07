package pl.wasko.time.manager.rest.api.model.response;

import pl.wasko.time.manager.rest.api.model.entity.*;

import javax.persistence.*;
import java.util.Date;

public class HistoryRestModel {

    private Integer idHistory;
    private Integer  modificatorId;
    private Date modificationTime ;
    private Integer typeId;
    private Integer operationType;

    public HistoryRestModel(History history){
        this.idHistory = history.getIdHistory();
        this.modificatorId = history.getUser().getIdUser();
        this.modificationTime = history.getModificationTime();
        this.typeId = history.getHistoryElement().getIdElement();
        this.operationType = history.getOperationType().getIdOperationType();

    }

    public Integer getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(Integer idHistory) {
        this.idHistory = idHistory;
    }

    public Integer getModificatorId() {
        return modificatorId;
    }

    public void setModificatorId(Integer modificatorId) {
        this.modificatorId = modificatorId;
    }

    public Date getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(Date modificationTime) {
        this.modificationTime = modificationTime;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }
}
