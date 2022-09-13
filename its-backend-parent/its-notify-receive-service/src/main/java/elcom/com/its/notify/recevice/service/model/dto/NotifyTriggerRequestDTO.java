package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotifyTriggerRequestDTO {

    @JsonProperty("type")
    private String type;
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("deviceType")
    private String deviceType;
    @JsonProperty("sites")
    private String sites;
    @JsonProperty("startTime")
    private String startTime;
    @JsonProperty("data")
    private Object data;

    public String toJsonString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

