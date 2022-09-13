package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccidentReport {
    String nameRoad;
    Integer numberAccident;
    Integer lowPriority;
    Integer mediumPriority;
    Integer seriousPriority;
    Integer maxSeriousPriority;
    Integer reasonRoad;
    Integer reasonPerson;
    Integer reasonVehicle;
    Integer reasonOther;
    Integer numberDead;
    Integer numberHurt;
    Float fortuneRoad;
    Float fortuneVehicle;
}
