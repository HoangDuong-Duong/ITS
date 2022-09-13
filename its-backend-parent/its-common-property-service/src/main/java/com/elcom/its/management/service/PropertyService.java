package com.elcom.its.management.service;

import com.elcom.its.management.dto.*;
import com.elcom.its.management.enums.EventStatus;
import com.elcom.its.management.model.EventFile;
import org.a.a.S;

import java.util.Date;
import java.util.List;

public interface PropertyService {

    Response saveListProperty(List<CommonProperty> commonPropertyList, String uuid, List<String> stages, Boolean isAdmin);

    Response saveListPropertyImport(List<CommonProperty> commonPropertyList, String uuid);

    Response updateListProperty(List<CommonProperty> commonPropertyList, String uuid);

    PropertyResponseDTO findProperty(String name, String position, List<Integer> types, List<Integer> status, String sort, Integer page, Integer size, List<String> stages, Boolean isAdmin,String site,String directionCode, String typeData);

    Response deleteProperty(List<String> properties,List<String> stages, Boolean isAdmin);

    Response saveHistoryProperty(String startTime, String endTime,String note, String propertyId, String uuid);

    Response updateHistoryProperty(String id , String startTime, String endTime, String note, String propertyId, String uuid, List<String> stages, Boolean isAdmin);

    HistoryPropertyResponseDTO findHistory(String propertyId, Integer page, Integer size);

    Response deleteHistoryProperty(List<String> ids);

    EventResponseDTO historyEvent(String parentId);

    Response getEvent(String parentId);
    
    EventDTO getEventByParentId(String parentId);

    EventFileResponseDTO getFileEvent(String parentId);

    Response saveManual(EventManualDTO eventManualDTO);



    Response getDetailEvent(String eventId, String startTime);

    Response getEventMap( String startTime,  String endTime, String stages, boolean isAdmin);

    Response updateViolation(String id, String sourceId, String objectType, Date startTime, Date endTime, Float speedOfVehicle, String laneId,
            String plate, String eventCode, String objectName, Integer eventStatus, String imageUrl,
            String videoUrl, String uuid);

    Response updateSecurity(String id, String sourceId, Date startTime, String laneId,
            String eventCode, Integer eventStatus, String imageUrl, String uuid);

    Response updateManual(String id, String sourceId, Date startTime,
            String eventCode, Integer eventStatus, String imageUrl, String uuid,
            String siteId, String directionCode, String videoUrl, String endSite, Date endTime, String note, String siteCorrect,String objectName);

    Response updateEvent(String id, String eventCode, String note, String siteCorrect,String imageUrl,  String videoUrl, String uuid );

    Response deleteEvent(String eventId, String startTime);

    Response deleteMultiEvent(List<String> eventIds);

    Response getMultiEvent(List<String> eventIds);

    Response updateStatus(String eventParentId, Date startTime, Integer status, String uuid);

    Response getStageViolation(String id, String startTime);

    Response getStageSecurity(String id, String startTime);

    Response getStageManual(String id, String startTime);

    Response updateEventStatus(String parentEventId, EventStatus eventStatus, String userId);

}
