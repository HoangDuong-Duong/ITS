package com.elcom.its.management.dto;

import com.elcom.its.management.model.Job;
import lombok.Data;

import java.util.List;

@Data
public class ReportEventJobDTO {
    private String groupId;
    private String groupName;
    private List<ReportJobDTO> data;
}
