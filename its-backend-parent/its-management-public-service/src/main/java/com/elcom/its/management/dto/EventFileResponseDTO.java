package com.elcom.its.management.dto;



import com.elcom.its.management.model.EventFile;
import java.util.List;

public class EventFileResponseDTO {
    private int status;
    private String message;
    private List<EventFile> data;

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

    public List<EventFile> getData() {
        return data;
    }

    public void setData(List<EventFile> data) {
        this.data = data;
    }
}
