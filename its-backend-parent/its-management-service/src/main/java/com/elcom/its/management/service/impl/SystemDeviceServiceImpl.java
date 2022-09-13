/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service.impl;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.GetDeviceStatusResponse;
import com.elcom.its.management.dto.Response;
import com.elcom.its.management.model.Device;
import com.elcom.its.management.service.SystemDeviceService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class SystemDeviceServiceImpl implements SystemDeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemDeviceServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<GetDeviceStatusResponse> getDeviceStatus(List<Device> listDevices) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/system-device/status";
        HttpEntity<List<Device>> requestEntity = new HttpEntity<>(listDevices);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
        Response getStatusResponse = response != null ? response.getBody() : null;
        if (getStatusResponse != null && (getStatusResponse.getStatus() == HttpStatus.OK.value() || getStatusResponse.getStatus() == HttpStatus.CREATED.value())) {
            ObjectMapper mapper = new ObjectMapper();
            List<GetDeviceStatusResponse> responses = mapper.convertValue(getStatusResponse.getData(), new TypeReference<List<GetDeviceStatusResponse>>() {
            });
            return responses;
        }
        return null;
    }

}
