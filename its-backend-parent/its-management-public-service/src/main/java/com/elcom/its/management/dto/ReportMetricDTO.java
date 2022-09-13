package com.elcom.its.management.dto;

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
public class ReportMetricDTO {
    private String cameraId;
    private String cameraName;
    private MetricDTO trafficMetrics;
    private MetricDTO violationMetrics;
}
