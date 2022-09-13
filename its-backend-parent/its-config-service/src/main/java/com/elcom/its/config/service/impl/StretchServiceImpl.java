package com.elcom.its.config.service.impl;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.Stretch;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.StretchService;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class StretchServiceImpl implements StretchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StretchServiceImpl.class);
    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/stage";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getAllStretch(String siteList, Boolean isAdmin) {
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
    public String findBySite(String siteId) {
        String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/stage/site";
        urlRequest = urlRequest + "/" + siteId;
        Response dto = restTemplate.getForObject(urlRequest, Response.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return (String) dto.getData();
        }
        return null;
    }

    @Override
    public Response getStretchByListCodes(String listCode) {
        String params = "stageCodes="+listCode;
        String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/stage/list?" + params;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response stretchPaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find Category by catType from DBM: {}", stretchPaginationDTO);
        return stretchPaginationDTO;
    }

    @Override
    public Response getAllStretch(String siteList, Integer page, Integer size, String search, Boolean isAdmin) {
        String params = null;
        if (!StringUtil.isNullOrEmpty(search)) {
            params = "page=" + page + "&size=" + size + "&search=" + URLEncoder.encode(search, StandardCharsets.UTF_8) + "&sort=id" + "&stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
        } else {
            params = "page=" + page + "&size=" + size + "&sort=id" + "&stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
        }

        String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/stage?" + params;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response stretchPaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find Category by catType from DBM: {}", stretchPaginationDTO);
        return stretchPaginationDTO;
    }

    @Override
    public Response getStretchById(String siteList, String id, Boolean isAdmin) {
        String uri = URI + "/" + id + "?stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response saveStretch(Stretch stretch) {

        HttpEntity<Stretch> requestBody = new HttpEntity<>(stretch, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateStretch(Stretch stretch) {
        HttpEntity<Stretch> requestBody = new HttpEntity<>(stretch, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + stretch.getId(), HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteStretch(String siteList, String stretch, Boolean isAdmin) {
        String uri = URI + "/" + stretch + "?stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.DELETE, null, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteMultiStage(String siteList, ListUuid stage, Boolean isAdmin) {
        HttpEntity<ListUuid> requestBody = new HttpEntity<>(stage, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/multi-delete" + "?stageIdLs=" + siteList + "&isAdmin=" + isAdmin, HttpMethod.DELETE, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response filterStretch(String siteList, Integer page, Integer size, String id, String name, String siteStart, String siteEnd, Boolean isAdmin) {
        String param = "page=" + page + "&size=" + size;
        if (name != null) {
            param += "&name=" + name;
        }
        if (id != null) {
            param += "&id=" + id;
        }
        if (siteStart != null) {
            param += "&siteStart=" + siteStart;
        }
        if (siteEnd != null) {
            param += "&siteEnd=" + siteEnd;
        }

        String urlRequest = URI + "/filter" + "?stageIdLs=" + siteList + "&isAdmin=" + isAdmin + "&" + param;
        Response stretchPaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        return stretchPaginationDTO;
    }
}
