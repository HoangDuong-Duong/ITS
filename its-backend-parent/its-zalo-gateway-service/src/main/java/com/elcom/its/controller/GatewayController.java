/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.controller;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.service.BucketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping(ResourcePath.VERSION)
public class GatewayController extends BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(GatewayController.class);
    @Autowired
    private BucketService bucketService;

    //GET
    @RequestMapping(value = "**", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMethod(@RequestParam Map<String, String> reqParam, HttpServletRequest request,
            @RequestHeader Map<String, String> headers, HttpServletRequest req) throws JsonProcessingException {
        if (bucketService.getBucket(request).tryConsume(1)) {
            return processRequest("GET", reqParam, null, headers, req);
        } else {
            LOGGER.info("Too many request by ip {}",bucketService.getIpAddress(request));
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

    }

    //POST
    @RequestMapping(value = "**", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postMethod(@RequestParam Map<String, String> reqParam, HttpServletRequest request,
            @RequestBody(required = false) Map<String, Object> requestBody, @RequestHeader Map<String, String> headers,
            HttpServletRequest req) throws JsonProcessingException {
        if (bucketService.getBucket(request).tryConsume(1)) {
            return processRequest("POST", reqParam, requestBody, headers, req);
        } else {
            LOGGER.info("Too many request by ip {}",bucketService.getIpAddress(request));
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

    }

    //PUT
    @RequestMapping(value = "**", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> putMethod(@RequestParam Map<String, String> reqParam, HttpServletRequest request,
            @RequestBody(required = false) Map<String, Object> requestBody, @RequestHeader Map<String, String> headers,
            HttpServletRequest req) throws JsonProcessingException {
        if (bucketService.getBucket(request).tryConsume(1)) {
            return processRequest("PUT", reqParam, requestBody, headers, req);
        } else {
            LOGGER.info("Too many request by ip {}",bucketService.getIpAddress(request));
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }

    }

    //PATCH
    @RequestMapping(value = "**", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> patchMethod(@RequestParam Map<String, String> reqParam, HttpServletRequest request,
            @RequestBody(required = false) Map<String, Object> requestBody, @RequestHeader Map<String, String> headers,
            HttpServletRequest req) throws JsonProcessingException {
        if (bucketService.getBucket(request).tryConsume(1)) {
            return processRequest("PATCH", reqParam, requestBody, headers, req);
        } else {
            LOGGER.info("Too many request by ip {}",bucketService.getIpAddress(request));
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
    }

    //DELETE
    @RequestMapping(value = "**", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteMethod(@RequestParam Map<String, String> reqParam, HttpServletRequest request,
            @RequestBody(required = false) Map<String, Object> requestBody, @RequestHeader Map<String, String> headers,
            HttpServletRequest req) throws JsonProcessingException {
        if (bucketService.getBucket(request).tryConsume(1)) {
            return processRequest("DELETE", reqParam, requestBody, headers, req);
        } else {
            LOGGER.info("Too many request by ip {}",bucketService.getIpAddress(request));
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        }
        
    }
}
