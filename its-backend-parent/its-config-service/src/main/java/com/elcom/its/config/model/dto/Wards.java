package com.elcom.its.config.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Column;

/**
 * @author ducduongn
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wards {
    private Long id;

    private String name;
    
    private String prefix;

    private Long provinceId;

    private Long districtId;
}
