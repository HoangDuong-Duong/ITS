package com.elcom.its.config.service.impl;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.model.ObjectTracking;
import com.elcom.its.config.model.dto.*;
import com.elcom.its.config.service.ObjectTrackingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author ducduongn
 */
@Service
public class ObjectTrackingServiceImpl implements ObjectTrackingService {

    private Logger LOGGER = LoggerFactory.getLogger(ObjectTrackingServiceImpl.class);

    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/object-tracking";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<ObjectTracking> getAll() {
        List<ObjectTracking> objectTrackingList = null;
        String urlRequest = URI;
        ObjectTrackingPaginationDTOMesage mesage = restTemplate.getForObject(urlRequest, ObjectTrackingPaginationDTOMesage.class);
        LOGGER.info("Get object tracking list from ITS_CORE: {}", mesage);
        if (mesage != null && mesage.getStatus() == HttpStatus.OK.value()) {
            objectTrackingList = mesage.getData();
        }
        return objectTrackingList;
    }

    @Override
    public Response getById(String id) {
        final String uri = URI + "/";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + id, HttpMethod.GET, requestBody, Response.class);

        LOGGER.info("Object tracking: " + result.getBody());

        return result.getBody();
    }

    @Override
    public ObjectTrackingPaginationDTOMesage getAll(Integer page, Integer size, String search,
            String brand, String reason, String vehicleType, String recognitionFromDate,
            String recognitionToDate, String processFromDate, String processToDate,
            String createFromDate, String createToDate) {
        String params = null;
        try {
            //params = Utils.getParams(page, size, search);
            params = "page=" + page + "&size=" + size
                    + "&search=" + (search != null ? URLEncoder.encode(search, StandardCharsets.UTF_8) : "")
                    + "&brand=" + (brand != null ? brand : "")
                    + "&reason=" + (reason != null ? reason : "")
                    + "&vehicleType=" + (vehicleType != null ? vehicleType : "")
                    + "&recognitionFromDate=" + (recognitionFromDate != null ? recognitionFromDate : "")
                    + "&recognitionToDate=" + (recognitionToDate != null ? recognitionToDate : "")
                    + "&processFromDate=" + (processFromDate != null ? processFromDate : "")
                    + "&processToDate=" + (processToDate != null ? processToDate : "")
                    + "&createFromDate=" + (createFromDate != null ? createFromDate : "")
                    + "&createToDate=" + (createToDate != null ? createToDate : "");
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }

        String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/object-tracking?" + params;
        LOGGER.info("urlRequest: {}", urlRequest);
        ObjectTrackingPaginationDTOMesage objectTrackingPaginationDTOMesage = restTemplate.getForObject(urlRequest, ObjectTrackingPaginationDTOMesage.class);
        LOGGER.info("find Object tracking by search query from DBM: {}", objectTrackingPaginationDTOMesage);
        return objectTrackingPaginationDTOMesage;
    }

    @Override
    public Response createObjectTracking(ObjectTrackingCreateUpdateDto objectTracking) {
        HttpEntity<ObjectTrackingCreateUpdateDto> requestBody = new HttpEntity<>(objectTracking, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateObjectTracking(ObjectTrackingCreateUpdateDto objectTracking, String id) {
        HttpEntity<ObjectTrackingCreateUpdateDto> requestBody = new HttpEntity<>(objectTracking, null);
        ResponseEntity<Response> result
                = restTemplate.exchange(URI + "/" + id, HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteObjectTracking(String id) {
        ResponseEntity<Response> result
                = restTemplate.exchange(URI + "/" + id, HttpMethod.DELETE, null, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteMultipleObjectTracking(List<String> listObjectTrackingIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestBody = new HttpEntity<>(listObjectTrackingIds, headers);
        LOGGER.info("objectTrackingIds: " + requestBody);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/delete-multi", HttpMethod.DELETE, requestBody, Response.class);
        return result.getBody();
    }

    @Override
    public Response findByIdentificationList(List<String> identificationList) {
        String urlRequest = URI + "/by-identification";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestBody = new HttpEntity<>(identificationList, headers);
        ResponseEntity<Response> result = restTemplate.exchange(urlRequest, HttpMethod.POST, requestBody, Response.class);
        return result.getBody();
    }

    @Override
    public Response findById(String id) {
        final String uri = URI + "/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteUnitDataInObjectTracking(List<String> unitIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestBody = new HttpEntity<>(unitIds, headers);
        LOGGER.info("objectTrackingIds: " + requestBody);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/units", HttpMethod.DELETE, requestBody, Response.class);
        return result.getBody();
    }

    @Override
    public Response updateObjectTrackingProcessed(ObjectTrackingProcessDTO objectTracking, String id) {
        HttpEntity<ObjectTrackingProcessDTO> requestBody = new HttpEntity<>(objectTracking, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/processed/" + id, HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }
}
