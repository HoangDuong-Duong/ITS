/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service;

import com.elcom.its.management.dto.PointPlaceResponse;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface PlaceService {

    List<PointPlaceResponse> getListPointPlace(List<String> listPlaceStringIds);
}
