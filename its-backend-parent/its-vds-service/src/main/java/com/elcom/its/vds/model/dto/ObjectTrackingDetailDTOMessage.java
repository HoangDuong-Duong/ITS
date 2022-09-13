package com.elcom.its.vds.model.dto;

import com.elcom.its.vds.model.ObjectTracking;
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
