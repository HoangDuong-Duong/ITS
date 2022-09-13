package com.elcom.its.config.service.impl;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.model.HistoryDisplayNewsCriteria;
import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.Pagination;
import com.elcom.its.config.model.VmsBoard;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.DistanceService;
import com.elcom.its.config.service.VmsBoardService;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class VmsBoardServiceImpl implements VmsBoardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceService.class);
    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/vms-board";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getAllVmsBoard() {
        final String uri = URI;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getAllVmsBoard(Integer page, Integer size, String search) {
        String params = null;
        try {
            if (!StringUtil.isNullOrEmpty(search)) {
                params = "page=" + page + "&size=" + size + "&search=" + URLEncoder.encode(search, "UTF-8") + "&sort=id";
            } else {
                params = "page=" + page + "&size=" + size + "&sort=id";
            }

        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/vms-board?" + params;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response routePaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find vms-board by id from itscore: {}", routePaginationDTO);
        return routePaginationDTO;
    }

    @Override
    public Response getDisplayScript(Integer page, Integer size, String keyword, String deviceId, String fromDate, String toDate) throws UnsupportedEncodingException {
        String params = "page=" + page + "&size=" + size + "&deviceId=" + URLEncoder.encode(deviceId, "UTF-8") + "&sort=createDate";
        try {
            if (!StringUtil.isNullOrEmpty(fromDate)) {
                params += "&fromDate=" + fromDate;
            }
            if(!StringUtil.isNullOrEmpty(toDate)){
                params += "&toDate=" + toDate;
            }

        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/vms-board/history?" + params;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response routePaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find vms-board by id from itscore: {}", routePaginationDTO);
        return routePaginationDTO;
    }

    @Override
    public Response getVmsBoardById(String id) {
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
    public Response saveVmsBoard(VmsBoard vmsBoard) {
        HttpEntity<VmsBoard> requestBody = new HttpEntity<>(vmsBoard, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateVmsBoard(VmsBoard vmsBoard) {
        HttpEntity<VmsBoard> requestBody = new HttpEntity<>(vmsBoard, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + vmsBoard.getId(), HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteVmsBoard(String vms) {
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + vms, HttpMethod.DELETE, null, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response multiDelete(ListUuid ids) {
        HttpEntity<ListUuid> requestBody = new HttpEntity<>(ids, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/multi-delete", HttpMethod.DELETE, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }
}
