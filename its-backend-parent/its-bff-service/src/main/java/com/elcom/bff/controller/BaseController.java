package com.elcom.bff.controller;

import com.elcom.bff.dto.AuthorizationResponseDTO;
import com.elcom.bff.rabbitmq.RabbitMQClient;
import com.elcom.bff.rabbitmq.RabbitMQProperties;
import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.*;


@Controller
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private RabbitMQClient rabbitMQClient;


    public ResponseMessage authenToken(Map<String, String> headerMap) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("POST");
        userRpcRequest.setRequestPath(RabbitMQProperties.AUTHENTICATION_URL);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setHeaderParam(headerMap);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
        LOGGER.info("authenToken - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
            } catch (JsonProcessingException ex) {
                LOGGER.info("Lỗi parse json khi gọi user service verify: " + ex.toString());
                return null;
            }
            if (response != null && response.getStatus() == HttpStatus.OK.value()) {
                try {
                    //Process
                    MessageContent content = response.getData();
                    Object data = content.getData();
                    if (data != null) {
                        AuthorizationResponseDTO dto = null;
                        if (data.getClass() == LinkedHashMap.class) {
                            dto = mapper.convertValue(data, new TypeReference<AuthorizationResponseDTO>() {
                            });
                        } else if (data.getClass() == AuthorizationResponseDTO.class) {
                            dto = mapper.convertValue(data, new TypeReference<AuthorizationResponseDTO>() {
                            });
                        }
                        if (dto != null && !StringUtil.isNullOrEmpty(dto.getUuid())) {
                            return new ResponseMessage(new MessageContent(dto));
                        }
                    }
                } catch (Exception ex) {
                    LOGGER.info("Lỗi giải mã AuthorizationResponseDTO khi gọi user service verify: " + ex.toString());
                    return null;
                }
            } else {
                //Forbidden
                return null;
            }
        } else {
            //Forbidden
            return null;
        }
//        if (result != null) {
//            ObjectMapper mapper = new ObjectMapper();
//            ResponseMessage response = null;
//            try {
//                response = mapper.readValue(result, ResponseMessage.class);
//            } catch (JsonProcessingException ex) {
//                LOGGER.info("Lỗi parse json khi gọi user service verify: " + ex.toString());
//                return null;
//            }
//
//            if (response != null && response.getStatus() == HttpStatus.OK.value()) {
//                try {
//                    //Process
//                    MessageContent content = response.getData();
//                    Object data = content.getData();
//                    if (data != null) {
//                        AuthorizationResponseDTO dto = null;
//                        if (data.getClass() == LinkedHashMap.class) {
//                            dto = mapper.convertValue(data, new TypeReference<AuthorizationResponseDTO>() {
//                            });
//                        } else if (data.getClass() == AuthorizationResponseDTO.class) {
//                            dto = mapper.convertValue(data, new TypeReference<AuthorizationResponseDTO>() {
//                            });
//                        }
//                        if (dto != null && !StringUtil.isNullOrEmpty(dto.getUuid())) {
//                            return new ResponseMessage(new MessageContent(dto));
//                        }
//                    }
//                } catch (Exception ex) {
//                    LOGGER.info("Lỗi giải mã AuthorizationResponseDTO khi gọi user service verify: " + ex.toString());
//                    return null;
//                }
//            } else {
//                //Forbidden
//                return new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), null));
//            }
//        } else {
//            //Forbidden
//            return new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), null));
//        }
        return new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), null));
    }


}
