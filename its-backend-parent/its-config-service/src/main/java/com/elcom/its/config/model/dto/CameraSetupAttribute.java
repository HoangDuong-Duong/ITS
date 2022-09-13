/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.model.dto;

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
public class CameraSetupAttribute implements Serializable {

    private String farestSp; // K/c xa nhất từ điểm nhìn thấy -> chân camera
    private String nearestSp; // K/c gần nhất từ điểm nhìn thấy -> chân camera
    private String focalLength; // Tiêu cự ống kính
    private String installationHeight; // Chiều cao lắp đặt
}
