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

    @Value("${its.event.export.worker.queue}")
    private String workerQueueExportEvent;

    @Value("${info_worker_queue_event_accident}")
    private String exportAccident;

    @Value("${info_worker_queue_event}")
    private String exportInfo;

    @Value("${its.report.event.daily.worker.queue}")
    private String itsEventDaily;

    @Bean("workerQueueExportEvent")
    public Queue initWorkerQueueExportEvent() {
        return new Queue(workerQueueExportEvent);
    }

    @Bean("exportAccident")
    public Queue initWorkerQueueExportAccident() {
        return new Queue(exportAccident);
    }

    @Bean("exportDailyEvent")
    public Queue initWorkerQueueExportDailyEvent() {
        return new Queue(itsEventDaily);
    }

    @Bean("exportInfo")
    public Queue initWorkerQueueExportEventInfo() {
        return new Queue(exportInfo);
    }

    @Value("${its.event.info.worker.queue}")
    private String workerQueueInfoEvent;

    @Value("${its.report.day.worker.queue}")
    private String workerQueueDayReport;

    @Value("${its.event.info.update.worker.queue}")
    private String workerQueueInfoEventUpdate;

    @Bean("workerQueueInfoEvent")
    public Queue initWorkerQueueInfoEvent() {
        return new Queue(workerQueueInfoEvent);
    }

    @Bean("workerQueueDayReport")
    public Queue initWorkerQueueDayReport() {
        return new Queue(workerQueueDayReport);
    }

    @Bean("workerQueueInfoEventUpdate")
    public Queue initWorkerQueueInfoEventUpdate() {
        return new Queue(workerQueueInfoEventUpdate);
    }
    
//    @Autowired
//    private QueueNameService queueNameService;

    @Bean("workerQueue")
    public Queue initWorkerQueue() {
        return new Queue(workerQueue);
    }
    
//    public List<String> getControlQueueName() {
//        return queueNameService.get();
//    }

    //@Bean
    public WorkerServer workerServer() {
        return new WorkerServer();
    }

}
