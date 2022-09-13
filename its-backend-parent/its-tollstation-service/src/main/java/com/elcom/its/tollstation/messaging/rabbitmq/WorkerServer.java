/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.tollstation.messaging.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    


//    @RabbitListener(queues = "${info_worker_queue_event}")
//    public void workerReceiveEventInfoReport(String json) throws IOException{
//        try {
//            LOGGER.info(" [-->] Server received request for : {}", json);
//            //Process here
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            mapper.setDateFormat(df);
//            EventInfoSend data = mapper.readValue(json, EventInfoSend.class);
//            eventInfoFileService.createEventReportFile(data.getSize(), data.getPage(), data.getStartDate(), data.getEndDate(), data.getTextHeader(), data.getUuid());
//
//        } catch (Exception ex) {
//            LOGGER.error(ex.getMessage());
//            ex.printStackTrace();
//        }
//    }


}
