/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.model.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 *
 * @author SONND
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CameraUrls implements Serializable {

    private String hlsUrl;
    private String hlsUsername;
    private String hlsPassword;
    private String streamUrl;
    private String streamUsername;
    private String streamPassword;
    private String liveUrl;
}
