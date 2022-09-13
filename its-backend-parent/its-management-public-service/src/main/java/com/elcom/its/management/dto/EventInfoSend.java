package com.elcom.its.management.dto;

import lombok.Data;

@Data
public class EventInfoSend {
    private String uuid;
    private String startDate;
    private String endDate;
    private Integer size;
    private Integer page;
    private String textHeader;
}
