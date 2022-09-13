/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service.impl;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.PlaceService;
import com.elcom.its.utils.StringUtil;
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

/**
 *
 * @author Admin
 */
@Service
public class PlaceServiceImpl implements PlaceService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceServiceImpl.class);
    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/place";
    @Autowired
    private RestTemplate restTemplate;
    
    @Override
    public Response getAllPlace(String urlParam, boolean adminBackend, String stageIds) {
        if (!StringUtil.isNullOrEmpty(urlParam)) {
            urlParam += "&";
        }
        urlParam += "adminBackend=" + adminBackend + "&stageIds=" + stageIds;
        final String uri = URI + "?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        LOGGER.info("Response from ITS core", response.toString());
        return response;
    }
    
}
