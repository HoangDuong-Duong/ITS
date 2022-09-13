/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.tollstation.dto;

import java.util.List;

/**
 *
 * @author Admin
 */
public class PointSiteDirectionResponse {

    private Integer status;

    private String message;

    private List<PointSiteDirectionDTO> data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PointSiteDirectionDTO> getData() {
        return data;
    }

    public void setData(List<PointSiteDirectionDTO> data) {
        this.data = data;
    }
}
