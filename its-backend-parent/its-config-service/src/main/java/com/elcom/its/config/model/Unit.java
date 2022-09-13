package com.elcom.its.config.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Unit {
    private String uuid;
    private String code;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;
    private String lisOfStage;
    private Object listOfJob;
}
