/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vms.service.impl;

import com.elcom.its.vms.config.ApplicationConfig;
import com.elcom.its.vms.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.elcom.its.vms.service.VmsService;

/**
 *
 * @author Admin
 */
@Service
public class VmsServiceImpl implements VmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VmsServiceImpl.class);
    private static final String URI = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/vms";
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getLiveImage(String urlParam) {
        final String uri = URI + "/live-image?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response cutVideo(String startTime, String endTime,String cameraId) {
        final String uri = URI + "/cut-video?" + "startTime="+startTime+ "&endTime="+endTime+ "&cameraId="+cameraId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }
}
