package elcom.com.its.notify.recevice.service.model.dto;

import java.io.Serializable;

public class NotifyTriggerDTO implements Serializable {

    private int status;
    private String message;
    private NotifyTrigger data;

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

    public NotifyTrigger getData() {
        return data;
    }

    public void setData(NotifyTrigger data) {
        this.data = data;
    }

}
