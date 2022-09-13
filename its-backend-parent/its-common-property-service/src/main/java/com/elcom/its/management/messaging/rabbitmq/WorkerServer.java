/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.messaging.rabbitmq;

import com.elcom.its.management.dto.EventExport;
import com.elcom.its.management.service.EventFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 *
 * @author admin
 */
@Component
public class WorkerServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerServer.class);

    public WorkerServer() {
    }

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private EventFileService eventFileService;
    

    @RabbitListener(queues = "${its.property.export.worker.queue}")
    public void workerReceiveHistory(String json) throws IOException{
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            EventExport data = mapper.readValue(json, EventExport.class);
            eventFileService.createFileEventHistory(data.getName(),data.getPosition(),data.getTypes(),data.getStatus(),data.getSort(),data.getPage(),data.getSize(),data.getStages(),data.getIsAdmin(),data.getUuid());

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }


}
