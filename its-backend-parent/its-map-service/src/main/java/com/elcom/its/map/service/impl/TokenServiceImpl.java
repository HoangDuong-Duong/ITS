/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.map.service.impl;

import com.elcom.its.dto.ITSCoreLoginRequest;
import com.elcom.its.dto.ITSCoreLoginResponse;
import com.elcom.its.map.config.ApplicationConfig;
import com.elcom.its.map.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

    
    @Autowired
    private CacheManager cacheManager;
    
    @Override
    @Cacheable(value = "BearerToken")
    public String getAccessToken() {
        LOGGER.info("BearerToken missed => Get New Token");
        ITSCoreLoginRequest loginRequest = new ITSCoreLoginRequest();
        loginRequest.setUsername(ApplicationConfig.ITS_CORE_USERNAME);
        loginRequest.setPassword(ApplicationConfig.ITS_CORE_PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        // Dữ liệu đính kèm theo yêu cầu.
        HttpEntity<ITSCoreLoginRequest> requestBody = new HttpEntity<>(loginRequest, headers);
        // Gửi yêu cầu với phương thức POST.
        ITSCoreLoginResponse respsonse = restTemplate.postForObject(ApplicationConfig.ITS_CORE_AUTHEN_URL,
                requestBody, ITSCoreLoginResponse.class);
        if (respsonse != null) {
            return respsonse.getData().getTokenType() + " " + respsonse.getData().getAccessToken();
        } else {
            return null;
        }
    }

    @Override
    public boolean removeAccessToken() {
        cacheManager.getCache("BearerToken").clear();
        return true ;
    }
}
