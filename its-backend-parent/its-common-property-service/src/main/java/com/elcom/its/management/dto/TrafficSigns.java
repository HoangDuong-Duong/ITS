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
public class TrafficSigns {

    //biển báo
    private String position;
    private String content;
    private String directionCode;
    private String size;
    private float acreagePillar;
    private int pillar;
    private float acreage;
    private String note;
    private DeviceStatus status;
    private String imagesUrl;

}
