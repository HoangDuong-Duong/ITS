/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.tollstation.dto;

import com.elcom.its.tollstation.enums.DeviceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TollStationDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String type;
    private String name;
    private String code;
    private String siteId;
    private int numberLanes;
    private String managementUnit;
    private DeviceStatus status;
    private Integer statusDevice;
    private Float longitude;
    private Float latitude;
    private String directionCode;
    private String directionName;
    private String createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    List<LaneDTO> lanes;
}
