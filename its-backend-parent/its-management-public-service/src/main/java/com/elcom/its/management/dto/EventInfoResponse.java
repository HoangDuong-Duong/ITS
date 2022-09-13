package com.elcom.its.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventInfoResponse {
    private Integer status;

    private String message;

    private EventInfoDTO data;
}
