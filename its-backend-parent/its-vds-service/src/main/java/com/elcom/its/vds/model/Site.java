package com.elcom.its.vds.model;

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
public class Site implements Serializable {

    private String id;

    private String name;

    private Integer km;

    private Integer m;

    private Float longitude;

    private Float latitude;

    private String address;

    private Long wardId;

    private Long districtId;

    private Long provinceId;

    private String note;

    private String code;
}
