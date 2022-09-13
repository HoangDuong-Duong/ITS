package com.elcom.its.tollstation.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDevice implements Serializable {
    ReportDeviceDTO camera ;
    ReportDeviceDTO ptz ;
    ReportDeviceDTO vsmBoard ;
    ReportDeviceDTO cadpro ;
    ReportDeviceDTO screen;
    ReportDeviceDTO countMonth;



}
