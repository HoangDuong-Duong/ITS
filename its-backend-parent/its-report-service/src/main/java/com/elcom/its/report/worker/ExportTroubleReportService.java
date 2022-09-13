/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.report.worker;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.report.job.TroubleReportExportExcel;
import com.elcom.its.report.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.report.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.report.model.dto.UploadResponseDTO;
import com.elcom.its.report.model.dto.report.AggTroubleInfo;
import com.elcom.its.report.service.EventReportService;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.elcom.its.report.model.dto.ExportTroubleReportRequest;

/**
 *
 * @author Admin
 */
@Service
public class ExportTroubleReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportTroubleReportService.class);

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private EventReportService eventReportService;

    public void processExport(ExportTroubleReportRequest exportRequest) {
        Workbook workbook = null;
        try {
            Map<String, String> urlMap = StringUtil.getUrlParamValues(exportRequest.getUrlParam());
            String month = urlMap.get("month");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_yy_MM_dd_HH_mm_ss");
            String excelFilePath = "So gio dam bao ATGT thang "+month + simpleDateFormat.format(new Date()) + ".xlsx";
            workbook = TroubleReportExportExcel.getWorkbook(excelFilePath);
            Sheet sheet = workbook.createSheet(month);
            int rowIndex = TroubleReportExportExcel.writeHeader(sheet, month);
            List<AggTroubleInfo> listAggTroubleInfos = eventReportService.getAggTroubleReport(exportRequest.getUrlParam());
            TroubleReportExportExcel.writeContent(sheet, listAggTroubleInfos, rowIndex);
            File path = new File(excelFilePath);
            FileOutputStream os = new FileOutputStream(path);
            workbook.write(os);
            String link = uploadFile(path);
            LOGGER.info("Link downLoad {}", link);
            sendNotifyToDevice(exportRequest.getUserId(), link, "export-file-monthly-trouble-report");
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

    private Map<String, Object> createNotifyBodyParam(String userId, String fileDownloadUrl, String objectType) throws JsonProcessingException, UnsupportedEncodingException {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 5);
        bodyParam.put("content", URLDecoder.decode(fileDownloadUrl, "UTF-8"));
        bodyParam.put("url", URLDecoder.decode(fileDownloadUrl, "UTF-8"));
        bodyParam.put("title", "Export file");
        bodyParam.put("objectType", objectType);
        bodyParam.put("objectUuid", "");
        bodyParam.put("userId", userId);
        return bodyParam;
    }

    private void sendNotifyToDevice(String userRequestId, String fileDownloadUrl, String objectType) {
        try {
            Map<String, Object> bodyParam = createNotifyBodyParam(userRequestId, fileDownloadUrl, objectType);
            RequestMessage rbacRpcRequest = new RequestMessage();
            rbacRpcRequest.setRequestMethod("POST");
            rbacRpcRequest.setRequestPath(RabbitMQProperties.ITS_NOTIFY_RPC_URL);
            rbacRpcRequest.setBodyParam(bodyParam);
            rbacRpcRequest.setUrlParam(null);
            rbacRpcRequest.setHeaderParam(null);
            rbacRpcRequest.setVersion(ResourcePath.VERSION);
            String result = rabbitMQClient.callRpcService(RabbitMQProperties.NOTIFY_RPC_EXCHANGE,
                    RabbitMQProperties.NOTIFY_RPC_QUEUE, RabbitMQProperties.NOTIFY_RPC_KEY, rbacRpcRequest.toJsonString());
            LOGGER.info("Result call notify {}", result);
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }
}
