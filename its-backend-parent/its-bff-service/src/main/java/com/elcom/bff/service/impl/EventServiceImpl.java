package com.elcom.bff.service.impl;

import com.elcom.bff.dto.AuthorizationResponseDTO;
import com.elcom.bff.dto.EventDTO;
import com.elcom.bff.dto.Response;
import com.elcom.bff.dto.User;
import com.elcom.bff.rabbitmq.RabbitMQClient;
import com.elcom.bff.rabbitmq.RabbitMQProperties;
import com.elcom.bff.service.EventService;
import com.elcom.bff.service.UserService;
import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl extends BaseService implements EventService {

    @Autowired
    private RabbitMQClient rabbitMQClient;


    @Override
    public Response getHistory(String urlParam, Map<String, String> headerParam, String pathParam) {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        ResponseMessage response;
        String result = callManagement(urlParam, headerParam, null, RabbitMQProperties.MANAGEMENT_HISTORY_URL, "GET", pathParam);
        List<EventDTO> data = null;
        try {
            response = mapper.readValue(result, ResponseMessage.class);
            if (response.getData() != null && response.getData().getStatus() == HttpStatus.OK.value()) {
                try {
                    JsonNode jsonNode = mapper.readTree(result);
                    data = mapper.readerFor(new TypeReference<List<EventDTO>>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    data.stream().map(item -> {
                        AuthorizationResponseDTO userDTO = getUsernameByUserId(item.getModifiedBy());
                        if (userDTO != null) {
                            item.setUserName(userDTO.getFullName());
                        }
                        return item;
                    }).collect(Collectors.toList());
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), data, (long) data.size()));
                    return new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), data);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi trong quá trình paser dữ liệu từ Id", null);
                }
            } else {
                return new Response(response.getData().getStatus(), response.getData().getMessage(), null);
            }
        } catch (Exception ex) {
            return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi từ service Id", null);
        }
    }

    AuthorizationResponseDTO getUsernameByUserId(String id) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_UUIDLIST_URL);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setPathParam(id);
        userRpcRequest.setHeaderParam(null);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
            } catch (JsonProcessingException ex) {
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
                    return null;
                }
            } else {
                //Forbidden
                return null;
            }
        }
        return null;
    }
}
