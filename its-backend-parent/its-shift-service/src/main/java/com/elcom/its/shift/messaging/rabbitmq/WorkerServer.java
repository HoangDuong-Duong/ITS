/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.messaging.rabbitmq;

import com.elcom.its.shift.dto.ExportShiftReportRequest;
import com.elcom.its.shift.service.ExportFileService;
import com.fasterxml.jackson.core.type.TypeReference;
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
    private ExportFileService exportFileService;

    @RabbitListener(queues = "${its.shift.export.worker.queue}")
    public void workerReceiveExportFile(String json) throws IOException {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);

            ExportShiftReportRequest exportRequest = mapper.readValue(json, new TypeReference<ExportShiftReportRequest>() {
            });
            switch (exportRequest.getReportType()) {
                case "WEEKLY":
                    exportWeeklyShiftReport(exportRequest);
                    break;
                case "MONTHLY":
                    exportMonthlyShiftReport(exportRequest);
                    break;
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void exportWeeklyShiftReport(ExportShiftReportRequest exportRequest) {
        exportFileService.exportWeeklyShiftReport(exportRequest);
    }

    private void exportMonthlyShiftReport(ExportShiftReportRequest exportRequest) {
        exportFileService.exportMonthlyShiftReport(exportRequest);
    }

}
