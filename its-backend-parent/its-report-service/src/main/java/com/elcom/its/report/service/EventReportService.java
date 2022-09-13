package com.elcom.its.report.service;

import com.elcom.its.report.model.dto.Response;
import com.elcom.its.report.model.dto.ViolationDetailPagingDTO;
import com.elcom.its.report.model.dto.report.AggTroubleInfo;
import java.util.List;

/**
 * @author ducduongn
 */
public interface EventReportService {

    Response getEventFlowReport(String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd);

    Response getEventReportByChart(String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd);

    Response getEventReportBySite(String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd);

    Response getEventList(String fromDate, String toDate, String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Boolean isAdminBackEnd, String objectType, Boolean manual, String objectCreate, String eventType);

    Response getEventFanChart(String fromDate, String toDate, String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Boolean isAdminBackEnd, String objectType, Boolean manual, String objectCreate, String eventType, String valueLevelFilter);

    Response getEventLineChart(String fromDate, String toDate, String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Boolean isAdminBackEnd, String objectType, Boolean manual, String objectCreate, String eventType);

    Response getEventForDayReport(String startTime, String endTime, Integer page, Integer size);

    ViolationDetailPagingDTO getViolationList(String fromDate, String toDate, String stages, String filterObjectIds,
            String filterObjectType, Boolean isAdminBackEnd, String objectType, String eventType,
            Integer page, Integer size);

    List<AggTroubleInfo> getAggTroubleReport(String urlParam);
}
