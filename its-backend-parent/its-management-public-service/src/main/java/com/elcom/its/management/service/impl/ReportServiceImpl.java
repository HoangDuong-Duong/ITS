package com.elcom.its.management.service.impl;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.Response;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.management.service.ReportService;
import com.elcom.its.message.RequestMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author ducduongn
 */
@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final String URI = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/report";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Override
    public Response getReportMetrics(String stages, String filterObjectType, String filterTimeLevel, String filterObjectIds, Integer isAdminBackEnd) {
        String params = "filterObjectType=" + filterObjectType + "&filterTimeLevel=" + filterTimeLevel + "&filterObjectIds=" + filterObjectIds + "&isAdminBackEnd=" + isAdminBackEnd;
        String urlRequest = URI + "/by-cam" + "?" + params;
        if (stages != null) {
            urlRequest += "&stages=" + stages;
        }
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }

    @Override
    public Response getSiteTrafficStatusHistory(String filterTimeLevel, String filterObjectIds, Integer isAdminBackEnd) {
        String params = "filterTimeLevel=" + filterTimeLevel + "&filterObjectIds=" + filterObjectIds + "&isAdminBackEnd=" + isAdminBackEnd;
        String urlRequest = URI + "/site-traffic-status-history" + "?" + params;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }

    @Override
    public Response getAllShift(String month) {
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("GET");
        rbacRpcRequest.setRequestPath("/shift/internal");
        rbacRpcRequest.setBodyParam(null);
        rbacRpcRequest.setUrlParam(month);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService("its_shift_rpc_exchange", "its_shift_rpc_queue", "its_shift_rpc", rbacRpcRequest.toJsonString());
        Response response = null;
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                response = mapper.readValue(result, Response.class);
                String dataMessage = mapper.writeValueAsString(response.getData());
                response = mapper.readValue(dataMessage, Response.class);
            } catch (JsonProcessingException ex) {
                LOGGER.info("Lỗi parse json khi gọi user service verify: " + ex);
                return null;
            }
        } else {
            return null;
        }
        return response;
    }

    @Override
    public Response getDailyReport(String startTime, String endTime) {
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("GET");
        rbacRpcRequest.setRequestPath("/shift/daily-report/internal");
        rbacRpcRequest.setBodyParam(null);
        rbacRpcRequest.setUrlParam("startTime="+startTime + "&endTime="+endTime);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService("its_shift_rpc_exchange", "its_shift_rpc_queue", "its_shift_rpc", rbacRpcRequest.toJsonString());
        Response response = null;
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                response = mapper.readValue(result, Response.class);
                String dataMessage = mapper.writeValueAsString(response.getData());
                response = mapper.readValue(dataMessage, Response.class);
            } catch (JsonProcessingException ex) {
                LOGGER.info("Lỗi parse json khi gọi user service verify: " + ex);
                return null;
            }
        } else {
            return null;
        }
        return response;
    }

    @Override
    public Response getTrafficReport(String startTime, String endTime) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/traffic-flow/report-day?startTime=" +startTime + "&endTime="+endTime;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }

    @Override
    public Response getEventReport(String startTime, String endTime, Integer page, Integer size) {
        final String uri = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/day-report?page="+page+"&size="+size+"&startTime=" +startTime + "&endTime="+endTime ;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Response response = restTemplate.getForObject(uri, Response.class);
        return response;
    }
}
