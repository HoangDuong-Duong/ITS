package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrafficFlowSite {
    private String siteId;
    private String siteName;
    private String siteAfterNearId;
    private String siteAfterNearName;
    private Long totalTraffic;
    private List<TrafficDataByLane> trafficDataByLanes;
    private List<TrafficDataByObject> trafficDataByObjects;
    private String directionCode;
    private TrafficStatus trafficStatus;
    private Float calculateAverageSpeed;
}
