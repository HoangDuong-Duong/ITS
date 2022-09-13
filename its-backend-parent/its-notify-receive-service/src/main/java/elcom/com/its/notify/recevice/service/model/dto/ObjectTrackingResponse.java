package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author Admin
 */
public class ObjectTrackingResponse implements Serializable {

    private int status;
    private String message;
    private ObjectTrackingDTO data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ObjectTrackingDTO getData() {
        return data;
    }

    public void setData(ObjectTrackingDTO data) {
        this.data = data;
    }

    public String toJsonString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            //mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

