package com.elcom.its.management.scheduler;

import com.elcom.its.management.dto.EventDTO;
import com.elcom.its.management.dto.EventManualDTO;
import com.elcom.its.management.dto.Response;
import com.elcom.its.management.dto.ScheduleEventNotifyRequest;
import com.elcom.its.management.dto.SiteDTO;
import com.elcom.its.management.dto.Unit;
import com.elcom.its.management.dto.User;
import com.elcom.its.management.enums.EventStatus;
import com.elcom.its.management.enums.JobStatus;
import com.elcom.its.management.enums.ScheduledEventStatus;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.management.model.Job;
import com.elcom.its.management.model.ScheduledEvent;
import com.elcom.its.management.service.EventService;
import com.elcom.its.management.service.JobService;
import com.elcom.its.management.service.ScheduledEventService;
import com.elcom.its.management.service.SiteService;
import com.elcom.its.management.service.UserService;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

/**
 *
 * @author anhdv
 */
@Configuration
@Service
public class Schedulers {
    
    @Autowired
    private ScheduledEventService scheduledEventService;
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private SiteService siteService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RabbitMQClient rabbitMQClient;
    
    @Autowired
    private JobService jobService;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Schedulers.class);
    
    @Scheduled(fixedDelayString = "60000")
    public void scanScheduledEvent() throws InterruptedException, JsonProcessingException {
        List<ScheduledEvent> listWaitingEvent = scheduledEventService.getListWaitingEvent(getStartOfDay(), getEndOfDay());
        for (ScheduledEvent event : listWaitingEvent) {
            if (event.isStarted()) {
                String eventId = createEvent(event);
                event.setStatus(ScheduledEventStatus.HAPPENED);
                event.getJob().setStatus(JobStatus.RECEIVE_PROCESSING);
                event.getJob().setEventStartTime(event.getStartTime());
                event.getJob().setEventId(eventId);
                scheduledEventService.save(event);
                notifyScheduledEvent("STARTED", "Sự kiện bắt đầu", event);
            } else if (event.isUpcoming() && event.getStatus().code() != ScheduledEventStatus.UPCOMING.code()) {
                event.setStatus(ScheduledEventStatus.UPCOMING);
                scheduledEventService.save(event);
                notifyScheduledEvent("UPCOMING", "Sự kiện sắp diễn ra", event);
            }
        }
    }
    
    @Bean
    public TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        return scheduler;
    }
    
    private String createEvent(ScheduledEvent scheduledEvent) {
        EventManualDTO manualEvent = createEventRecord(scheduledEvent);
        Response response = eventService.saveManual(manualEvent);
        if (response.getData() != null) {
            ObjectMapper oMapper = new ObjectMapper();
            Map<String, Object> map = oMapper.convertValue(response.getData(), Map.class);
            String eventId = (String) map.get("id");
            return eventId;
        }
        return null;
    }
    
    private EventManualDTO createEventRecord(ScheduledEvent scheduledEvent) {
        EventManualDTO manualEvent = new EventManualDTO();
        Job job = scheduledEvent.getJob();
        manualEvent.setDirectionCode(scheduledEvent.getDirectionCode());
        manualEvent.setSiteId(scheduledEvent.getSiteId());
        manualEvent.setEndSite(job.getEndSiteId());
        manualEvent.setEventCode(scheduledEvent.getEventType());
        manualEvent.setEventStatus(EventStatus.PROCESSING.code());
        manualEvent.setStartTime(scheduledEvent.getStartTime());
        manualEvent.setEndTime(scheduledEvent.getEndTime());
        manualEvent.setUuid(UUID.randomUUID().toString());
        manualEvent.setObjectName("Sự kiện theo kế hoạch");
        manualEvent.setManualEvent(false);
        manualEvent.setUuid(scheduledEvent.getCreatedBy());
        return manualEvent;
    }
    
    private void notifyScheduledEvent(String actionCode, String actionName, ScheduledEvent scheduledEvent) {
        ScheduleEventNotifyRequest notifyRequest = ScheduleEventNotifyRequest.builder()
                .actionCode(actionCode)
                .actionName(actionName)
                .scheduledEvent(scheduledEvent)
                .build();
        Job job = scheduledEvent.getJob();
        if (!StringUtil.isNullOrEmpty(job.getEventId())) {
            EventDTO eventDTO = eventService.getEventByParentId(job.getEventId());
            if (eventDTO != null) {
                notifyRequest.setEventKey(eventDTO.getKey());
                notifyRequest.setEventName(eventDTO.getEventName());
                notifyRequest.setEventPositionM(eventDTO.getSite().getPositionM());
            }
        }
        if (job.getStartSiteId() != null) {
            SiteDTO site = siteService.findById(job.getStartSiteId());
            notifyRequest.setJobStartSiteName(site.getSiteName());
            notifyRequest.setJobStartPositionM(site.getPositionM());
        }
        if (job.getEndSiteId() != null) {
            SiteDTO site = siteService.findById(job.getEndSiteId());
            notifyRequest.setJobEndSiteName(site.getSiteName());
            notifyRequest.setJobEndPositionM(site.getPositionM());
        }
        String groupName = null;
        if (!StringUtil.isNullOrEmpty(job.getUserIds())) {
            List<User> listUsers = userService.findByListId(job.getUserIds());
            List<String> listUserNames = listUsers.stream().map(u -> u.getFullName()).collect(Collectors.toList());
            notifyRequest.setListUsers(String.join(",", listUserNames));
            groupName = listUsers.get(0).getUnit().getName();
        }
        if (!StringUtil.isNullOrEmpty(job.getCreatedBy())) {
            List<User> listUsers = userService.findByListId(job.getCreatedBy());
            List<String> actorName = listUsers.stream().map(u -> u.getFullName()).collect(Collectors.toList());
            notifyRequest.setActor(String.join(",", actorName));
        }
        
        if (groupName == null) {
            Unit unit = userService.findUnitById(job.getGroupId());
            groupName = unit.getName();
        }
        notifyRequest.setGroupName(groupName);
        
        rabbitMQClient.callWorkerService(RabbitMQProperties.NOTIFY_SCHEDULED_EVENT_WOKER_QUEUE, notifyRequest.toJsonString());
    }
    
    private Date getEndOfDay() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    
    private Date getStartOfDay() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
