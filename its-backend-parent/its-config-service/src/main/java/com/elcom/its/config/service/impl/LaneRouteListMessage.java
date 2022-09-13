package com.elcom.its.config.service.impl;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LaneRouteListMessage implements Serializable {
    private int status;
    private String message;
    private LaneRouteListDto data;
}
