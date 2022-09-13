package com.elcom.its.report.service.impl;

import com.elcom.its.report.config.ApplicationConfig;
import com.elcom.its.report.model.dto.Response;
import com.elcom.its.report.model.dto.ViolationDetailPagingDTO;
import com.elcom.its.report.model.dto.report.AggTroubleInfo;
import com.elcom.its.report.service.EventReportService;
import com.elcom.its.report.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author ducduongn
 */
@Service
public class EventReportServiceImpl implements EventReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrafficFlowReportServiceImpl.class);

    private static final String URI = ApplicationConfig.ITS_ROOT_URL;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getEventFlowReport(String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd) {
        String params = null;
        try {
            params = Utils.getParams(filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/report/event?" + params;
        if (stages != null) {
            urlRequest += "&stages=" + stages;
        }
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get event reports from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getEventReportByChart(String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd) {
        String params = null;
        try {
            params = Utils.getParams(filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/report/event/chart?" + params;
        if (stages != null) {
            urlRequest += "&stages=" + stages;
        }
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get event reports by chart from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getEventReportBySite(String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd) {
        String params = null;
        try {
            params = Utils.getParams(filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/report/event/site?" + params;
        if (stages != null) {
            urlRequest += "&stages=" + stages;
        }
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get event reports by site from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getEventList(String fromDate, String toDate, String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Boolean isAdminBackEnd, String objectType, Boolean manual, String objectCreate, String eventType) {
        String params = null;
        try {
            params = "fromDate=" + fromDate
                    + "&toDate=" + toDate
                    + "&filterObjectType=" + filterObjectType
                    + "&filterObjectIds=" + filterObjectIds
                    + "&objectType=" + objectType
                    + "&manual=" + manual
                    + "&objectCreate=" + objectCreate
                    + "&eventType=" + eventType
                    + "&levelFilterByTime=" + URLEncoder.encode(filterTimeLevel, "UTF-8")
                    + "&valueLevelFilter=" + ""
                    + "&isAdmin=" + isAdminBackEnd;
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/management/report/event?" + params;
        if (stages != null) {
            urlRequest += "&stages=" + stages;
        }
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get traffic-flow reports by chart from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getEventFanChart(String fromDate, String toDate, String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Boolean isAdminBackEnd, String objectType, Boolean manual, String objectCreate, String eventType, String valueLevelFilter) {
        String params = null;
        try {
            params = "fromDate=" + fromDate
                    + "&toDate=" + toDate
                    + "&filterObjectType=" + filterObjectType
                    + "&filterObjectIds=" + filterObjectIds
                    + "&objectType=" + objectType
                    + "&manual=" + manual
                    + "&objectCreate=" + objectCreate
                    + "&eventType=" + eventType
                    + "&levelFilterByTime=" + URLEncoder.encode(filterTimeLevel, "UTF-8")
                    + "&valueLevelFilter=" + valueLevelFilter
                    + "&isAdmin=" + isAdminBackEnd;
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/management/report/event/fan-chart?" + params;
        if (stages != null) {
            urlRequest += "&stages=" + stages;
        }
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get traffic-flow reports by chart from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getEventLineChart(String fromDate, String toDate, String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Boolean isAdminBackEnd, String objectType, Boolean manual, String objectCreate, String eventType) {
        String params = null;
        try {
            params = "fromDate=" + fromDate
                    + "&toDate=" + toDate
                    + "&filterObjectType=" + filterObjectType
                    + "&filterObjectIds=" + filterObjectIds
                    + "&objectType=" + objectType
                    + "&manual=" + manual
                    + "&objectCreate=" + objectCreate
                    + "&eventType=" + eventType
                    + "&levelFilterByTime=" + URLEncoder.encode(filterTimeLevel, "UTF-8")
                    + "&valueLevelFilter=" + ""
                    + "&isAdmin=" + isAdminBackEnd;
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/management/report/event/line-chart?" + params;
        if (stages != null) {
            urlRequest += "&stages=" + stages;
        }
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get traffic-flow reports by chart from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getEventForDayReport(String startTime, String endTime, Integer page, Integer size) {
        final String uri = URI + "/v1.0/its/management/event/day-report?page="+page+"&size"+size+"&startTime=" + startTime + "&endTime=" + endTime;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Response response = restTemplate.getForObject(uri, Response.class);
        return response;
    }

    @Override
    public ViolationDetailPagingDTO getViolationList(String fromDate, String toDate, String stages,
            String filterObjectIds, String filterObjectType, Boolean isAdminBackEnd,
            String objectType, String eventType, Integer page, Integer size) {
        String params = null;
        try {
            params = "fromDate=" + fromDate
                    + "&toDate=" + toDate
                    + "&filterObjectType=" + filterObjectType
                    + "&filterObjectIds=" + filterObjectIds
                    + "&objectName=" + (objectType != null ? objectType : "")
                    + "&eventCode=" + (eventType != null ? eventType : "")
                    + "&isAdmin=" + isAdminBackEnd
                    + "&stages=" + stages
                    + "&page=" + page + "&pagination.page=" + page
                    + "&size=" + size + "&pagination.size=" + size;
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
        final String uri = URI + "/v1.0/its/management/event/violation?" + params;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        ViolationDetailPagingDTO response = restTemplate.getForObject(uri, ViolationDetailPagingDTO.class);
        return response;
    }

    @Override
    public List<AggTroubleInfo> getAggTroubleReport(String urlParam) {
        String urlRequest = URI + "/v1.0/its/report/event/trouble?" + urlParam;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(response.getData(), new TypeReference<List<AggTroubleInfo>>() {
        });
    }
}
