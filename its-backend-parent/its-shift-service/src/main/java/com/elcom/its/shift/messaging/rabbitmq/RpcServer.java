/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.messaging.rabbitmq;

import com.elcom.its.shift.controller.DailyLoginController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.shift.controller.ShiftController;
import com.elcom.its.shift.controller.UserShiftController;
import com.elcom.its.shift.controller.ExportController;
import com.elcom.its.shift.controller.ShiftLoginController;
import com.elcom.its.shift.controller.ShiftReportController;
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
    private ShiftController shiftController;
    
    @Autowired
    private UserShiftController userShiftController;
    
    @Autowired
    private ExportController exportController ;

    @Autowired
    private DailyLoginController dailyLoginController ;
    
    @Autowired
    private ShiftLoginController shiftLoginController;
    
   @Autowired
   private ShiftReportController shiftReportController;
    
    @RabbitListener(queues = "${shift.rpc.queue}")
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
                       if ("/shift".equalsIgnoreCase(requestPath)) {
                           response = shiftController.getAllShift(request.getRequestPath(), request.getRequestMethod(), headerParam,urlParam);
                       }
                        if ("/shift/internal".equalsIgnoreCase(requestPath)) {
                            response = shiftController.getAllShiftInternal(request.getRequestPath(), request.getRequestMethod(), headerParam,urlParam);
                        }
                        else if ("/shift/user".equalsIgnoreCase(requestPath)) {
                           response = userShiftController.getListUserShift(request.getRequestPath(), request.getRequestMethod(), headerParam, urlParam);
                       }
                       else if ("/shift/user/list-user-on-shift".equalsIgnoreCase(requestPath)) {
                           response = userShiftController.getListUserOnShift(request.getRequestPath(), request.getRequestMethod(), headerParam, urlParam);
                       }
                       else if ("/shift/export/weekly".equalsIgnoreCase(requestPath)) {
                           response = exportController.exportWeeklyReport(request.getRequestPath(), request.getRequestMethod(), headerParam, urlParam);
                       }
                       else if ("/shift/export/monthly".equalsIgnoreCase(requestPath)) {
                           response = exportController.exportMonthlyReport(request.getRequestPath(), request.getRequestMethod(), headerParam, urlParam);
                       }

                       else if ("/shift/login".equalsIgnoreCase(requestPath)) {
                           response = shiftLoginController.processLogin(headerParam);
                       }
                       else if ("/shift/daily-report".equalsIgnoreCase(requestPath)) {
                           response = dailyLoginController.getAllShift(request.getRequestPath(), request.getRequestMethod(), headerParam, urlParam);

                       }
                        else if ("/shift/daily-report/internal".equalsIgnoreCase(requestPath)) {
                            response = dailyLoginController.getAllShiftInternal(request.getRequestPath(), request.getRequestMethod(), headerParam, urlParam);

                        }
                       else if ("/shift/notification".equalsIgnoreCase(requestPath)) {
                           response = shiftReportController.getShiftNotification(headerParam);

                       }
                        break;
                    case "POST":
                        if ("/shift".equalsIgnoreCase(requestPath)) {
                           response = shiftController.createShift(request.getRequestPath(), request.getRequestMethod(), headerParam, bodyParam);
                       }
                        else if ("/shift/user".equalsIgnoreCase(requestPath)) {
                           response = userShiftController.createUserShift(request.getRequestPath(), request.getRequestMethod(), headerParam, bodyParam);
                       }
                        break;
                    case "PUT":       
                        if ("/shift".equalsIgnoreCase(requestPath)) {
                           response = shiftController.updateShift(request.getRequestPath(), request.getRequestMethod(), headerParam, pathParam, bodyParam);
                       }
                        else if ("/shift/user".equalsIgnoreCase(requestPath)) {
                           response = userShiftController.getListUserOnShift(request.getRequestPath(), request.getRequestMethod(), headerParam, urlParam);
                       }
                        break;
                    case "PATCH":
                        break;
                    case "DELETE":  
                        if ("/shift".equalsIgnoreCase(requestPath)) {
                           response = shiftController.deleteShift(request.getRequestPath(), request.getRequestMethod(), headerParam, pathParam);
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
