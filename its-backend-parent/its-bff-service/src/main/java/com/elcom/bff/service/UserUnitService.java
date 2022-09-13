/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.bff.service;

import com.elcom.bff.dto.Response;
import java.util.Map;

/**
 *
 * @author Admin
 */
public interface UserUnitService {

    Response findUserByUnit(String urlParam, String pathParam, Map<String, String> headerParam);
}
