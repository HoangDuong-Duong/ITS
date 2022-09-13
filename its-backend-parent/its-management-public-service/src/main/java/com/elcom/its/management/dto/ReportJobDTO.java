package com.elcom.its.management.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReportJobDTO {
    private String jobName;
    private String jobCode;
    private String key;
    private String eventCode;
    private String eventName;
    private Date startTime;
    private Date updateTime;
    private String statusName;


}
