package com.elcom.its.management.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class HistoryStatusReportDTO {

    private int status;
    private String message;
    private List<HistoryStatusDeviceDTO> data;
    private Long total;

    public HistoryStatusReportDTO() {
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

    public List<HistoryStatusDeviceDTO> getData() {
        return data;
    }

    public void setData(List<HistoryStatusDeviceDTO> data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
