package com.elcom.its.report.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.report.constant.Constant;
import com.elcom.its.report.model.dto.ABACResponseDTO;
import com.elcom.its.report.model.dto.AuthorizationResponseDTO;
import com.elcom.its.report.model.dto.Response;
import com.elcom.its.report.service.TrafficFlowReportService;
import com.elcom.its.report.validator.ReportValidator;
import com.elcom.its.utils.DateUtils;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author ducduongn
 */
@Controller
public class TrafficFlowReportController extends BaseController {

    private final Logger LOGGER = LoggerFactory.getLogger(TrafficFlowReportController.class);

    @Autowired
    private TrafficFlowReportService trafficFlowReportService;

    public ResponseMessage getTrafficFlowReport(Map<String, String> headerParam, String requestPath,
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
                        Response response = trafficFlowReportService.getTrafficFlowReport(stages, filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo lưu lượng", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage getTrafficFlowReportBySite(Map<String, String> headerParam,
            String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
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
                        Response response = trafficFlowReportService.getTrafficFlowReportBySite(stages,
                                filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo lưu lượng", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage getTrafficFlowReportList(Map<String, String> headerParam, String requestPath,
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
                    String filterTimeLevel = params.get("levelFilterByTime");
                    String filterObjectType = params.get("filterObjectType");
                    String filterObjectIds = params.get("filterObjectIds");
                    LocalDateTime time;
                    LocalDateTime endTimeS = LocalDateTime.now();
                    try {
                        time = DateUtils.parse(params.get("startTime"));
                        if(params.get("endTime")!= null)
                            endTimeS = DateUtils.parse(params.get("endTime"));

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
                        Response response = trafficFlowReportService.getTrafficFlowReportList(params.get("startTime"),params.get("endTime"),stages,filterObjectIds,filterObjectType,filterTimeLevel,isAdminBackEnd,objectType);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo lưu lượng", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage getTrafficFlowReportFanChart(Map<String, String> headerParam, String requestPath,
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
                    String filterTimeLevel = params.get("levelFilterByTime");
                    String filterObjectType = params.get("filterObjectType");
                    String filterObjectIds = params.get("filterObjectIds");
                    String valueLevelFilter = params.get("valueLevelFilter");
                    LocalDateTime time;
                    LocalDateTime endTimeS = LocalDateTime.now();
                    try {
                        time = DateUtils.parse(params.get("startTime"));
                        if(params.get("endTime")!= null)
                            endTimeS = DateUtils.parse(params.get("endTime"));

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
                        Response response = trafficFlowReportService.getTrafficFlowReportFanChart(params.get("startTime"),params.get("endTime"),stages,filterObjectIds,filterObjectType,filterTimeLevel,isAdminBackEnd,objectType,valueLevelFilter);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo lưu lượng", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage getTrafficFlowReportLineChart(Map<String, String> headerParam, String requestPath,
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
                    String filterTimeLevel = params.get("levelFilterByTime");
                    String filterObjectType = params.get("filterObjectType");
                    String filterObjectIds = params.get("filterObjectIds");
                    LocalDateTime time;
                    LocalDateTime endTimeS = LocalDateTime.now();
                    try {
                        time = DateUtils.parse(params.get("startTime"));
                        if(params.get("endTime")!= null)
                            endTimeS = DateUtils.parse(params.get("endTime"));

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
                        Response response = trafficFlowReportService.getTrafficFlowReportLineChart(params.get("startTime"),params.get("endTime"),stages,filterObjectIds,filterObjectType,filterTimeLevel,isAdminBackEnd,objectType);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo lưu lượng", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage getTrafficFlowReportByChart(Map<String, String> headerParam,
            String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
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
                        Response response = trafficFlowReportService.getTrafficFlowReportByChart(stages, 
                                filterObjectIds, filterObjectType, filterTimeLevel, isAdminBackEnd);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem báo cáo lưu lượng", null));
            }
        }
        return responseMessage;
    }
}
