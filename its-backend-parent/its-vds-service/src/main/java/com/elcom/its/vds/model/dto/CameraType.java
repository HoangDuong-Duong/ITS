package com.elcom.its.vds.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

/**
 * @author ducduongn
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CameraType implements Serializable {
    private String status;
    private Integer id;
    private String catType;
    private String catName;
    private String code;
    private String description;
    private String value;
    private String name;
    private Integer orderInType;
}
