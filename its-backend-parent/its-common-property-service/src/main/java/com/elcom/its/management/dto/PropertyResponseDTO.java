package com.elcom.its.management.dto;

import java.util.ArrayList;
import java.util.List;

public class PropertyResponseDTO {
    private int status;
    private String message;
    private List<CommonProperty> data;
    private Long total;

    public PropertyResponseDTO() {
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

    public List<CommonProperty> getData() {
        return data;
    }

    public void setData(List<CommonProperty> data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
