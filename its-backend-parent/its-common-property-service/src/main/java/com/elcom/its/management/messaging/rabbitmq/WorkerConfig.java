/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.messaging.rabbitmq;

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

    @Value("${user.worker.queue}")
    private String workerQueue;

    @Value("${its.property.export.worker.queue}")
    private String workerQueueExportEvent;

    @Bean("workerQueueExportEvent")
    public Queue initWorkerQueueExportEvent() {
        return new Queue(workerQueueExportEvent);
    }

    @Value("${its.event.info.worker.queue}")
    private String workerQueueInfoEvent;

    @Value("${its.event.info.update.worker.queue}")
    private String workerQueueInfoEventUpdate;

    @Bean("workerQueueInfoEvent")
    public Queue initWorkerQueueInfoEvent() {
        return new Queue(workerQueueInfoEvent);
    }

    @Bean("workerQueueInfoEventUpdate")
    public Queue initWorkerQueueInfoEventUpdate() {
        return new Queue(workerQueueInfoEventUpdate);
    }

    @Bean("workerQueue")
    public Queue initWorkerQueue() {
        return new Queue(workerQueue);
    }

    public WorkerServer workerServer() {
        return new WorkerServer();
    }

}
