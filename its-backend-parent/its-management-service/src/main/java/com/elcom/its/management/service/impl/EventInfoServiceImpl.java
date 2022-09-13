package com.elcom.its.management.service.impl;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.*;
import com.elcom.its.management.service.EventInfoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class EventInfoServiceImpl implements EventInfoService {
    @Autowired
    RestTemplate restTemplate;

    private static final String URI = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/info";

    @Override
    public Response saveEventInfo(EventInfoDTO eventInfoDTO) {
        HttpEntity<EventInfoDTO> requestBody = new HttpEntity<>(eventInfoDTO, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateEventInfo(EventInfoDTO eventInfoDTO, String id) {
        HttpEntity<EventInfoDTO> requestBody = new HttpEntity<>(eventInfoDTO, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + id, HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getEventInfoById(String id) {
        final String uri = URI + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + id, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getEventInfoByEventId(String eventId) {
        final String uri = URI + "/eventId/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + eventId, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public EventInfoPage getAllEventInfo(Integer size, Integer page, String startDate, String endDate) {
        final String uri = URI+"?size="+size+"&page="+page+"&startDate="+startDate+"&endDate="+endDate;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<EventInfoPage> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, EventInfoPage.class);
        EventInfoPage response = result.getBody();
        return response;
    }

    @Override
    public EventInfoResponseDTO getDataReportEventInfo(String startDate, String endDate) {
        final String uri = URI+"/report?startDate="+startDate+"&endDate="+endDate;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<EventInfoResponseDTO> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, EventInfoResponseDTO.class);
//        ObjectMapper mapper = new ObjectMapper();
//        EventInfoResponseDTO attributeProductDTOS = mapper.convertValue(result.getBody(), new TypeReference<AccidentReportResponseDTO>() {
//        });
        return result.getBody();
    }

    @Override
    public AccidentReportResponseDTO getDataReport(String startDate, String endDate) {
        final String uri = URI+"/report/table?startDate="+startDate+"&endDate="+endDate;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        ObjectMapper mapper = new ObjectMapper();
        AccidentReportResponseDTO attributeProductDTOS = mapper.convertValue(result.getBody(), new TypeReference<AccidentReportResponseDTO>() {
        });
        return attributeProductDTOS;
    }



    @Override
    public EventInfoResponse getByEventId(String eventId) {
        final String uri = URI + "/eventId/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<EventInfoResponse> result = restTemplate.exchange(uri + eventId, HttpMethod.GET, requestBody, EventInfoResponse.class);
        EventInfoResponse response = result.getBody();
        return response;
    }

    @Override
    public List<HistoryDisplayScript> getHistory(String startDate, String endDate, String vmsId) {
        final String uri = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/vms-board/history?startTime="+startDate +"&endTime="+endDate + "&vmsId=" + vmsId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Response result = restTemplate.getForObject(uri, Response.class);
        ObjectMapper mapper = new ObjectMapper();
        List<HistoryDisplayScript> attributeProductDTOS = mapper.convertValue(result.getData(), new TypeReference<List<HistoryDisplayScript>>() {
        });
        return attributeProductDTOS;
    }
}
