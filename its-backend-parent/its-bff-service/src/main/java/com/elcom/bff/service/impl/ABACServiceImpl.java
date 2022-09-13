/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.bff.service.impl;

import com.elcom.bff.dto.Response;
import com.elcom.bff.dto.RoleUser;
import com.elcom.bff.rabbitmq.RabbitMQProperties;
import com.elcom.bff.service.ABACService;
import com.elcom.its.message.ResponseMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Admin
 */
@Service
@Transactional
public class ABACServiceImpl extends BaseService implements ABACService {

    @Override
    public Response saveRole(String urlParam, Map<String, String> headerParam, Map<String, Object> body) {
        String result = callAbac(urlParam, headerParam, body, RabbitMQProperties.ABAC_ADD_ROLE_URL, "POST", null);
        RoleUser roleUser = null;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        ResponseMessage response;
        try {
            response = mapper.readValue(result, ResponseMessage.class);
            if (response.getData() != null && response.getData().getStatus() == HttpStatus.OK.value()) {
                try {
                    JsonNode jsonNode = mapper.readTree(result);
                    roleUser = mapper.readerFor(new TypeReference<RoleUser>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), roleUser);
                } catch (Exception ex) {
                    return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi ko lấy được dữ liệu từ service ABAC", null);
                }
            } else {
                return new Response(response.getData().getStatus(), response.getData().getMessage(), null);
            }
        } catch (Exception ex) {
            return new Response(HttpStatus.FORBIDDEN.value(), ex.toString(), null);
        }
    }

    @Override
    public Response deleteRole(String urlParam, Map<String, String> headerParam, String pathParam) {
        String result = callAbac(urlParam, headerParam, null, RabbitMQProperties.ABAC_DELETE_ROLE_URL, "DELETE", pathParam);
        RoleUser roleUser = null;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        ResponseMessage response;
        try {
            response = mapper.readValue(result, ResponseMessage.class);
            if (response.getData() != null && response.getData().getStatus() == HttpStatus.OK.value()) {
                try {
                    JsonNode jsonNode = mapper.readTree(result);
                    roleUser = mapper.readerFor(new TypeReference<RoleUser>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), roleUser);
                } catch (Exception ex) {
                    return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi ko lấy được dữ liệu từ service ABAC", null);
                }
            } else {
                return new Response(response.getData().getStatus(), response.getData().getMessage(), null);
            }
        } catch (Exception ex) {
            return new Response(HttpStatus.FORBIDDEN.value(), ex.toString(), null);
        }
    }

    @Override
    public Response updateRoleUser(String urlParam,Map<String, String> headerParam, Map<String, Object> bodyParam) {
        String result = callAbac(urlParam, headerParam, bodyParam, RabbitMQProperties.ABAC_ADD_ROLE_URL, "PUT", null);
        RoleUser roleUser = null;
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        ResponseMessage response;
        try {
            response = mapper.readValue(result, ResponseMessage.class);
            if (response.getData() != null && response.getData().getStatus() == HttpStatus.OK.value()) {
                try {
                    JsonNode jsonNode = mapper.readTree(result);
                    roleUser = mapper.readerFor(new TypeReference<RoleUser>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), roleUser);
                } catch (Exception ex) {
                    return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi ko lấy được dữ liệu từ service ABAC", null);
                }
            } else {
                return new Response(response.getData().getStatus(), response.getData().getMessage(), null);
            }
        } catch (Exception ex) {
            return new Response(HttpStatus.FORBIDDEN.value(), ex.toString(), null);
        }
    }
}
