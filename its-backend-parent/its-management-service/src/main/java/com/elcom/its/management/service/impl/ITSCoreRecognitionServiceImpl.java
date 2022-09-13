/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service.impl;

import com.elcom.its.management.dto.*;
import com.elcom.its.management.service.ITSRecognitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import com.elcom.its.management.config.ApplicationConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Admin
 */
@Service
public class ITSCoreRecognitionServiceImpl implements ITSRecognitionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ITSCoreRecognitionServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public RecognitionPlateResponseDTO findRecognition(List<String> stages,String fromDate, String toDate,
                                                       String plate, String vehicleType, String filterObjectType, List<String> filterObjectIds,
                                                       String brand, String color, Integer page, Integer size, Boolean isAdmin, Boolean distinctPlate) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/recognition";
        //Payload request dbm.root.url//v1.0/dbm/management/mons/recognitions
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("filterObjectType", filterObjectType);
        bodyParam.put("filterObjectIds", filterObjectIds);
        bodyParam.put("fromDate", fromDate);
        bodyParam.put("toDate", toDate);
        bodyParam.put("page", page);
        bodyParam.put("size", size);
        bodyParam.put("plate", plate);
        bodyParam.put("vehicleType", vehicleType);
        bodyParam.put("brand", brand);
        bodyParam.put("color", color);
        bodyParam.put("isAdmin", isAdmin);
        bodyParam.put("stages",stages);
        bodyParam.put("distinctPlate",distinctPlate);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        //Call rest api with url, method, payload, response entity
        HttpEntity<RecognitionPlateResponseDTO> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, RecognitionPlateResponseDTO.class);
        RecognitionPlateResponseDTO dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }

    @Override
    public RecognitionPlateResponseDTO findHistory(List<String> stages, String fromDate, String toDate,
                                                   String plate, String vehicleType, String filterObjectType, List<String> filterObjectIds,
                                                   String brand, String color, Integer page, Integer size, Boolean isAdmin, Boolean extract) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/recognition/history";
        //Payload request dbm.root.url//v1.0/dbm/management/mons/recognitions
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("filterObjectType", filterObjectType);
        bodyParam.put("filterObjectIds", filterObjectIds);
        bodyParam.put("fromDate", fromDate);
        bodyParam.put("toDate", toDate);
        bodyParam.put("page", page);
        bodyParam.put("size", size);
        bodyParam.put("plate", plate);
        bodyParam.put("vehicleType", vehicleType);
        bodyParam.put("brand", brand);
        bodyParam.put("color", color);
        bodyParam.put("stages",stages);
        bodyParam.put("isAdmin", isAdmin);
        bodyParam.put("extract",extract);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        //Call rest api with url, method, payload, response entity
        HttpEntity<RecognitionPlateResponseDTO> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, RecognitionPlateResponseDTO.class);
        RecognitionPlateResponseDTO dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }

    @Override
    public RecognitionStatisticResponseDTO findRecognitionStatistic(List<String> stages, String fromDate, String toDate, String plate, String vehicleType, String filterObjectType, List<String> filterObjectIds, String brand, String color, Integer page, Integer size, Boolean isAdmin) {
        RecognitionPlateResponseDTO response = this.findHistory(stages, fromDate,
                toDate, plate, vehicleType, filterObjectType, filterObjectIds, brand, color,
                page, size, true,true);

        if (response.getData() != null
                && (response.getStatus() == HttpStatus.OK.value() || response.getStatus() == HttpStatus.CREATED.value())) {
            Map<String, RecognitionStatisticDTO> recognitionStatisticDTOMap = new HashMap<>();

            LOGGER.info("List recog: {}", response.getData());

            for (RecognitionPlateDTO recognitionPlateDTO : response.getData()) {
                if (!recognitionStatisticDTOMap.containsKey(recognitionPlateDTO.getSite().getSiteId())) {
                    RecognitionStatisticDTO recognitionStatistic =
                            RecognitionStatisticDTO.builder()
                                    .count(1)
                                    .startTime(recognitionPlateDTO.getStartTime())
                                    .plate(recognitionPlateDTO.getPlate())
                                    .site(recognitionPlateDTO.getSite())
                                    .build();
                    recognitionStatisticDTOMap.put(recognitionPlateDTO.getSite().getSiteId(), recognitionStatistic);
                } else {
                    RecognitionStatisticDTO item = recognitionStatisticDTOMap.get(recognitionPlateDTO.getSite().getSiteId());
                    int count = item.getCount();
                    item.setCount(count + 1);
                }
            }
            RecognitionStatisticResponseDTO responseMessage =
                    new RecognitionStatisticResponseDTO(
                            response.getStatus(),
                            response.getMessage(),
                            recognitionStatisticDTOMap.values().stream().collect(Collectors.toList()));
            return responseMessage;
        }

        return null;
    }

    @Override
    public RecognitionPlateCorrectDTO correctData(String id, CorrectRecognitionInfoRequest correctRecognitionInfoRequest) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/recognition/" + id ;
        HttpEntity<CorrectRecognitionInfoRequest> requestEntity = new HttpEntity<>(correctRecognitionInfoRequest);
        HttpEntity<RecognitionPlateCorrectDTO> response = restTemplate.exchange(urlRequest, HttpMethod.PUT, requestEntity, RecognitionPlateCorrectDTO.class);
        RecognitionPlateCorrectDTO dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }

    @Override
    public Response deleteRecognitionFromDBM(String id) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/recognition/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.DELETE, requestBody, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }

    @Override
    public Response detailRecognition(String id) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/recognition/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.GET, requestBody, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }

    @Override
    public Response deleteMultiRecognition(List<String> recognitionIdList) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/recognition/multi-delete";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("recognitionIdList", recognitionIdList);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.DELETE, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }

    @Override
    public Response getStageMultiEvent(List<String> recognitionIds) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/recognition/stage";
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("recognitionIds", recognitionIds);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(bodyParam);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.POST, requestEntity, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return null;
    }
}
