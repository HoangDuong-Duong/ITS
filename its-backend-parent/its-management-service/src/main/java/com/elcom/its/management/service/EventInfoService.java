package com.elcom.its.management.service;

import com.elcom.its.management.dto.*;
import com.groupdocs.conversion.internal.c.a.pd.Operator;

import java.util.List;

public interface EventInfoService {
    Response saveEventInfo(EventInfoDTO eventInfoDTO);
    Response updateEventInfo(EventInfoDTO eventInfoDTO, String id);
    Response getEventInfoById(String id);
    Response getEventInfoByEventId(String eventId);
    EventInfoPage getAllEventInfo(Integer size, Integer page, String startDate, String endDate);

    EventInfoResponseDTO getDataReportEventInfo(String startDate, String endDate);
    AccidentReportResponseDTO getDataReport(String startDate, String endDate);

    EventInfoResponse getByEventId(String eventId);
    List<HistoryDisplayScript> getHistory(String startDate, String endDate, String vmsId);
}
