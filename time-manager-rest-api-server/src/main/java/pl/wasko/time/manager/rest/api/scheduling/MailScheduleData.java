package pl.wasko.time.manager.rest.api.scheduling;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class MailScheduleData {

    private Date sendDate;

    private List<Integer> emailsIds;

}
