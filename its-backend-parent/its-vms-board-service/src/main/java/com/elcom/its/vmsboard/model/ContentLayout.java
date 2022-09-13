package com.elcom.its.vmsboard.model;

import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContentLayout implements Serializable {
    private String name;
    private String content;
    private Integer site;
    private Integer layoutId;
}
