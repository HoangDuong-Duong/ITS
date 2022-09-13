package com.elcom.its.config.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

/**
 * @author ducduongn
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AIEngineJson {
    private String event;
    private Integer time;
    private String linkAPI;
    private String liveImageUrl;
    private List<EncroachingInfo> encroaching;
    private List<CrowdingInfo> crowding;
    private List<FallenObjectInfo> fallenobject;
}
