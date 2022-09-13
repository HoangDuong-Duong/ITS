package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShiftDTO {
    public String id;
    public String number;
    public String startTime;
    public String endTime;
    public String status;
    public String note;
    public String createdBy;
    public String createdDate;
    public String modifiedBy;
    public String modifiedDate;
    public String month;
}
