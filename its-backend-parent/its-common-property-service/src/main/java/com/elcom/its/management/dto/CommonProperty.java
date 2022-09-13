/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.dto;

import com.elcom.its.management.enums.DeviceStatus;
import com.elcom.its.management.enums.PropertyType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Admin
 */
@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonProperty implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private Integer km;
    private Integer m;
    private Long positionM;
    private Integer kmEnd;
    @JsonProperty("mEnd")
    private Integer mEnd;
    private Long positionMEnd;
    private String position;
    private String note;
    private PropertyType type;
    private String imageUrl;
    private String createdBy;
    private Date createdDate;
    private Object data;
    private DeviceStatus status;
}
