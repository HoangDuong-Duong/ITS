package com.elcom.its.vds.service;

import com.elcom.its.vds.model.dto.Response;

public interface ReportService {
    Response getEventFlowReport(String filterObjectIds,
                                String filterObjectType,
                                String filterTimeLevel,
                                Integer isAdminBackEnd);

    Response getEventReportByChart(String filterObjectIds,
                                   String filterObjectType,
                                   String filterTimeLevel,
                                   Integer isAdminBackEnd);

    Response getEventReportBySite(String filterObjectIds,
                                  String filterObjectType,
                                  String filterTimeLevel,
                                  Integer isAdminBackEnd);

    Response getTrafficFlowReport(String filterObjectIds,
                                String filterObjectType,
                                String filterTimeLevel,
                                Integer isAdminBackEnd);

    Response getTrafficFlowReportByChart(String filterObjectIds,
                                   String filterObjectType,
                                   String filterTimeLevel,
                                   Integer isAdminBackEnd);

    Response getTrafficFlowReportBySite(String filterObjectIds,
                                  String filterObjectType,
                                  String filterTimeLevel,
                                  Integer isAdminBackEnd);

}
