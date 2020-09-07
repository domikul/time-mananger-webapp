package pl.wasko.time.manager.rest.api.model.entity;

import pl.wasko.time.manager.rest.api.model.enumeration.HistoryElementEnum;

public interface Archivable {

    Integer getArchivableId();
    HistoryElementEnum getHistoryElement();
}
