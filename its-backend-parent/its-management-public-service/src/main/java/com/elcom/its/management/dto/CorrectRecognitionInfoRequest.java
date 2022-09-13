/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.dto;

import lombok.*;

/**
 *
 * @author Admin
 */

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CorrectRecognitionInfoRequest {

    private String imageUrl;
    private String plate;
    private String objectType;
    private String color;
    private String brand;
    private String reason;

}
