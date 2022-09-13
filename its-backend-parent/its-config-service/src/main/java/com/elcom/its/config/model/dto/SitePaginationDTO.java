package com.elcom.its.config.model.dto;

import com.elcom.its.config.model.SiteInfo;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SitePaginationDTO implements Serializable {
    private int status;
    private String message;
    private List<SiteInfo> data;
    private Long total;
    private Long size;
    private Long page;
}
