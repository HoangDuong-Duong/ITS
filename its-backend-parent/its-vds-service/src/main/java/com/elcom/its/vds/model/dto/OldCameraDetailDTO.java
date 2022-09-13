/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.model.dto;

import com.elcom.its.vds.enums.CameraStatus;
import com.elcom.its.vds.enums.DataStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hanh
 */
@SuperBuilder
@Setter
@Getter
@RequiredArgsConstructor
@ToString
@Data
@AllArgsConstructor
public class OldCameraDetailDTO implements Serializable {

    private String id;
    private String name;
    private String cameraKey;
    private String cameraModel;
    private String cameraType;
    private Sites site;
    private Float longitude;
    private Float latitude;
    private CameraSetupAttribute setupAttributes;
    private CameraUrls urls;
    private String note;
    private String imageUrl;
    private DataStatus status;
    private CameraStatus cameraStatus;
    private List<String> groupCameraIds;
    private String createdBy;
    private String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedDate;
    
    //Add new
    private String siteId;
    private PtzInfo ptzInfo;
    private String vmsId;
    private String directionCode;
    private String directionString;
    private int ptz;
}
