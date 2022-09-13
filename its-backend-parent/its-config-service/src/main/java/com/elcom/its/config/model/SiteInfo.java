package com.elcom.its.config.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@SuperBuilder
@Setter
@Getter
@RequiredArgsConstructor
@ToString
public class SiteInfo implements Serializable {
    public String siteId;

    public String siteName;

    public String siteAddress;

    public Float longitude;

    public Float latitude;

    public Long provinceId;

    public String provinceName;

    public Long districtId;

    public String districtName;

    public Long wardId;

    public String wardName;

    public Long positionM;
}
