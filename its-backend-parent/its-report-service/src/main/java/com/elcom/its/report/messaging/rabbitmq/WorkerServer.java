/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.report.messaging.rabbitmq;

import com.elcom.its.report.worker.ExportTroubleReportService;
import com.elcom.its.report.worker.ExportViolationWorker;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.elcom.its.report.model.dto.ExportTroubleReportRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Admin
 */
@Component
public class WorkerServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerServer.class);

    @Autowired
    private ExportViolationWorker exportViolationWorker;

    @Autowired
    private ExportTroubleReportService exportTroubleReportService;

    public WorkerServer() {
    }

    @RabbitListener(queues = "${its.violation.export.worker.queue}")
    public void violationExportWorkerReceive(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            exportViolationWorker.processViolationExporterJob(json);
        } catch (Exception ex) {
            LOGGER.error(ExceptionUtils.getStackTrace(ex));
            ex.printStackTrace();
        }
    }

    @RabbitListener(queues = "${trouble.report.export.worker.queue}")
    public void troubleReportExportWorkerReceive(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            ObjectMapper mapper = new ObjectMapper();
            ExportTroubleReportRequest exportRequest = mapper.readValue(json, new TypeReference<ExportTroubleReportRequest>() {
            });
            exportTroubleReportService.processExport(exportRequest);
        } catch (Exception ex) {
            LOGGER.error(ExceptionUtils.getStackTrace(ex));
            ex.printStackTrace();
        }
    }
}
