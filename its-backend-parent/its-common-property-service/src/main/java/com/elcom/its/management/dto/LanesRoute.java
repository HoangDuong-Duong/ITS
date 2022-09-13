package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LanesRoute {

    @JsonProperty("id")
    public String id;

    @JsonProperty("laneCode")
    public String laneCode;

    @JsonProperty("laneName")
    public String laneName;

    @JsonProperty("number")
    public Short number;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @JsonProperty("mapDirection")
    public String mapDirection;


}
