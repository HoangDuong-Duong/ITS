/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.tollstation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Admin
 */
@Builder
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PointPlaceResponse {

    private double longitude;
    private double latitude;
    private String placeId;
    private String directionCode;

}
