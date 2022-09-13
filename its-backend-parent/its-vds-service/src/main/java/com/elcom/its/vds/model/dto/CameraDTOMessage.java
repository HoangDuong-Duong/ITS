package com.elcom.its.vds.model.dto;

import com.elcom.its.vds.model.CameraDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author ducduongn
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CameraDTOMessage implements Serializable {
    private int status;
    private String message;
    private List<CameraDTO> data;
    private Long total;
    private Long size;
    private Long page;
}
