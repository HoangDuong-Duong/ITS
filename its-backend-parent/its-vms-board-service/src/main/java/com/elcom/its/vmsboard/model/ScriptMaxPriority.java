package com.elcom.its.vmsboard.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ScriptMaxPriority {
    private String name;
    private String boardId;
    private String newsLetterId;
    private Object content;
    private Object source;
    private String preview;
    private String endTime;
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+7")
    private Date date;
}
