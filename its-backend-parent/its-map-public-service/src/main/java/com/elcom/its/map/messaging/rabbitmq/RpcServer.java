/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.map.messaging.rabbitmq;

import com.elcom.its.map.controller.MapController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elcom.its.constant.ResourcePath;
import com.elcom.its.map.controller.LaneRouteController;
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
 *
 * @author Admin
 */
public class RpcServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);
    
    
    @Autowired
    private MapController mapController;
    
    @Autowired
    private LaneRouteController laneRouteController;
               
    
    @RabbitListener(queues = "${map.rpc.queue}")
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
                       if ("/map/system-device".equalsIgnoreCase(requestPath)) {
                            response = mapController.getAllSystemDevices(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam); 
                       } 
                       else if ("/map/stages".equalsIgnoreCase(requestPath)) {
                            response = mapController.getStageForMap(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam); 
                       } 
                       else if ("/map/event-in-stages".equalsIgnoreCase(requestPath)) {
                            response = mapController.getPageEventInStages(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam); 
                       }
                       else if ("/map/object-tracking-in-stages".equalsIgnoreCase(requestPath)) {
                            response = mapController.getObjectTrackingHistoryInStagesForMap(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam); 
                       }
                       else if ("/map/stage/traffic-flow".equalsIgnoreCase(requestPath)) {
                            response = mapController.getTrafficFlowInStageForMap(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam); 
                       }
                       else if ("/map/system-device-in-stages".equalsIgnoreCase(requestPath)) {
                            response = mapController.getSystemDevicesInStagesForMap(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam); 
                       }
                       else if ("/map/map-between-site".equalsIgnoreCase(requestPath)) {
                            response = mapController.getMapBetweenSite(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam); 
                       }
                       else if ("/map/stage/report/event".equalsIgnoreCase(requestPath)) {
                            response = mapController.getReportEventByTypeInStage(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam); 
                       }else if ("/map/systemconfig/lanes".equalsIgnoreCase(requestPath)) {
                            response = laneRouteController.getAllLane(headerParam, requestPath,  request.getRequestMethod(), urlParam);
                        }else if ("/map/systemconfig/lanes".equalsIgnoreCase(requestPath)) {
                            response = laneRouteController.getAllLane(headerParam, requestPath,  request.getRequestMethod(), urlParam);
                        }else if ("/map/stage/mid-point".equalsIgnoreCase(requestPath)) {
                            response = mapController.getMidPointInStages(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam); 
                       }
                        break;
                    case "POST":                         
                        break;
                    case "PUT":       
                        break;
                    case "PATCH":
                        break;
                    case "DELETE":                     
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
