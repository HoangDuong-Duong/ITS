package com.elcom.its.report.model.dto.report;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

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
public class VehicleTrafficFlowMetrics {

    private String filterBy;
    private List<ObjectTypeValue> dataObjectType;

}

