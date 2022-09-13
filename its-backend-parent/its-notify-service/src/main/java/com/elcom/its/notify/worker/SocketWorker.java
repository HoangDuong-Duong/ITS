/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.notify.worker;

import com.elcom.its.notify.constant.Constant;
import com.elcom.its.notify.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.notify.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.notify.model.dto.Message;
import com.elcom.its.notify.model.dto.MobileNotifyRequestDTO;
import com.elcom.its.notify.scheduler.NotifyScheduler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class SocketWorker {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyScheduler.class);
    
    @Value("${socket_rpc.service.name}")
    private String socketRpcServiceName;
    
    @Autowired
    private RabbitMQClient rabbitMQClient;

    public void handleSendNoti(String userId) {
        try {
            MobileNotifyRequestDTO mobileNotifyRequestDTO = new MobileNotifyRequestDTO();
            mobileNotifyRequestDTO.setContent("Phát hiện phương tiện vi phạm giao thông");
            mobileNotifyRequestDTO.setTitle("Thông bá");
            mobileNotifyRequestDTO.setUserId(userId);
            mobileNotifyRequestDTO.setData("");

            Message message = new Message();
            message.setType(Constant.NOTIFY_MOBILE);
            message.setData(mobileNotifyRequestDTO.toJsonString());
            boolean result = rabbitMQClient.callWorkerService(RabbitMQProperties.WORKER_QUEUE_MESSAGE, message.toJsonString());
            LOGGER.info("Socket work queue - Push to {} msg: {} => {}", RabbitMQProperties.WORKER_QUEUE_MESSAGE, message.toJsonString(), result);
        } catch (Exception e) {
            LOGGER.error("Error push to onesignal", e);
        }
    }

    public boolean checkSocketOnline(String userId) throws JsonProcessingException {
        boolean isOnline = false;
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("serviceName", socketRpcServiceName);
        bodyParam.put("data", data);

        //Authorize user action with api -> call rbac service
        ObjectMapper mapper = new ObjectMapper();
        String meg = mapper.writeValueAsString(bodyParam);
        try {
            String result = rabbitMQClient.callRpcService(RabbitMQProperties.SOCKET_RPC_EXCHANGE,
                    RabbitMQProperties.SOCKET_RPC_QUEUE, RabbitMQProperties.SOCKET_RPC_KEY, meg);
            if (result != null) {
                List<String> res = mapper.readValue(result, List.class);
                if (!Objects.isNull(res) && !res.isEmpty()) {
                    isOnline = true;
                } else {
                    isOnline = false;
                }
            }
        } catch (Exception e) {
            LOGGER.error("err call socket rpc {}", e);
        }
        return isOnline;
    }
}
