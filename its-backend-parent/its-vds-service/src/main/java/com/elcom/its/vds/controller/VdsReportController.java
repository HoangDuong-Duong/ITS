package com.elcom.its.vds.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.elcom.its.vds.constant.Constant;
import com.elcom.its.vds.model.dto.ABACResponseDTO;
import com.elcom.its.vds.model.dto.AuthorizationResponseDTO;
import com.elcom.its.vds.model.dto.Response;
import com.elcom.its.vds.service.ReportService;
import com.elcom.its.vds.service.VdsService;
import com.elcom.its.vds.validation.ReportValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class VdsReportController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VdsReportController.class);

    @Autowired
    private VdsService vdsService;

    @Autowired
    private ReportService reportService;

    public ResponseMessage getEventReport(Map<String, String> headerParam, String requestPath, 
            String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
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
                    Integer isAdminBackEnd = (params.get("isAdminBackEnd") != null && !params.get("isAdminBackEnd").isEmpty())
                            ? Integer.parseInt(params.get("isAdminBackEnd")) : null;
                    //Site
                    List<String> siteIds = new ArrayList<>();
                    for (String id : vdsService.findAllSiteIds()) {
                        if (id != null) {
                            siteIds.add(id);
                        }
                    }
                    //Camera
                    List<String> cameraIds = vdsService.findAllCameraIds();
                    String filterObjectIds = "";
                    if (filterObjectType.equals("cam")) {
                        filterObjectIds = String.join(",", cameraIds);
                    } else {
                        filterObjectIds = String.join(",", siteIds);
                    }
                    LOGGER.info("List object ids: {} - {}", filterObjectType, filterObjectIds);
                    
                    String validateData = new ReportValidation().validateInsertProcessUnit(filterTimeLevel, filterObjectType, isAdminBackEnd);
                    if (validateData != null) {
                        responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), validateData,
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        Response response = reportService.getEventFlowReport(
                                filterObjectIds,
                                filterObjectType,
                                filterTimeLevel,
                                isAdminBackEnd);
                        responseMessage = new ResponseMessage(new MessageContent(
                                response.getStatus(),
                                response.getMessage(),
                                response.getData()
                        ));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem báo cáo lưu lượng", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage getEventReportBySite(Map<String, String> headerParam,
            String requestPath,
            String method,
            String urlParam,
            Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    //LOGGER.info("Map: " + params);
                    String filterTimeLevel = params.get("filterTimeLevel");
                    String filterObjectType = params.get("filterObjectType");
                    Integer isAdminBackEnd = (params.get("isAdminBackEnd") != null && !params.get("isAdminBackEnd").isEmpty())
                            ? Integer.parseInt(params.get("isAdminBackEnd")) : null;
                    List<String> siteIds = new ArrayList<>();
                    for (String id : vdsService.findAllSiteIds()) {
                        if (id != null) {
                            siteIds.add(id);
                        }
                    }

                    List<String> cameraIds = vdsService.findAllCameraIds();
                    String filterObjectIds = "";
                    if (filterObjectType.equals("cam")) {
                        filterObjectIds = String.join(",", cameraIds);
                    } else {
                        filterObjectIds = String.join(",", siteIds);
                    }
                    LOGGER.info("List object ids: {} - {}", filterObjectType, filterObjectIds);

                    String validateData = new ReportValidation().validateInsertProcessUnit(filterTimeLevel, filterObjectType, isAdminBackEnd);
                    if (validateData != null) {
                        responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(),
                                validateData,
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        Response response = reportService.getEventReportBySite(
                                filterObjectIds,
                                filterObjectType,
                                filterTimeLevel,
                                isAdminBackEnd);
                        responseMessage = new ResponseMessage(new MessageContent(
                                response.getStatus(),
                                response.getMessage(),
                                response.getData()
                        ));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem báo cáo sự kiện theo vị trí", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage getEventReportByChart(Map<String, String> headerParam,
            String requestPath,
            String method,
            String urlParam,
            Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);

                    LOGGER.info("Map: " + params);

                    String filterTimeLevel = params.get("filterTimeLevel");
                    String filterObjectType = params.get("filterObjectType");
                    Integer isAdminBackEnd = (params.get("isAdminBackEnd") != null && !params.get("isAdminBackEnd").isEmpty())
                            ? Integer.parseInt(params.get("isAdminBackEnd")) : null;

                    List<String> siteIds = new ArrayList<>();

                    for (String id : vdsService.findAllSiteIds()) {
                        if (id != null) {
                            siteIds.add(id);
                        }
                    }

                    List<String> cameraIds = vdsService.findAllCameraIds();

                    String filterObjectIds = "";

                    if (filterObjectType.equals("cam")) {
                        filterObjectIds = String.join(",", cameraIds);
                    } else {
                        filterObjectIds = String.join(",", siteIds);
                    }

                    LOGGER.info("List object ids: {} - {}", filterObjectType, filterObjectIds);

                    String validateData = new ReportValidation().validateInsertProcessUnit(filterTimeLevel, filterObjectType, isAdminBackEnd);

                    if (validateData != null) {
                        responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(),
                                validateData,
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        Response response = reportService.getEventReportByChart(
                                filterObjectIds,
                                filterObjectType,
                                filterTimeLevel,
                                isAdminBackEnd);
                        responseMessage = new ResponseMessage(new MessageContent(
                                response.getStatus(),
                                response.getMessage(),
                                response.getData()
                        ));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem báo cáo sự kiện theo biểu đồ", null));
            }
        }

        return responseMessage;
    }

    public ResponseMessage getTrafficFlowReport(Map<String, String> headerParam,
            String requestPath,
            String method,
            String urlParam,
            Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
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
                    Integer isAdminBackEnd = (params.get("isAdminBackEnd") != null && !params.get("isAdminBackEnd").isEmpty())
                            ? Integer.parseInt(params.get("isAdminBackEnd")) : null;

                    List<String> siteIds = new ArrayList<>();

                    for (String id : vdsService.findAllSiteIds()) {
                        if (id != null) {
                            siteIds.add(id);
                        }
                    }

                    List<String> cameraIds = vdsService.findAllCameraIds();

                    String filterObjectIds = "";

                    if (filterObjectType.equals("cam")) {
                        filterObjectIds = String.join(",", cameraIds);
                    } else {
                        filterObjectIds = String.join(",", siteIds);
                    }

                    LOGGER.info("List object ids: {} - {}", filterObjectType, filterObjectIds);

                    String validateData = new ReportValidation().validateInsertProcessUnit(filterTimeLevel, filterObjectType, isAdminBackEnd);

                    if (validateData != null) {
                        responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(),
                                validateData,
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        Response response = reportService.getTrafficFlowReport(
                                filterObjectIds,
                                filterObjectType,
                                filterTimeLevel,
                                isAdminBackEnd);
                        responseMessage = new ResponseMessage(new MessageContent(
                                response.getStatus(),
                                response.getMessage(),
                                response.getData()
                        ));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem báo cáo lưu lượng", null));
            }
        }

        return responseMessage;
    }

    public ResponseMessage getTrafficFlowReportBySite(Map<String, String> headerParam,
            String requestPath,
            String method,
            String urlParam,
            Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);

                    LOGGER.info("Map: " + params);

                    String filterTimeLevel = params.get("filterTimeLevel");
                    String filterObjectType = params.get("filterObjectType");
                    Integer isAdminBackEnd = (params.get("isAdminBackEnd") != null && !params.get("isAdminBackEnd").isEmpty())
                            ? Integer.parseInt(params.get("isAdminBackEnd")) : null;

                    List<String> siteIds = new ArrayList<>();

                    for (String id : vdsService.findAllSiteIds()) {
                        if (id != null) {
                            siteIds.add(id);
                        }
                    }

                    List<String> cameraIds = vdsService.findAllCameraIds();

                    String filterObjectIds = "";

                    if (filterObjectType.equals("cam")) {
                        filterObjectIds = String.join(",", cameraIds);
                    } else {
                        filterObjectIds = String.join(",", siteIds);
                    }

                    LOGGER.info("List object ids: {} - {}", filterObjectType, filterObjectIds);

                    String validateData = new ReportValidation().validateInsertProcessUnit(filterTimeLevel, filterObjectType, isAdminBackEnd);

                    if (validateData != null) {
                        responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(),
                                validateData,
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        Response response = reportService.getTrafficFlowReportBySite(
                                filterObjectIds,
                                filterObjectType,
                                filterTimeLevel,
                                isAdminBackEnd);
                        responseMessage = new ResponseMessage(new MessageContent(
                                response.getStatus(),
                                response.getMessage(),
                                response.getData()
                        ));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem báo cáo sự kiện theo vị trí", null));
            }
        }

        return responseMessage;
    }

    public ResponseMessage getTrafficFlowReportByChart(Map<String, String> headerParam,
            String requestPath,
            String method,
            String urlParam,
            Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);

                    LOGGER.info("Map: " + params);

                    String filterTimeLevel = params.get("filterTimeLevel");
                    String filterObjectType = params.get("filterObjectType");
                    Integer isAdminBackEnd = (params.get("isAdminBackEnd") != null && !params.get("isAdminBackEnd").isEmpty())
                            ? Integer.parseInt(params.get("isAdminBackEnd")) : null;

                    List<String> siteIds = new ArrayList<>();

                    for (String id : vdsService.findAllSiteIds()) {
                        if (id != null) {
                            siteIds.add(id);
                        }
                    }

                    List<String> cameraIds = vdsService.findAllCameraIds();

                    String filterObjectIds = "";

                    if (filterObjectType.equals("cam")) {
                        filterObjectIds = String.join(",", cameraIds);
                    } else {
                        filterObjectIds = String.join(",", siteIds);
                    }

                    LOGGER.info("List object ids: {} - {}", filterObjectType, filterObjectIds);

                    String validateData = new ReportValidation().validateInsertProcessUnit(filterTimeLevel, filterObjectType, isAdminBackEnd);

                    if (validateData != null) {
                        responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(),
                                validateData,
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), validateData, null));
                    } else {
                        Response response = reportService.getTrafficFlowReportByChart(
                                filterObjectIds,
                                filterObjectType,
                                filterTimeLevel,
                                isAdminBackEnd);
                        responseMessage = new ResponseMessage(new MessageContent(
                                response.getStatus(),
                                response.getMessage(),
                                response.getData()
                        ));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem báo cáo sự kiện theo biểu đồ", null));
            }
        }

        return responseMessage;
    }

}
