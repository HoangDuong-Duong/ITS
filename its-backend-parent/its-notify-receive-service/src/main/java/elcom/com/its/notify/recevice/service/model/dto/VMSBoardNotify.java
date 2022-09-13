package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VMSBoardNotify {
    private VmsBoard vmsBoard;
    private String displayScriptId;
    private String displayScriptName;
    private Date time;
    private String errorMessage;

}
