package pl.wasko.time.manager.rest.api.helper;

import pl.wasko.time.manager.rest.api.model.enumeration.StatusEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static pl.wasko.time.manager.rest.api.model.enumeration.StatusEnum.*;

public class StatusHelper {

    public static List<StatusEnum> getChildStatusesFromStatusName(StatusEnum statusName) {
        switch(statusName) {
            case NEW:
                return Arrays.asList(NEW, SUSPENDED, IN_PROGRESS, FINISHED);

            case IN_PROGRESS:
                return Arrays.asList(IN_PROGRESS, SUSPENDED, FINISHED);

            case SUSPENDED:
                return Arrays.asList(SUSPENDED, IN_PROGRESS, FINISHED);

            default:
                return Collections.emptyList();
        }
    }
}
