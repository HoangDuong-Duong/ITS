package com.elcom.its.management.service.impl;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.EventDTO;
import com.elcom.its.management.dto.EventJobDTO;
import com.elcom.its.management.dto.EventResponseDTO;
import com.elcom.its.management.model.Job;
import com.elcom.its.management.repository.JobCustomize;
import com.elcom.its.management.repository.JobRepository;
import com.elcom.its.management.service.CalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl  implements CalendarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalendarServiceImpl.class);

    @Autowired
    private JobCustomize jobCustomize;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public List<EventJobDTO> getMyJob(Date startDate, Date endDate, List<String> jobType, Integer priority, Integer status, List<String> siteIds, String eventCodes, String groupId, String uuid, Date expireStart, Date expireEnd, String key) {
        List<Job> jobs=  jobCustomize.getMyJob(startDate,endDate,expireStart,expireEnd, jobType,priority,status,siteIds, groupId,uuid);
        List<EventJobDTO> result = new ArrayList<>();
        List<String> parentIds = jobs.stream().map((item) -> item.getEventId()).collect(Collectors.toList());
        Set<String>  eventIds = new HashSet<>();
        eventIds.addAll(parentIds);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(endDate == null){
            endDate = new Date();
        }
        if(eventIds.isEmpty()){
            return new ArrayList<>();
        }
        String events =  String.join(",", eventIds) ;
        EventResponseDTO dto = findMyEvent(df.format(startDate),df.format(endDate),eventCodes,events,key);
        Map<String,EventDTO> eventDTOMap = new HashMap<>();
        List<EventDTO> eventDTOS = new ArrayList<>();
        if(dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())){
            eventDTOS =dto.getData();
        } else {
            return new ArrayList<>();
        }
        if(eventDTOS == null || eventDTOS.isEmpty()){
            return new ArrayList<>();
        } else {
            for (EventDTO event: eventDTOS
                 ) {
                eventDTOMap.put(event.getParentId(),event);
            }
        }
        for (Job job: jobs
             ) {
            if(eventDTOMap.get(job.getEventId())!= null){
                result.add(new EventJobDTO(eventDTOMap.get(job.getEventId()),job));
            }

        }
        return result;
    }

    @Override
    public List<EventJobDTO> getJobForReport(Date startDate, Date endDate,String eventCodes,String key) {
        List<Job> jobs=  jobRepository.getJobReport(startDate,endDate);
        List<EventJobDTO> result = new ArrayList<>();
        List<String> parentIds = jobs.stream().map((item) -> item.getEventId()).collect(Collectors.toList());
        Set<String>  eventIds = new HashSet<>();
        eventIds.addAll(parentIds);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(endDate == null){
            endDate = new Date();
        }
        if(eventIds.isEmpty()){
            return new ArrayList<>();
        }
        String events =  String.join(",", eventIds) ;
        EventResponseDTO dto = findMyEventForReport(df.format(startDate),df.format(endDate),eventCodes,events,key);
        Map<String,EventDTO> eventDTOMap = new HashMap<>();
        List<EventDTO> eventDTOS = new ArrayList<>();
        if(dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())){
            eventDTOS =dto.getData();
        } else {
            return new ArrayList<>();
        }
        if(eventDTOS == null || eventDTOS.isEmpty()){
            return new ArrayList<>();
        } else {
            for (EventDTO event: eventDTOS
            ) {
                eventDTOMap.put(event.getParentId(),event);
            }
        }
        for (Job job: jobs
        ) {
            if(eventDTOMap.get(job.getEventId())!= null){
                result.add(new EventJobDTO(eventDTOMap.get(job.getEventId()),job));
            }

        }
        return result;
    }

    public EventResponseDTO findMyEvent( String startTime, String endTime, String eventCodes, String parentIds, String key) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/job";
        String params = "startTime=" + startTime
                + "&endTime=" + endTime
                + "&eventCodes=" + eventCodes
                + "&parentIds=" + parentIds
                + "&key=" + key;
        urlRequest = urlRequest + "?" + params;
        EventResponseDTO dto = restTemplate.getForObject(urlRequest, EventResponseDTO.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }

    public EventResponseDTO findMyEventForReport( String startTime, String endTime, String eventCodes, String parentIds, String key) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/job";
        String params = "startTime=" + startTime
                + "&endTime=" + endTime
                + "&parentIds=" + parentIds;
        urlRequest = urlRequest + "?" + params;
        EventResponseDTO dto = restTemplate.getForObject(urlRequest, EventResponseDTO.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
}
