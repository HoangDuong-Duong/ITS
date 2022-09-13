/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.cabinet.service.impl;

import com.elcom.its.cabinet.config.ApplicationConfig;
import com.elcom.its.cabinet.dto.Response;
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
import com.elcom.its.cabinet.service.ElectricCabinetService;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Admin
 */
@Service
@Transactional
public class ElectricCabinetServiceImpl implements ElectricCabinetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElectricCabinetServiceImpl.class);
    private static final String URI = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/electric-cabinet";
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getListElectricCabinet(String urlParam, String listStageCodes, boolean adminBackend) {
        urlParam = urlParam + "&adminBackend=" + adminBackend + "&stageCodes=" + listStageCodes;
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
    public Response createElectricCabinet(Map<String, Object> bodyMap) {
        final String uri = URI;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyMap);
        HttpEntity<Response> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Response.class);
        return response.getBody();
    }

    @Override
    public Response deleteElectricCabinet(List<String> electricCabinetIds) {
        final String uri = URI;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(electricCabinetIds);
        HttpEntity<Response> response = restTemplate.exchange(uri, HttpMethod.DELETE, requestEntity, Response.class);
        return response.getBody();
    }

    @Override
    public Response updateElectricCabinet(Map<String, Object> bodyMap) {
        final String uri = URI;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyMap);
        HttpEntity<Response> response = restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, Response.class);
        return response.getBody();
    }

    @Override
    public Response processTurnOnFan(List<String> listCabinetIds) {
        final String uri = URI + "/fan/turn-on";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(listCabinetIds);
        HttpEntity<Response> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Response.class);
        return response.getBody();
    }

    @Override
    public Response processTurnOffFan(List<String> listCabinetIds) {
        final String uri = URI + "/fan/turn-off";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(listCabinetIds);
        HttpEntity<Response> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Response.class);
        return response.getBody();
    }

    @Override
    public Response processTurnOnFireAlarm(List<String> listCabinetIds) {
        final String uri = URI + "/fire-alarm/turn-on";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(listCabinetIds);
        HttpEntity<Response> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Response.class);
        return response.getBody();
    }

    @Override
    public Response processTurnOffFireAlarm(List<String> listCabinetIds) {
        final String uri = URI + "/fire-alarm/turn-off";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(listCabinetIds);
        HttpEntity<Response> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Response.class);
        return response.getBody();
    }

    @Override
    public Response processOpenDoor(List<String> listCabinetIds) {
        final String uri = URI + "/door/open";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(listCabinetIds);
        HttpEntity<Response> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Response.class);
        return response.getBody();
    }

    @Override
    public Response processCloseDoor(List<String> listCabinetIds) {
        final String uri = URI + "/door/close";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(listCabinetIds);
        HttpEntity<Response> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Response.class);
        return response.getBody();
    }
}
