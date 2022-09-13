package elcom.com.its.notify.recevice.service.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Response implements Serializable {

    @JsonProperty("status")
    @NonNull
    private int status;

    @JsonProperty("message")
    @NonNull
    private String message;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("total")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long total;

    public Response(int status, String message, Object data){
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
