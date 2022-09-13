package com.elcom.its.vmsboard.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DisplayScript implements Serializable {
    private String id;

    private String name;

    private String boardId;

    private String newsletterId;

    private String startTime;

    private Object content;

    private Object source;

    private String preview;

    private String scripBaseId;

    private Boolean isDefaultDisplay;

    private String endTime;

    private Integer priority;

    private Integer repeat;

    private Integer status;

    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date date;

    private String parentId;
}
