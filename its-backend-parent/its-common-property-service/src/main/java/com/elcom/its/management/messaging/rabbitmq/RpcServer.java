/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.messaging.rabbitmq;

import com.elcom.its.management.controller.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * @author Admin
 */
public class RpcServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    @Autowired
    private PropertyController propertyController;


    @RabbitListener(queues = "${property.rpc.queue}")
    public String processService(String json) {
        long start = System.currentTimeMillis();
        try {
            LOGGER.info(" [-->] Server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            RequestMessage request = mapper.readValue(json, RequestMessage.class);
            ResponseMessage response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            if (request != null) {
                String requestPath = request.getRequestPath().replace(request.getVersion() != null
                        ? request.getVersion() : ResourcePath.VERSION, "");
                String urlParam = request.getUrlParam();
                String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();
                switch (request.getRequestMethod()) {
                    case "GET":
                         if("/property/history".equalsIgnoreCase(requestPath)){
                            response = propertyController.findSearchHistory(request.getRequestPath(),urlParam,headerParam);
                        } else if("/property/export".equalsIgnoreCase(requestPath)){
                            response = propertyController.export(request.getRequestPath(),urlParam,headerParam);
                        }

                        break;
                    case "POST":
                        if ("/property".equalsIgnoreCase(requestPath)) {
                            response = propertyController.save(request.getRequestPath(),headerParam,bodyParam);
                        } else if("/property/history".equalsIgnoreCase(requestPath)){
                            response = propertyController.saveHistory(request.getRequestPath(),headerParam,bodyParam);
                        } else if ("/property/search".equalsIgnoreCase(requestPath)) {
                            response = propertyController.findSearch(request.getRequestPath(),bodyParam,headerParam);
                        }
                        else if ("/property/group".equalsIgnoreCase(requestPath)) {
                            response = propertyController.findFmsGroup(request.getRequestPath(),bodyParam,headerParam);
                        }
                        break;
                    case "PUT":
                        if ("/property".equalsIgnoreCase(requestPath)) {
                            response = propertyController.update(request.getRequestPath(),headerParam,bodyParam);
                        } else if ("/property/history".equalsIgnoreCase(requestPath)) {
                            response = propertyController.updateHistory(request.getRequestPath(),headerParam,bodyParam);
                        }

                        break;
                    case "PATCH":
                        break;
                    case "DELETE":
                        if ("/property/multi".equalsIgnoreCase(requestPath)) {
                            response = propertyController.deleteMulti(headerParam, bodyParam, request.getRequestPath());
                        }  else if ("/property/history".equalsIgnoreCase(requestPath)) {
                            response = propertyController.deleteMultiHistory(headerParam,bodyParam,request.getRequestPath());
                        }
                        break;
                    default:
                        break;
                }
            }
            LOGGER.info(" [<--] Server returned " + response.toJsonString());
            long end = System.currentTimeMillis();
            LOGGER.info("[RpcServer] ================> Time to process data : " + (end - start) + " miliseconds");
            return response.toJsonString();
        } catch (Exception ex) {
            LOGGER.error("Error to process request >>> " + ex.toString());
            ex.printStackTrace();
        }
        return null;
    }
}
