package com.elcom.its.management.service;

import com.elcom.its.management.dto.*;
import com.elcom.its.management.enums.EventStatus;
import com.elcom.its.management.model.EventFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface EventService {

    EventResponseDTO findEventPage(String stages, String fromDate, String toDate,
            String filterObjectType, String filterObjectIds,
            String objectName, String eventCode, Integer eventStatus,
            String directionCode, String plate, String key, Integer page, Integer size, Boolean isAdmin, String manual);

    EventResponseDTO findEventPageCommon(List<String> stages, List<String> parentId, String fromDate, String toDate, String filterObjectType, List<String> filterObjectIds, String objectName, List<String> eventCode, Integer eventStatus, String directionCode, String plate, String key, Integer page, Integer size, Boolean isAdmin, String manual);

    EventResponseDTO historyEvent(String parentId);

    Response getEvent(String parentId);

    EventDTO getEventByParentId(String parentId);

    EventFileResponseDTO getFileEvent(String parentId);
    CategoryResponseDTO getPrinted(String printed);

    Response saveManual(EventManualDTO eventManualDTO);

    public Response getHistory(Map<String, String> headerParam, String pathParam);

    Response getDetailEvent(String eventId, String startTime);

    List<EventDTO> getListEventDTOByListParentIds(List<String> parentIds);

    Response getEventMap(String startTime, String endTime, String stages, boolean isAdmin);

    Response getEventOnStraightMap(String startTime, String endTime, String stages, boolean isAdmin);

    Response updateViolation(String id, String sourceId, String objectType, Date startTime, Date endTime, Float speedOfVehicle, String laneId,
            String plate, String eventCode, String objectName, Integer eventStatus, String imageUrl,
            String videoUrl, String uuid);

    Response updateSecurity(String id, String sourceId, Date startTime, String laneId,
            String eventCode, Integer eventStatus, String imageUrl, String uuid);

    Response updateManual(String id, EventManualDTO eventManualDTO);

    Response updateManual(String id, String sourceId, Date startTime,
            String eventCode, Integer eventStatus, String imageUrl, String uuid,
            String siteId, String directionCode, String videoUrl, String endSite, Date endTime, String note, String siteCorrect, String objectName);

    Response updateEvent(String id, String eventCode, String note, String siteCorrect, String imageUrl, String videoUrl, String uuid);

    Response deleteEvent(String eventId, String startTime);

    Response deleteMultiEvent(List<String> eventIds);

    Response getMultiEvent(List<String> eventIds);

    Response updateStatus(String eventParentId, Date startTime, Integer status, String uuid);

    Response getStageViolation(String id, String startTime);

    Response getStageSecurity(String id, String startTime);

    Response getStageManual(String id, String startTime);

    Response updateEventStatus(String parentEventId, EventStatus eventStatus, String userId);

    Response getTrafficJamDetailHistory(String startTime, String endTime, String eventId);

    Response createEventFile(EventFileDTO eventFile);
    ReportEventDailyResponse getReportDailyEvent(String startDate, String endDate);


    Response updateReportStatusEvent(ReportStatusEvent reportStatusEvent);

}
