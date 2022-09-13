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
public class EventTypeValue {

    private String eventType;
    private String eventTypeName;
    private Integer value;
}