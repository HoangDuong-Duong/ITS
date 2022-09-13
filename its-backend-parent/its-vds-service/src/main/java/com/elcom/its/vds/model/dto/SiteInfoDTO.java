/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteInfoDTO implements Serializable {


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

}
