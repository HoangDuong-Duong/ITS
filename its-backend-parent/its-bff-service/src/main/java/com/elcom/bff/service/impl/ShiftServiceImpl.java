/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.bff.service.impl;

import com.elcom.bff.dto.Response;
import com.elcom.bff.dto.User;
import com.elcom.bff.rabbitmq.RabbitMQProperties;
import com.elcom.bff.service.ShiftService;
import com.elcom.its.message.ResponseMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class ShiftServiceImpl extends BaseService implements ShiftService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserUnitServiceImpl.class);

    @Override
    public Response findUserInShift(String urlParam, Map<String, String> headerParam) {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        ResponseMessage response;
        String result = callShift(urlParam, headerParam, null, RabbitMQProperties.SHIFT_USER_URL, "GET", null);
        List<User> data = new ArrayList<>();
        try {
            response = mapper.readValue(result, ResponseMessage.class);
            if (response.getData() != null && response.getData().getStatus() == HttpStatus.OK.value()) {
                try {
                    JsonNode jsonNode = mapper.readTree(result);
                    data = mapper.readerFor(new TypeReference<List<String>>() {}).readValue(jsonNode.get("data").get("data"));
                    return new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), data);
                } catch (Exception ex) {
                    LOGGER.error(ExceptionUtils.getStackTrace(ex));
                    ex.printStackTrace();
                    return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi trong quá trình paser dữ liệu từ Shift service", null);
                }
            } else {
                return new Response(response.getData().getStatus(), response.getData().getMessage(), null);
            }
        } catch (Exception ex) {
            LOGGER.error(ExceptionUtils.getStackTrace(ex));
            return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi từ Shift service", null);
        }
    }
    
}
