package com.elcom.its.config.model.dto;

import com.elcom.its.config.model.CameraDTO;

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
public class CameraPaginationDTO {
    private int status;
    private String message;
    private List<CameraDTO> data;
    private Long total;
    private Long size;
    private Long page;
}
