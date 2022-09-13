/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service.impl;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.Point;
import com.elcom.its.management.dto.PointPlaceResponse;
import com.elcom.its.management.dto.Response;
import com.elcom.its.management.service.PlaceService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
public class PlaceServiceImpl implements PlaceService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<PointPlaceResponse> getListPointPlace(List<String> listPlaceStringIds) {
        String listPlaceIds = String.join(",", listPlaceStringIds);
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/place/listPoint?placeIds=" + listPlaceIds;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.GET, requestBody, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            ObjectMapper mapper = new ObjectMapper();
            List<PointPlaceResponse> point = mapper.convertValue(dto.getData(), new TypeReference<List<PointPlaceResponse>>() {
            });
            return point;
        }
        return null;
    }

}
