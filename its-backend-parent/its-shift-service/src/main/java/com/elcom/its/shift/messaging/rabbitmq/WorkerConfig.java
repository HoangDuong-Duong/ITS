/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.messaging.rabbitmq;

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

    @Value("${its.shift.export.worker.queue}")
    private String workerQueueExportEvent;

    @Bean("workerQueueExportEvent")
    public Queue initWorkerQueueExportEvent() {
        return new Queue(workerQueueExportEvent);
    }
    
    public WorkerServer workerServer() {
        return new WorkerServer();
    }

}
