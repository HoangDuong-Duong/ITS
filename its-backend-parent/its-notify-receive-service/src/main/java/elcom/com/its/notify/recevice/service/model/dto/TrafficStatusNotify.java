package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TrafficStatusNotify {
    private TrafficStatus newTrafficStatus;

    private List<String> listSiteId;

    private String directionCode;

    private long startPositionM;

    private long endPositionM;

}
