package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventInfoDTO {
    private String id;
    private String eventType;
    private String siteId;
    private String endSiteId;
    private String siteCorrect;
    private String direction;
    private String source;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date eventDate;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date dateReceived;
    private String reportMethod;
    private String priority;
    private String info;
    private String reasonRoad;
    private String reasonPerson;
    private String reasonVehicle;
    private String reasonOther;
    private String reasonOrigin;
    private Integer numberDead;
    private Integer numberHurt;
    private Float fortuneRoad;
    private Float fortuneVehicle;
    @JsonProperty("vehicle")
    private List<String> vehicle;
    private String road;
    private String trafficCondition;
    private String note;
    private String process;
    private String videoUrl;
    private String imageUrl;
    private String eventId;
    private Integer type;
    private String objectName;
    private String eventKey;
    private Long startKm;
    private Long startM;
    private Integer startProvinceId;
    private Integer startDistrictId;
    private Integer startWardId;
    private String startAddress;
    private Long endKm;
    private Long endM;
    private Integer endProvinceId;
    private Integer endDistrictId;
    private Integer endWardId;
    private String endAddress;
    private String reasonPolice;
    private Integer lane;
    private Integer damageVehicle;
    private Integer damageStructure;
    private String state;
    private String content;
    private String dear;
    private String title;
    private String email;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+7")
    private Date endTime;
}
