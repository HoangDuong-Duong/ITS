package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DisplayVMSBoardNotify {
    private String status ;
    private VmsBoard vmsBoard ;
    private String name ;
    private String startTime ;
    private String endTime ;

}
