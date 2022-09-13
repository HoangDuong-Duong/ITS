package com.elcom.its.management.service;

import com.elcom.its.management.dto.EventJobDTO;

import java.util.Date;
import java.util.List;

public interface CalendarService {
    List<EventJobDTO> getMyJob(Date startDate, Date endDate, List<String> jobType, Integer priority, Integer status, List<String> siteIds, String eventCodes, String groupId, String uuid, Date expireStart, Date expireEnd, String key );
    List<EventJobDTO> getJobForReport(Date startDate, Date endDate, String eventCodes, String key);

}
