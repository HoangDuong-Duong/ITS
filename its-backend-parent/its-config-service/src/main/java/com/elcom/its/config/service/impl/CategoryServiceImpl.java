package com.elcom.its.config.service.impl;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.model.ListId;
import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.Route;
import com.elcom.its.config.model.dto.Category;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.CategoryService;
import com.elcom.its.config.service.DistanceService;
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
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceService.class);
    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/category";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getAllCategory() {
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
    public Response getAllCategory(Integer page, Integer size, String search) {
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
        String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/category?" + params;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response routePaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find Category by catType from DBM: {}", routePaginationDTO);
        return routePaginationDTO;
    }

    @Override
    public Response getAllCategoryByType(String catType) {
        String params = null;
        try {
            if (!StringUtil.isNullOrEmpty(catType)) {
                params = "catType=" + URLEncoder.encode(catType, "UTF-8") + "&sort=id";
            }
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/category/filter?" + params;
        LOGGER.info("urlRequest: {}", urlRequest);
        Response categoryDto = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find all Category by catType: {}", categoryDto);
        return categoryDto;
    }

    @Override
    public Response getCategoryById(String id) {
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
    public Response saveCategory(Category category) {
        HttpEntity<Category> requestBody = new HttpEntity<>(category, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateCategory(Category category) {
        HttpEntity<Category> requestBody = new HttpEntity<>(category, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + category.getId(), HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteCategory(String category) {
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + category, HttpMethod.DELETE,null, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteMultiCategory(ListId category) {
        HttpEntity<ListId> requestBody = new HttpEntity<>(category, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/multi-delete", HttpMethod.DELETE, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

}
