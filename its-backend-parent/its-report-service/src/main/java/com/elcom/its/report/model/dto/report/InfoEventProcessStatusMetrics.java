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
public class InfoEventProcessStatusMetrics {

    private Integer totalEvent;
    private Integer totalEventWait;
    private Integer totalEventNotSeen;
    private Integer totalEventVerification;
    private Integer totalEventProcessing;
    private Integer totalEventProcessed;
    private Integer totalEventIncorrect;
}