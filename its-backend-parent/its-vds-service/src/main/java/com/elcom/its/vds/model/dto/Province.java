package com.elcom.its.vds.model.dto;

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
public class Province {

    private Long id;

    private String code;

    private String name;
}
