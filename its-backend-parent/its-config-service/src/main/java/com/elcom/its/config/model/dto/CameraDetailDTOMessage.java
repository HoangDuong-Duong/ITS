package com.elcom.its.config.model.dto;

import com.elcom.its.config.model.CameraDTO;
import lombok.*;

import java.io.Serializable;

/**
 * @author ducduongn
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CameraDetailDTOMessage implements Serializable {
    private int status;
    private String message;
    private CameraDTO data;
}
