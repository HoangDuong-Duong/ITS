package com.elcom.its.config.service.impl;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.LaneRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LaneRouteServiceImpl implements LaneRouteService {

    private Logger logger = LoggerFactory.getLogger(LaneRouteServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    private static final String requestURL = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/lanes-route";

    @Override
    public LaneRouteListMessage getAllLaneRoutes() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);

        logger.info("urlRequest: {}", requestURL);
        ResponseEntity<LaneRouteListMessage> result = restTemplate.exchange(requestURL, HttpMethod.GET, requestBody, LaneRouteListMessage.class);
        logger.info("Lane routes: " + result);

        return result.getBody();
    }

    @Override
    public Response getAllLane(String urlParam) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(requestURL + "?" + urlParam, HttpMethod.GET, requestBody, Response.class);
        return result.getBody();
    }

}
