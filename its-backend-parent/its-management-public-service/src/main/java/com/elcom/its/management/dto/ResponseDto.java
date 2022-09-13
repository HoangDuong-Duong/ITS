package com.elcom.its.management.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private int status;
    private String message;
    private ExportFileResponse data;
}
