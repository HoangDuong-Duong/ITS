package com.elcom.its.vds.service.impl;

import com.elcom.its.vds.config.ApplicationConfig;
import com.elcom.its.vds.model.ListUuid;
import com.elcom.its.vds.model.Stretch;
import com.elcom.its.vds.model.dto.Response;
import com.elcom.its.vds.model.dto.StretchDTO;
import com.elcom.its.vds.model.dto.StretchDetailDTO;
import com.elcom.its.vds.model.dto.StretchPaginationDTO;
import com.elcom.its.vds.service.StretchService;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class StretchServiceImpl implements StretchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StretchServiceImpl.class);
    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/stage";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getAllStretch() {
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
    public Response getAllStretch(Integer page, Integer size, String search) {
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
        String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/stage?" + params;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response stretchPaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find Category by catType from DBM: {}", stretchPaginationDTO);
        return stretchPaginationDTO;
    }

    @Override
    public Response getStretchById(String id) {
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
    public Response deleteStretch(String stretch) {
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + stretch, HttpMethod.DELETE,null, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteMultiStage(ListUuid stage) {
        HttpEntity<ListUuid> requestBody = new HttpEntity<>(stage, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/multi-delete", HttpMethod.DELETE, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response filterStretch(Integer page, Integer size, String id, String name, String siteStart, String siteEnd) {
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

        String urlRequest = URI + "/filter?" + param;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response stretchPaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find Category by catType from DBM: {}", stretchPaginationDTO);
        return stretchPaginationDTO;
    }
}
