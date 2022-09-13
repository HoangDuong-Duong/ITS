package com.elcom.its.management.service.impl;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.management.dto.*;
import com.elcom.its.management.service.EventService;

import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.enums.EventStatus;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

@Service
public class ITSCoreEventServiceImpl implements EventService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ITSCoreEventServiceImpl.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private RabbitMQClient rabbitMQClient;
    
    @Override
    public EventResponseDTO findEventPage(String stages, String fromDate, String toDate, String filterObjectType, String filterObjectIds, String objectName, String eventCode, Integer eventStatus, String directionCode, String plate, String key, Integer page, Integer size, Boolean isAdmin, String manual) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event";
        String params = "fromDate=" + fromDate
                + "&toDate=" + toDate
                + "&page=" + page
                + "&filterObjectType=" + filterObjectType
                + "&size=" + size
                + "&filterObjectIds=" + filterObjectIds
                + "&objectName=" + objectName
                + "&eventCode=" + eventCode
                + "&directionCode=" + directionCode
                + "&isAdmin=" + isAdmin
                + "&eventStatus=" + eventStatus
                + "&manual=" + manual;
        if (stages != null) {
            params += "&stages=" + stages;
        }
        if (!StringUtil.isNullOrEmpty(plate)) {
            params += "&plate=" + plate;
        }
        if (!StringUtil.isNullOrEmpty(key)) {
            params += "&key=" + key;
        }
        urlRequest = urlRequest + "?" + params;
        EventResponseDTO dto = restTemplate.getForObject(urlRequest, EventResponseDTO.class);
