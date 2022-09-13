package com.elcom.its.vds.model.dto;

import java.util.ArrayList;
import java.util.List;

public class EventResponseDTO {
    private int status;
    private String message;
    private List<EventDTO> data;
    private Long total;

    public EventResponseDTO() {
        total = Long.valueOf("0");
        data = new ArrayList<>();
    }

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

    public List<EventDTO> getData() {
        return data;
    }

    public void setData(List<EventDTO> data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
