package com.elcom.its.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportEventDailyResponse {
    private Integer status;

    private String message;

    private ReportEventDaily data;
}
