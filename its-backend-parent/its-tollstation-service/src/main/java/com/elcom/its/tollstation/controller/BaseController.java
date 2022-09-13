/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.tollstation.controller;

import com.elcom.its.tollstation.dto.*;
import com.elcom.its.tollstation.enums.JobType;
import com.elcom.its.tollstation.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.tollstation.messaging.rabbitmq.RabbitMQProperties;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.tollstation.model.ActionHistory;
import com.elcom.its.tollstation.model.File;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 *
 * @author Admin
 */
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private RabbitMQClient rabbitMQClient;

    /**
     * Check token qua id service => Trả về detail user
     *
     * @param headerMap header chứa jwt token
     * @return detail user
     */
    public AuthorizationResponseDTO authenToken(Map<String, String> headerMap) {
        //Authen -> call rpc authen headerMap
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("POST");
        userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_AUTHEN_URL);
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
                            return dto;
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
        return null;
    }

    public ABACResponseDTO authorizeABAC(Map<String, Object> bodyParam, String requestMethod, String userUuid, String apiPath) throws ExecutionException, InterruptedException {
        Map<String, Object> bodyParamSend = new HashMap<>();
        if (bodyParam != null && !bodyParam.isEmpty()) {
            bodyParamSend.putAll(bodyParam);
        }
        bodyParamSend.put("uuid", userUuid);
        bodyParamSend.put("api", apiPath);
        bodyParamSend.put("method", requestMethod);
        RequestMessage abacRpcRequest = new RequestMessage();
        abacRpcRequest.setRequestMethod("POST");
        abacRpcRequest.setRequestPath(RabbitMQProperties.ABAC_RPC_AUTHOR_URL);
        abacRpcRequest.setBodyParam(bodyParamSend);
        abacRpcRequest.setUrlParam(null);
        abacRpcRequest.setHeaderParam(null);
        LOGGER.info("REQUEST" + abacRpcRequest.toJsonString());
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.ABAC_RPC_EXCHANGE,
                RabbitMQProperties.ABAC_RPC_QUEUE, RabbitMQProperties.ABAC_RPC_KEY,
                abacRpcRequest.toJsonString());
        LOGGER.info("RESULT" + result.toString());
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    JsonNode jsonNode = mapper.readTree(result);
                    ABACResponseDTO resultCheckDto = mapper.treeToValue(jsonNode.get("data").get("data"), ABACResponseDTO.class);
                    return resultCheckDto;
                }
                return null;
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    public void notifyFinishEvent(String eventId, String siteId, String userId) {
        NotifyFinishEventRequest notifyFinishEventRequest = new NotifyFinishEventRequest();
        notifyFinishEventRequest.setSiteId(siteId);
        notifyFinishEventRequest.setEventId(eventId);
        notifyFinishEventRequest.setUserId(userId);
        rabbitMQClient.callWorkerService(RabbitMQProperties.NOTIFY_FINISH_EVENT_WOKER_QUEUE, notifyFinishEventRequest.toJsonString());
        LOGGER.info("Call end event job : {}", notifyFinishEventRequest.toJsonString());
    }

    public AuthorizationResponseDTO getUrlParam(String urlParam) {
        AuthorizationResponseDTO dto;
        Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
        String dtoUuid = params.get("dto");
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        try {
            dto = mapper.readValue(dtoUuid, AuthorizationResponseDTO.class);
            return dto;
        } catch (Exception ex) {
            return null;
        }
    }

    public Map<String, Object> requestABACAttribute(String requestMethod, String userUuid, String apiPath) throws ExecutionException, InterruptedException {
        Map<String, Object> bodyParamSend = new HashMap<>();
        bodyParamSend.put("uuid", userUuid);
        bodyParamSend.put("api", ResourcePath.VERSION + apiPath);
        bodyParamSend.put("method", requestMethod);
        RequestMessage abacRpcRequest = new RequestMessage();
        abacRpcRequest.setRequestMethod("POST");
        abacRpcRequest.setRequestPath(RabbitMQProperties.ABAC_RPC_ATTRIBUTE_URL);
        abacRpcRequest.setBodyParam(bodyParamSend);
        abacRpcRequest.setUrlParam(null);
        abacRpcRequest.setHeaderParam(null);

        String result = rabbitMQClient.callRpcService(RabbitMQProperties.ABAC_RPC_EXCHANGE,
                RabbitMQProperties.ABAC_RPC_QUEUE, RabbitMQProperties.ABAC_RPC_KEY,
                abacRpcRequest.toJsonString());

        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    JsonNode jsonNode = mapper.readTree(result);
                    Map<String, Object> resultCheckDto = mapper.treeToValue(jsonNode.get("data").get("data"), HashMap.class);
                    return resultCheckDto;
                }
                return null;
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    AuthorizationResponseDTO getUsernameByUserId(Map<String, Object> bodyParam) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("POST");
        bodyParam.put("systemType", "1");
        userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_UUIDLIST_URL);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        userRpcRequest.setBodyParam(bodyParam);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setHeaderParam(null);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
        LOGGER.info("getUser - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
            } catch (JsonProcessingException ex) {
                LOGGER.info("Lỗi parse json khi gọi user service verify: " + ex.toString());
                return null;
            }
            if (response != null && response.getStatus() == HttpStatus.OK.value()) {
                try {
                    MessageContent content = response.getData();
                    Object data = content.getData();
                    if (data != null) {
                        AuthorizationResponseDTO userDTO = null;
                        if (data.getClass() == LinkedHashMap.class) {
                            userDTO = new AuthorizationResponseDTO((Map<String, Object>) data);
                        } else if (data.getClass() == AuthorizationResponseDTO.class) {
                            userDTO = (AuthorizationResponseDTO) data;
                        } else if (data.getClass() == ArrayList.class) {
                            JsonNode jsonNode = mapper.readTree(result);
                            List<AuthorizationResponseDTO> userReceiverDTOList = mapper.readerFor(new TypeReference<List<AuthorizationResponseDTO>>() {
                            }).readValue(jsonNode.get("data").get("data"));
                            userDTO = userReceiverDTOList.get(0);
                        }

                        if (userDTO != null && !StringUtil.isNullOrEmpty(userDTO.getUuid())) {
                            return userDTO;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    LOGGER.info("Lỗi giải mã AuthorizationResponseDTO khi gọi user service verify: " + ex.toString());
                    return null;
                }
            } else {
                //Forbidden
                return null;
            }
        }
        return null;
    }

    public Map<String, Object> createBodyCheckABAC(AuthorizationResponseDTO user, String stageId) {
        String[] stagesForUser = null;
        if (user.getUnit() != null && user.getUnit().getLisOfStage() != null) {
            stagesForUser = user.getUnit().getLisOfStage().split(",");
        }
        Map<String, Object> bodyCheckABAC = new HashMap<>();

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("stageId", stagesForUser);
        Map<String, Object> subject = new HashMap<>();
        subject.put("stageId",new String[]{stageId});
        bodyCheckABAC.put("attributes", attributes);
        bodyCheckABAC.put("subject", subject);

        return bodyCheckABAC;
    }
}
