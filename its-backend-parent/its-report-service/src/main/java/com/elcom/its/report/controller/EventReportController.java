package com.elcom.its.report.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.report.constant.Constant;
import com.elcom.its.report.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.report.model.dto.ABACResponseDTO;
import com.elcom.its.report.model.dto.AuthorizationResponseDTO;
import com.elcom.its.report.model.dto.Response;
import com.elcom.its.report.model.dto.ViolationDetailPagingDTO;
import com.elcom.its.report.service.EventReportService;
import com.elcom.its.report.validator.ReportValidator;
import com.elcom.its.utils.DateUtils;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.elcom.its.report.model.dto.ExportTroubleReportRequest;

/**
 * @author ducduongn
 */
@Controller
public class EventReportController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EventReportController.class);
    
    @Value("${its.violation.export.worker.queue}")
    private String violationQueueExport;
    
    @Value("${trouble.report.export.worker.queue}")
    private String troubleExportWorkerQueue;
    
    @Autowired
    private EventReportService eventReportService;
    
    @Autowired
    private RabbitMQClient rabbitMQClient;
    
    public ResponseMessage getEventReport(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
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
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    String filterTimeLevel = params.get("filterTimeLevel");
                    String filterObjectType = params.get("filterObjectType");
                    String filterObjectIds = params.get("filterObjectIds");
                    String validateData = new ReportValidator().validateReportFilter(filterTimeLevel, filterObjectType);
                    if (validateData != null) {
                        responseMessage = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        Integer isAdminBackEnd = 1;
                        String stages = null;
                        //Nếu không phải admin => Chỉ thống kê trên đoạn đường của đội user quản lý
                        if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                            isAdminBackEnd = 0;
                            stages = dto.getUnit().getLisOfStage();
                        }
                        Response response = eventReportService.getEventFlowReport(stages, filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo lưu lượng", null));
            }
        }
        return responseMessage;
    }
    
    public ResponseMessage getEventReportBySite(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    
                    String filterTimeLevel = params.get("filterTimeLevel");
                    String filterObjectType = params.get("filterObjectType");
                    String filterObjectIds = params.get("filterObjectIds");
                    String validateData = new ReportValidator().validateReportFilter(filterTimeLevel, filterObjectType);
                    if (validateData != null) {
                        responseMessage = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        Integer isAdminBackEnd = 1;
                        String stages = null;
                        //Nếu không phải admin => Chỉ thống kê trên đoạn đường của đội user quản lý
                        if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                            isAdminBackEnd = 0;
                            stages = dto.getUnit().getLisOfStage();
                        }
                        Response response = eventReportService.getEventReportBySite(stages, filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo sự kiện theo vị trí", null));
            }
        }
        
        return responseMessage;
    }
    
    public ResponseMessage getEventReportByChart(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;
        
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    String filterTimeLevel = params.get("filterTimeLevel");
                    String filterObjectType = params.get("filterObjectType");
                    String filterObjectIds = params.get("filterObjectIds");
                    String validateData = new ReportValidator().validateReportFilter(filterTimeLevel, filterObjectType);
                    if (validateData != null) {
                        responseMessage = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        Integer isAdminBackEnd = 1;
                        String stages = null;
                        //Nếu không phải admin => Chỉ thống kê trên đoạn đường của đội user quản lý
                        if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                            isAdminBackEnd = 0;
                            stages = dto.getUnit().getLisOfStage();
                        }
                        Response response = eventReportService.getEventReportByChart(stages, filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo sự kiện theo biểu đồ", null));
            }
        }
        return responseMessage;
    }
    
    public ResponseMessage getEventList(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
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
                    String filterTimeLevel = params.get("levelFilterByTime");
                    String filterObjectType = params.get("filterObjectType");
                    String filterObjectIds = params.get("filterObjectIds");
                    Boolean manual = Boolean.parseBoolean(params.get("manual"));
                    String objectCreate = params.get("objectCreate");
                    String eventType = params.get("eventType");
                    LocalDateTime time;
                    LocalDateTime endTimeS = LocalDateTime.now();
                    try {
                        time = DateUtils.parse(params.get("startTime"));
                        if (params.get("endTime") != null) {
                            endTimeS = DateUtils.parse(params.get("endTime"));
                        }
                        
                    } catch (Exception ex) {
                        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endTime không đúng định dạng", null);
                    }
                    String objectType = params.get("objectType");
                    String validateData = new ReportValidator().validateReportFilter(filterTimeLevel, filterObjectType);
                    if (validateData != null) {
                        responseMessage = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        Boolean isAdminBackEnd = true;
                        String stages = null;
                        //Nếu không phải admin => Chỉ thống kê trên đoạn đường của đội user quản lý
                        if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                            isAdminBackEnd = false;
                            stages = dto.getUnit().getLisOfStage();
                        }
                        Response response = eventReportService.getEventList(params.get("startTime"), params.get("endTime"), stages, filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd, objectType, manual, objectCreate, eventType);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo sự kiện", null));
            }
        }
        return responseMessage;
    }
    
    public ResponseMessage getEventFanChart(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
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
                    String filterTimeLevel = params.get("levelFilterByTime");
                    String filterObjectType = params.get("filterObjectType");
                    String filterObjectIds = params.get("filterObjectIds");
                    Boolean manual = Boolean.parseBoolean(params.get("manual"));
                    String objectCreate = params.get("objectCreate");
                    String eventType = params.get("eventType");
                    LocalDateTime time;
                    LocalDateTime endTimeS = LocalDateTime.now();
                    String valueLevelFilter = params.get("valueLevelFilter");
                    try {
                        time = DateUtils.parse(params.get("startTime"));
                        if (params.get("endTime") != null) {
                            endTimeS = DateUtils.parse(params.get("endTime"));
                        }
                        
                    } catch (Exception ex) {
                        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endTime không đúng định dạng", null);
                    }
                    String objectType = params.get("objectType");
                    String validateData = new ReportValidator().validateReportFilter(filterTimeLevel, filterObjectType);
                    if (validateData != null) {
                        responseMessage = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        Boolean isAdminBackEnd = true;
                        String stages = null;
                        //Nếu không phải admin => Chỉ thống kê trên đoạn đường của đội user quản lý
                        if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                            isAdminBackEnd = false;
                            stages = dto.getUnit().getLisOfStage();
                        }
                        Response response = eventReportService.getEventFanChart(params.get("startTime"), params.get("endTime"), stages, filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd, objectType, manual, objectCreate, eventType, valueLevelFilter);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo sự kiện", null));
            }
        }
        return responseMessage;
    }
    
    public ResponseMessage getEventLineChart(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
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
                    String filterTimeLevel = params.get("levelFilterByTime");
                    String filterObjectType = params.get("filterObjectType");
                    String filterObjectIds = params.get("filterObjectIds");
                    Boolean manual = Boolean.parseBoolean(params.get("manual"));
                    String objectCreate = params.get("objectCreate");
                    String eventType = params.get("eventType");
                    LocalDateTime time;
                    LocalDateTime endTimeS = LocalDateTime.now();
                    try {
                        time = DateUtils.parse(params.get("startTime"));
                        if (params.get("endTime") != null) {
                            endTimeS = DateUtils.parse(params.get("endTime"));
                        }
                        
                    } catch (Exception ex) {
                        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endTime không đúng định dạng", null);
                    }
                    String objectType = params.get("objectType");
                    String validateData = new ReportValidator().validateReportFilter(filterTimeLevel, filterObjectType);
                    if (validateData != null) {
                        responseMessage = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        Boolean isAdminBackEnd = true;
                        String stages = null;
                        //Nếu không phải admin => Chỉ thống kê trên đoạn đường của đội user quản lý
                        if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                            isAdminBackEnd = false;
                            stages = dto.getUnit().getLisOfStage();
                        }
                        Response response = eventReportService.getEventLineChart(params.get("startTime"), params.get("endTime"), stages, filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd, objectType, manual, objectCreate, eventType);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo sự kiện", null));
            }
        }
        return responseMessage;
    }
    
    public ResponseMessage getEventForReport(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValuesNoDecode(urlParam);
                String startTime = params.get("startTime");
                String endTime = params.get("endTime");
                Integer page = 0;
                Integer size = 20;
                page = Integer.parseInt(params.get("page"));
                size = Integer.parseInt(params.get("size"));
                return new ResponseMessage(new MessageContent(eventReportService.getEventForDayReport(startTime, endTime,page,size)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách báo cáo", null));
            }
            
        }
        return response;
    }
    
    public ResponseMessage getAggTroubleEventReport(Map<String, String> headerParam, String requestPath, String method, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                return new ResponseMessage(new MessageContent(eventReportService.getAggTroubleReport(urlParam)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách báo cáo", null));
            }
            
        }
        return response;
    }
    
    public ResponseMessage exportAggTroubleEventReport(Map<String, String> headerParam, String requestPath, String method, String urlParam) throws JsonProcessingException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                ExportTroubleReportRequest exportRequest = ExportTroubleReportRequest.builder()
                        .urlParam(urlParam)
                        .userId(dto.getUuid())
                        .build();
                ObjectMapper mapper = new ObjectMapper();
                rabbitMQClient.callWorkerService(troubleExportWorkerQueue, mapper.writeValueAsString(exportRequest));
                return new ResponseMessage(new MessageContent("OK"));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách báo cáo", null));
            }
            
        }
        return response;
    }
    
    public ResponseMessage getEventViolation(Map<String, String> headerParam, String requestPath,
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
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    String filterObjectType = params.get("filterObjectType");
                    String filterObjectIds = params.get("filterObjectIds");
                    String startTime = params.get("startTime");
                    String endTime = params.get("endTime");
                    String objectType = params.get("objectType");
                    String eventCode = params.get("eventCode");
                    Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : 0;
                    Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : 20;
                    String validateData = new ReportValidator().validateObjectTrackingFilter(startTime, endTime);
                    if (validateData != null) {
                        responseMessage = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        Boolean isAdminBackEnd = true;
                        String stages = null;
                        //Nếu không phải admin => Chỉ thống kê trên đoạn đường của đội user quản lý
                        if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                            isAdminBackEnd = false;
                            stages = dto.getUnit().getLisOfStage();
                        }
                        ViolationDetailPagingDTO response = eventReportService.getViolationList(startTime,
                                endTime, stages, filterObjectIds, filterObjectType, isAdminBackEnd,
                                objectType, eventCode, page, size);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData(), response.getTotal()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo vi phạm giao thông", null));
            }
        }
        return responseMessage;
    }
    
    public ResponseMessage exportViolation(String requestPath, String urlParam, Map<String, String> headerParam) {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xuất báo cáo vi phạm giao thông",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xuất báo cáo vi phạm giao thông", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String filterObjectType = params.get("filterObjectType");
                String filterObjectIds = params.get("filterObjectIds");
                String startTime = params.get("startTime");
                String endTime = params.get("endTime");
                String objectType = params.get("objectType");
                String eventCode = params.get("eventCode");
                String validateData = new ReportValidator().validateObjectTrackingFilter(startTime, endTime);
                if (validateData != null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                } else {
                    Boolean isAdminBackEnd = true;
                    String stages = null;
                    //Nếu không phải admin => Chỉ thống kê trên đoạn đường của đội user quản lý
                    if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                        isAdminBackEnd = false;
                        stages = dto.getUnit().getLisOfStage();
                    }
                    params.put("isAdminBackEnd", isAdminBackEnd.toString());
                    params.put("stages", stages != null ? stages : "");
                    params.put("userId", dto.getUuid());
                    ObjectMapper mapper = new ObjectMapper();
                    String msg = null;
                    try {
                        msg = mapper.writeValueAsString(params);
                    } catch (JsonProcessingException ex) {
                        LOGGER.error(ExceptionUtils.getStackTrace(ex));
                    }
                    rabbitMQClient.callWorkerService(violationQueueExport, msg);
                    return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                }
            }
        }
        
        return response;
    }
}
