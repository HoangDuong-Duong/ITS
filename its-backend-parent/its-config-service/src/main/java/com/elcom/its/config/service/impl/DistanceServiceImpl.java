package com.elcom.its.config.service.impl;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.model.Distance;
import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.dto.*;
import com.elcom.its.config.service.DistanceService;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class DistanceServiceImpl implements DistanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceServiceImpl.class);
    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/distance";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getAllDistance() {
        final String uri = URI;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getAllDistance(Integer page, Integer size, String search) {
        String params = null;
        try {
            if(!StringUtil.isNullOrEmpty(search)){
                params = "page=" + page + "&size=" + size + "&search=" + URLEncoder.encode(search, "UTF-8") + "&sort=id";
            }else{
                params = "page=" + page + "&size=" + size + "&search="+ "&sort=id";
            }

        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/distance?" + params;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response distancePaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find Category by catType from DBM: {}", distancePaginationDTO);
        return distancePaginationDTO;

    }

    @Override
    public Response getDistanceById(String id) {
        final String uri = URI + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + id, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response saveDistance(Distance distance) {
        HttpEntity<Distance> requestBody = new HttpEntity<>(distance, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI, HttpMethod.POST,requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateDistance(Distance distance) {
        HttpEntity<Distance> requestBody = new HttpEntity<>(distance, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + distance.getId(), HttpMethod.PUT,requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteDistance(String distance) {
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + distance, HttpMethod.DELETE,null, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteMultiDistance(ListUuid distance) {
        HttpEntity<ListUuid> requestBody = new HttpEntity<>(distance, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/multi-delete", HttpMethod.DELETE, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response filterDistance(Integer page,Integer size,String id,String name,String siteStart,String siteEnd) {

        String param = "page=" + page + "&size=" + size;
        if(name != null){
            param +=  "&name=" + name;
        }
        if(id != null){
            param += "&id=" + id;
        }
        if(siteStart != null){
            param += "&siteStart=" + siteStart;
        }
        if(siteEnd != null){
            param += "&siteEnd=" + siteEnd;
        }

        String urlRequest = URI + "/filter?" + param;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response distancePaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find Category by catType from DBM: {}", distancePaginationDTO);
        return distancePaginationDTO;
    }
}
