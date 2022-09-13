package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrafficDataByLane {
    private Short numberLane;
    private Long totalTraffic;
    List<TrafficDataByObject> trafficDataDetails;
}
