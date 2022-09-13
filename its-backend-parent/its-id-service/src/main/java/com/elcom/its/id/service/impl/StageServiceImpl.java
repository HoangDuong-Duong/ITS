/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.id.service.impl;

import com.elcom.its.id.config.ApplicationConfig;
import com.elcom.its.id.model.dto.Response;
import com.elcom.its.id.model.dto.StageIdListResponse;
import com.elcom.its.id.service.StageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
public class StageServiceImpl implements StageService {

    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/stage/site";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<String> findIdListBySiteIdList(String siteIdList) {
        String urlRequest = URI + "?siteIdList=" + siteIdList;
        StageIdListResponse response = restTemplate.getForObject(urlRequest, StageIdListResponse.class);
        if (response != null && response.getStatus() == HttpStatus.OK.value()) {
            return response.getData();
        }
        return null;
    }

}
