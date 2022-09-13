package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportOnlineStatusDTO implements Serializable {

    private String id;
    private String jobType;
    private String eventId;
    private String eventCode;
    private String eventName;
    private String startTime;
    private String endTime;
    private String groupId;
    private String groupCode;
    private String groupName;
    private String startSiteId;
    private String endSiteId;
    private String startSite;
    private String endSite;
    private String description;
    private String processResult;
    private String placeName;
    private String eventStartTime;

}
