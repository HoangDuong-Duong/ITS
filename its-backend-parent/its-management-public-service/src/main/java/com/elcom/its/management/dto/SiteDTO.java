/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 *
 * @author Admin
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteDTO {
     public String siteId;

    public String siteName;

    public String siteAddress;

    public double longitude;

    public double latitude;

    public Long provinceId;

    public String provinceName;

    public Long districtId;

    public String districtName;

    public Long wardId;

    public String wardName;

    public Long positionM;

    public Integer km;

    public Integer m;

    public String code;

    public String note;

    public boolean existed;
}
