/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service.impl;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.shift.dto.Unit;
import com.elcom.its.shift.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.shift.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.shift.service.UnitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class UnitServiceImpl implements UnitService {

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Override
    public Unit findById(String id) throws JsonProcessingException {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_UNIT_DETAIL_URL);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setPathParam(id);
        userRpcRequest.setHeaderParam(null);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
        ObjectMapper mapper = new ObjectMapper();
        ResponseMessage responseMessage = mapper.readValue(result, new TypeReference<ResponseMessage>() {
        });
        Unit unit = mapper.convertValue(responseMessage.getData().getData(), new TypeReference<Unit>() {
        });
        return unit;
    }

}
