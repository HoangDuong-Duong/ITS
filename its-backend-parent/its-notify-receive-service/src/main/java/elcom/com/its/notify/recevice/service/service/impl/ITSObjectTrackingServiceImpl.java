/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.service.impl;

import com.elcom.its.utils.StringUtil;
import elcom.com.its.notify.recevice.service.config.ApplicationConfig;
import elcom.com.its.notify.recevice.service.model.dto.ObjectTrackingDTO;
import elcom.com.its.notify.recevice.service.model.dto.ObjectTrackingResponse;
import elcom.com.its.notify.recevice.service.model.dto.ObjectTrackingResponseList;
import elcom.com.its.notify.recevice.service.model.dto.QueueNameResponse;
import elcom.com.its.notify.recevice.service.service.ITSObjectTrackingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 *
 * @author Admin
 */
@Service
public class ITSObjectTrackingServiceImpl implements ITSObjectTrackingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ITSObjectTrackingServiceImpl.class);
    private static final String URI = ApplicationConfig.ITSCORE_ROOT_URL + "/v1.0/its/management/object-tracking";
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ObjectTrackingResponseList findObjectTrackingAll(Integer page, Integer size, String sort, String fromDate, String toDate,
                                                            String identification, String infoObject, String model, String reason, String objectType, String typeList, String filterObjectType, List<String> filterObjectIds) {
        final String uri = URI + "?";
        String params = null;
        String identificationEncode = "";
        try {
            if (!StringUtil.isNullOrEmpty(identification)) {
                identificationEncode = URLEncoder.encode(identification, "UTF-8");
            }
            params = "&fromDate=" + fromDate
                    + "&toDate=" + toDate
                    + "&infoObject=" + infoObject
                    + "&model=" + model
                    + "&identification=" + identificationEncode
                    + "&reason=" + reason
                    + "&objectType=" + objectType
                    + "&typeList=" + typeList
                    + "&page=" + page
                    + "&size=" + size
                    + "&sort=" + sort
                    + "&filterObjectType" + filterObjectType
                    + "filterObjectIds" + filterObjectIds;
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        LOGGER.info("url + params => {}", uri + params);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<ObjectTrackingResponseList> result = restTemplate.exchange(uri + params, HttpMethod.GET, requestBody, ObjectTrackingResponseList.class);
        ObjectTrackingResponseList response = result.getBody();
        return response;
    }

    @Override
    public ObjectTrackingResponse findByIdentification(String id) {
        final String uri = URI + "/identification/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<ObjectTrackingResponse> result = restTemplate.exchange(uri + id, HttpMethod.GET, requestBody, ObjectTrackingResponse.class);
        ObjectTrackingResponse response = result.getBody();
        return response;
    }

    @Override
    public ObjectTrackingResponse findById(String id) {
        final String uri = URI + "/id/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<ObjectTrackingResponse> result = restTemplate.exchange(uri + id, HttpMethod.GET, requestBody, ObjectTrackingResponse.class);
        ObjectTrackingResponse response = result.getBody();
        return response;
    }

    @Override
    public ObjectTrackingResponse createObjectTrackingFromDBM(ObjectTrackingDTO track) {
        final String uri = URI;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ObjectTrackingDTO> requestBody = new HttpEntity<>(track, headers);
        ResponseEntity<ObjectTrackingResponse> result = restTemplate.exchange(uri, HttpMethod.POST, requestBody, ObjectTrackingResponse.class);
        ObjectTrackingResponse response = result.getBody();
        return response;
    }

    @Override
    public void deleteObjectTrackingFromDBM(String identification) {
        final String uri = URI + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ObjectTrackingDTO> requestBody = new HttpEntity<>(headers);
        ResponseEntity<ObjectTrackingResponse> result = restTemplate.exchange(uri + identification, HttpMethod.DELETE, requestBody, ObjectTrackingResponse.class);
    }

//    @Override
//    public void deleteMultiObjectTrackingFromDBM(List<String> identificationObjectTrackList) {
//        final String uri = URI + "/multi-delete";
//        restTemplate.postForObject(uri, identificationObjectTrackList, Object.class);
//    }
    @Override
    public ObjectTrackingResponse updateObjectTrackingFromDBM(String id, ObjectTrackingDTO objectTrack) {
        final String uri = URI + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ObjectTrackingDTO> requestBody = new HttpEntity<>(objectTrack, headers);
        ResponseEntity<ObjectTrackingResponse> result = restTemplate.exchange(uri + id, HttpMethod.PUT, requestBody, ObjectTrackingResponse.class);
        ObjectTrackingResponse response = result.getBody();
        return response;
    }

    @Override
    public List<String> getAllQueueName() {
        final String uri = URI + "/rabbitmqQueueName";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<QueueNameResponse> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, QueueNameResponse.class);
        QueueNameResponse response = result.getBody();
        if (response != null) {
            return response.getData() == null ? null : response.getData();
        }
        return null;
    }

    @Override
    public ObjectTrackingResponseList findListByIdentification(String identification) {
        final String uri = URI + "/list/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<ObjectTrackingResponseList> result = restTemplate.exchange(uri + identification, HttpMethod.GET, requestBody, ObjectTrackingResponseList.class);
        ObjectTrackingResponseList response = result.getBody();
        return response;
    }

}
