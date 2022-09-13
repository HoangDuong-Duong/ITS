package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrafficDataByObject {
    private String objectType;
    private String objectName;
    private long volume;
    private Float speed;
}
