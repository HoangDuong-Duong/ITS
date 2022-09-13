package com.elcom.its.shift.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import com.elcom.its.shift.service.impl.RabbitQueueService;

/**
 *
 * @author anhdv
 */
@Configuration
@Service
public class Schedulers {

    @Autowired
    private RabbitQueueService rabbitQueueService;

    private static final Logger LOGGER = LoggerFactory.getLogger(Schedulers.class);

    @Scheduled(fixedDelayString = "60000")
    public void scanScheduledEvent() throws InterruptedException, JsonProcessingException {
        rabbitQueueService.addQueueToListener("haha", "its_management_rpc_queue_t");
    }

    @Bean
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        return scheduler;
    }

}
