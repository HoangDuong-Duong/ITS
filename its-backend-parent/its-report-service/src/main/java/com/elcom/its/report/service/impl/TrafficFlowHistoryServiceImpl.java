package com.elcom.its.report.service.impl;

import com.elcom.its.report.config.ApplicationConfig;
import com.elcom.its.report.model.dto.Response;
import com.elcom.its.report.service.TrafficJamHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TrafficFlowHistoryServiceImpl implements TrafficJamHistoryService {

    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/traffic-jam";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getTrafficJamReport(String urlParam,boolean adminBackend, String stageIds) {
        if (adminBackend) {
            urlParam += "&adminBackend=" + true;
        } else {
            urlParam += "&adminBackend=" + false + "&stageIds=" + stageIds;
        }
        final String uri = URI+"/report" + "?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getTrafficTimeLine(String urlParam,boolean adminBackend, String stageIds) {
        if (adminBackend) {
            urlParam += "&adminBackend=" + true;
        } else {
            urlParam += "&adminBackend=" + false + "&stageIds=" + stageIds;
        }
        final String uri = URI+"/totalTimeTrafficJam" + "?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getTrafficSort(String urlParam,boolean adminBackend, String stageIds) {
        if (adminBackend) {
            urlParam += "&adminBackend=" + true;
        } else {
            urlParam += "&adminBackend=" + false + "&stageIds=" + stageIds;
        }
        final String uri = URI+"/trafficJamSort" + "?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getTrafficJamMonitoringData(String urlParam, boolean adminBackend, String stageIds) {
        if (adminBackend) {
            urlParam += "&adminBackend=" + true;
        } else {
            urlParam += "&adminBackend=" + false + "&stageIds=" + stageIds;
        }
        final String uri = URI+"/monitoring" + "?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getTrafficJamMonitoring(String urlParam, boolean adminBackend, String stageIds, Integer size, Integer page) {
        if (adminBackend) {
            urlParam += "&adminBackend=" + true + "&size="+ size + "&page="+page;
        } else {
            urlParam += "&adminBackend=" + false + "&stageIds=" + stageIds+ "&size="+ size + "&page="+page;
        }
        final String uri = URI+"/monitoring-all" + "?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }
}
