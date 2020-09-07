package pl.wasko.time.manager.rest.api.model.response;

import pl.wasko.time.manager.rest.api.model.entity.Status;

public class StatusRestModel {

    private Integer idStatus;
    private String statusName;

    public StatusRestModel() {
    }

    public StatusRestModel(Status status) {
        this.idStatus = status.getIdStatus();
        this.statusName = status.getStatusName().name();
    }

    public Integer getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Integer idStatus) {
        this.idStatus = idStatus;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
