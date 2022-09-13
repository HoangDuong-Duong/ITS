package elcom.com.its.notify.recevice.service.scheduler;
import com.fasterxml.jackson.core.JsonProcessingException;
import elcom.com.its.notify.recevice.service.service.QueueNameService;
import elcom.com.its.notify.recevice.service.service.RabbitQueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author anhdv
 */
@Configuration
@Service
public class Schedulers {

    @Autowired
    private RabbitQueueService rabbitQueueService;
    @Autowired
    private QueueNameService queueNameService;

    private static final Logger LOGGER = LoggerFactory.getLogger(Schedulers.class);

    @Scheduled(fixedDelayString = "60000")
    public void scanScheduledEvent() throws InterruptedException, JsonProcessingException {
        List<String> queueEvent = queueNameService.getQueueEvent();
        for (String queue: queueEvent
             ) {
            rabbitQueueService.addQueueToListener("processEvent", queue);
        }
        List<String> queueRecognition = queueNameService.getQueueCam();
        for (String queue: queueRecognition
        ) {
            rabbitQueueService.addQueueToListener("processRecognition", queue);
        }

    }

    @Bean
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        return scheduler;
    }

}
