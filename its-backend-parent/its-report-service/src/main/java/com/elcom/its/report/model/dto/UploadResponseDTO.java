package com.elcom.its.report.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class UploadResponseDTO implements Serializable {

    private int status;
    private String message;
    private ExportFileResponse data;
}
