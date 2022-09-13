package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyReportDTO {
    private String id;
    private String day;
    private String userId;
    private String username;
    private String userFullName;
    private String shift;
    private String loginHistory;
    private Integer totalTime;
}
