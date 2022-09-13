package com.elcom.its.management.dto;

import com.elcom.its.management.enums.HotLineType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotlineDTO {
    private String id;
    private String phoneNumber;
    private String content;
    private String process;
    private String eventCode;
    private String eventName;
    private HotLineType type;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date startTime;
}
