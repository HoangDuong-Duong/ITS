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
public class ElectricalCabinets {
    // tủ điện
    private String site;
    private int substation;
    private int lowVoltageStation;
    private int ats;
    private int distributionCabinet;
    private int chargingStation;
    private int lightingControl;
    private String unit;
    private int quantity;
    private String note;
    private DeviceStatus status;
    private String imagesUrl;
}
