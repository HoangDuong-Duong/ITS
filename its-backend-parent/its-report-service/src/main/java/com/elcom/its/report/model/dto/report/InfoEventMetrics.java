package com.elcom.its.report.model.dto.report;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author ducduongn
 */
@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Data
public class InfoEventMetrics {

    private String filterBy;
    private InfoEventProcessStatusMetrics infoEventProcessStatusMetrics;
    private InfoEventChartMetrics infoEventChartMetrics;

}
