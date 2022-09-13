/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service;

import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.Response;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public interface VmsService {

    Response getAllVms(String urlParam);

    Response findVmsById(String vmsId);

    Response createVms(Map<String, Object> bodyMap, AuthorizationResponseDTO user);

    Response updateVms(Map<String, Object> bodyMap, String vmsId);

    Response deleteVms(String vmsId);

    Response checkConnectionById(String urlParam);

    Response checkConnectionByInfo(String urlParam);

    Response loadVmsCamera(String urlParam);

    Response addMultiCamera( Map<String, Object> bodyMap);

    Response deleteMulti(Map<String, Object> bodyMap);

}
