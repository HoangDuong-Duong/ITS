/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service.impl;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.GetDeviceStatusResponse;
import com.elcom.its.management.dto.Response;
import com.elcom.its.management.dto.UpdateFmsHistoryRequest;
import com.elcom.its.management.model.Device;
import com.elcom.its.management.service.FmsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
public class FmsServiceImpl implements FmsService {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(FmsServiceImpl.class);

    @Override
    public void updateFmsHistory(List<UpdateFmsHistoryRequest> listUpdateFmsHistoryRequests) {
        try {
            String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/common-property/list-history";
            HttpEntity<List<UpdateFmsHistoryRequest>> requestEntity = new HttpEntity<>(listUpdateFmsHistoryRequests);
            HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
            LOGGER.info(response.toString());
        } catch (Exception e) {
        }

    }

}
