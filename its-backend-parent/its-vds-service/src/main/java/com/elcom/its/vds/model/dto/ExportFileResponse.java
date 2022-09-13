package com.elcom.its.vds.model.dto;

import lombok.Data;

@Data
public class ExportFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private int size;
}
