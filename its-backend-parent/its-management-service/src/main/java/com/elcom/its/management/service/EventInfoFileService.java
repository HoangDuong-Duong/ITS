package com.elcom.its.management.service;

import com.elcom.its.management.dto.EventInfoDTO;
import com.elcom.its.management.dto.HistoryDisplayDTO;

public interface EventInfoFileService {
    EventInfoDTO createAccidentReportFile(String startDate, String endDate,String textHeader,String uuid);
    EventInfoDTO createEventReportFile(Integer page, Integer size,String startDate, String endDate,String textHeader,String uuid);
    String createHistoryDisplayFile(String startDate, String endDate, String vmsId, String uuid);
    String createActionStatusFile(String startDate, String endDate, String uuid);
}
