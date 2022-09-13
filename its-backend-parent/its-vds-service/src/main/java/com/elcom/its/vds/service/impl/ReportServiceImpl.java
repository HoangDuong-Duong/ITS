package com.elcom.its.vds.service.impl;

import com.elcom.its.vds.config.ApplicationConfig;
import com.elcom.its.vds.model.dto.Response;
import com.elcom.its.vds.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.UnsupportedEncodingException;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    private static final String URI = ApplicationConfig.ITS_ROOT_URL;

    @Autowired
    private RestTemplate restTemplate;

    public static String getParams(String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd)
            throws UnsupportedEncodingException {
        String params = null;
        params = "filterObjectType=" + filterObjectType
                + "&filterTimeLevel=" + filterTimeLevel
                + "&filterObjectIds=" + filterObjectIds
                + "&isAdminBackEnd=" + isAdminBackEnd;
        return params;
    }

    @Override
    public Response getEventFlowReport(String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd) {
        String params = null;
        try {
            params = getParams(filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/report/event/vds?" + params + "&forVds=true";
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get event reports from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getEventReportByChart(String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd) {
        String params = null;
        try {
            params = getParams(filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/report/event/vds/chart?" + params + "&forVds=true";
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get event reports by chart from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getEventReportBySite(String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd) {
        String params = null;
        try {
            params = getParams(filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/report/event/vds/site?" + params + "&forVds=true";
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get event reports by site from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getTrafficFlowReport(String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd) {
        String params = null;
        try {
            params = getParams(filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/report/traffic-flow?" + params;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get traffic-flow reports from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getTrafficFlowReportByChart(String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd) {
        String params = null;
        try {
            params = getParams(filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/report/traffic-flow/chart?" + params;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get traffic-flow reports by chart from ITS core: {}", response);
        return response;
    }

    @Override
    public Response getTrafficFlowReportBySite(String filterObjectIds, String filterObjectType, String filterTimeLevel, Integer isAdminBackEnd) {
        String params = null;
        try {
            params = getParams(filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = URI + "/v1.0/its/report/traffic-flow/vds/site?" + params;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("Get traffic-flow reports by site from ITS core: {}", response);
        return response;
    }
}
