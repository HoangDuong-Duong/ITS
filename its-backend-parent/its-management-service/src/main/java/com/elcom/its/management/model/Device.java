/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 *
 * @author Admin
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {

    private String deviceId;
    private String deviceType;
    private String deviceName;

}
