package com.elcom.its.management.service;

import com.elcom.its.management.dto.AggJobByStatus;
import com.elcom.its.management.dto.EventJobDTO;
import com.elcom.its.management.dto.ReportEventJobDTO;
import com.elcom.its.management.dto.ReportOnlineStatusDTO;
import com.elcom.its.management.dto.ReportStatisticResponse;
import com.elcom.its.management.model.Job;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

public interface ReportJobService {

    List<ReportStatisticResponse> reportJobGroup(Date fromDate, Date toDate, List<String> groupIds);

    List<ReportStatisticResponse> reportJobStatus(Date fromDate, Date toDate);

    List<ReportEventJobDTO> reportJobEvent(Date startDate, Date endDate, String status, String eventCodes, List<String> groupId);

    Page<ReportOnlineStatusDTO> reportJobOnlineStatus(Date startTime, Date endTime, Pageable pageable) throws FileNotFoundException, JsonProcessingException;

    List<AggJobByStatus> getAggJobByStatus(String groupId, Date startDate, Date endDate);

}
