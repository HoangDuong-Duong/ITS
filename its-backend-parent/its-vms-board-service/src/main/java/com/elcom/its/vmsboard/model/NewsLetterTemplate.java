package com.elcom.its.vmsboard.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewsLetterTemplate implements Serializable {
    private String id;

    private String name;

    private String typeBoard;

    private String typeEvent;

    private Object content;

    private Object source;

    private String preview;

    private String createdBy;

    private Integer priority;

    private String jobType;

    private Date createDate;

}
