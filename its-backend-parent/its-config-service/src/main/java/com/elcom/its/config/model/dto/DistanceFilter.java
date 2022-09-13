package com.elcom.its.config.model.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DistanceFilter {
    private String id;
    private String name;
    private String siteStart;
    private String siteEnd;
}
