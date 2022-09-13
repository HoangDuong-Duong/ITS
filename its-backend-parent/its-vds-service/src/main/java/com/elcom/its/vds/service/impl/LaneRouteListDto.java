package com.elcom.its.vds.service.impl;

import com.elcom.its.vds.model.dto.DirectionDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LaneRouteListDto implements Serializable {
    private List<DirectionDto> content;
    private Integer totalElements;
    private Integer totalPages;
}
