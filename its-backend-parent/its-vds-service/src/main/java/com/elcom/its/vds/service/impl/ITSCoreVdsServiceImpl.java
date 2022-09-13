/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service.impl;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.vds.config.ApplicationConfig;
import com.elcom.its.vds.constant.Constant;
import com.elcom.its.vds.controller.VdsEventController;
import com.elcom.its.vds.enums.CameraLayoutType;
import com.elcom.its.vds.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.vds.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.vds.model.Vds;
import com.elcom.its.vds.model.dto.AuthorizationResponseDTO;
import com.elcom.its.vds.model.dto.EventResponseDTO;
import com.elcom.its.vds.model.dto.Response;
import com.elcom.its.vds.model.dto.ResponseDTO;
import com.elcom.its.vds.service.ITSCoreVdsService;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
public class ITSCoreVdsServiceImpl implements ITSCoreVdsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ITSCoreVdsService.class);

    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Override
    public Response getAggEventTypeByVds(Vds vds, String filterTimeLevel) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date filterDate = getDateFromFilterTimeLevel(filterTimeLevel);
        String startDate = dateFormat.format(filterDate);
        String endDate = dateFormat.format(new Date());
        String urlRequest = URI + "/report/event/event-by-type?cameraId=" + vds.getCameraId()
                + "&startDate=" + startDate + "&endDate=" + endDate
                + "&violationEventCode=" + String.join(",", Constant.VDS_VIOLATION_CODE)
                + "&securityEventCode=" + String.join(",", Constant.VDS_SECURITY_CODE);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(urlRequest, HttpMethod.GET, requestBody, Response.class);
        return result.getBody();
    }

    @Override
    public Response getTrafficflowDataByVds(Vds vds, String filterTimeLevel) {
        Date filterDate = getDateFromFilterTimeLevel(filterTimeLevel);
        String urlRequest = URI + "/management/traffic-flow/data-by-camera?cameraId=" + vds.getCameraId()
                + "&filterTimeLevel=" + filterTimeLevel;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(urlRequest, HttpMethod.GET, requestBody, Response.class);
        return result.getBody();
    }

    private Date getDateFromFilterTimeLevel(String filterTimeLevel) {
        LocalDateTime localDate = LocalDate.now().atStartOfDay();
        if (filterTimeLevel.equals("hour")) {
            localDate = LocalDate.now().atTime(LocalDateTime.now().getHour(), 00, 00);
        } else if (filterTimeLevel.equals("day")) {
            localDate = LocalDate.now().atStartOfDay();
        } else if (filterTimeLevel.equals("week")) {
            localDate = LocalDate.now().with(DayOfWeek.MONDAY).atTime(LocalTime.of(00, 00, 00));
        } else if (filterTimeLevel.equals("month")) {
            localDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 01).atTime(LocalTime.of(00, 00, 00));
        } else if (filterTimeLevel.equals("year")) {
            localDate = LocalDate.of(LocalDate.now().getYear(), 01, 01).atTime(LocalTime.of(00, 00, 00));
        }
        return Date.from(localDate
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    @Override
    public Response getListEventByVds(Vds vds, int page, int size, String filterTimeLevel) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date filterDate = getDateFromFilterTimeLevel(filterTimeLevel);
        String startDate = dateFormat.format(filterDate);
        String endDate = dateFormat.format(new Date());
        String urlRequest = URI + "/management/event/camera?cameraId=" + vds.getCameraId()
                + "&page=" + page + "&size=" + size + "&startDate=" + startDate + "&endDate=" + endDate;
        List<String> eventCodeList = Constant.VDS_VIOLATION_CODE;
        if (vds.getLayoutType() == CameraLayoutType.LAYOUT_GSAN.layoutType()) {
            eventCodeList = Constant.VDS_SECURITY_CODE;
        }
        urlRequest += "&eventCode=" + String.join(",", eventCodeList);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(urlRequest, HttpMethod.GET, requestBody, Response.class);
        return result.getBody();
    }

    @Override
    public EventResponseDTO findEventPage(String stages, String fromDate, String toDate,
            String filterObjectType, String filterObjectIds, String objectName, String eventCode,
            Integer eventStatus, String directionCode, String plate, Boolean reportStatus, Integer page, Integer size, Boolean isAdmin) {
        String urlRequest = URI + "/management/event";
        String params = "fromDate=" + fromDate
                + "&toDate=" + toDate
                + "&page=" + page
                + "&filterObjectType=" + filterObjectType
                + "&size=" + size
                + "&filterObjectIds=" + filterObjectIds
                + "&objectName=" + objectName
                + "&eventCode=" + eventCode
                + "&directionCode=" + directionCode
                + "&isAdmin=" + isAdmin
                + "&eventStatus=" + eventStatus
                + "&getVdsEvent=true"
                + "&plate=" + (plate != null ? plate : "");
        if (stages != null) {
            params += "&stages=" + stages;
        }
        if (reportStatus != null) {
            params += "&reportStatus=" + reportStatus;
        }
        urlRequest = urlRequest + "?" + params;
        EventResponseDTO dto = restTemplate.getForObject(urlRequest, EventResponseDTO.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }

    @Override
    public String downloadZipImageAndVideoViolation(String key, String startTime, List<String> listOfFileNames) throws IOException {
        // Lay token tu id service
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("username", "admin");
        bodyParam.put("password", "Elcom@123");
        String result = getTokenFromIdService(bodyParam);

        File path = new File(key + ".zip");
        FileOutputStream fos = new FileOutputStream(path);

        ZipOutputStream zipOut = new ZipOutputStream(fos);
        for (String srcFile : listOfFileNames) {
            File fileToZip = new File(srcFile);
            ApplicationContext appContext = new ClassPathXmlApplicationContext();
            Resource resource = appContext.getResource(srcFile);
            InputStream fis = resource.getInputStream();
//                FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();
        fos.close();

        //Upload file
        String link = uploadFile(path, result);
        LOGGER.info(link);

        return link;
    }

    private String uploadFile(File path, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", token);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(path));
        body.add("keepFileName", true);
        body.add("localUpload", true);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseDTO> response = restTemplate.postForEntity(RabbitMQProperties.UPLOAD_URL, requestEntity, ResponseDTO.class);
        if (response != null && response.getBody() != null && response.getBody().getData() != null) {
            path.delete();
            return response.getBody().getData().getFileDownloadUri();
        }
        return null;
    }

    private String getTokenFromIdService(Map<String, Object> bodyParam) {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.setRequestMethod("POST");
        requestMessage.setRequestPath("/v1.0/user/login");
        requestMessage.setBodyParam(bodyParam);
        requestMessage.setHeaderParam(null);
        requestMessage.setVersion(ResourcePath.VERSION);
        requestMessage.setPathParam("");

        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, requestMessage.toJsonString());
        LOGGER.info("Get token from ID Service - result" + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    Object data = resultResponse.getData().getData();
                    Map<String, Object> mapUser = mapper.convertValue(data, new TypeReference<Map<String, Object>>() {
                    });
                    return mapUser.get("accessToken").toString();
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return result;
    }

}
