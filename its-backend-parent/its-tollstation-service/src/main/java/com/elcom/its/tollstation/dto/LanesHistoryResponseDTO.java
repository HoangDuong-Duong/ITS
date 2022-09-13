package com.elcom.its.tollstation.dto;

import java.util.ArrayList;
import java.util.List;

public class LanesHistoryResponseDTO {
    private int status;
    private String message;
    private List<TollstationLaneActionHistory> data;
    private Long total;

    public LanesHistoryResponseDTO() {
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

    public List<TollstationLaneActionHistory> getData() {
        return data;
    }

    public void setData(List<TollstationLaneActionHistory> data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
