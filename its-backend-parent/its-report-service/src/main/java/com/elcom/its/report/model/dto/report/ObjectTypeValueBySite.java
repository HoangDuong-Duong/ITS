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
public class ObjectTypeValueBySite {

    private String site;
    private List<ObjectTypeValue> dataObjectType;
    private Long total;
}
