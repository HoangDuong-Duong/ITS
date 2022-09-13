/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

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
public class ReportStatisticResponse {

    private String sourceId;
    private String sourceName;
    private String siteAddress;
    private List<ObjectTypeValue> objectTypeValueList;  
}
