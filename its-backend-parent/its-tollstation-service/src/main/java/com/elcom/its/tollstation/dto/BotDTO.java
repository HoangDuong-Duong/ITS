package com.elcom.its.tollstation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BotDTO implements Serializable {
    private String name;
    private String code;
    private String siteId;
    private String directionCode;
    private Integer numberLanes;
    private String managementUnit;
}
