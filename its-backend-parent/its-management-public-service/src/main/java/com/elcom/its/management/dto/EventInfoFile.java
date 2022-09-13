package com.elcom.its.management.dto;

import lombok.*;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventInfoFile {
    private static final long serialVersionUID = 1L;
    private String id;
    private Date startTime;
    private String fileType;
    private String fileName;
    private String fileUrl;
    private Integer size;
    private Date uploadTime;
    private String createBy;
    private Date createDate;
}
