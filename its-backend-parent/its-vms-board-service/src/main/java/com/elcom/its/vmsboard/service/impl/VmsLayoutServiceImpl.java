package com.elcom.its.vmsboard.service.impl;

import com.elcom.its.vmsboard.config.ApplicationConfig;
import com.elcom.its.vmsboard.dto.Response;
import com.elcom.its.vmsboard.model.ContentLayout;
import com.elcom.its.vmsboard.model.Layout;
import com.elcom.its.vmsboard.model.LayoutComponentAttribute;
import com.elcom.its.vmsboard.model.NewsLetterTemplate;
import com.elcom.its.vmsboard.service.VmsLayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class VmsLayoutServiceImpl implements VmsLayoutService {
    private static final String URI_content = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/vms-layout/content";
    private static final String URI_layout = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/vms-layout/layout";
    private static final String URI_layout_attribute = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/vms-layout/attribute";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getAllLayout(String id) {
        final String uri = URI_layout + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + id, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response createLayout(Layout layout) {
        HttpEntity<Layout> requestBody = new HttpEntity<>(layout, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI_layout, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteLayout(String id) {
        ResponseEntity<Response> result = restTemplate.exchange(URI_layout + "/" + id, HttpMethod.DELETE,null, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getAllContent(Integer siteLayout, Integer layoutId) {
        final String uri = URI_content + "?siteLayout="+siteLayout+"&layoutId="+layoutId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri , HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response createContent(ContentLayout contentLayout) {
        HttpEntity<ContentLayout> requestBody = new HttpEntity<>(contentLayout, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI_content, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteContent(String id) {
        ResponseEntity<Response> result = restTemplate.exchange(URI_content + "/" + id, HttpMethod.DELETE,null, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getAttributeInLayout(String layout) {
        final String uri = URI_layout_attribute + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + layout, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateAttributeInLayout(String layout, List<LayoutComponentAttribute> list) {
        HttpEntity<List<LayoutComponentAttribute>> requestBody = new HttpEntity<>(list, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI_layout_attribute + "/"+layout, HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }
}