//        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
//            return dto;
//        }
        return dto;
    }
    
    @Override
    public EventResponseDTO findEventPageCommon(List<String> stages, List<String> parentId, String fromDate, String toDate, String filterObjectType, List<String> filterObjectIds, String objectName, List<String> eventCode, Integer eventStatus, String directionCode, String plate, String key, Integer page, Integer size, Boolean isAdmin, String manual) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/common";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("fromDate", fromDate);
        bodyParam.put("toDate", toDate);
        bodyParam.put("filterObjectType", filterObjectType);
        bodyParam.put("size", size);
        bodyParam.put("page", page);
        bodyParam.put("filterObjectIds", filterObjectIds);
        List<String> objectNames = new ArrayList<>();
        objectNames.add(objectName);
        bodyParam.put("objectName", objectNames);
        bodyParam.put("eventCode", eventCode);
        bodyParam.put("directionCode", directionCode);
        bodyParam.put("isAdmin", isAdmin);
        bodyParam.put("parentId", parentId);
        bodyParam.put("stages", stages);
        bodyParam.put("eventStatus", eventStatus);
        bodyParam.put("plate", plate);
        bodyParam.put("key", key);
        bodyParam.put("manual", manual);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<EventResponseDTO> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, EventResponseDTO.class);
        EventResponseDTO dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public EventResponseDTO historyEvent(String parentID) {
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/history";
        urlRequest = urlRequest + "/" + parentID;
        EventResponseDTO dto = restTemplate.getForObject(urlRequest, EventResponseDTO.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response getEvent(String parentId) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/parent";
        urlRequest = urlRequest + "/" + parentId;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }
    
    @Override
    public EventFileResponseDTO getFileEvent(String parentId) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/file";
        urlRequest = urlRequest + "/" + parentId;
        EventFileResponseDTO dto = restTemplate.getForObject(urlRequest, EventFileResponseDTO.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }

    @Override
    public CategoryResponseDTO getPrinted(String printed) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/category/filter";
        String params = "catType=" + printed;
        urlRequest = urlRequest + "?" + params;
        CategoryResponseDTO dto = restTemplate.getForObject(urlRequest, CategoryResponseDTO.class);
//        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
//            return dto;
//        }
        return dto;
    }

    @Override
    public Response saveManual(EventManualDTO eventManualDTO) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/manual";
        //Payload request dbm.root.url//v1.0/dbm/management/mons/recognitions
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("siteId", eventManualDTO.getSiteId());
        bodyParam.put("startKm", eventManualDTO.getStartKm());
        bodyParam.put("startM", eventManualDTO.getStartM());
        bodyParam.put("startProvinceId", eventManualDTO.getStartProvinceId());
        bodyParam.put("startDistrictId", eventManualDTO.getStartDistrictId());
        bodyParam.put("startWardId", eventManualDTO.getStartWardId());
        bodyParam.put("startAddress", eventManualDTO.getStartAddress());
        bodyParam.put("imageUrl", eventManualDTO.getImageUrl());
        bodyParam.put("videoUrl", eventManualDTO.getVideoUrl());
        bodyParam.put("eventCode", eventManualDTO.getEventCode());
        bodyParam.put("sourceId", eventManualDTO.getSourceId());
        bodyParam.put("objectName", eventManualDTO.getObjectName());
        bodyParam.put("directionCode", eventManualDTO.getDirectionCode());
        bodyParam.put("uuid", eventManualDTO.getUuid());
        bodyParam.put("endSite", eventManualDTO.getEndSite());
        bodyParam.put("endKm", eventManualDTO.getEndKm());
        bodyParam.put("endM", eventManualDTO.getEndM());
        bodyParam.put("endProvinceId", eventManualDTO.getEndProvinceId());
        bodyParam.put("endDistrictId", eventManualDTO.getEndDistrictId());
        bodyParam.put("endWardId", eventManualDTO.getEndWardId());
        bodyParam.put("endAddress", eventManualDTO.getEndAddress());
        bodyParam.put("endTime", eventManualDTO.getEndTime());
        bodyParam.put("startTime", eventManualDTO.getStartTime());
        bodyParam.put("eventStatus", eventManualDTO.getEventStatus());
        bodyParam.put("manualEvent", eventManualDTO.isManualEvent());
        bodyParam.put("note", eventManualDTO.getNote());
        bodyParam.put("siteCorrect", eventManualDTO.getSiteCorrect());
        bodyParam.put("user", eventManualDTO.getUser());
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        //Call rest api with url, method, payload, response entity
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }

    @Override
    public ReportEventDailyResponse getReportDailyEvent(String startDate, String endDate) {
        final String uri =ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/report/daily"+"?startDate="+startDate+"&endDate="+endDate;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<ReportEventDailyResponse> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, ReportEventDailyResponse.class);
        return result.getBody();
    }
    
    @Override
    public Response getDetailEvent(String eventId, String startTime) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event";
        urlRequest = urlRequest + "/" + eventId;
        String params = "startTime=" + startTime;
        urlRequest = urlRequest + "?" + params;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public List<EventDTO> getListEventDTOByListParentIds(List<String> eventIds) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/list";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("eventIds", eventIds);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> result = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
        Response response = result != null ? result.getBody() : null;
        if (response != null && (response.getStatus() == HttpStatus.OK.value() || response.getStatus() == HttpStatus.CREATED.value())) {
            ObjectMapper mapper = new ObjectMapper();
            List<EventDTO> eventList = mapper.convertValue(response.getData(), new TypeReference<List<EventDTO>>() {
            });
            return eventList;
        }
        return null;
    }
    
    @Override
    public Response getEventMap(String startTime, String endTime, String stages, boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/map";
        String params = "startTime=" + startTime
                + "&endTime=" + endTime
                + "&admin=" + isAdmin + "&stages=" + stages;
        urlRequest = urlRequest + "?" + params;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response getEventOnStraightMap(String startTime, String endTime, String stages, boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/straight-map";
        String params = "startTime=" + startTime
                + "&endTime=" + endTime
                + "&admin=" + isAdmin + "&stages=" + stages;
        urlRequest = urlRequest + "?" + params;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response updateViolation(String id, String sourceId, String objectType, Date startTime, Date endTime, Float speedOfVehicle, String laneId,
            String plate, String eventCode, String objectName, Integer eventStatus, String imageUrl,
            String videoUrl, String uuid) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/violation/";
        urlRequest = urlRequest + id;
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("sourceId", sourceId);
        bodyParam.put("objectType", objectType);
        bodyParam.put("startTime", startTime);
        bodyParam.put("endTime", endTime);
        bodyParam.put("speedOfVehicle", speedOfVehicle);
        bodyParam.put("laneId", laneId);
        bodyParam.put("plate", plate);
        bodyParam.put("eventCode", eventCode);
        bodyParam.put("objectName", objectName);
        bodyParam.put("eventStatus", eventStatus);
        bodyParam.put("imageUrl", imageUrl);
        bodyParam.put("videoUrl", videoUrl);
        bodyParam.put("uuid", uuid);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response updateSecurity(String id, String sourceId, Date startTime, String laneId,
            String eventCode, Integer eventStatus, String imageUrl, String uuid) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/security/";
        urlRequest = urlRequest + id;
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("sourceId", sourceId);
        bodyParam.put("laneId", laneId);
        bodyParam.put("eventCode", eventCode);
        bodyParam.put("eventStatus", eventStatus);
        bodyParam.put("imageUrl", imageUrl);
        bodyParam.put("uuid", uuid);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response updateManual(String id, String sourceId, Date startTime,
            String eventCode, Integer eventStatus, String imageUrl, String uuid,
            String siteId, String directionCode, String videoUrl, String endSite, Date endTime, String note, String siteCorrect, String objectName) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/manual/";
        urlRequest = urlRequest + id;
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("sourceId", sourceId);
        bodyParam.put("startTime", startTime);
        bodyParam.put("eventCode", eventCode);
        bodyParam.put("eventStatus", eventStatus);
        bodyParam.put("imageUrl", imageUrl);
        bodyParam.put("uuid", uuid);
        bodyParam.put("siteId", siteId);
        bodyParam.put("directionCode", directionCode);
        bodyParam.put("videoUrl", videoUrl);
        bodyParam.put("endSite", endSite);
        bodyParam.put("objectName", objectName);
//        bodyParam.put("endTime", endTime);
        bodyParam.put("manualEvent", true);
        bodyParam.put("note", note);
        bodyParam.put("siteCorrect", siteCorrect);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.PUT, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response updateManual(String id, EventManualDTO eventManualDTO) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/manual/";
        urlRequest = urlRequest + id;
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("siteId", eventManualDTO.getSiteId());
        bodyParam.put("startKm", eventManualDTO.getStartKm());
        bodyParam.put("startM", eventManualDTO.getStartM());
        bodyParam.put("startProvinceId", eventManualDTO.getStartProvinceId());
        bodyParam.put("startDistrictId", eventManualDTO.getStartDistrictId());
        bodyParam.put("startWardId", eventManualDTO.getStartWardId());
        bodyParam.put("startAddress", eventManualDTO.getStartAddress());
        bodyParam.put("imageUrl", eventManualDTO.getImageUrl());
        bodyParam.put("videoUrl", eventManualDTO.getVideoUrl());
        bodyParam.put("eventCode", eventManualDTO.getEventCode());
        bodyParam.put("sourceId", eventManualDTO.getSourceId());
        bodyParam.put("objectName", eventManualDTO.getObjectName());
        bodyParam.put("directionCode", eventManualDTO.getDirectionCode());
        bodyParam.put("uuid", eventManualDTO.getUuid());
        bodyParam.put("endSite", eventManualDTO.getEndSite());
        bodyParam.put("endKm", eventManualDTO.getEndKm());
        bodyParam.put("endM", eventManualDTO.getEndM());
        bodyParam.put("endProvinceId", eventManualDTO.getEndProvinceId());
        bodyParam.put("endDistrictId", eventManualDTO.getEndDistrictId());
        bodyParam.put("endWardId", eventManualDTO.getEndWardId());
        bodyParam.put("endAddress", eventManualDTO.getEndAddress());
        bodyParam.put("endTime", eventManualDTO.getEndTime());
        bodyParam.put("startTime", eventManualDTO.getStartTime());
        bodyParam.put("eventStatus", eventManualDTO.getEventStatus());
        bodyParam.put("manualEvent", eventManualDTO.isManualEvent());
        bodyParam.put("note", eventManualDTO.getNote());
        bodyParam.put("siteCorrect", eventManualDTO.getSiteCorrect());
        bodyParam.put("user", eventManualDTO.getUser());
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.PUT, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response updateEvent(String id, String eventCode, String note, String siteCorrect, String imageUrl, String videoUrl, String uuid) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/base/";
        urlRequest = urlRequest + id;
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("note", note);
        bodyParam.put("eventCode", eventCode);
        bodyParam.put("siteCorrect", siteCorrect);
        bodyParam.put("videoUrl", videoUrl);
        bodyParam.put("imageUrl", imageUrl);
        bodyParam.put("manualEvent", true);
        bodyParam.put("uuid", uuid);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.PUT, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response deleteEvent(String eventId, String startTime) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/" + eventId;
        String params = "startTime=" + startTime;
        urlRequest = urlRequest + "?" + params;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.GET, requestBody, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response deleteMultiEvent(List<String> eventIds) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("eventIds", eventIds);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.DELETE, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response getMultiEvent(List<String> eventIds) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/stage";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("eventIds", eventIds);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response updateStatus(String eventParentId, Date startTime, Integer status, String uuid) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/";
        urlRequest = urlRequest + eventParentId;
        String params = "status=" + status
                + "&uuid=" + uuid;
        urlRequest = urlRequest + "?" + params;
        Map<String, Object> bodyParam = new HashMap<>();
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.PUT, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response getStageViolation(String id, String startTime) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/stage/violation";
        String params = "id=" + id
                + "&startTime=" + startTime;
        urlRequest = urlRequest + "?" + params;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response getStageSecurity(String id, String startTime) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/stage/security";
        String params = "id=" + id
                + "&startTime=" + startTime;
        urlRequest = urlRequest + "?" + params;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response getStageManual(String id, String startTime) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/stage/manual";
        String params = "id=" + id
                + "&startTime=" + startTime;
        urlRequest = urlRequest + "?" + params;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
    
    @Override
    public Response updateEventStatus(String parentEventId, EventStatus eventStatus, String userId) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/" + parentEventId
                + "?status=" + eventStatus.code() + "&uuid=" + userId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.PUT, requestBody, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }
    
    @Override
    public EventDTO getEventByParentId(String parentId) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/parent";
        urlRequest = urlRequest + "/" + parentId;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            try {
                EventDTO event = mapper.convertValue(dto.getData(), new TypeReference<EventDTO>() {
                });
                return event;
            } catch (Exception e) {
                return null;
            }
            
        }
        return null;
    }
    
    @Override
    public Response getHistory(Map<String, String> headerParam, String pathParam) {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        ResponseMessage response;
        EventResponseDTO eventResponseDTO = historyEvent(pathParam);
        try {
            List<EventDTO> data = eventResponseDTO.getData();
            data.stream().map(item -> {
                AuthorizationResponseDTO userDTO = getUsernameByUserId(item.getModifiedBy());
                if (userDTO != null) {
                    item.setUserName(userDTO.getFullName());
                }
                return item;
            }).collect(Collectors.toList());
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), data, (long) data.size()));
            return new Response(HttpStatus.OK.value(), HttpStatus.OK.toString(), data);
            
        } catch (Exception ex) {
            return new Response(HttpStatus.FORBIDDEN.value(), "Lỗi từ service Id", null);
        }
    }
    
    AuthorizationResponseDTO getUsernameByUserId(String id) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_INTERNAL_URL);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setPathParam(id);
        userRpcRequest.setHeaderParam(null);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
            } catch (JsonProcessingException ex) {
                return null;
            }
            if (response != null && response.getStatus() == HttpStatus.OK.value()) {
                try {
                    MessageContent content = response.getData();
                    Object data = content.getData();
                    if (data != null) {
                        AuthorizationResponseDTO userDTO = null;
                        if (data.getClass() == LinkedHashMap.class) {
                            userDTO = new AuthorizationResponseDTO((Map<String, Object>) data);
                        } else if (data.getClass() == AuthorizationResponseDTO.class) {
                            userDTO = (AuthorizationResponseDTO) data;
                        } else if (data.getClass() == ArrayList.class) {
                            JsonNode jsonNode = mapper.readTree(result);
                            List<AuthorizationResponseDTO> userReceiverDTOList = mapper.readerFor(new TypeReference<List<AuthorizationResponseDTO>>() {
                            }).readValue(jsonNode.get("data").get("data"));
                            userDTO = userReceiverDTOList.get(0);
                        }
                        
                        if (userDTO != null && !StringUtil.isNullOrEmpty(userDTO.getUuid())) {
                            return userDTO;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            } else {
                //Forbidden
                return null;
            }
        }
        return null;
    }
    
    @Override
    public Response getTrafficJamDetailHistory(String startTime, String endTime, String eventId) {
        String urlParam = "eventId=" + eventId + "&startTime=" + startTime + "&endTime=" + endTime;
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/traffic-jam/detail?" + urlParam;
        try {
            Response response = restTemplate.getForObject(urlRequest, Response.class);
            return response;
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return null;
        }
        
    }
    
    @Override
    public Response createEventFile(EventFileDTO eventFile) {
        try {
            String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/event-file";
            Response response = restTemplate.postForObject(urlRequest, eventFile, Response.class);
            return response;
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return null;
        }
    }

    @Override
    public Response updateReportStatusEvent(ReportStatusEvent reportStatusEvent) {
        try {
            String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/report-status";
            Map<String, Object> bodyParam = new HashMap<>();
            bodyParam.put("eventId", reportStatusEvent.getEventId());
            bodyParam.put("startTime", reportStatusEvent.getStartTime());
            bodyParam.put("reportStatus", reportStatusEvent.getReportStatus());
            bodyParam.put("modifiedBy", reportStatusEvent.getModifiedBy());

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
            HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
            return response.getBody();
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return null;
        }
    }
}
