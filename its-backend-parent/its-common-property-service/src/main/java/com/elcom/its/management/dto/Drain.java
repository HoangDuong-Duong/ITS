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
public class Drain {
    // cống
    private String position;
    private String categories;
    private float bridgeRailingLength;
    private String aperture; // khẩu độ
    private String note;
    private DeviceStatus status;
    private String imageUrl;


}
