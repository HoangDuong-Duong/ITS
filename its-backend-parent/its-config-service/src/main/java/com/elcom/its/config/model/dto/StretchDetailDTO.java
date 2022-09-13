package com.elcom.its.config.model.dto;

import com.elcom.its.config.model.Distance;
import com.elcom.its.config.model.Stretch;
import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StretchDetailDTO implements Serializable {
    private int status;
    private String message;
    private Stretch data;
}
