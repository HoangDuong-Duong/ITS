/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.report.messaging.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author admin
 */
@Configuration
public class WorkerConfig {

    @Value("${its.violation.export.worker.queue}")
    private String violationWorkerQueue;

    @Bean("violationWorkerQueue")
    public Queue initWorkerViolationWorkerQueueQueueExport() {
        return new Queue(violationWorkerQueue);
    }

    public WorkerServer workerServer() {
        return new WorkerServer();
    }
}
