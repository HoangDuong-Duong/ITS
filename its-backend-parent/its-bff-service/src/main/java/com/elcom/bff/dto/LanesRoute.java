package com.elcom.bff.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @JsonProperty("mapDirection")
    public String mapDirection;
}
