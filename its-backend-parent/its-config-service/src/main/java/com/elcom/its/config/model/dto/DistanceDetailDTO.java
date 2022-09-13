package com.elcom.its.config.model.dto;

import com.elcom.its.config.model.Distance;
import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DistanceDetailDTO implements Serializable {
    private int status;
    private String message;
    private Distance data;
}
