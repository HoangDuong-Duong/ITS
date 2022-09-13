package com.elcom.its.tollstation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrafficStatus {
    private Integer id;
    private long volume;
    private String statusCode;
    private String statusName;
    private String createdBy;
    private Date createDate;
}
