/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service.impl;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.model.Vms;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.VmsService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
public class VmsServiceImpl implements VmsService {

    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/vms";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getAllVms(String urlParam) {
        final String uri = URI + "?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response findVmsById(String vmsId) {
        final String uri = URI + "/" + vmsId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response createVms(Map<String, Object> bodyMap, AuthorizationResponseDTO user) {
        Vms newVms = createVmsRecord(bodyMap);
        newVms.setCreatedBy(user.getUuid());
        HttpEntity<Vms> requestBody = new HttpEntity<>(newVms, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateVms(Map<String, Object> bodyMap, String vmsId) {
        final String uri = URI + "/" + vmsId;
        Vms vms = createVmsRecord(bodyMap);
        HttpEntity<Vms> requestBody = new HttpEntity<>(vms, null);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteVms(String vmsId) {
        final String uri = URI + "/" + vmsId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.DELETE, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response checkConnectionById(String urlParam) {
        final String uri = URI + "/connection?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response checkConnectionByInfo(String urlParam) {
        final String uri = URI + "/connection-by-info?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response loadVmsCamera(String urlParam) {
        final String uri = URI + "/camera?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    private Vms createVmsRecord(Map<String, Object> bodyMap) {
        String id = UUID.randomUUID().toString();
        String name = (String) bodyMap.get("name");
        String supplier = (String) bodyMap.get("supplier");
        String connectionAddress = (String) bodyMap.get("connectionAddress");
        String connectionPort = (String) bodyMap.get("connectionPort");
        String userInfo = (String) bodyMap.get("userInfo");
        String password = (String) bodyMap.get("password");
        String description = (String) bodyMap.get("description");
        int status = 0;
        String type = (String) bodyMap.get("type");
        Date createDate = new Date();
        Vms vms = Vms.builder()
                .id(id)
                .name(name)
                .connectionAddress(connectionAddress)
                .supplier(supplier)
                .connectionPort(connectionPort)
                .userInfo(userInfo)
                .password(password)
                .createDate(createDate)
                .description(description)
                .status(status)
                .type(type)
                .build();
        return vms;
    }

    @Override
    public Response addMultiCamera(Map<String, Object> bodyMap) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> listDeviceIds = mapper.convertValue(bodyMap.get("listDeviceIds"), new TypeReference<List<String>>() {
        });
        String vmsId = (String) bodyMap.get("vmsId");
        final String uri = URI + "/camera?vmsId=" + vmsId;
        HttpEntity<List<String>> requestBody = new HttpEntity<>(listDeviceIds, null);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteMulti(Map<String, Object> bodyMap) {
        ObjectMapper mapper = new ObjectMapper();
        List<String> listVmsIds = mapper.convertValue(bodyMap.get("listVmsIds"), new TypeReference<List<String>>() {
        });
        final String uri = URI + "/delete-multi";
        HttpEntity<List<String>> requestBody = new HttpEntity<>(listVmsIds, null);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.DELETE, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

}
