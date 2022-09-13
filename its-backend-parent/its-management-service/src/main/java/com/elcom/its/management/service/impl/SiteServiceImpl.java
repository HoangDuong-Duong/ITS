/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service.impl;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.*;
import com.elcom.its.management.service.SiteService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
public class SiteServiceImpl implements SiteService {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteService.class);

    @Override
    public SiteDTO findById(String siteId) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/site/" + siteId + "?isAdmin=true&stageIdLs=null";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        try {
            HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.GET, requestBody, Response.class);
            ObjectMapper mapper = new ObjectMapper();
            SiteDTO site = mapper.convertValue(response.getBody().getData(), new TypeReference<SiteDTO>() {
            });
            return site;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<SiteDTO> findByListId(List<String> siteIds) {
        String listSiteString = String.join(",", siteIds);
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/site/list-site?siteIds=" + listSiteString;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        try {
            HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.GET, requestBody, Response.class);
            ObjectMapper mapper = new ObjectMapper();
            List<SiteDTO> siteList = mapper.convertValue(response.getBody().getData(), new TypeReference<List<SiteDTO>>() {
            });
            return siteList;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<SiteDTO> findSiteByListSiteIds(List<String> siteIds) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/site/list";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("siteIds", siteIds);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> result = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
        Response response = result != null ? result.getBody() : null;
        if(response != null && (response.getStatus() == HttpStatus.OK.value() || response.getStatus() == HttpStatus.CREATED.value())) {
            ObjectMapper mapper = new ObjectMapper();
            List<SiteDTO> eventList = mapper.convertValue(response.getData(), new TypeReference<List<SiteDTO>>() {
            });
            return eventList;
        }
        return null;
    }

    @Override
    public Point getPointByKmAndM(String urlParam) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/site/point?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        try {
            HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.GET, requestBody, Response.class);
            ObjectMapper mapper = new ObjectMapper();
            Point point = mapper.convertValue(response.getBody().getData(), new TypeReference<Point>() {
            });
            return point;
        } catch (Exception e){
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @Override
    public SiteDTO getSiteByPositionM(String urlParam) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/site/position?" + urlParam;
        String[] urlParamSplit = urlParam.split("[\\W]");
        Long positionM = Long.parseLong(urlParamSplit[1]) * 1000 + Long.parseLong(urlParamSplit[3]);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        try {
            HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.GET, requestBody, Response.class);
            ObjectMapper mapper = new ObjectMapper();
            SiteDTO siteDTO = mapper.convertValue(response.getBody().getData(), new TypeReference<SiteDTO>() {
            });
            if(siteDTO.getPositionM().equals(positionM)){
                siteDTO.setExisted(true);
            } else {
                siteDTO.setExisted(false);
            }
            return siteDTO;
        } catch (Exception e){
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @Override
    public SiteDTO checkSiteExist(SiteDTO siteDTO) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/site/check-site";
        HttpEntity<SiteDTO> requestBody = new HttpEntity<>(siteDTO, null);
        HttpEntity<Response> result = restTemplate.exchange(urlRequest, HttpMethod.POST, requestBody, Response.class);
        Response response = result != null ? result.getBody() : null;
        if(response != null && (response.getStatus() == HttpStatus.OK.value() || response.getStatus() == HttpStatus.CREATED.value())) {
            ObjectMapper mapper = new ObjectMapper();
            SiteDTO siteDTOResult = mapper.convertValue(response.getData(), new TypeReference<SiteDTO>() {
            });
            return siteDTOResult;
        }
        return null;
    }


}
