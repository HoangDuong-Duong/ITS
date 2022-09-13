package com.elcom.its.vds.model.dto;

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
public class EncroachingInfo {
    private Long id;
    private String zone;
    private List<AffectObject> affect_object;
}
