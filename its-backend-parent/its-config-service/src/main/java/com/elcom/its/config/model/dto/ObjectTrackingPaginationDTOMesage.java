package com.elcom.its.config.model.dto;

import com.elcom.its.config.model.ObjectTracking;
import lombok.*;

import java.util.List;

/**
 * @author ducduongn
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ObjectTrackingPaginationDTOMesage {
    private int status;
    private String message;
    private List<ObjectTracking> data;
    private Long total;
    private Long size;
    private Long page;
}
