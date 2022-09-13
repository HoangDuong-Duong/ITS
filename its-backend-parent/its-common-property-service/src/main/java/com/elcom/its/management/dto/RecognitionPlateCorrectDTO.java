/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.dto;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class RecognitionPlateCorrectDTO implements Serializable {

    private int status;
    private String message;
    private RecognitionPlateDTO data;

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

    public RecognitionPlateDTO getData() {
        return data;
    }

    public void setData(RecognitionPlateDTO data) {
        this.data = data;
    }
}
