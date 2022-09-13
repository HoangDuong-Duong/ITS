package com.elcom.its.management.dto;

import com.elcom.its.management.enums.HotLineType;
import com.elcom.its.management.enums.ReportDeviceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDeviceDTO {
    private String id;
    private String name;
    private Integer online;
    private Integer offline;
    private ReportDeviceType type;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date startTime;
}
