package com.elcom.its.management.controller;

import com.elcom.its.management.dto.*;
import com.elcom.its.management.service.DeviceStatusService;
import com.elcom.its.management.service.StageService;
import com.elcom.its.management.validation.DeviceStatus;
import com.elcom.its.management.validation.EventValidation;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.DateUtil;
import com.elcom.its.utils.DateUtils;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller
public class HistoryStatusDeviceController extends BaseController{

    static final Logger LOGGER = LoggerFactory.getLogger(HistoryStatusDeviceController.class);

    @Autowired
    private DeviceStatusService deviceStatusService;

    @Autowired
    private StageService stageService;

    public ResponseMessage findHistory(String requestPath,Map<String, Object> body, Map<String, String> headerParam ) throws ExecutionException, InterruptedException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem lịch sử trạng thái thiết bị",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem lịch sử trạng thái thiết bị", null));
            } else {
                int currentPage = (int) body.get("page");
                int rowsPerPage = (Integer)body.get("size");
                String startDate = (String) body.get("fromDate");
                String endDate = (String) body.get("toDate");
                String type = (String) body.get("type");
                String search = (String) body.get("search");

                HistoryStatusPagingDTO responseDTO = null;
//                filterObjectType = "cam";
                // Neu khong phai la admin
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    String stages = dto.getUnit().getLisOfStage();
                    responseDTO = deviceStatusService.historyStatus(startDate,endDate,type,search,stages,currentPage,rowsPerPage, false);
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                    } else {
                        response = new ResponseMessage(new MessageContent(null, 0L));
                    }

                } else {
                    responseDTO = deviceStatusService.historyStatus(startDate,endDate,type,search,null,currentPage,rowsPerPage, true);

                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                    } else {
                        response = new ResponseMessage(new MessageContent(null, 0L));
                    }
                }

            }
        }

        return response;
    }

    public ResponseMessage findReport(String requestPath, Map<String, Object> body, Map<String, String> headerParam ) throws ExecutionException, InterruptedException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo lịch sử trạng thái thiết bị",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo lịch sử trạng thái thiết bị", null));
            } else {
                int currentPage = (int) body.get("page");
                int rowsPerPage = (Integer)body.get("size");
                String startDate = (String) body.get("fromDate");
                String endDate = (String) body.get("toDate");
                String type = (String) body.get("type");
                String search = (String) body.get("search");

                HistoryStatusReportDTO responseDTO = null;
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    String stages = dto.getUnit().getLisOfStage();
                    responseDTO = deviceStatusService.reportStatus(startDate,endDate,type,search,stages,currentPage,rowsPerPage, false);
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                    } else {
                        response = new ResponseMessage(new MessageContent(null, 0L));
                    }

                } else {
                    responseDTO = deviceStatusService.reportStatus(startDate,endDate,type,search,null,currentPage,rowsPerPage, true);

                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                    } else {
                        response = new ResponseMessage(new MessageContent(null, 0L));
                    }
                }

            }
        }

        return response;
    }

    public ResponseMessage findReportDisconnect(String requestPath, Map<String, Object> body, Map<String, String> headerParam ) throws ExecutionException, InterruptedException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo lịch sử trạng thái thiết bị",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo lịch sử trạng thái thiết bị", null));
            } else {
                int currentPage = (int) body.get("page");
                int rowsPerPage = (Integer)body.get("size");
                String startDate = (String) body.get("fromDate");
                String endDate = (String) body.get("toDate");
                String type = (String) body.get("type");
                String search = (String) body.get("search");

                HistoryStatusReportDTO responseDTO = null;
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    String stages = dto.getUnit().getLisOfStage();
                    responseDTO = deviceStatusService.reportStatusDisconnect(startDate,endDate,type,search,stages,currentPage,rowsPerPage, false);
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                    } else {
                        response = new ResponseMessage(new MessageContent(null, 0L));
                    }

                } else {
                    responseDTO = deviceStatusService.reportStatusDisconnect(startDate,endDate,type,search,null,currentPage,rowsPerPage, true);

                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                    } else {
                        response = new ResponseMessage(new MessageContent(null, 0L));
                    }
                }

            }
        }

        return response;
    }

    public ResponseMessage createDeviceStatus(Map<String, Object> body, String requestPath,Map<String, String> headerParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            String deviceId = (String) body.get("deviceId");
            String siteId = (String) body.get("siteId");
            String type = (String) body.get("type");
            String note = (String) body.get("note");
            String endTime = (String) body.get("endTime");
            String startTime = (String) body.get("startTime");
            Integer status =  (Integer) body.get("status");
            String invalidData = new DeviceStatus().create(deviceId,siteId,type,startTime,endTime,status);
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                String stage = stageService.findBySite(siteId);
                String listStages = dto.getUnit().getLisOfStage();
                List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                LOGGER.info("Check Abac");
                Map<String,Object> subject = new HashMap<>();
                subject.put("stageId",stage);
                Map<String,Object> attributes = new HashMap<>();
                attributes.put("stageId",myList);
                Map<String,Object> bodyParam = new HashMap<>();
                bodyParam.put("subject", subject);
                bodyParam.put("attributes", attributes);
                ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "POST", dto.getUuid(), requestPath);
                if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tạo lịch sử thiết bị", null));
                } else {
                    LocalDateTime time;
                    LocalDateTime endTimeS = LocalDateTime.now();
                    try {
                        time = DateUtils.parse(startTime);
                        if(endTime!= null)
                            endTimeS = DateUtils.parse(endTime);

                    } catch (Exception ex) {
                        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endTime không đúng định dạng", null);
                    }
                    deviceStatusService.saveHistoryStatus(deviceId,siteId,type,note,startTime,endTime,status);

                }

            }

        }
        return response;
    }

    public ResponseMessage updateDeviceStatus(Map<String, Object> body, String requestPath,Map<String, String> headerParam, String pathParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            String id = pathParam;
            String deviceId = (String) body.get("deviceId");
            String siteId = (String) body.get("siteId");
            String type = (String) body.get("type");
            String note = (String) body.get("note");
            String endTime = (String) body.get("endTime");
            String startTime = (String) body.get("startTime");
            Integer status =  (Integer) body.get("status");
            String invalidData = new DeviceStatus().update(id,deviceId,siteId,type,startTime,endTime,status);
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                String stage = stageService.findBySite(siteId);
                String listStages = dto.getUnit().getLisOfStage();
                List<String> myList;
                if(listStages!= null) {
                     myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                } else {
                    myList = new ArrayList<>();
                }
                LOGGER.info("Check Abac");
                Map<String,Object> subject = new HashMap<>();
                subject.put("stage",stage);
                Map<String,Object> attributes = new HashMap<>();
                attributes.put("stage",myList);
                Map<String,Object> bodyParam = new HashMap<>();
                bodyParam.put("subject", subject);
                bodyParam.put("attributes", attributes);
                ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "PUT", dto.getUuid(), requestPath);
                if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền sửa lịch sử thiết bị", null));
                } else {
                    LocalDateTime time;
                    LocalDateTime endTimeS = LocalDateTime.now();
                    try {
                        time = DateUtils.parse(startTime);
                        if(endTime!= null)
                            endTimeS = DateUtils.parse(endTime);

                    } catch (Exception ex) {
                        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endTime không đúng định dạng", null);
                    }

                    deviceStatusService.updateHistoryStatus(id,deviceId,siteId,type,note,startTime,endTime,status);

                }

            }

        }
        return response;
    }


}
