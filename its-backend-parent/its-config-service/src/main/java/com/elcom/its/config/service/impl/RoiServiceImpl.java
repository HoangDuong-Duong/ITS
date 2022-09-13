/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service.impl;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.model.dto.RoisUpsertDTO;
import com.elcom.its.config.service.RoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
public class RoiServiceImpl implements RoiService {

    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/rois";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response save(RoisUpsertDTO dto) {
        return restTemplate.postForObject(URI, dto, Response.class);
    }

    @Override
    public Response remove(RoisUpsertDTO dto) {
        HttpEntity<RoisUpsertDTO> requestBody = new HttpEntity<>(dto, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI, HttpMethod.DELETE, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

}
