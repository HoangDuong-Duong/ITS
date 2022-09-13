package com.elcom.its.vds.model.dto;

import com.elcom.its.vds.model.Distance;
import com.elcom.its.vds.model.Stretch;
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
