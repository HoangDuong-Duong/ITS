/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Admin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class VmsBoardJobProcessing {
    private String vmsBoardId;
    private String vmsBoardType;
    private String siteId;
    private String siteName;
    private String templateId;
    private String content;
    private boolean status;
}
