/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service.impl;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.shift.dto.ExportShiftReportRequest;
import com.elcom.its.shift.dto.NumberOfShiftByDay;
import com.elcom.its.shift.dto.Response;
import com.elcom.its.shift.dto.Stage;
import com.elcom.its.shift.dto.Unit;
import com.elcom.its.shift.dto.UserShiftGroupByUser;
import com.elcom.its.shift.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.shift.service.ExportFileService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.elcom.its.shift.export.*;
import com.elcom.its.shift.model.Shift;
import com.elcom.its.shift.repository.ShiftRepository;
import com.elcom.its.shift.service.UserShiftService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.elcom.its.shift.dto.UploadFileResponse;
import com.elcom.its.shift.dto.WeeklyShiftReportContent;
import com.elcom.its.shift.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.shift.model.UserShift;
import com.elcom.its.shift.service.ShiftService;
import com.elcom.its.shift.service.StageService;
import com.elcom.its.shift.service.UnitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Admin
 */
@Service
public class ExportFileImpl implements ExportFileService {

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private UserShiftService userShiftService;

    @Autowired
    private UnitService unitService;

    @Autowired
    private StageService stageService;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportFileImpl.class);

    @Override
    public void exportMonthlyShiftReport(ExportShiftReportRequest exportRequest) {
        try {
            String month = getMonthString(exportRequest.getStartTime());
            List<Shift> listAllShifts = shiftService.findAll(month);
            Unit unit = unitService.findById(exportRequest.getGroupCode());
            final String excelFilePath = "Bảng phân công ca trực tháng " + month + " - " + unit.getName() + new SimpleDateFormat("(HH_mm_ss)").format(new Date()) + ".xlsx";
            Workbook workbook = WriteMonthlyReportExcelFile.getWorkbook(excelFilePath);
            Sheet sheet = workbook.createSheet("Sheet1");
            int cellIndex = WriteMonthlyReportExcelFile.writeHeader(sheet, 0, exportRequest.getStartTime(), exportRequest.getEndTime(), listAllShifts.size(), unit);
            List<UserShift> listUserShift = userShiftService.
                    findByGroupCodeAndDayBetween(exportRequest.getStartTime(), exportRequest.getEndTime(), exportRequest.getGroupCode());
            List<UserShiftGroupByUser> listUserShiftGroupByUsers = userShiftService.transformToListUserShiftGroupByUser(listUserShift, month);
            List<NumberOfShiftByDay> listNumberOfShiftByDays = userShiftService.getNumberOfShiftByDay(listUserShift, month);

            int rowIndex = WriteMonthlyReportExcelFile.
                    writeContent(sheet, listUserShiftGroupByUsers, exportRequest.getStartTime(), exportRequest.getEndTime(), listAllShifts.size());
            rowIndex = WriteMonthlyReportExcelFile.writeContent(sheet, rowIndex, listAllShifts.size(), listNumberOfShiftByDays);

            WriteMonthlyReportExcelFile.writeFooter(sheet, rowIndex, cellIndex, listAllShifts.size());
            WriteMonthlyReportExcelFile.autosizeColumn(sheet, cellIndex + 1);

            java.io.File path = new File(excelFilePath);
            FileOutputStream os = new FileOutputStream(path);
            workbook.write(os);
            String linkDownloadFile = uploadFile(path);
            LOGGER.info("Link export {}", linkDownloadFile);
            sendNotifyToDevice(exportRequest.getUserRequest(), linkDownloadFile, "export-file-monthly-shift-report");
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
    }

    private String uploadFile(File path) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(path));
        body.add("keepFileName", true);
        body.add("localUpload ", true);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Response> response = restTemplate.postForEntity(RabbitMQProperties.UPLOAD_URL, requestEntity, Response.class);
        if (response != null && response.getBody() != null && response.getBody().getData() != null) {
            ObjectMapper mapper = new ObjectMapper();
            UploadFileResponse uploadFileResponse = mapper.convertValue(response.getBody().getData(), new TypeReference<UploadFileResponse>() {
            });
            path.delete();
            return uploadFileResponse.getFileDownloadUri() + "?cache=false";
        }
        return null;
    }

    private void sendNotifyToDevice(String userRequestId, String fileDownloadUrl, String objectType) {
        try {
            Map<String, Object> bodyParam = createNotifyBodyParam(userRequestId, fileDownloadUrl, objectType);
            RequestMessage rbacRpcRequest = new RequestMessage();
            rbacRpcRequest.setRequestMethod("POST");
            rbacRpcRequest.setRequestPath(RabbitMQProperties.its_NOTIFY_RPC_INTERNAL_SEND_URL);
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

    @Override
    public void exportWeeklyShiftReport(ExportShiftReportRequest exportRequest) {
        try {
            String month = getMonthString(exportRequest.getStartTime());
            List<Shift> listAllShifts = shiftService.findAll(month);
            Unit unit = unitService.findById(exportRequest.getGroupCode());
            List<Stage> listStages = stageService.findByStageCodes(unit.getLisOfStage());
            removeDuplicateStage(listStages);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyy");
            final String excelFilePath = "Bảng phân công ca trực tuần từ ngày "
                    + simpleDateFormat.format(exportRequest.getStartTime()) + " đến ngày "
                    + simpleDateFormat.format(exportRequest.getEndTime()) + " - " + unit.getName() + new SimpleDateFormat("(HH_mm_ss)").format(new Date()) + ".xlsx";
            Workbook workbook = WriteMonthlyReportExcelFile.getWorkbook(excelFilePath);
            Sheet sheet = workbook.createSheet("Sheet1");
            int rowIndex = WriteWeeklyReportExcelFile.writeHeader(sheet, unit, listStages, listAllShifts.size(), exportRequest.getStartTime(), exportRequest.getEndTime());
            List<WeeklyShiftReportContent> listWeeklyShiftReportContents = userShiftService.getWeeklyShiftReportContents(exportRequest.getStartTime(), exportRequest.getEndTime(), exportRequest.getGroupCode());
            WriteWeeklyReportExcelFile.writeContent(sheet, listAllShifts.size(), listWeeklyShiftReportContents, listStages, exportRequest.getStartTime());
            WriteWeeklyReportExcelFile.autosizeColumn(sheet, rowIndex + 1);
            java.io.File path = new File(excelFilePath);
            FileOutputStream os = new FileOutputStream(path);
            workbook.write(os);
            String linkDownloadFile = uploadFile(path);
            LOGGER.info("Link export {}", linkDownloadFile);
            sendNotifyToDevice(exportRequest.getUserRequest(), linkDownloadFile, "export-file-weekly-shift-report");
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
    }

    private void removeDuplicateStage(List<Stage> listStages) {
        Set<String> stageCodes = new HashSet<>();
        List<Stage> listRemoveStages = new ArrayList<>();
        for (Stage stage : listStages) {
            if (stageCodes.contains(stage.getCode())) {
                listRemoveStages.add(stage);
            } else {
                stageCodes.add(stage.getCode());
            }
        }
        listStages.removeAll(listRemoveStages);
        Collections.sort(listStages);
    }

    private int getMonth(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    private String getMonthString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
        return simpleDateFormat.format(date);
    }
}
