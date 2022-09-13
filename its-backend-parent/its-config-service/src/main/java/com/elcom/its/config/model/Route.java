package com.elcom.its.config.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Route implements Serializable {
    public String id;

    public String name;

    public String siteStart;

    public String siteStartName;

    public long positionStartM;

    public String siteEnd;

    public String siteEndName;

    public Long positionEndM;

    public String line;

    public Long numberLanes;

    public Long emergencyStop;

    public String createdBy;

    public String code;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createDate;
}
