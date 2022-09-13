/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.messaging.rabbitmq;

import com.elcom.its.config.model.dto.DeleteDataPublishMessage;
import com.elcom.its.config.service.ObjectTrackingService;
import com.elcom.its.message.RequestMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author admin
 */
public class SubscriberServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberServer.class);
    
    @Autowired
    private ObjectTrackingService objectTrackingService;
    
    @RabbitListener(queues = "#{directAutoDeleteSubscriberQueue.name}")
    public void subscriberReceive(String json) {
        try {
            LOGGER.info(" [-->] Server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            DeleteDataPublishMessage deleteDataMessage = mapper.readValue(json, DeleteDataPublishMessage.class);
            //Process here
            if (deleteDataMessage != null) {
                

                switch (deleteDataMessage.getDataType()) {
                    case "UNIT":
                        handleDeleteUnitMessage(deleteDataMessage);
                        break;
                    
                    default:
                        break;
                }
            }
        } catch (JsonProcessingException ex) {
            LOGGER.error(ex.getMessage());
        }
    }
    
    private void handleDeleteUnitMessage(DeleteDataPublishMessage deleteDataMessage){
       ObjectMapper mapper = new ObjectMapper();
       DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       mapper.setDateFormat(df);
       List<String> listUnitIds = mapper.convertValue(deleteDataMessage.getData(), new TypeReference<List<String>>(){});
       objectTrackingService.deleteUnitDataInObjectTracking(listUnitIds);
    }
    
}
