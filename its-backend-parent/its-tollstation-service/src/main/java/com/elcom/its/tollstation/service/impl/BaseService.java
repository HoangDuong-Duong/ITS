/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.tollstation.service.impl;

import com.elcom.its.tollstation.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.tollstation.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.message.RequestMessage;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class BaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    private RabbitMQClient rabbitMQClient;

    public String callUser(String urlParam, Map<String, String> headerParam, Map<String, Object> bodyParam, String url, String method, String pathParam) {
        LOGGER.info("CALL USER SERVICE -> link : " + url);
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod(method);
        userRpcRequest.setRequestMethod(method);
        userRpcRequest.setRequestPath(url);
        userRpcRequest.setVersion(null);
        userRpcRequest.setPathParam(pathParam);
        userRpcRequest.setUrlParam(urlParam);
        userRpcRequest.setBodyParam(bodyParam);
        userRpcRequest.setHeaderParam(headerParam);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE, RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
        return result;
    }

    public String callAbac(String urlParam, Map<String, String> headerParam, Map<String, Object> bodyParam, String url, String method, String pathParam) {
        LOGGER.info("CALL ABAC SERVICE -> link : " + url);
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod(method);
        userRpcRequest.setRequestPath(url);
        userRpcRequest.setUrlParam(urlParam);
        userRpcRequest.setPathParam(pathParam);
        userRpcRequest.setBodyParam(bodyParam);
        userRpcRequest.setHeaderParam(headerParam);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.ABAC_RPC_EXCHANGE, RabbitMQProperties.ABAC_RPC_QUEUE, RabbitMQProperties.ABAC_RPC_KEY, userRpcRequest.toJsonString());
        return result;
    }

}
