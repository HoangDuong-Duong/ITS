/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service.impl;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.Response;
import com.elcom.its.management.service.TrafficFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
public class TrafficFlowServiceImpl implements TrafficFlowService{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TrafficFlowService.class);
    
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getDetailTrafficBySite(String startTime, String endTime, String siteId, String directionCode) {
         String urlParam = "startTime=" + startTime + "&endTime=" + endTime + "&siteId=" + siteId+"&directionCode="+directionCode;
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/traffic-flow/site?" + urlParam;
        try {
            Response response = restTemplate.getForObject(urlRequest, Response.class);
            return response;
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return null;
        }
    }
    
}
