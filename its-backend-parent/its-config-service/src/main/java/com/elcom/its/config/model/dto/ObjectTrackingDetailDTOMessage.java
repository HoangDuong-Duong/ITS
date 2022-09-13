package com.elcom.its.config.model.dto;

import com.elcom.its.config.model.ObjectTracking;
import lombok.*;

/**
 * @author ducduongn
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ObjectTrackingDetailDTOMessage {
    private int status;
    private String message;
    private ObjectTracking data;
}
