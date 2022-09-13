package com.elcom.its.vmsboard.model;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DisplayDetailDTO {
    private String name;

    private Integer priority;

    private Object content;

    private Object source;

    private String preview;
}
