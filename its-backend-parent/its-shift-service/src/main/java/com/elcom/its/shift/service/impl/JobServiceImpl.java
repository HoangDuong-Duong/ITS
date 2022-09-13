/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service.impl;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.shift.dto.AggJobByStatus;
import com.elcom.its.shift.dto.UserDTO;
import com.elcom.its.shift.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.shift.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.shift.service.JobService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Override
    public List<AggJobByStatus> getAggJobByStatus(String groupId, Date startDate, Date endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String urlParam = "startTime=" + dateFormat.format(startDate) + "&endTime=" + dateFormat.format(endDate) + "&groupId=" + groupId;
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(RabbitMQProperties.MANAGEMENT_JOB_REPORT_BY_STATUS_URL);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(urlParam);
        userRpcRequest.setPathParam(null);
        userRpcRequest.setHeaderParam(null);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.MANAGEMENT_RPC_EXCHANGE,
                RabbitMQProperties.MANAGEMENT_RPC_QUEUE, RabbitMQProperties.MANAGEMENT_RPC_KEY, userRpcRequest.toJsonString());
        ObjectMapper mapper = new ObjectMapper();
        try {
            ResponseMessage responseMessage = mapper.readValue(result, new TypeReference<ResponseMessage>() {
            });
            List<AggJobByStatus> listAggJobByStatus = mapper.convertValue(responseMessage.getData().getData(), new TypeReference<List<AggJobByStatus>>() {
            });
            return listAggJobByStatus;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
