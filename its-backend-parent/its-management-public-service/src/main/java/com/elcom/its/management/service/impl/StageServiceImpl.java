
package com.elcom.its.management.service.impl;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.EventInfoDTO;
import com.elcom.its.management.dto.Response;
import com.elcom.its.management.service.StageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StageServiceImpl implements StageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StageService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String findBySite(String siteId) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/stage/site";
        urlRequest = urlRequest + "/" + siteId;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return (String) dto.getData();
        }
        return null;
    }
    public String getStageContainSite(String siteId) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/stage/site/" + siteId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        try {
            HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.GET, requestBody, Response.class);
            return (String) response.getBody().getData();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }

    }

    @Override
    public String getStageHaveEvent(String eventId) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/stage?eventId=" + eventId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        try {
            HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.GET, requestBody, Response.class);
            return (String) response.getBody().getData();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @Override
    public String getStageByPositionM(Long positionM) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/stage/site";
        HttpEntity<String> requestBody = new HttpEntity(positionM, null);
        try {
            HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestBody, Response.class);
            return (String) response.getBody().getData();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

}
