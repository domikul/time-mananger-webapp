package pl.wasko.time.manager.rest.api.model.response;

import pl.wasko.time.manager.rest.api.model.entity.HistoryElement;


public class HistoryElementRestModel {

    private Integer idElement;
    private String elementName;

    public HistoryElementRestModel(Integer idElement, String elementName) {
        this.idElement = idElement;
        this.elementName = elementName;
    }

    public HistoryElementRestModel() {
    }

    public HistoryElementRestModel(HistoryElement historyElement) {
        this.idElement = historyElement.getIdElement();
        this.elementName = historyElement.getElementName().name();
    }

    public Integer getIdElement() {
        return idElement;
    }

    public void setIdElement(Integer idElement) {
        this.idElement = idElement;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}
