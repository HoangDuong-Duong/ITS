package com.elcom.its.management.service.impl;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.Response;
import com.elcom.its.management.service.ObjectTrackingHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ObjectTrackingHistoryServiceImpl implements ObjectTrackingHistoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectTrackingHistoryServiceImpl.class);

    private final String URI = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/object-tracking-history";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getObjectTrackingHistoryList(String stages, Boolean isAdmin, Long hourInterval) {
        String urlRequest = URI + "/recently?hourInterval=" + hourInterval;
        urlRequest += "&stageIdList="+ stages + "&isAdmin=" +isAdmin;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }
}
