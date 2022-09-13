package com.elcom.its.management.dto;

import com.elcom.its.management.enums.DeviceStatus;
import com.elcom.its.management.enums.TypeEquipment;
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
public class AuxiliaryEquipment {
    // Thiết bị phụ trợ

    private String position;

    private String site;

    private TypeEquipment type;

    private int quantity;

    private String unit;

    private String nameCamera;

    private String note;

    private DeviceStatus status;

    private String imagesUrl;
}
