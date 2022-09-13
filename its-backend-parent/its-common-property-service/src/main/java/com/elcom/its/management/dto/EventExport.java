package com.elcom.its.management.dto;

import lombok.Data;

@Data
public class EventExport {
    private String uuid;
    int size ;
    int page;
    String sort;
    String name;
    String position;
    String types;
    String status;
    String stages;
    Boolean isAdmin;

}
