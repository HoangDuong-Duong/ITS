/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.shift.dto.ABACResponseDTO;
import com.elcom.its.shift.dto.AuthorizationResponseDTO;
import com.elcom.its.shift.dto.ExportShiftReportRequest;
import com.elcom.its.shift.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.shift.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class ExportController extends BaseController {

    @Autowired
    private RabbitMQClient rabbitMQClient;

    public ResponseMessage exportWeeklyReport(String requestUrl, String method, Map<String, String> headerMap, String urlParam)
            throws ExecutionException, InterruptedException, ParseException, JsonProcessingException {
        ResponseMessage response;
        AuthorizationResponseDTO userDTO = authenToken(headerMap);
        if (userDTO == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", userDTO.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                ExportShiftReportRequest exportShiftReportRequest = createExportRequest(urlParam, "WEEKLY", userDTO.getUuid());
                callExportShiftReportWoker(exportShiftReportRequest);
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.toString()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xuất báo cáo ca trực ",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xuất báo cáo ca trực ", null));
            }
        }
        return response;
    }

    public ResponseMessage exportMonthlyReport(String requestUrl, String method, Map<String, String> headerMap, String urlParam)
            throws ExecutionException, InterruptedException, ParseException, JsonProcessingException {
        ResponseMessage response;
        AuthorizationResponseDTO userDTO = authenToken(headerMap);
        if (userDTO == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", userDTO.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                ExportShiftReportRequest exportShiftReportRequest = createExportRequest(urlParam, "MONTHLY", userDTO.getUuid());
                callExportShiftReportWoker(exportShiftReportRequest);
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.toString()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xuất báo cáo ca trực ",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xuất báo cáo ca trực ", null));
            }
        }
        return response;
    }

    private ExportShiftReportRequest createExportRequest(String urlParam, String reportType, String userId) throws ParseException {
        Map<String, String> urlMap = StringUtil.getUrlParamValues(urlParam);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startTime = dateFormat.parse(urlMap.get("startTime"));
        Date endTime = dateFormat.parse(urlMap.get("endTime"));
        String groupCode = urlMap.get("groupCode");
        return ExportShiftReportRequest.builder()
                .reportType(reportType)
                .startTime(startTime)
                .endTime(endTime)
                .groupCode(groupCode)
                .userRequest(userId)
                .build();
    }

    private void callExportShiftReportWoker(ExportShiftReportRequest exportShiftReportRequest) throws JsonProcessingException {
        rabbitMQClient.callWorkerService(RabbitMQProperties.SHIFT_EXPORT_WORKER_QUEUE, exportShiftReportRequest.toJsonString());
    }
}
