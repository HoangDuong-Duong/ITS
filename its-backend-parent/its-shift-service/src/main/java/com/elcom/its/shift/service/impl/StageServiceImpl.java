/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service.impl;

import com.elcom.its.shift.config.ApplicationConfig;
import com.elcom.its.shift.dto.Response;
import com.elcom.its.shift.dto.Stage;
import org.springframework.stereotype.Service;
import com.elcom.its.shift.service.StageService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
public class StageServiceImpl implements StageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StageServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getStageCodeBySite(String siteId) {
        try {
            String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/stage/site/" + siteId;
            Response response = restTemplate.getForObject(urlRequest, Response.class);
            return (String) response.getData();
        } catch (Exception e) {
            LOGGER.info("ERROR {}", e.toString());
            return null;
        }

    }

    @Override
    public List<Stage> findByStageCodes(String listOfStageIds) {
        try {
            String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/stage/list?stageCodes=" + listOfStageIds;
            Response response = restTemplate.getForObject(urlRequest, Response.class);
            ObjectMapper mapper = new ObjectMapper();

            List<Stage> listStages = mapper.convertValue(response.getData(), new TypeReference<List<Stage>>() {
            });
            return listStages;
        } catch (Exception e) {
            LOGGER.info("ERROR {}", e.toString());
            return null;
        }
    }

    @Override
    public List<String> getListStageCodeByListSite(String[] listSiteIds) {
        try {
            String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/stage/site?";
            boolean isFirst = true;
            for (String siteId : listSiteIds) {
                if (!isFirst) {
                    urlRequest += "&";
                }
                urlRequest += "siteIdList=" + siteId;
                isFirst = false;
            }
            Response response = restTemplate.getForObject(urlRequest, Response.class);
            ObjectMapper mapper = new ObjectMapper();

            List<String> listStages = mapper.convertValue(response.getData(), new TypeReference<List<String>>() {
            });
            return listStages;
        } catch (Exception e) {
            LOGGER.info("ERROR {}", e.toString());
            return null;
        }
    }

}
