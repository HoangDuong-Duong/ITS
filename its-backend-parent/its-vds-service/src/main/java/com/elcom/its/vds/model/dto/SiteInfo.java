/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SiteInfo implements Serializable {


    @JsonProperty("site_id")
    public String siteId;
    @JsonProperty("site_name")
    public String siteName;
    @JsonProperty("site_address")
    public String siteAddress;
    @JsonProperty("longitude")
    public Float longitude;
    @JsonProperty("latitude")
    public Float latitude;
    @JsonProperty("province_id")
    public Long provinceId;
    @JsonProperty("province_name")
    public String provinceName;
    @JsonProperty("district_id")
    public Long districtId;
    @JsonProperty("district_name")
    public String districtName;
    @JsonProperty("ward_id")
    public Long wardId;
    @JsonProperty("ward_name")
    public String wardName;
    @JsonProperty("position_m")
    public Long positionM;
    @JsonProperty("km")
    public Integer km;
    @JsonProperty("m")
    public Integer m;
    @JsonProperty("code")
    public String code;
    public String note;
}
