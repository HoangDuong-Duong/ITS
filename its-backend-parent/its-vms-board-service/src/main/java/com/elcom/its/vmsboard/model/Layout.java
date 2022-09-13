package com.elcom.its.vmsboard.model;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Layout implements Serializable {
    private Integer contentSite;
    private Integer type;
    private Object properties;
    private Integer layoutId;
}
