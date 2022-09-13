package com.elcom.its.vmsboard.service.impl;

import com.elcom.its.utils.StringUtil;
import com.elcom.its.vmsboard.config.ApplicationConfig;
import com.elcom.its.vmsboard.dto.Response;
import com.elcom.its.vmsboard.model.ListUuid;
import com.elcom.its.vmsboard.model.NewsLetterTemplate;
import com.elcom.its.vmsboard.service.NewsLetterTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class NewsLetterTemplateServiceImpl implements NewsLetterTemplateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsLetterTemplateServiceImpl.class);
    private static final String URI = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/news-letter";

    @Autowired
    private RestTemplate restTemplate;
    @Override
    public Response getAllNewsLetterTemplate() {
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
    public Response getAllNewsLetterTemplate(Integer page, Integer size, String name, String typeBoard, String typeEvent, String search) {
        String params = "page=" + page + "&size=" + size;
        try {
            if (!StringUtil.isNullOrEmpty(name)) {
                params += "&name=" + URLEncoder.encode(name, "UTF-8");
            }
            if (!StringUtil.isNullOrEmpty(typeBoard)) {
                params += "&typeBoard=" + URLEncoder.encode(typeBoard, "UTF-8");
            }
            if (!StringUtil.isNullOrEmpty(typeEvent)) {
                params += "&typeEvent=" + URLEncoder.encode(typeEvent, "UTF-8");
            }
            if (!StringUtil.isNullOrEmpty(search)) {
                params += "&search=" + URLEncoder.encode(search, "UTF-8");
            }

        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/news-letter?" + params + "&sort=name";
        LOGGER.info("urlRequest : {}", urlRequest);
        Response routePaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find Category by catType from DBM: {}", routePaginationDTO);
        return routePaginationDTO;
    }

    @Override
    public Response getNewsLetterTemplateById(String id) {
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
    public Response saveNewsLetterTemplate(NewsLetterTemplate newsLetterTemplate) {
        HttpEntity<NewsLetterTemplate> requestBody = new HttpEntity<>(newsLetterTemplate, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateNewsLetterTemplate(NewsLetterTemplate newsLetterTemplate) {
        HttpEntity<NewsLetterTemplate> requestBody = new HttpEntity<>(newsLetterTemplate, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + newsLetterTemplate.getId(), HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteNewsLetterTemplate(String route) {
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + route, HttpMethod.DELETE,null, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response multiDelete(ListUuid uuid) {
        HttpEntity<ListUuid> requestBody = new HttpEntity<>(uuid, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/multi-delete", HttpMethod.DELETE, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }
}
