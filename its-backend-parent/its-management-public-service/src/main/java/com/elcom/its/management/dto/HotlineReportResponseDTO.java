package com.elcom.its.management.dto;

import java.util.ArrayList;
import java.util.List;

public class HotlineReportResponseDTO {
    private int status;
    private String message;
    private Hotline data;
    private Long total;

    public HotlineReportResponseDTO() {
        total = Long.valueOf("0");
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

    public Hotline getData() {
        return data;
    }

    public void setData(Hotline data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
