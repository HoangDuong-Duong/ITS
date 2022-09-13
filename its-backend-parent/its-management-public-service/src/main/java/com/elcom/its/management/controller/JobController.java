/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.controller;

import com.elcom.its.management.model.Job;
import com.elcom.its.management.service.JobService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
public class JobController extends BaseController {

    static final Logger LOGGER = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService jobService;

    public ResponseMessage findJobById(Map<String, String> headerParam, String requestPath, String pathParam) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        Job jobById = jobService.findById(pathParam);
        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobService.transform(jobById)));
        return response;
    }

    public ResponseMessage getListJobNotifyInMap(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMetho) throws ExecutionException, InterruptedException {
        ResponseMessage response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobService.getListJobNotificationInMap()));
        return response;
    }

}
