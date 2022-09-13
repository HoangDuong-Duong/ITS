package com.elcom.its.management.dto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
public class HistoryStatusPagingDTO {

    private int status;
    private String message;
    private List<HistoryStatusDevice> data;
    private Long total;

    public HistoryStatusPagingDTO() {
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

    public List<HistoryStatusDevice> getData() {
        return data;
    }

    public void setData(List<HistoryStatusDevice> data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
