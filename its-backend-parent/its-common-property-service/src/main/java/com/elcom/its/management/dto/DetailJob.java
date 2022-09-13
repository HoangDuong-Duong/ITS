/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.dto;

import com.elcom.its.management.enums.JobStatus;
import com.elcom.its.management.enums.Priority;
import com.elcom.its.management.model.ActionHistory;
import com.elcom.its.management.model.Device;
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
@NoArgsConstructor
@AllArgsConstructor
public class DetailJob {

    private String id;
    private String jobType;

    private String groupId;

    private String userIds;

    private Date startTime;

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

    private List<ActionHistory> actionHistory;

    private String description;

    private String eventId;

    private String createdBy;

    private Integer limitSpeed;

    private String placeId;

    private String placeName;

    private Date createdDate;

    private Object processResult;

    private Date eventStartTime;

    private SiteDTO startSite;

    private SiteDTO endSite;

    private String eventCode;

    private String eventName;

    private String sourceId;

}
