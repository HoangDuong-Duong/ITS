package com.elcom.its.management.service.impl;

import com.elcom.its.management.dto.*;
import com.elcom.its.management.service.PropertyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.enums.EventStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;

@Service
public class PropertyServiceImpl implements PropertyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response saveListProperty(List<CommonProperty> commonPropertyList,String uuid, List<String> stages, Boolean isAdmin){
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/common-property/multi";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("commonProperties", commonPropertyList);
        bodyParam.put("uuid", uuid);
        bodyParam.put("stages",stages);
        bodyParam.put("isAdmin",isAdmin);
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
    public Response saveListPropertyImport(List<CommonProperty> commonPropertyList, String uuid) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/common-property/multi/import";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("commonProperties", commonPropertyList);
        bodyParam.put("uuid", uuid);
        bodyParam.put("stages",null);
        bodyParam.put("isAdmin",true);
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
    public Response updateListProperty(List<CommonProperty> commonPropertyList, String uuid) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/common-property/multi";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("commonProperties", commonPropertyList);
        bodyParam.put("uuid", uuid);
        bodyParam.put("stages",null);
        bodyParam.put("isAdmin",true);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        //Call rest api with url, method, payload, response entity
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.PUT, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }

    @Override
    public PropertyResponseDTO findProperty(String name, String position, List<Integer> types, List<Integer> status, String sort, Integer page, Integer size, List<String> stages, Boolean isAdmin,String site,String directionCode, String typeData) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/common-property/";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("name", name);
        bodyParam.put("position",position);
        bodyParam.put("types",types);
        bodyParam.put("isAdmin", isAdmin);
        bodyParam.put("size",size);
        bodyParam.put("page",page);
        bodyParam.put("sort",null);
        bodyParam.put("status",status);
        bodyParam.put("stages",stages);
        bodyParam.put("site",site);
        bodyParam.put("directionCode",directionCode);
        bodyParam.put("typeData",typeData);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<PropertyResponseDTO> dto = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, PropertyResponseDTO.class);
//        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
//            return dto;
//        }
        return dto.getBody();
    }

    @Override
    public Response deleteProperty(List<String> properties, List<String> stages, Boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/common-property/multi";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("properties", properties);
        bodyParam.put("stages",stages);
        bodyParam.put("isAdmin",isAdmin);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.DELETE, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }

    @Override
    public Response saveHistoryProperty(String startTime, String endTime, String note,String propertyId, String uuid) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/common-property/history";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("startTime", startTime);
        bodyParam.put("endTime", endTime);
        bodyParam.put("note", note);
        bodyParam.put("propertyId", propertyId);
        bodyParam.put("uuid", uuid);
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
    public Response updateHistoryProperty(String id  ,String startTime, String endTime, String note,String propertyId,  String uuid, List<String> stages, Boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/common-property/history";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("startTime", startTime);
        bodyParam.put("endTime", endTime);
        bodyParam.put("note", note);
        bodyParam.put("propertyId", propertyId);
        bodyParam.put("uuid", uuid);
        bodyParam.put("id", id);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        //Call rest api with url, method, payload, response entity
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.PUT, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }

    @Override
    public HistoryPropertyResponseDTO findHistory(String propertyId, Integer page, Integer size) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/common-property/history" + "/"+propertyId;
        String params = "page=" + page
                + "&size=" + size;
        urlRequest = urlRequest + "?" + params;
        HistoryPropertyResponseDTO dto = restTemplate.getForObject(urlRequest, HistoryPropertyResponseDTO.class);
//        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
//            return dto;
//        }
        return dto;
    }

    @Override
    public Response deleteHistoryProperty(List<String> ids) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/common-property/history/multi";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("ids", ids);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.DELETE, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
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
    public Response saveManual(EventManualDTO eventManualDTO) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/manual";
        //Payload request dbm.root.url//v1.0/dbm/management/mons/recognitions
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("siteId", eventManualDTO.getSiteId());
        bodyParam.put("imageUrl", eventManualDTO.getImageUrl());
        bodyParam.put("videoUrl", eventManualDTO.getVideoUrl());
        bodyParam.put("eventCode", eventManualDTO.getEventCode());
        bodyParam.put("sourceId", eventManualDTO.getSourceId());
        bodyParam.put("objectName", eventManualDTO.getObjectName());
        bodyParam.put("directionCode", eventManualDTO.getDirectionCode());
        bodyParam.put("uuid", eventManualDTO.getUuid());
        bodyParam.put("endSite", eventManualDTO.getEndSite());
        bodyParam.put("endTime", eventManualDTO.getEndTime());
        bodyParam.put("startTime", eventManualDTO.getStartTime());
        bodyParam.put("eventStatus", eventManualDTO.getEventStatus());
        bodyParam.put("manualEvent", eventManualDTO.isManualEvent());
        bodyParam.put("note",eventManualDTO.getNote());
        bodyParam.put("siteCorrect",eventManualDTO.getSiteCorrect());
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
        bodyParam.put("objectName",objectName);
//        bodyParam.put("endTime", endTime);
        bodyParam.put("manualEvent", true);
        bodyParam.put("note",note);
        bodyParam.put("siteCorrect",siteCorrect);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.PUT, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }

    @Override
    public Response updateEvent(String id, String eventCode, String note, String siteCorrect ,String imageUrl,String videoUrl, String uuid) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/base/";
        urlRequest = urlRequest + id;
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("note", note);
        bodyParam.put("eventCode", eventCode);
        bodyParam.put("siteCorrect", siteCorrect);
        bodyParam.put("videoUrl", videoUrl);
        bodyParam.put("imageUrl", imageUrl);
        bodyParam.put("manualEvent", true);
        bodyParam.put("uuid",uuid);
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
}
