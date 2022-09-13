package com.elcom.its.management.service.impl;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.*;
import com.elcom.its.management.service.HotlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Service
public class HotlineServiceImpl implements HotlineService {
    @Autowired
    RestTemplate restTemplate;

    private static final String URI = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/hotline";

    @Override
    public Response saveHotline(List<HotlineDTO> eventInfoDTO) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC+7"));
        HttpEntity<List<HotlineDTO>> requestBody = new HttpEntity<>(eventInfoDTO, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateHotline(HotlineDTO hotlineDTO, String id) {
        HttpEntity<HotlineDTO> requestBody = new HttpEntity<>(hotlineDTO, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + id, HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response delete(List<String> ids) {
        String urlRequest = URI;
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("ids", ids);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(URI, HttpMethod.DELETE, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }

    @Override
    public HotlineResponseDTO getListReport(String startDate, String endDate) {
        final String uri = URI+"/report/list"+"?startDate="+startDate+"&endDate="+endDate;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<HotlineResponseDTO> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, HotlineResponseDTO.class);
        HotlineResponseDTO response = result.getBody();
        return response;
    }

    @Override
    public HotlineReportResponseDTO getReport(String startDate, String endDate) {
        final String uri = URI+"/report"+"?startDate="+startDate+"&endDate="+endDate;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<HotlineReportResponseDTO> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, HotlineReportResponseDTO.class);
        HotlineReportResponseDTO response = result.getBody();
        return response;
    }

}
