/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.messaging.rabbitmq;

import com.elcom.its.management.dto.AccidentEventExport;
import com.elcom.its.management.dto.EventExport;
import com.elcom.its.management.dto.EventInfoSend;
import com.elcom.its.management.dto.ReportDayExport;
import com.elcom.its.management.service.EventFileService;
import com.elcom.its.management.service.EventInfoFileService;
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

    @Autowired
    private EventInfoFileService eventInfoFileService;
    

    @RabbitListener(queues = "${its.event.export.worker.queue}")
    public void workerReceiveHistory(String json) throws IOException{
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            EventExport eventExport = mapper.readValue(json, EventExport.class);
            eventFileService.createFileEventHistory(eventExport.getEventId(),eventExport.getUuid());

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }


    @RabbitListener(queues = "${its.event.info.worker.queue}")
    public void workerReceiveInfo(String json) throws IOException{
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            ObjectMapper mapper = new ObjectMapper();
//            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            mapper.setDateFormat(df);
            EventExport eventExport = mapper.readValue(json, EventExport.class);
            eventFileService.createFileEventInfo(eventExport.getEventId(),eventExport.getUuid());

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @RabbitListener(queues = "${its.report.day.worker.queue}")
    public void workerReceiveReportDay(String json) throws IOException{
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            ObjectMapper mapper = new ObjectMapper();
            ReportDayExport eventExport = mapper.readValue(json, ReportDayExport.class);
            eventFileService.createFileDayReport(eventExport.getUuid(),eventExport.getStartTime(),eventExport.getEndTime());

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @RabbitListener(queues = "${its.report.event.daily.worker.queue}")
    public void workerReceiveReportDailyReport(String json) throws IOException{
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            ObjectMapper mapper = new ObjectMapper();
            ReportDayExport eventExport = mapper.readValue(json, ReportDayExport.class);
            eventFileService.createReportEventDaily(eventExport.getStartTime(),eventExport.getEndTime(),eventExport.getUuid());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @RabbitListener(queues = "${its.event.info.update.worker.queue}")
    public void workerReceiveInfoUpdate(String json) throws IOException{
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
//            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            mapper.setDateFormat(df);
            EventExport eventExport = mapper.readValue(json, EventExport.class);
            eventFileService.createFileEventInfoUpdate(eventExport.getEventId(),eventExport.getUuid());

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @RabbitListener(queues = "${info_worker_queue_event_accident}")
    public void workerReceiveAccidentReport(String json) throws IOException{
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            AccidentEventExport data = mapper.readValue(json, AccidentEventExport.class);
            eventInfoFileService.createAccidentReportFile(data.getStartDate(), data.getEndDate(), data.getTextHeader(), data.getUuid());

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @RabbitListener(queues = "${info_worker_queue_event}")
    public void workerReceiveEventInfoReport(String json) throws IOException{
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            EventInfoSend data = mapper.readValue(json, EventInfoSend.class);
            eventInfoFileService.createEventReportFile(data.getSize(), data.getPage(), data.getStartDate(), data.getEndDate(), data.getTextHeader(), data.getUuid());

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }


}
