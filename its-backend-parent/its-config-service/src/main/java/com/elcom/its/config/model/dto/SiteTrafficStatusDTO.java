package com.elcom.its.config.model.dto;

import com.elcom.its.config.model.TrafficStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SiteTrafficStatusDTO {

    private String id;

    private String name;

    private float longitude;

    private float latitude;

    private String address;

    private Wards wardId;

    private Province provinceId;

    private District districtId;

    private Integer km;

    private Integer m;

    private Long positionM;

    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdDate;

    private Float longitudeTop;

    private Float latitudeTop;

    private Float longitudeBottom;

    private Float latitudeBottom;

    private Float size;

    private TrafficStatus trafficStatusTop;

    private TrafficStatus trafficStatusBottom;
}
