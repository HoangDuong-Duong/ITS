package com.elcom.its.config.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author ducduongn
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class District {

    private Long provinceId;

    private Long id;

    private String name;

    private String prefix;
}
