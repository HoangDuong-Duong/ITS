/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.elcom.its.vds.constant.Constant;
import com.elcom.its.vds.model.dto.ABACResponseDTO;
import com.elcom.its.vds.model.dto.AuthorizationResponseDTO;
import com.elcom.its.vds.model.dto.EventResponseDTO;
import com.elcom.its.vds.service.ITSCoreVdsService;
import com.elcom.its.vds.service.VdsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class VdsEventController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VdsEventController.class);

    @Autowired
    private VdsService vdsService;

    @Autowired
    private ITSCoreVdsService itsCoreVdsService;

    public ResponseMessage findVdsEvent(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                int currentPage = Integer.parseInt(params.get("page"));
                int rowsPerPage = Integer.parseInt(params.get("size"));
                String startDate = params.get("fromDate");
                String endDate = params.get("toDate");
                String objectName = params.get("objectName");
                String eventCode = params.get("eventCode");
                String directionCode = params.get("directionCode");
                String filterObjectType = params.get("filterObjectType");
                List<String> filterObjectIds = params.get("filterObjectIds") != null ? Arrays.asList(params.get("filterObjectIds").split(",")) : null;
                int eventStatus = !params.get("eventStatus").isEmpty() ? Integer.parseInt(params.get("eventStatus")) : -1;
                String plate = params.get("keyword");
                Boolean reportStatus = Boolean.valueOf(params.get("reportStatus"));

                boolean validEventCode = true;
                if (!StringUtil.isNullOrEmpty(eventCode)) {
                    String[] eventCodeList = eventCode.split(",");
                    if (eventCodeList != null && eventCodeList.length > 0) {
                        for (String tmpEventCode : eventCodeList) {
                            if (!Constant.VDS_EVENT_CODE.contains(tmpEventCode)) {
                                validEventCode = false;
                                break;
                            }
                        }
                    }
                }
                if (!validEventCode) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                            "eventCode chỉ nằm một trong các mã : " + String.join(", ", Constant.VDS_EVENT_CODE), null));
                } else {
                    if (StringUtil.isNullOrEmpty(eventCode)) {
                        eventCode = String.join(", ", Constant.VDS_EVENT_CODE);
                    }

                    EventResponseDTO responseDTO = null;
                    String itsFilterObjectIds = filterObjectIds != null ? String.join(",", filterObjectIds) : null;
                    if (itsFilterObjectIds == null) {
                        //Site
                        List<String> siteIds = vdsService.findAllSiteIds().parallelStream().filter(siteId -> siteId != null).collect(Collectors.toList());
                        //Camera
                        List<String> cameraIds = vdsService.findAllCameraIds();
                        if ("cam".equalsIgnoreCase(filterObjectType) && cameraIds != null) {
                            itsFilterObjectIds = String.join(",", cameraIds);
                        } else if ("site".equalsIgnoreCase(filterObjectType) && siteIds != null) {
                            itsFilterObjectIds = String.join(",", siteIds);
                        }
                    }
                    // Neu khong phai la admin
                    if (aBACResponseDTO.getAdmin() == null || !aBACResponseDTO.getAdmin()) {
                        String stages = dto.getUnit().getLisOfStage();
                        responseDTO = itsCoreVdsService.findEventPage(stages, startDate, endDate, filterObjectType, itsFilterObjectIds, objectName, eventCode, eventStatus,
                                directionCode, plate,reportStatus, currentPage, rowsPerPage, false);
                        if (responseDTO != null && responseDTO.getData() != null) {
                            response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                        } else {
                            response = new ResponseMessage(new MessageContent(null, 0L));
                        }
                    } else {
                        responseDTO = itsCoreVdsService.findEventPage(null, startDate, endDate, filterObjectType, itsFilterObjectIds, objectName, eventCode, eventStatus,
                                directionCode, plate, reportStatus, currentPage, rowsPerPage, true);
                        if (responseDTO != null && responseDTO.getData() != null) {
                            response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                        } else {
                            response = new ResponseMessage(new MessageContent(null, 0L));
                        }
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem sự kiện phát hiện", null));
            }
        }
        return response;
    }

    public ResponseMessage downloadZipImageAndVideoViolation(String requestPath, Map<String, Object> body,String urlParam, Map<String, String> headerParam) throws IOException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        String key = (String) body.get("key");
        String startTime = (String) body.get("startTime");
        List<String> listOfFileNames = (List<String>) body.get("listOfFileNames");
        String link = itsCoreVdsService.downloadZipImageAndVideoViolation(key,startTime,listOfFileNames);
        return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "ok", link));
    }
}
