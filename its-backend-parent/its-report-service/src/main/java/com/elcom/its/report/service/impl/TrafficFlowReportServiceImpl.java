package com.elcom.its.report.service.impl;

import com.elcom.its.report.config.ApplicationConfig;
import com.elcom.its.report.model.dto.Response;
import com.elcom.its.report.service.TrafficFlowReportService;
import com.elcom.its.report.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * @author ducduongn
 */
@Service
public class TrafficFlowReportServiceImpl implements TrafficFlowReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrafficFlowReportServiceImpl.class);

    private static final String URI = ApplicationConfig.ITS_ROOT_URL;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getTrafficFlowReport(String stages, String filterObjectIds, String filterObjectType,
            String filterTimeLevel, Integer isAdminBackEnd) {
        String params = null;
        try {
            params = Utils.getParams(filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/report/traffic-flow?" + params;
        if (stages != null) {
            urlRequest += "&stages=" + stages;
        }
        LOGGER.info("getTrafficFlowReport urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get traffic-flow reports from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getTrafficFlowReportList(String fromDate, String toDate, String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Boolean isAdminBackEnd, String objectType) {
        String params = null;
        try {
            params = "fromDate=" + fromDate
                    + "&toDate=" + toDate
                    + "&filterObjectType=" + filterObjectType
                    + "&filterObjectIds=" + filterObjectIds
                    + "&objectType=" + objectType
                    + "&levelFilterByTime=" + URLEncoder.encode(filterTimeLevel, "UTF-8")
                    + "&valueLevelFilter=" + ""
                    + "&isAdmin=" + isAdminBackEnd;
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/management/report/traffic-flow?" + params;
        if (stages != null) {
            urlRequest += "&stages=" + stages;
        }
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get traffic-flow reports by chart from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getTrafficFlowReportFanChart(String fromDate, String toDate, String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Boolean isAdminBackEnd, String objectType, String valueLevelFilter) {
        String params = null;
        try {
            params = "fromDate=" + fromDate
                    + "&toDate=" + toDate
                    + "&filterObjectType=" + filterObjectType
                    + "&filterObjectIds=" + filterObjectIds
                    + "&objectType=" + objectType
                    + "&levelFilterByTime=" + URLEncoder.encode(filterTimeLevel, "UTF-8")
                    + "&valueLevelFilter=" + valueLevelFilter
                    + "&isAdmin=" + isAdminBackEnd;
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/management/report/traffic-flow/fan-chart?" + params;
        if (stages != null) {
            urlRequest += "&stages=" + stages;
        }
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get traffic-flow reports by chart from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getTrafficFlowReportLineChart(String fromDate, String toDate, String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Boolean isAdminBackEnd, String objectType) {
        String params = null;
        try {
            params = "fromDate=" + fromDate
                    + "&toDate=" + toDate
                    + "&filterObjectType=" + filterObjectType
                    + "&filterObjectIds=" + filterObjectIds
                    + "&objectType=" + objectType
                    + "&levelFilterByTime=" + URLEncoder.encode(filterTimeLevel, "UTF-8")
                    + "&valueLevelFilter=" + ""
                    + "&isAdmin=" + isAdminBackEnd;
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/management/report/traffic-flow/line-chart?" + params;
        if (stages != null) {
            urlRequest += "&stages=" + stages;
        }
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get traffic-flow reports by chart from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getTrafficFlowReportByChart(String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd) {
        String params = null;
        try {
            params = Utils.getParams(filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/report/traffic-flow/chart?" + params;
        if (stages != null) {
            urlRequest += "&stages=" + stages;
        }
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get traffic-flow reports by chart from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getTrafficFlowReportBySite(String stages, String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd) {
        String params = null;
        try {
            params = Utils.getParams(filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/report/traffic-flow/site?" + params;
        if (stages != null) {
            urlRequest += "&stages=" + stages;
        }
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get traffic-flow reports by site from ITS core: {}", response);
        return response;
    }
}
