package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ReportStatusEvent {
    private String eventId;
    private String startTime;
    private Boolean reportStatus;
    private String modifiedBy;
}
