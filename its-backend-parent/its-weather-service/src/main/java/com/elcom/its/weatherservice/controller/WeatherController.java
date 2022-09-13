/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.weatherservice.controller;

import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/v1.0")
public class WeatherController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherController.class);
    @Autowired
    private RestTemplate restTemplate;

    @Value("${weather.request.url}")
    private String weatherRequestUrl;

    /**
     * Upload file
     *
     * @param reqParam
     * @return image upload link
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @RequestMapping(value = "/weather", method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getWeather(@RequestParam Map<String, String> reqParam) throws JsonProcessingException {
        String urlParam = StringUtil.generateMapString(reqParam);
        ResponseEntity<Object> response
                = restTemplate.getForEntity(weatherRequestUrl + "?" + urlParam, Object.class);
        return response;
    }

}
