package com.elcom.its.config.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DisplayScript {
    private String id;

    private String name;

    private String boardId;

    private String newsType;

    private String newsletterId;

    private String startTime;

    private String endTime;

    private float startTimeFloat;

    private float endTimeFloat;

    private Integer priority;

    private Integer repeat;

    private Integer status;

    private String origin;

    private String now;

    private String createdBy;

    private Date createDate;
}
