package com.elcom.its.vds.model.dto;

import com.elcom.its.vds.model.Distance;
import com.elcom.its.vds.model.Route;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoutePaginationDTO implements Serializable {
    private int status;
    private String message;
    private List<Route> data;
    private Long total;
    private Long size;
    private Long page;
}
