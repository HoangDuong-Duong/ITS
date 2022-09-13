package com.elcom.its.management.dto;

public class DeviceResponseDTO {
    private int status;
    private String message;
    private ReportDevice data;
    private Long total;

    public DeviceResponseDTO() {
        total = Long.valueOf("0");
        data = new ReportDevice();
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

    public ReportDevice getData() {
        return data;
    }

    public void setData(ReportDevice data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
