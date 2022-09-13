package com.elcom.its.report.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class ExportFileResponse implements Serializable {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private int size;
}
