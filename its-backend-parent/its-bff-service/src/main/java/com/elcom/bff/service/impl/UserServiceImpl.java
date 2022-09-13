package com.elcom.bff.service.impl;

import com.elcom.bff.dto.*;
import com.elcom.bff.controller.BaseController;
import com.elcom.bff.rabbitmq.RabbitMQProperties;
import com.elcom.bff.service.UserService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private BaseController baseController;

    @Override
    public Response getAllUser(String urlParam, Map<String, String> headerParam) {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        ResponseMessage response;
        String result = callUser(urlParam, headerParam, null, RabbitMQProperties.USER_ALL_URL, "GET", null);
        List<User> data = new ArrayList<>();
        Long total;
        try {
            response = mapper.readValue(result, ResponseMessage.class);
            if (response.getData() != null && response.getData().getStatus() == HttpStatus.OK.value()) {
                try {
                    JsonNode jsonNode = mapper.readTree(result);
                    data = mapper.readerFor(new TypeReference<List<User>>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    if(data!=null){
                        total = Long.parseLong(String.valueOf(jsonNode.get("data").get("total")));
                    }else{
                        total = Long.valueOf(0);
                    }

                    return new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), data, total);
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

    @Override
    public Response getUserById(String urlParam, Map<String, String> headerParam, String pathParam) {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        ResponseMessage response;
        String result = callUser(urlParam, headerParam, null, RabbitMQProperties.USER_DETAIL_URL, "GET", pathParam);
        User data = null;
        try {
            response = mapper.readValue(result, ResponseMessage.class);
            if (response.getData() != null && response.getData().getStatus() == HttpStatus.OK.value()) {
                try {
                    JsonNode jsonNode = mapper.readTree(result);
                    data = mapper.readerFor(new TypeReference<User>() {
                    }).readValue(jsonNode.get("data").get("data"));
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

    @Override
    public Response getUserBySiteId(String urlParam, Map<String, String> headerParam, String pathParam) {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        ResponseMessage response;
        String result = callUser(urlParam, headerParam, null, RabbitMQProperties.USER_DETAIL_URL+"/site", "GET", pathParam);
        List<User> data = null;
        try {
            response = mapper.readValue(result, ResponseMessage.class);
            if (response.getData() != null && response.getData().getStatus() == HttpStatus.OK.value()) {
                try {
                    JsonNode jsonNode = mapper.readTree(result);
                    data = mapper.readerFor(new TypeReference<List<User>>() {
                    }).readValue(jsonNode.get("data").get("data"));
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

    @Override
    public Response getAllRole(String urlParam, Map<String, String> headerParam) {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        ResponseMessage response;
        String result = callAbac(urlParam, headerParam, null, RabbitMQProperties.ABAC_ROLE_URL, "GET", null);
        Map<String, List<Role>> mapUuidRole = new HashMap<>();
        try {
            response = mapper.readValue(result, ResponseMessage.class);
            if (response.getData() != null && response.getData().getStatus() == HttpStatus.OK.value()) {
                try {
                    JsonNode jsonNode = mapper.readTree(result);
                    mapUuidRole = mapper.readerFor(new TypeReference<Map<String, List<Role>>>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), mapUuidRole);
                } catch (Exception ex) {
                    return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi ko lấy được dữ liệu từ service ABAC", null);
                }
            } else {
                return new Response(response.getData().getStatus(), response.getData().getMessage());
            }
        } catch (Exception ex) {
            return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi từ service Abac", null);
        }
    }

    @Override
    public Response saveUser(String urlParam, Map<String, String> headerParam, Map<String, Object> body) {
        String result = callUser(urlParam, headerParam, body, RabbitMQProperties.USER_ADD_URL, "POST", null);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        ResponseMessage response;
        User data = null;
        try {
            response = mapper.readValue(result, ResponseMessage.class);
            if (response.getData() != null && response.getData().getStatus() == HttpStatus.OK.value()) {
                try {
                    JsonNode jsonNode = mapper.readTree(result);
                    data = mapper.readerFor(new TypeReference<User>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), data);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi trong quá trình paser dữ liệu từ Id", null);
                }
            } else {
                return new Response(response.getData().getStatus(), response.getData().getMessage(), null);
            }
        } catch (Exception ex) {
            return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi từ service id", null);
        }
    }

    @Override
    public Response updateUser(String urlParam, Map<String, String> headerParam, Map<String, Object> body, String pathParam) {
        String result = callUser(urlParam, headerParam, body, RabbitMQProperties.USER_ADD_URL, "PUT", pathParam);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        ResponseMessage response;
        User data = null;
        try {
            response = mapper.readValue(result, ResponseMessage.class);
            if (response.getData() != null && response.getData().getStatus() == HttpStatus.OK.value()) {
                try {
                    JsonNode jsonNode = mapper.readTree(result);
                    data = mapper.readerFor(new TypeReference<User>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), data);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi trong quá trình paser dữ liệu từ Id", null);
                }
            } else {
                return new Response(response.getData().getStatus(), response.getData().getMessage(), null);
            }
        } catch (Exception ex) {
            return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi từ service id", null);
        }
    }

    @Override
    public Response deleteUser(String urlParam, Map<String, String> headerParam, Map<String, Object> body, String pathParam) {
        String result = callUser(urlParam, headerParam, body, RabbitMQProperties.USER_ADD_URL, "DELETE", pathParam);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        ResponseMessage response;
        User data = null;
        try {
            response = mapper.readValue(result, ResponseMessage.class);
            if (response.getData() != null && response.getData().getStatus() == HttpStatus.OK.value()) {
                try {
                    JsonNode jsonNode = mapper.readTree(result);
                    data = mapper.readerFor(new TypeReference<User>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), data);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi trong quá trình paser dữ liệu từ Id", null);
                }
            } else {
                return new Response(response.getData().getStatus(), response.getData().getMessage(), null);
            }
        } catch (Exception ex) {
            return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi từ service id", null);
        }
    }

    @Override
    public Response deleteRole(String urlParam, Map<String, String> headerParam, Map<String, Object> body, String pathParam) {
        String result = callAbac(urlParam, headerParam, body, RabbitMQProperties.ABAC_DELETE_ROLE_URL, "DELETE", pathParam);
        ResponseMessage response;
        ObjectMapper mapper = new ObjectMapper();
        try {
            response = mapper.readValue(result, ResponseMessage.class);
            if (response.getData() != null && response.getData().getStatus() == HttpStatus.OK.value()) {
                return new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), null);
            } else {
                return new Response(response.getData().getStatus(), response.getData().getMessage(), null);
            }
        } catch (Exception ex) {
            return new Response(HttpStatus.FORBIDDEN.value(), ex.toString(), null);
        }
    }
}
