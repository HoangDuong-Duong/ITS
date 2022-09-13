package com.elcom.its.management.dto;

import java.util.ArrayList;
import java.util.List;

public class HistoryDisplayDTO {
    private int status;
    private String message;
    private List<HistoryDisplayScript> data;
    private Long total;
    String link;

    public HistoryDisplayDTO() {
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

    public List<HistoryDisplayScript> getData() {
        return data;
    }

    public void setData(List<HistoryDisplayScript> data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
