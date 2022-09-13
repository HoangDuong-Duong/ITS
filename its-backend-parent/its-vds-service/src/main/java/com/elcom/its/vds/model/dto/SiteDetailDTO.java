package com.elcom.its.vds.model.dto;

import com.elcom.its.vds.model.SiteInfo;
import lombok.*;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SiteDetailDTO implements Serializable {
    private int status;
    private String message;
    private SiteInfo data;
}
