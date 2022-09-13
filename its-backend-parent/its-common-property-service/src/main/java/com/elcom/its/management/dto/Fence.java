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
public class Fence {
    //hàng rào
    private String positionStart;
    private String positionEnd;
    private String unit;
    private float length;
    private String note;
    private DeviceStatus status;
    private String imagesUrl;



}
