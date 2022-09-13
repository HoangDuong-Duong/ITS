package com.elcom.its.report.service;

import com.elcom.its.report.model.dto.Response;

import java.util.Date;

/**
 * @author ducduongn
 */
public interface TrafficFlowReportService {

    Response getTrafficFlowReport(String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd);

    Response getTrafficFlowReportList(String fromDate, String toDate, String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Boolean isAdminBackEnd, String objectType);

    Response getTrafficFlowReportFanChart(String fromDate, String toDate, String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Boolean isAdminBackEnd, String objectType, String valueLevelFilter);

    Response getTrafficFlowReportLineChart(String fromDate, String toDate, String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Boolean isAdminBackEnd, String objectType);

    Response getTrafficFlowReportByChart(String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd);

    Response getTrafficFlowReportBySite(String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd);
}
