/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.tollstation.messaging.rabbitmq;

import com.elcom.its.tollstation.controller.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;

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
    private ReportTollStationController reportTollStationController;
    @Autowired
    private TollStationController tollStationController;




    @RabbitListener(queues = "${tollstation.rpc.queue}")
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
                        if ("/tollstation".equalsIgnoreCase(requestPath)) {
                            response = tollStationController.getListBOT(headerParam,request.getRequestPath(),urlParam);
                        } else if ("/tollstation/lanes".equalsIgnoreCase(requestPath)) {
                            response = reportTollStationController.reportLaneStatus(headerParam,request.getRequestPath(),urlParam);
                        } else if ("/tollstation/traffic".equalsIgnoreCase(requestPath)) {
                            response = reportTollStationController.reportTraffic(headerParam,request.getRequestPath(),urlParam);
                        } else if ("/tollstation/traffic/report-line".equalsIgnoreCase(requestPath)) {
                            response = reportTollStationController.reportTrafficLine(headerParam,request.getRequestPath(),urlParam);
                        } else if ("/tollstation/traffic/report-lanes".equalsIgnoreCase(requestPath)) {
                            response = reportTollStationController.reportTrafficLane(headerParam,request.getRequestPath(),urlParam);
                        } else if ("/tollstation/lanes/history".equalsIgnoreCase(requestPath)) {
                            response = reportTollStationController.reportLaneHistoryClose(headerParam,request.getRequestPath(),urlParam);
                        } else if ("/tollstation/direction".equalsIgnoreCase(requestPath)) {
                            response = tollStationController.getListBOTByDirectionCode(headerParam,request.getRequestPath(),urlParam);
                        }

                        break;
                    case "POST":
                        if ("/tollstation".equalsIgnoreCase(requestPath)) {
                            response = tollStationController.createBOT(headerParam, bodyParam, request.getRequestPath());
                        }
                        break;
                    case "PUT":
                        if ("/tollstation".equalsIgnoreCase(requestPath)) {
                            response = tollStationController.updateBOT(headerParam, bodyParam, request.getRequestPath(),pathParam);
                        }
                        break;
                    case "PATCH":
                        break;
                    case "DELETE":
                        if ("/tollstation".equalsIgnoreCase(requestPath)) {
                            response = tollStationController.deleteBOT(request.getRequestPath(), headerParam, bodyParam, pathParam);
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
