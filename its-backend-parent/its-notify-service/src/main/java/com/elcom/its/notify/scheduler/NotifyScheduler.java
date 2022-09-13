package com.elcom.its.notify.scheduler;

import com.elcom.its.notify.model.DeviceMap;
import com.elcom.its.notify.service.DeviceMapService;
import com.elcom.its.notify.worker.SocketWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;

@Service
public class NotifyScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyScheduler.class);
    
    @Value("${fix-delay-noti}")
    private Long delayTime;

    @Autowired
    private DeviceMapService deviceMapService;
    
    @Autowired
    private SocketWorker socketWorker;

    //@Scheduled(fixedDelay = 1000)
    public void processNotify() throws JsonProcessingException {
        List<DeviceMap> deviceMaps = deviceMapService.findAll();
        Instant now = Instant.now();
        if (deviceMaps == null) {
            LOGGER.info("Không có thông tin config");
            return;
        }
        for (DeviceMap deviceMap : deviceMaps) {
            if (deviceMap.isPatrolViolation()) {
                boolean checked = socketWorker.checkSocketOnline(deviceMap.getUserId());
                if (!checked) {
                    Instant compare = now.minusSeconds(delayTime);
                    if (compare.isAfter(deviceMap.getLastTimeOnline())) {
                        socketWorker.handleSendNoti(deviceMap.getUserId());
                        deviceMap.setPatrolViolation(false);
                        deviceMap.setLastTimeOnline(now);
                    }
                }
            }
        }
        deviceMapService.saveAll(deviceMaps);
    }
}
