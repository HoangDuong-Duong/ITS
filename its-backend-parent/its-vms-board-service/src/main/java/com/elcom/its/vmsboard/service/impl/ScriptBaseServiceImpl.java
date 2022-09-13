package com.elcom.its.vmsboard.service.impl;

import com.elcom.its.utils.StringUtil;
import com.elcom.its.vmsboard.config.ApplicationConfig;
import com.elcom.its.vmsboard.dto.Response;
import com.elcom.its.vmsboard.model.ListUuid;
import com.elcom.its.vmsboard.model.ScriptBase;
import com.elcom.its.vmsboard.service.ScriptBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class ScriptBaseServiceImpl implements ScriptBaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VmsBoardServiceImpl.class);
    private static final String URI = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/vms-board/base";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getAllScriptBase(String startDate, String endDate, String name, Integer status, String vmsIds) {
        String params = "";
        try {
            if (!StringUtil.isNullOrEmpty(startDate)) {
                params += "&startDate=" + startDate;
            }
            if (!StringUtil.isNullOrEmpty(endDate)) {
                params += "&endDate=" + endDate;
            }
            if (!StringUtil.isNullOrEmpty(name)) {
                params += "&name=" + URLEncoder.encode(name, "UTF-8");
            }
            if (status != null) {
                params += "&status=" + status;
            }
            if (vmsIds != null) {
                params += "&vmsIds=" + vmsIds;
            }

        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/vms-board/base?" + params;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response routePaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find vms-board by id from itscore: {}", routePaginationDTO);
        return routePaginationDTO;
    }

    @Override
    public Response activeScript(String id, Integer status) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/vms-board/base/active?id="+id+"&status="+status;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response routePaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find vms-board by id from itscore: {}", routePaginationDTO);
        return routePaginationDTO;
    }

    @Override
    public Response getDetailScriptBase(String id) {
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
    public Response save(ScriptBase scriptBase) {

        HttpEntity<ScriptBase> requestBody = new HttpEntity<>(scriptBase, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response update(ScriptBase scriptBase) {

        HttpEntity<ScriptBase> requestBody = new HttpEntity<>(scriptBase, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + scriptBase.getId(), HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response delete(String sc) {
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + sc, HttpMethod.DELETE, null, Response.class);
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
