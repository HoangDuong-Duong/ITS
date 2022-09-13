/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Admin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteInfoNewDTO implements Serializable {

    private String id;
    private String name;
    public double longitude;
    public double latitude;
    private String address;
    private Wards wardId;
    private Province provinceId;
    private District districtId;
    private Long positionM;
    private Integer km;
    private Integer m;
    private String code;
    private String note;
    private double longitudeTop;
    private double latitudeTop;
    private double longitudeBottom;
    private double latitudeBottom;
    private Integer size;
}
