package com.elcom.its.tollstation.service.impl;

import com.elcom.its.tollstation.config.ApplicationConfig;
import com.elcom.its.tollstation.dto.*;
import com.elcom.its.tollstation.service.TollStationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TollStationServiceImpl implements TollStationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TollStationService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public TollStationResponseDTO getTollStation(String stages, Boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/toll-station";
        String params = "isAdmin=" + isAdmin;
        if (stages != null) {
            params += "&stages=" + stages;
        }else {
            params += "&stages=" ;
        }
        urlRequest = urlRequest + "?" + params;
        TollStationResponseDTO dto = restTemplate.getForObject(urlRequest, TollStationResponseDTO.class);
        return dto;
    }

    @Override
    public LanesResponseDTO reportLaneStatus(String tollStationId,String stage, Boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/toll-station/lanes";
        String params = "tollStationId=" + tollStationId;
        params += "&isAdmin=" + isAdmin;
        if (stage != null) {
            params += "&stages=" + stage;
        }else {
            params += "&stages=" ;
        }
        urlRequest = urlRequest + "?" + params;
        LanesResponseDTO dto = restTemplate.getForObject(urlRequest, LanesResponseDTO.class);
        return dto;
    }

    @Override
    public Response reportLine(String tollStationId, String fromDate, String toDate, String levelFilterByTime,String stage, Boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/toll-station/traffic-flow/report-line";
        String params = "tollStationId=" + tollStationId;
        params += "&fromDate=" + fromDate;
        params += "&toDate=" + toDate;
        params += "&levelFilterByTime=" + levelFilterByTime;
        params += "&isAdmin=" + isAdmin;
        if (stage != null) {
            params += "&stages=" + stage;
        }else {
            params += "&stages=" ;
        }
        urlRequest = urlRequest + "?" + params;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        return dto;
    }

    @Override
    public Response report(String tollStationId, String fromDate, String toDate,String stage, Boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/toll-station/traffic-flow/report";
        String params = "tollStationId=" + tollStationId;
        params += "&fromDate=" + fromDate;
        params += "&toDate=" + toDate;
        params += "&isAdmin=" + isAdmin;
        if (stage != null) {
            params += "&stages=" + stage;
        }else {
            params += "&stages=" ;
        }
        urlRequest = urlRequest + "?" + params;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        return dto;
    }

    @Override
    public Response reportLane(String tollStationId, String fromDate, String toDate,String stage, Boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/toll-station/traffic-flow/report-lanes";
        String params = "tollStationId=" + tollStationId;
        params += "&fromDate=" + fromDate;
        params += "&toDate=" + toDate;
        params += "&levelFilterByTime=day" ;
        params += "&isAdmin=" + isAdmin;
        if (stage != null) {
            params += "&stages=" + stage;
        }else {
            params += "&stages=" ;
        }
        urlRequest = urlRequest + "?" + params;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        return dto;
    }

    @Override
    public LanesHistoryResponseDTO getHistoryCloseLane(String tollStationId, String fromDate, String toDate, String stage, Boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/toll-station/lane-action-history";
        String params = "tollStationId=" + tollStationId;
        params += "&fromDate=" + fromDate;
        params += "&toDate=" + toDate;
        params += "&isAdmin=" + isAdmin;
        if (stage != null) {
            params += "&stages=" + stage;
        }else {
            params += "&stages=" ;
        }
        urlRequest = urlRequest + "?" + params;
        LanesHistoryResponseDTO dto = restTemplate.getForObject(urlRequest, LanesHistoryResponseDTO.class);
        return dto;
    }

    @Override
    public List<TollStationDTO> getTollStationByDirectionCode(String directionCode, String stage, Boolean isAdmin) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/toll-station/direction-code";
        String params = "directionCode=" + directionCode;
        params += "&isAdmin=" + isAdmin;
        if (stage != null) {
            params += "&stages=" + stage;
        }else {
            params += "&stages=" ;
        }
        urlRequest = urlRequest + "?" + params;
        TollStationResponseDTO dto = restTemplate.getForObject(urlRequest, TollStationResponseDTO.class);
        return dto.getData();
    }

    @Override
    public Response saveBot(BotDTO botDTO) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/toll-station";
        HttpEntity<BotDTO> requestBody = new HttpEntity<>(botDTO, null);
        ResponseEntity<Response> result = restTemplate.exchange(urlRequest, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateBot(BotDTO botDTO, String id) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/toll-station";
        HttpEntity<BotDTO> requestBody = new HttpEntity<>(botDTO, null);
        ResponseEntity<Response> result = restTemplate.exchange(urlRequest + "/" + id, HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteBot(String id) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/toll-station";
        ResponseEntity<Response> result = restTemplate.exchange(urlRequest + "/" + id, HttpMethod.DELETE,null, Response.class);
        Response response = result.getBody();
        return response;
    }
}
