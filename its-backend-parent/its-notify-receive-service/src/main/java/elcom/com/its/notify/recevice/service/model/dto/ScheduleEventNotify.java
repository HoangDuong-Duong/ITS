package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleEventNotify {

    String actionCode;
    String actionName;
    String actor;
    ScheduledEvent scheduledEvent;
    String eventKey;
    String jobStartSiteName;
    String jobEndSiteName;
    String listUsers ;
    String groupName ;
    String eventName;

}
