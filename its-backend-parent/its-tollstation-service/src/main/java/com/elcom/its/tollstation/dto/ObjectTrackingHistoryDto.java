package com.elcom.its.tollstation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectTrackingHistoryDto {
    private ObjectTrackingDto objectTrackingId;
    private Float speed;
    private String createBy;
    private Date createDate;
    private String sourceId;
    private String sourceName;
    private SiteInfo site;
    private String imageUrl;
}
