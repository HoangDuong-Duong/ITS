package com.elcom.its.config.model.dto;

import com.elcom.its.config.model.Distance;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DistanceDTO implements Serializable {
    private int status;
    private String message;
    private List<Distance> data;
    private Long total;
    private Long size;
    private Long page;
}
