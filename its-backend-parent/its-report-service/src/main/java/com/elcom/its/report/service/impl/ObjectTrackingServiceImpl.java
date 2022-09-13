/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.report.service.impl;

import com.elcom.its.report.config.ApplicationConfig;
import com.elcom.its.report.model.dto.ObjectTrackingPaginationDTOMesage;
import com.elcom.its.report.service.ObjectTrackingService;
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
public class ObjectTrackingServiceImpl implements ObjectTrackingService {

    private Logger LOGGER = LoggerFactory.getLogger(ObjectTrackingServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ObjectTrackingPaginationDTOMesage findProcessedObjectTracking(Integer page,
            Integer size, String processFromDate, String processToDate) {
        String params = null;
        try {
            params = "page=" + page + "&size=" + size
                    + "&processFromDate=" + (processFromDate != null ? processFromDate : "")
                    + "&processToDate=" + (processToDate != null ? processToDate : "");
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/object-tracking/processed?" + params;
        LOGGER.info("urlRequest: {}", urlRequest);
        ObjectTrackingPaginationDTOMesage objectTrackingPaginationDTOMesage = restTemplate.getForObject(urlRequest, ObjectTrackingPaginationDTOMesage.class);
        return objectTrackingPaginationDTOMesage;
    }

}
