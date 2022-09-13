/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service.impl;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.shift.dto.AggJobByStatus;
import com.elcom.its.shift.dto.AggScheduledEvent;
import com.elcom.its.shift.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.shift.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.shift.service.ScheduledEventService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class ScheduledEventServiceImpl implements ScheduledEventService{
    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Override
    public AggScheduledEvent getAggScheduledEvent(String userId, Date shiftStartDate, Date shiftEndDate, Date nextShiftStartDate, Date nextShiftEndDate) {
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String urlParam = "shiftStartTime=" + dateFormat.format(shiftStartDate) + "&shiftEndTime=" + dateFormat.format(shiftEndDate) + "&userId=" + userId
                +"&nextShiftStartTime=" + dateFormat.format(nextShiftStartDate) + "&nextShiftEndTime=" + dateFormat.format(nextShiftEndDate);
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(RabbitMQProperties.MANAGEMENT_SCHEDULED_EVENT_REPORT_URL);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(urlParam);
        userRpcRequest.setPathParam(null);
        userRpcRequest.setHeaderParam(null);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.MANAGEMENT_RPC_EXCHANGE,
                RabbitMQProperties.MANAGEMENT_RPC_QUEUE, RabbitMQProperties.MANAGEMENT_RPC_KEY, userRpcRequest.toJsonString());
        ObjectMapper mapper = new ObjectMapper();
        try {
            ResponseMessage responseMessage = mapper.readValue(result, new TypeReference<ResponseMessage>() {
            });
            AggScheduledEvent aggScheduledEvent = mapper.convertValue(responseMessage.getData().getData(), new TypeReference<AggScheduledEvent>() {
            });
            return aggScheduledEvent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
