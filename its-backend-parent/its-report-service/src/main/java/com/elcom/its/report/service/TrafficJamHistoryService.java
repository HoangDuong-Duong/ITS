package com.elcom.its.report.service;

import com.elcom.its.report.model.dto.Response;

public interface TrafficJamHistoryService {

    Response getTrafficJamReport(String urlParam,boolean adminBackend, String stageIds);

    Response getTrafficTimeLine(String urlParam,boolean adminBackend, String stageIds);

    Response getTrafficSort(String urlParam,boolean adminBackend, String stageIds);

    Response getTrafficJamMonitoringData(String urlParam, boolean adminBackend, String stageIds);

    Response getTrafficJamMonitoring(String urlParam, boolean adminBackend, String stageIds, Integer size, Integer page);

}
