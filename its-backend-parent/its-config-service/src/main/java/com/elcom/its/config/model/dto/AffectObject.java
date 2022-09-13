package com.elcom.its.config.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * @author ducduongn
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AffectObject {
    private String label;
    private double det_ratio;
    private Long max_ratio;
    private double min_ratio;
    private Long min_area;
    private Long direction;
}
