package com.elcom.its.vds.model.dto;

import lombok.Data;

@Data
public class ResponseDTO {
    private int status;
    private String message;
    private ExportFileResponse data;
}
