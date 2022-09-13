/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service.impl;

import com.elcom.its.management.dto.Unit;
import com.elcom.its.management.dto.User;
import com.elcom.its.management.dto.UserDTO;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.management.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Override
    public List<User> findByListId(String uuid) {
        String urlParam = "userIds=" + uuid;
        String getListUserResponse = callUser(urlParam, null, null, RabbitMQProperties.USER_RPC_INTERNAL_LIST_URL, "GET", null);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        try {
            JsonNode jsonNode = mapper.readTree(getListUserResponse);
            List<User> data = mapper.readerFor(new TypeReference<List<User>>() {
            }).readValue(jsonNode.get("data").get("data"));
            return data;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public User findById(String uuid) {
        return null;
    }

    @Override
    public Unit findUnitById(String unitId) {
        String getListUserResponse = callUser(null, null, null, RabbitMQProperties.GROUP_GET_URL, "GET", unitId);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        try {
            JsonNode jsonNode = mapper.readTree(getListUserResponse);
            Unit data = mapper.readerFor(new TypeReference<Unit>() {
            }).readValue(jsonNode.get("data").get("data"));
            return data;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<UserDTO> transform(List<User> listUser) {
        List<UserDTO> listUserDTO = new ArrayList<>();
        listUser.stream().forEach(user -> {
            listUserDTO.add(UserDTO.builder()
                    .userId(user.getUuid())
                    .userName(user.getUserName())
                    .fullName(user.getFullName())
                    .build()
            );
        });
        return listUserDTO;
    }

}
