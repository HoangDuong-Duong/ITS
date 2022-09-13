package com.elcom.its.management.service;

import com.elcom.its.management.dto.Response;

/**
 * @author ducduongn
 */
public interface ReportService {

    Response getReportMetrics(String stages, String filterObjectType, String filterTimeLevel, String filterObjectIds, Integer isAdminBackEnd);

    Response getSiteTrafficStatusHistory(String filterTimeLevel, String filterObjectIds, Integer isAdminBackEnd);

    Response getAllShift(String month);

    Response getDailyReport(String startTime, String endTime);

    Response getTrafficReport(String startTime, String endTime);

    Response getEventReport(String startTime, String endTime, Integer page, Integer size);
}
