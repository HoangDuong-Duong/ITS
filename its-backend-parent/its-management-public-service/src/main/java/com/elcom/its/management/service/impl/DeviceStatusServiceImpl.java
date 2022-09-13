package com.elcom.its.management.service.impl;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.*;
import com.elcom.its.management.service.DeviceStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class DeviceStatusServiceImpl implements DeviceStatusService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceStatusServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public HistoryStatusPagingDTO historyStatus(String fromDate, String toDate, String type, String search, String stage,  Integer page, Integer size, Boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/device-status/history";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("fromDate", fromDate);
        bodyParam.put("toDate", toDate);
        bodyParam.put("page", page);
        bodyParam.put("size", size);
        bodyParam.put("type", type);
        bodyParam.put("isAdmin", isAdmin);
        bodyParam.put("search", search);
        if(stage!=null&& !stage.isEmpty())
            bodyParam.put("stages", Arrays.asList(stage.split(",")));
        else
            bodyParam.put("stages", null);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<HistoryStatusPagingDTO> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, HistoryStatusPagingDTO.class);
        return response.getBody();

    }

    @Override
    public HistoryStatusReportDTO reportStatus(String fromDate, String toDate, String type, String search, String stage,  Integer page, Integer size, Boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/device-status/report";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("fromDate", fromDate);
        bodyParam.put("toDate", toDate);
        bodyParam.put("page", page);
        bodyParam.put("size", size);
        bodyParam.put("type", type);
        bodyParam.put("isAdmin", isAdmin);
        bodyParam.put("search", search);
        if(stage!=null&& !stage.isEmpty())
            bodyParam.put("stages", Arrays.asList(stage.split(",")));
        else
            bodyParam.put("stages", null);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<HistoryStatusReportDTO> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, HistoryStatusReportDTO.class);
        return response.getBody();
    }

    @Override
    public HistoryStatusReportDTO reportStatusDisconnect(String fromDate, String toDate, String type, String search, String stage,  Integer page, Integer size, Boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/device-status/report/disconnect";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("fromDate", fromDate);
        bodyParam.put("toDate", toDate);
        bodyParam.put("page", page);
        bodyParam.put("size", size);
        bodyParam.put("type", type);
        bodyParam.put("isAdmin", isAdmin);
        bodyParam.put("search", search);
        if(stage!=null&& !stage.isEmpty())
            bodyParam.put("stages", Arrays.asList(stage.split(",")));
        else
            bodyParam.put("stages", null);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<HistoryStatusReportDTO> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, HistoryStatusReportDTO.class);
        return response.getBody();
    }

    @Override
    public Response saveHistoryStatus(String deviceId, String siteId, String type, String note, String startTime, String endTime, Integer status) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/device-status";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("deviceId", deviceId);
        bodyParam.put("siteId", siteId);
        bodyParam.put("type", type);
        bodyParam.put("note", note);
        bodyParam.put("endTime", startTime);
        bodyParam.put("startTime", endTime);
        bodyParam.put("status", status);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }

    @Override
    public Response updateHistoryStatus(String id, String deviceId, String siteId, String type, String note, String startTime, String endTime, Integer status) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/device-status";
        urlRequest = urlRequest + "/" + id;
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("deviceId", deviceId);
        bodyParam.put("siteId", siteId);
        bodyParam.put("type", type);
        bodyParam.put("note", note);
        bodyParam.put("endTime", startTime);
        bodyParam.put("startTime", endTime);
        bodyParam.put("status", status);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.PUT, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }
}
