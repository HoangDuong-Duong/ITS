/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.dto;

import com.elcom.its.management.enums.JobStatus;
import com.elcom.its.management.enums.Priority;
import com.elcom.its.management.model.Device;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Admin
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDataInMap {

    private String id;
    private String jobType;
    private String groupId;
    private String userIds;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date endTime;
    private String direction;
    private String startSiteId;
    private String endSiteId;
    private String lane;
    private Priority priority;
    private String vehicleType;
    private String vehicleWeight;
    private List<Device> listDevices;
    private String notifyMethod;
    private List<String> notifyOrganization;
    private JobStatus status;
    private String description;
    private String eventId;
    private String createdBy;
    private Integer limitSpeed;
    private String placeId;
    private String placeName;
    private Date createdDate;
    private Date eventStartTime;
}
