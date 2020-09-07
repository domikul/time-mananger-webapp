package pl.wasko.time.manager.rest.api.model.response;

import pl.wasko.time.manager.rest.api.model.entity.Position;

import javax.persistence.Column;

public class PositionRestModel {

    private Integer idPosition;
    private String positionName;


    public PositionRestModel() {
    }

    public PositionRestModel(Position position){
        this.idPosition = position.getIdPosition();
        this.positionName = position.getPositionName();
    }

    public Integer getIdPosition() {
        return idPosition;
    }

    public void setIdPosition(Integer idPosition) {
        this.idPosition = idPosition;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
