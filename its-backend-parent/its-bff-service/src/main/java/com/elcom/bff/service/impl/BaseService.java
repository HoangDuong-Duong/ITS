/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.bff.service.impl;

import com.elcom.bff.controller.BaseController;
import com.elcom.bff.rabbitmq.RabbitMQClient;
import com.elcom.bff.rabbitmq.RabbitMQProperties;
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
        LOGGER.info("result: {}", result);
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

    public String callManagement(String urlParam, Map<String, String> headerParam, Map<String, Object> bodyParam, String url, String method, String pathParam) {
        LOGGER.info("CALL MANA SERVICE -> link : " + url);
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod(method);
        userRpcRequest.setRequestPath(url);
        userRpcRequest.setUrlParam(urlParam);
        userRpcRequest.setPathParam(pathParam);
        userRpcRequest.setBodyParam(bodyParam);
        userRpcRequest.setHeaderParam(headerParam);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.MANAGEMENT_RPC_EXCHANGE, RabbitMQProperties.MANAGEMENT_RPC_QUEUE, RabbitMQProperties.MANAGEMENT_RPC_KEY, userRpcRequest.toJsonString());
        return result;
    }

    public String callShift(String urlParam, Map<String, String> headerParam, Map<String, Object> bodyParam, String url, String method, String pathParam) {
        LOGGER.info("CALL SHIFT SERVICE -> link : " + url);
        RequestMessage shiftRpcRequest = new RequestMessage();
        shiftRpcRequest.setRequestMethod(method);
        shiftRpcRequest.setRequestPath(url);
        shiftRpcRequest.setUrlParam(urlParam);
        shiftRpcRequest.setPathParam(pathParam);
        shiftRpcRequest.setBodyParam(bodyParam);
        shiftRpcRequest.setHeaderParam(headerParam);
        LOGGER.info("request shift: {}", shiftRpcRequest.toJsonString());
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.SHIFT_RPC_EXCHANGE, RabbitMQProperties.SHIFT_RPC_QUEUE, RabbitMQProperties.SHIFT_RPC_KEY, shiftRpcRequest.toJsonString());
        LOGGER.info("result: {}", result);
        return result;
    }
    
    public String callReport(String urlParam, Map<String, String> headerParam, Map<String, Object> bodyParam, 
            String url, String method, String pathParam) {
        LOGGER.info("CALL REPORT SERVICE -> link : " + url);
        RequestMessage reportRequest = new RequestMessage();
        reportRequest.setRequestMethod(method);
        reportRequest.setRequestPath(url);
        reportRequest.setUrlParam(urlParam);
        reportRequest.setPathParam(pathParam);
        reportRequest.setBodyParam(bodyParam);
        reportRequest.setHeaderParam(headerParam);
        LOGGER.info("request report: {}", reportRequest.toJsonString());
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.REPORT_RPC_EXCHANGE, 
                RabbitMQProperties.REPORT_RPC_QUEUE, RabbitMQProperties.REPORT_RPC_KEY, reportRequest.toJsonString());
        LOGGER.info("result: {}", result);
        return result;
    }

}
