/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.bff.dto;

import com.elcom.bff.enums.EventStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Admin
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

@JsonIgnoreProperties(ignoreUnknown = true)
public class ViolationDetailDTO implements Serializable {

    private String id;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    
    private String eventId;
    
    private String sourceId;
    
    private String sourceName;
    
    private String directionCode;
    
    private SiteInfo site;
    
    private Object detail;
    
    private LanesRouteDTO lane;
    
    private String imageUrl;
    
    private String videoUrl;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    
    private String plate;
    
    private Integer event;
    
    private String eventCode;
    
    private String eventName;
    
    private String objectType;
    
    private String objectName;
    
    private Float speed;
    
    private EventStatus eventStatus;
    
    private String nameStatus;
    
    private String eventState;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date imageTime;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    
    private Integer isNewest;
    
    private Integer isDelete;

    private String parentId;

    private String createBy;

    private String modifiedBy;

    private String modifiedAction;

    private String eventIdString;

    private String imageCtxtUrl;
}
