/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.report.worker;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.report.job.ViolationExportExcel;
import com.elcom.its.report.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.report.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.report.model.dto.Response;
import com.elcom.its.report.model.dto.UploadResponseDTO;
import com.elcom.its.report.model.dto.ViolationDetailPagingDTO;
import com.elcom.its.report.service.EventReportService;
import com.elcom.its.utils.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
public class ExportViolationWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportViolationWorker.class);

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private EventReportService eventReportService;

    public void processViolationExporterJob(String json) {
        Workbook workbook = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            Map<String, String> params = mapper.readValue(json, Map.class);
            if (params != null) {
                String filterObjectType = params.get("filterObjectType");
                String filterObjectIds = params.get("filterObjectIds");
                String startTime = params.get("startTime");
                String endTime = params.get("endTime");
                String objectType = params.get("objectType");
                String eventCode = params.get("eventCode");
                String isAdminBackEnd = params.get("isAdminBackEnd");
                String stages = params.get("stages");
                String userId = params.get("userId");

                ViolationDetailPagingDTO response = eventReportService.getViolationList(startTime,
                        endTime, stages, filterObjectIds, filterObjectType, Boolean.valueOf(isAdminBackEnd),
                        objectType, eventCode, 0, 50000);
                if (response != null && response.getData() != null) {
                    int rowIndex = 0;
                    DateFormat df1 = new SimpleDateFormat("dd_MM_yyyy");
                    String startDate = startTime != null ? df1.format(DateUtil.getDateTime(startTime, "yyyy-MM-dd HH:mm:ss")) : "";
                    String toDate = endTime != null ? df1.format(DateUtil.getDateTime(endTime, "yyyy-MM-dd HH:mm:ss")) : "";
                    String filterDateType = "Ngày";
                    String excelFilePath = "Báo_Cáo_Vi_Phạm_Giao_Thông_" + filterDateType + "_" + startDate + "_Đến_" + toDate + "_" + System.currentTimeMillis() + ".xlsx";

                    workbook = ViolationExportExcel.getWorkbook(excelFilePath);
                    // Create sheet
                    Sheet sheet = workbook.createSheet("Báo_Cáo_Vi_Phạm_Giao_Thông"); // Create sheet with sheet name
                    // Write header
                    ViolationExportExcel.writeHeader(sheet, rowIndex);

                    //Write data
                    rowIndex = 1;
                    rowIndex = ViolationExportExcel.writeExcel(response.getData(), excelFilePath, rowIndex, sheet);

                    File path = new File(excelFilePath);
                    FileOutputStream os = new FileOutputStream(path);
                    workbook.write(os);

                    //Upload file 
                    String link = uploadFile(path);

                    //Send notify
                    Map<String, Object> param = putBodyParam(userId, link);
                    handleSendNotifyToDevice(param);
                } else {
                    LOGGER.info("No data for export violation");
                }
            } else {
                LOGGER.info("No param for export violation");
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("processViolationExporterJob >>> {}", ExceptionUtils.getStackTrace(e));
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private String uploadFile(File path) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(path));
        body.add("keepFileName", true);
        body.add("localUpload", true);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UploadResponseDTO> response = restTemplate.postForEntity(RabbitMQProperties.UPLOAD_URL, requestEntity, UploadResponseDTO.class);
        if (response != null && response.getBody() != null && response.getBody().getData() != null) {
            path.delete();
            return response.getBody().getData().getFileDownloadUri();
        }
        return null;
    }

    private Map<String, Object> putBodyParam(String userId, String url) throws JsonProcessingException {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 5);
        bodyParam.put("content", url);
        bodyParam.put("url", url);
        bodyParam.put("title", "Thực hiện xuất dữ liệu báo cáo vi phạm giao thông");
        bodyParam.put("objectType", "export-violation");
        bodyParam.put("objectUuid", "");
        bodyParam.put("userId", userId);
        return bodyParam;
    }

    private void handleSendNotifyToDevice(Map<String, Object> bodyParam) {
        try {
            RequestMessage rbacRpcRequest = new RequestMessage();
            rbacRpcRequest.setRequestMethod("POST");
            rbacRpcRequest.setRequestPath(RabbitMQProperties.ITS_NOTIFY_RPC_URL);
            rbacRpcRequest.setBodyParam(bodyParam);
            rbacRpcRequest.setUrlParam(null);
            rbacRpcRequest.setHeaderParam(null);
            rbacRpcRequest.setVersion(ResourcePath.VERSION);
            String result = rabbitMQClient.callRpcService(RabbitMQProperties.NOTIFY_RPC_EXCHANGE,
                    RabbitMQProperties.NOTIFY_RPC_QUEUE, RabbitMQProperties.NOTIFY_RPC_KEY,
                    rbacRpcRequest.toJsonString());
            LOGGER.info("send notify export xls link - result: " + result);
            if (result != null) {
                ObjectMapper mapper = new ObjectMapper();
                Response resultResponse = null;
                try {
                    resultResponse = mapper.readValue(result, Response.class);
                    if (resultResponse.getStatus() != HttpStatus.OK.value()) {
                        LOGGER.info("Error send notify to from ExportViolationWorker service");
                    }
                } catch (Exception ex) {
                    LOGGER.info("Error parse json in ExportViolationWorker: " + ex.toString());
                }
            }
        } catch (Exception ex) {
            LOGGER.info("Error parse json in ExportViolationWorker service: " + ex.toString());
        }
    }
}
