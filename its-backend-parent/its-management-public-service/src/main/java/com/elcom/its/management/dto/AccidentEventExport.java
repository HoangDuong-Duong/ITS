package com.elcom.its.management.dto;

import lombok.Data;

@Data
public class AccidentEventExport {
    private String uuid;
    private String startDate;
    private String endDate;
    private String textHeader;
}
