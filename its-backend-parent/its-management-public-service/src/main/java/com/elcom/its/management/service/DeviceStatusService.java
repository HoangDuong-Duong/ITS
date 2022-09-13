package com.elcom.its.management.service;

import com.elcom.its.management.dto.*;

public interface DeviceStatusService {
    HistoryStatusPagingDTO historyStatus(String fromDate, String toDate, String type,String search, String stage, Integer page, Integer size, Boolean isAdmin);
    HistoryStatusReportDTO reportStatus(String fromDate, String toDate, String type,String search, String stage, Integer page, Integer size, Boolean isAdmin);
    HistoryStatusReportDTO reportStatusDisconnect(String fromDate, String toDate, String type,String search, String stage, Integer page, Integer size, Boolean isAdmin);
    Response saveHistoryStatus(String deviceId, String siteId, String type, String note, String startTime, String endTime, Integer status);
    Response updateHistoryStatus(String id, String deviceId, String siteId, String type, String note, String startTime, String endTime, Integer status);
}
