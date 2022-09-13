package com.elcom.its.management.dto;

import com.elcom.its.management.enums.DeviceStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bridge {
    // cầu
    private String position;
    private String name;
    private float length;
    private float with;
    private String diagram;
    private Integer rhythm;
    private float meters;
    private int pillar;
    private String note;
    private String bearingType;
    private int bearingQuantity;
    private String expansionJointsType;
    private float expansionJointsQuantity;
    private float bridgeRailingLength;
    private float acreage;
    private float acreageNew;
    private float acreagePillar;
    private DeviceStatus status;
    private String imageUrl;

}
