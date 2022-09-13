/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author Admin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RepairJobProcessing {

    private String deviceId;
    private String repairComment;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date repairedDate;
    private String deviceStatus;
    private String deviceType;
    private String deviceName;
}
