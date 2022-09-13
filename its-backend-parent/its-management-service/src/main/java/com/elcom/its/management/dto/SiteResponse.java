package com.elcom.its.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteResponse {

    private Integer status;

    private String message;

    private List<SiteDTO> data;
}
