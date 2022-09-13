package com.elcom.its.vds.model.dto;

import com.elcom.its.vds.model.Distance;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DistancePaginationDTO implements Serializable {
    private int status;
    private String message;
    private List<Distance> data;
    private Long total;
    private Long size;
    private Long page;

}
