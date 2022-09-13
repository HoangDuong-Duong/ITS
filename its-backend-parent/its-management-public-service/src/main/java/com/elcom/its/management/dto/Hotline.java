package com.elcom.its.management.dto;

import lombok.Data;

import java.util.List;

@Data
public class Hotline {
    List<HotlineDTO> security;
    List<HotlineDTO> trafficIntructions;
    List<HotlineDTO> serviceQuality;
    List<HotlineDTO> otherInfo;
    Long countMonth;
}
