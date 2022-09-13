package com.elcom.its.config.model.dto;

import com.elcom.its.config.model.Route;
import com.elcom.its.config.model.Stretch;
import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RouteDetailDTO implements Serializable {
    private int status;
    private String message;
    private Route data;
}
