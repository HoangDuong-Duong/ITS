/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.report.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.report.constant.Constant;
import com.elcom.its.report.model.dto.ABACResponseDTO;
import com.elcom.its.report.model.dto.AuthorizationResponseDTO;
import com.elcom.its.report.model.dto.ObjectTrackingPaginationDTOMesage;
import com.elcom.its.report.service.ObjectTrackingService;
import com.elcom.its.report.validator.ReportValidator;
import com.elcom.its.utils.StringUtil;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class ObjectTrackingController extends BaseController {

    @Autowired
    private ObjectTrackingService objectTrackingService;

    public ResponseMessage getObjectTrackingProcessed(Map<String, String> headerParam, String requestPath,
            String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    responseMessage = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValuesNoDecode(urlParam);
                    Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                    Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                    String processFromDate = params.get("processFromDate");
                    String processToDate = params.get("processToDate");

                    String validateData = new ReportValidator().validateObjectTrackingFilter(processFromDate, processToDate);
                    if (validateData != null) {
                        responseMessage = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        ObjectTrackingPaginationDTOMesage objectTrackingPaginationDTOMesage = objectTrackingService.findProcessedObjectTracking(
                                page, size, processFromDate, processToDate);
                        if (objectTrackingPaginationDTOMesage.getData() != null && objectTrackingPaginationDTOMesage.getStatus() == HttpStatus.OK.value()) {
                            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                                    HttpStatus.OK.toString(), objectTrackingPaginationDTOMesage.getData(), objectTrackingPaginationDTOMesage.getTotal()));
                        } else {
                            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                        }
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo xử lý phương tiện", null));
            }
        }
        return responseMessage;
    }
}
