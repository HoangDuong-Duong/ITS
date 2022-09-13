package com.elcom.its.config.controller;

import com.elcom.its.config.constant.Constant;
import com.elcom.its.config.model.dto.*;
import com.elcom.its.config.service.ObjectTrackingService;
import com.elcom.its.config.validation.ObjectTrackingValidation;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Map;

/**
 * @author ducduongn
 */
@Controller
public class ObjectTrackingController extends BaseController {

    private Logger log = LoggerFactory.getLogger(ObjectTrackingController.class);

    @Autowired
    private ObjectTrackingService objectTrackingService;

    public ResponseMessage getAllObjectTracking(Map<String, String> headerParam,
            String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);

            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    responseMessage = new ResponseMessage(new MessageContent(objectTrackingService.getAll()));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                    Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                    String search = params.get("search");
                    String brand = params.get("brand");
                    String reason = params.get("reason");
                    String vehicleType = params.get("vehicleType");
                    String recognitionFromDate = params.get("recognitionFromDate");
                    String recognitionToDate = params.get("recognitionToDate");
                    String processFromDate = params.get("processFromDate");
                    String processToDate = params.get("processToDate");
                    String createFromDate = params.get("createFromDate");
                    String createToDate = params.get("createToDate");

                    if (page != null && size != null) {
                        ObjectTrackingPaginationDTOMesage objectTrackingPaginationDTOMesage = objectTrackingService.getAll(
                                page, size, search, brand, reason, vehicleType, recognitionFromDate,
                                recognitionToDate, processFromDate, processToDate, createFromDate, createToDate);
                        if (objectTrackingPaginationDTOMesage.getData() != null && objectTrackingPaginationDTOMesage.getStatus() == HttpStatus.OK.value()) {
                            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                                    HttpStatus.OK.toString(), objectTrackingPaginationDTOMesage.getData(), objectTrackingPaginationDTOMesage.getTotal()));
                        }
                    } else {
                        return new ResponseMessage(new MessageContent(objectTrackingService.getAll()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền lấy thông tin phương tiện theo dõi", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage createObjectTracking(Map<String, String> headerParam,
            Map<String, Object> bodyParam,
            String requestPath,
            String requestMethod) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "POST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (bodyParam == null || bodyParam.isEmpty()) {
                    responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    String model = (String) bodyParam.get("model");
                    String infoObject = (String) bodyParam.get("infoObject");
                    String reason = (String) bodyParam.get("reason");
                    String alertChannel = (String) bodyParam.get("alertChannel");
                    String alertReceiver = (String) bodyParam.get("alertReceiver");
                    String queue = (String) bodyParam.get("queue");
                    String note = (String) bodyParam.get("note");
                    String objectType = (String) bodyParam.get("objectType");
                    String objectImages = (String) bodyParam.get("objectImages");
                    String description = (String) bodyParam.get("description");
                    String identification = (String) bodyParam.get("indentification");
                    String vehicleType = (String) bodyParam.get("vehicleType");

                    ObjectTrackingCreateUpdateDto objectTrackingDto = new ObjectTrackingCreateUpdateDto();

                    objectTrackingDto.setModel(model);
                    objectTrackingDto.setInfoObject(infoObject);
                    objectTrackingDto.setReason(reason);
                    objectTrackingDto.setAlertChannel(alertChannel);
                    objectTrackingDto.setAlertReceiver(alertReceiver);
                    objectTrackingDto.setQueue(queue);
                    objectTrackingDto.setNote(note);
                    objectTrackingDto.setObjectType(objectType);
                    objectTrackingDto.setObjectImages(objectImages);
                    objectTrackingDto.setDescription(description);
                    objectTrackingDto.setIndentification(identification);
                    objectTrackingDto.setVehicleType(vehicleType);

                    String validatedData = new ObjectTrackingValidation().validateObjectTracking(objectTrackingDto);

                    if (validatedData != null) {
                        responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(),
                                validatedData,
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), validatedData, null));
                    } else {
                        Response message = objectTrackingService.createObjectTracking(objectTrackingDto);
                        responseMessage = new ResponseMessage(new MessageContent(
                                message.getStatus(),
                                message.getMessage(),
                                message.getData()
                        ));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền thêm phương tiện theo dõi", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage getObjectTrackingByIdentificationList(Map<String, String> headerParam,
            String requestPath,
            String method,
            String urlParam,
            Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    responseMessage = new ResponseMessage(new MessageContent(objectTrackingService.getAll()));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    String identificationList = params.get("identificationList");

                    if (identificationList != null) {
                        List<String> identificationIdList = new ArrayList<>();

                        String[] stringIdentificationIds = identificationList.split(",");
                        for (String identificationId : stringIdentificationIds) {
                            identificationIdList.add(identificationId);
                        }

                        log.info("identificationIdList: {}", identificationIdList);

                        Response response = objectTrackingService.findByIdentificationList(identificationIdList);

                        responseMessage = new ResponseMessage(new MessageContent(response.getData()));
                    } else {
                        responseMessage = new ResponseMessage(new MessageContent(objectTrackingService.getAll()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền lấy danh sách camera theo danh sách phương tiện theo dõi theo biển số", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage updateObjectTracking(Map<String, String> headerParam,
            Map<String, Object> bodyParam,
            String requestPath,
            String requestMethod,
            String pathParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "PUT", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (bodyParam == null || bodyParam.isEmpty()) {
                    responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    String model = (String) bodyParam.get("model");
                    String infoObject = (String) bodyParam.get("infoObject");
                    String reason = (String) bodyParam.get("reason");
                    String alertChannel = (String) bodyParam.get("alertChannel");
                    String alertReceiver = (String) bodyParam.get("alertReceiver");
                    String queue = (String) bodyParam.get("queue");
                    String note = (String) bodyParam.get("note");
                    String objectType = (String) bodyParam.get("objectType");
                    String objectImages = (String) bodyParam.get("objectImages");
                    String description = (String) bodyParam.get("description");
                    String identification = (String) bodyParam.get("indentification");
                    String vehicleType = (String) bodyParam.get("vehicleType");

                    ObjectTrackingCreateUpdateDto objectTrackingDto = new ObjectTrackingCreateUpdateDto();

                    objectTrackingDto.setModel(model);
                    objectTrackingDto.setInfoObject(infoObject);
                    objectTrackingDto.setReason(reason);
                    objectTrackingDto.setAlertChannel(alertChannel);
                    objectTrackingDto.setAlertReceiver(alertReceiver);
                    objectTrackingDto.setQueue(queue);
                    objectTrackingDto.setNote(note);
                    objectTrackingDto.setObjectType(objectType);
                    objectTrackingDto.setObjectImages(objectImages);
                    objectTrackingDto.setDescription(description);
                    objectTrackingDto.setIndentification(identification);
                    objectTrackingDto.setVehicleType(vehicleType);

                    String validatedData = new ObjectTrackingValidation().validateObjectTracking(objectTrackingDto);

                    if (validatedData != null) {
                        responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(),
                                validatedData,
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), validatedData, null));
                    } else {
                        Response response = objectTrackingService.updateObjectTracking(objectTrackingDto, pathParam);
                        responseMessage = new ResponseMessage(new MessageContent(
                                response.getStatus(),
                                response.getMessage(),
                                response.getData()
                        ));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền sửa phương tiện theo dõi", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage deleteObjectTracking(Map<String, String> headerParam,
            Map<String, Object> bodyParam,
            String requestPath,
            String requestMethod,
            String pathParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response response = objectTrackingService.deleteObjectTracking(pathParam);
                log.info("Id", pathParam);
                responseMessage = new ResponseMessage(new MessageContent(
                        response.getStatus(),
                        response.getMessage(),
                        response.getData()
                ));
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa phương tiện theo dõi", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage deleteMultiObjectTracking(Map<String, String> headerParam,
            String requestPath,
            String method,
            String urlParam,
            Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {

            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                @SuppressWarnings("unchecked")
                List<String> objectTrackingIds = (List<String>) bodyParam.get("listObjectTrackingIds");

                if (objectTrackingIds.size() == 0) {
                    responseMessage = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "Không tìm thấy phương tiện theo dõi", null));
                } else {
                    Response response = objectTrackingService.deleteMultipleObjectTracking(objectTrackingIds);

                    responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                }

            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa nhiều phương tiện theo dõi", null));
            }
        }

        return responseMessage;
    }

    public ResponseMessage findById(Map<String, String> headerParam, String requestPath,
            String method, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacStatus = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (abacStatus.getStatus()) {
                response = new ResponseMessage(new MessageContent(objectTrackingService.findById(pathParam).getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem chi tiết phương tiện theo dõi",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem chi tiết phương tiện theo dõi", null));
            }
        }
        return response;
    }

    public ResponseMessage updateObjectTrackingProcessed(Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestPath, String requestMethod, String pathParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "PUT", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (bodyParam == null || bodyParam.isEmpty()) {
                    responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    String processNote = (String) bodyParam.get("processNote");
                    String processDate = (String) bodyParam.get("processDate");
                    Integer processStatus = bodyParam.get("processStatus") != null ? (Integer) bodyParam.get("processStatus") : null;
                    ObjectTrackingProcessDTO objectTrackingDto = new ObjectTrackingProcessDTO(processDate, processNote, processStatus);
                    String validatedData = new ObjectTrackingValidation().validateObjectTrackingProcessed(objectTrackingDto);
                    if (validatedData != null) {
                        responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(),
                                validatedData, new MessageContent(HttpStatus.BAD_REQUEST.value(), validatedData, null));
                    } else {
                        Response response = objectTrackingService.updateObjectTrackingProcessed(objectTrackingDto, pathParam);
                        responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()
                        ));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền sửa phương tiện theo dõi", null));
            }
        }
        return responseMessage;
    }
}
