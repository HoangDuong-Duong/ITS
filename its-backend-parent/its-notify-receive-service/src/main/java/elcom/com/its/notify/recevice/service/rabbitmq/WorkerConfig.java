/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.rabbitmq;

import elcom.com.its.notify.recevice.service.service.QueueNameService;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
@Configuration
public class WorkerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerConfig.class);

    @Value("${user.worker.queue}")
    private String workerQueue;

    @Autowired
    private QueueNameService queueNameService;

    @Bean("workerQueue")
    public Queue initWorkerQueue() {
        return new Queue(workerQueue);
    }

//    public List<String> getControlQueueName() {
//        LOGGER.info("=======> getControlQueueName");
//        return queueNameService.get();
//    }

    public List<String> getRecognition() {
        LOGGER.info("=======> getQueueCam");
        return queueNameService.getQueueCam();
    }

    public List<String> getEvent() {
        LOGGER.info("=======> getQueueEvent");
        return queueNameService.getQueueEvent();
    }

    public List<String> getEventChangeStatus() {
        LOGGER.info("=======> getQueueEvent");
        return queueNameService.getQueueEventChangeStatus();
    }
//
    public List<String> getHeartbeatCamera() {
        LOGGER.info("=======> getQueueHeartBeatCamera");
        return queueNameService.getQueueHeartBeatCamera();
    }
//
    public List<String> getHeartbeatVms() {
        LOGGER.info("=======> getQueueHeartBeatVms");
        return queueNameService.getQueueHeartBeatVms();
    }

    public List<String> getHeartbeatVmsBoard() {
        LOGGER.info("=======> getHeartbeatVmsBoard");
        return queueNameService.getQueueHeartBeatVMSBoard();
    }

    public List<String> getNewsVmsBoard() {
        LOGGER.info("=======> getHeartbeatVmsBoard");
        return queueNameService.getQueueNewsVMSBoard();
    }

    public List<String> getDisplayVmsBoard() {
        LOGGER.info("=======> getHeartbeatVmsBoard");
        return queueNameService.getQueueDisplayVMSBoard();
    }

    public List<String> getObjectTracking() {
        LOGGER.info("=======> getQueueObjectTracking");
        return queueNameService.getQueueObjectTracking();
    }

    public List<String> getTraffic() {
        LOGGER.info("=======> getQueueStage");
        return queueNameService.getQueueStage();
    }

    public List<String> getJob() {
        LOGGER.info("=======> getQueueStage");
        return queueNameService.getQueueNameJob();
    }
    public List<String> getFinishEvent() {
        LOGGER.info("=======> getQueueStage");
        return queueNameService.getQueueNameFinishEvent();
    }

    public List<String> getScheduleEvent() {
        LOGGER.info("=======> getQueueStage");
        return queueNameService.getQueueNameScheduleEvent();
    }


    //@Bean
    //public WorkerServer workerServer() {
    //    return new WorkerServer();
    //}

}
