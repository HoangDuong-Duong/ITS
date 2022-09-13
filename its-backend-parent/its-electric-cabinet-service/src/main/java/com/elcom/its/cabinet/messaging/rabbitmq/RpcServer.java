/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.cabinet.messaging.rabbitmq;

import com.elcom.its.cabinet.controller.ElectricCabinetController;
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
 *
 * @author Admin
 */
public class RpcServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);
    
    
    @Autowired
    private ElectricCabinetController electricCabinetController;
    
   
    
    @RabbitListener(queues = "${electric.cabinet.rpc.queue}")
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
                       if ("/electric-cabinet".equalsIgnoreCase(requestPath)) {
                            response = electricCabinetController.getListElectricCabinet(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam); 
                       } 
                        break;
                    case "POST":  
                        if ("/electric-cabinet".equalsIgnoreCase(requestPath))
                            response = electricCabinetController.createElectricCabinet(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam,bodyParam);
                        else if ("/electric-cabinet/fan/turn-on".equalsIgnoreCase(requestPath))
                            response = electricCabinetController.turnOnFanCabinet(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam,bodyParam);
                        else if ("/electric-cabinet/fan/turn-off".equalsIgnoreCase(requestPath))
                            response = electricCabinetController.turnOffFanCabinet(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam,bodyParam);
                        else if ("/electric-cabinet/fire-alarm/turn-on".equalsIgnoreCase(requestPath))
                            response = electricCabinetController.turnOnFireAlarmCabinet(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam,bodyParam);
                        else if ("/electric-cabinet/fire-alarm/turn-off".equalsIgnoreCase(requestPath))
                            response = electricCabinetController.turnOffFireAlarm(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam,bodyParam);
                        else if ("/electric-cabinet/door/open".equalsIgnoreCase(requestPath))
                            response = electricCabinetController.openDoor(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam,bodyParam);
                        else if ("/electric-cabinet/door/close".equalsIgnoreCase(requestPath))
                            response = electricCabinetController.closeDoor(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam,bodyParam);
                        break;
                    case "PUT":    
                        if ("/electric-cabinet".equalsIgnoreCase(requestPath)) {
                            response = electricCabinetController.updateElectricCabinet(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam,bodyParam); 
                       }
                        break;
                    case "PATCH":
                        break;
                    case "DELETE":  
                        if ("/electric-cabinet".equalsIgnoreCase(requestPath)) {
                            response = electricCabinetController.deleteElectricCabinet(request.getRequestPath(), request.getRequestMethod(), urlParam, headerParam,bodyParam); 
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
