/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.controller;

import com.elcom.its.management.constant.Constant;
import com.elcom.its.management.dto.*;
import com.elcom.its.management.service.ITSRecognitionService;
import com.elcom.its.management.service.StageService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.elcom.its.management.validation.RecognitionValidation;

/**
 * @author Admin
 */
@Controller
public class RecognitionController extends BaseController {

    static final Logger LOGGER = LoggerFactory.getLogger(RecognitionController.class);

    @Autowired
    private ITSRecognitionService itsRecognitionService;

    @Autowired
    private StageService stageService;


    public ResponseMessage findRecognition(Map<String, Object> body, String requestPath, Map<String, String> headerParam ) throws ExecutionException, InterruptedException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tìm kiếm phương tiện", null));
            } else {
                int currentPage = (int) body.get("page");
                int currentSize = (int) body.get("size");
                String plate = (String) body.get("plate");
                String startDate = (String) body.get("fromDate");
                String endDate = (String) body.get("toDate");
                String filterObjectType = (String) body.get("filterObjectType");
                List<String> filterObjectIds = body.get("filterObjectIds") != null ? (List<String>) body.get("filterObjectIds") : null;
                String vehicleType = (String) body.get("vehicleType");
                String brand = (String) body.get("brand");
                String color = (String) body.get("color");
                Boolean distinctPlate = (Boolean) body.get("plateNull");
                ObjectMapper mapper = new ObjectMapper();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mapper.setDateFormat(df);
                RecognitionPlateResponseDTO responseDTO = null;
                if (resultCheckABAC.getAdmin() == null) {
                    String stages = dto.getUnit().getLisOfStage();
                    List<String> myList = new ArrayList<String>(Arrays.asList(stages.split(",")));
                    responseDTO = itsRecognitionService.findRecognition(myList, startDate,
                            endDate, plate, vehicleType, filterObjectType, filterObjectIds, brand, color,
                            currentPage, currentSize, false,distinctPlate);
                } else {
                    responseDTO = itsRecognitionService.findRecognition(null, startDate,
                            endDate, plate, vehicleType, filterObjectType, filterObjectIds, brand, color,
                            currentPage, currentSize, true,distinctPlate);
                    end = System.currentTimeMillis();
                    LOGGER.info("findRecognition >>> findRecognition (admin) in {} ms", (end - start));
                }
                if (responseDTO != null && responseDTO.getData() != null) {
                    response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                } else {
                    response = new ResponseMessage(new MessageContent(null, 0L));
                }
            }
        }
        end = System.currentTimeMillis();
        LOGGER.info("findRecognition >>> total process in {} ms", (end - start));
        return response;
    }

    public ResponseMessage findHistoryStatistic(Map<String, Object> body, String requestPath, Map<String, String> headerParam) throws ExecutionException, InterruptedException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tìm kiếm phương tiện", null));
            } else {
                int currentPage = (int) body.get("page");
                int currentSize = (int) body.get("size");
                String plate = (String) body.get("plate");
                String startDate = (String) body.get("fromDate");
                String endDate = (String) body.get("toDate");
                String filterObjectType = (String) body.get("filterObjectType");
                List<String> filterObjectIds = body.get("filterObjectIds") != null ? (List<String>) body.get("filterObjectIds") : null;
                String vehicleType = (String) body.get("vehicleType");
                String brand = (String) body.get("brand");
                String color = (String) body.get("color");
                ObjectMapper mapper = new ObjectMapper();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mapper.setDateFormat(df);
                RecognitionStatisticResponseDTO responseDTO = null;
                if (resultCheckABAC.getAdmin() == null) {
                    String stages = dto.getUnit().getLisOfStage();
                    List<String> myList = new ArrayList<String>(Arrays.asList(stages.split(",")));


                    responseDTO = itsRecognitionService.findRecognitionStatistic(myList, startDate,
                            endDate, plate, vehicleType, filterObjectType, filterObjectIds, brand, color,
                            currentPage, currentSize, false);
                } else {
                    responseDTO = itsRecognitionService.findRecognitionStatistic(null, startDate,
                            endDate, plate, vehicleType, filterObjectType, filterObjectIds, brand, color,
                            currentPage, currentSize, true);
                    end = System.currentTimeMillis();
                    LOGGER.info("findRecognition >>> findRecognition (admin) in {} ms", (end - start));
                }
                if (responseDTO != null && responseDTO.getData() != null) {
                    response = new ResponseMessage(new MessageContent(responseDTO.getData()));
                } else {
                    response = new ResponseMessage(new MessageContent(null));
                }
            }
        }
        end = System.currentTimeMillis();
        LOGGER.info("findRecognition >>> total process in {} ms", (end - start));
        return response;
    }

    public ResponseMessage findHistoryRecognition(Map<String, Object> body, String requestPath, Map<String, String> headerParam) throws ExecutionException, InterruptedException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tìm kiếm phương tiện", null));
            } else {
                Integer currentPage = (Integer) body.get("page");
                Integer currentSize = (Integer) body.get("size");
                String plate = (String) body.get("plate");
                String startDate = (String) body.get("fromDate");
                String endDate = (String) body.get("toDate");
                String filterObjectType = (String) body.get("filterObjectType");
                List<String> filterObjectIds = body.get("filterObjectIds") != null ? (List<String>) body.get("filterObjectIds") : null;
                String vehicleType = (String) body.get("vehicleType");
                String brand = (String) body.get("brand");
                String color = (String) body.get("color");
                Boolean extract = (Boolean) body.get("extract");
                String invalidData = new RecognitionValidation().validateRecognitionHistory(startDate, endDate, currentPage, currentSize, filterObjectType, plate);
                if (invalidData != null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                } else {
                    ObjectMapper mapper = new ObjectMapper();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    mapper.setDateFormat(df);
                    RecognitionPlateResponseDTO responseDTO = null;
                    if (resultCheckABAC.getAdmin() == null) {
                        String stages = dto.getUnit().getLisOfStage();
                        List<String> myList = new ArrayList<String>(Arrays.asList(stages.split(",")));
                        responseDTO = itsRecognitionService.findHistory(myList, startDate,
                                endDate, plate, vehicleType, filterObjectType, filterObjectIds, brand, color,
                                currentPage, currentSize, false,extract);

                    } else {
                        responseDTO = itsRecognitionService.findHistory(null, startDate,
                                endDate, plate, vehicleType, filterObjectType, filterObjectIds, brand, color,
                                currentPage, currentSize, true,extract);
                        end = System.currentTimeMillis();
                        LOGGER.info("findRecognition >>> findRecognition (admin) in {} ms", (end - start));
                    }
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                    } else {
                        response = new ResponseMessage(new MessageContent(null, 0L));
                    }
                }
            }
        }
        end = System.currentTimeMillis();
        LOGGER.info("findRecognition >>> total process in {} ms", (end - start));
        return response;
    }


    public ResponseMessage correctData(Map<String, String> headerParam, String requestPath,
                                       Map<String, Object> bodyParam, String id) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            if (bodyParam == null || bodyParam.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            } else {
                String plate = (String) bodyParam.get("plate");
                String reason = !StringUtil.isNullOrEmpty((String) bodyParam.get("reason")) ? (String) bodyParam.get("reason") : "";
                String objectType = (String) bodyParam.get("objectType");
                String brand = (String) bodyParam.get("brand");
                String color = (String) bodyParam.get("color");
                String imageUrl = (String) bodyParam.get("imageUrl");
                String invalidData = new RecognitionValidation().validateUpdateInfo(brand, color, objectType, reason, imageUrl, plate);
                if (invalidData != null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                } else {
                    Response responseITS = itsRecognitionService.detailRecognition(id);
                    if(responseITS==null){
                        return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy bản ghi", null));
                    }
                    if(responseITS.getData()!=null) {
                        ObjectMapper oMapper = new ObjectMapper();
                        RecognitionPlateDTO recognitionPlateDTO = oMapper.convertValue(responseITS.getData(), RecognitionPlateDTO.class);
                        String stages = stageService.findBySite(recognitionPlateDTO.getSite().getSiteId());
                        String listStages = dto.getUnit().getLisOfStage();
                        List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                        LOGGER.info("Check Abac");
                        Map<String, Object> subject = new HashMap<>();
                        subject.put("stage", stages);
                        Map<String, Object> attributes = new HashMap<>();
                        attributes.put("stage", myList);
                        Map<String, Object> bodyParamABAC = new HashMap<>();
                        bodyParamABAC.put("subject", subject);
                        bodyParamABAC.put("attributes", attributes);
                        ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParamABAC, "PUT", dto.getUuid(), requestPath);
                        if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhâp chi tiết dò xe", null));
                        } else {
                            CorrectRecognitionInfoRequest correctRecognitionInfoRequest = new CorrectRecognitionInfoRequest(imageUrl, plate, objectType, color, brand, reason);
                            RecognitionPlateCorrectDTO recognitionPlateHistoryCorrectDTO = itsRecognitionService.correctData(id, correctRecognitionInfoRequest);
                            response = new ResponseMessage(new MessageContent(recognitionPlateHistoryCorrectDTO.getStatus(), recognitionPlateHistoryCorrectDTO.getMessage(),
                                    recognitionPlateHistoryCorrectDTO.getData()));
                        }
                    }else {
                        response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy bản ghi", null));
                    }
                }
            }
        }
        return response;
    }

    public ResponseMessage getDetail(Map<String, String> headerParam, String requestPath, String paramPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            Response responseITS = itsRecognitionService.detailRecognition(paramPath);
            ObjectMapper oMapper = new ObjectMapper();
            if(responseITS==null){
                return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy bản ghi", null));
            }
            RecognitionPlateDTO recognitionPlateDTO = oMapper.convertValue(responseITS.getData(), RecognitionPlateDTO.class);
            String stages = stageService.findBySite(recognitionPlateDTO.getSite().getSiteId());
            String listStages = dto.getUnit().getLisOfStage();
            List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
            LOGGER.info("Check Abac");
            Map<String, Object> subject = new HashMap<>();
            subject.put("stage", stages);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("stage", myList);
            Map<String, Object> bodyParamABAC = new HashMap<>();
            bodyParamABAC.put("subject", subject);
            bodyParamABAC.put("attributes", attributes);
            ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParamABAC, "DETAIL", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo tra cứu phương tiện", null));
            } else {
                response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
            }
        }

        return response;
    }

    public ResponseMessage deleteRecognition(Map<String, String> headerParam, String requestPath, String paramPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            Response responseITS = itsRecognitionService.detailRecognition(paramPath);
            RecognitionPlateDTO recognitionPlateDTO = (RecognitionPlateDTO) responseITS.getData();
            String stage = stageService.findBySite(recognitionPlateDTO.getSite().getSiteId());
            List<String> stages = new ArrayList<>();
            stages.add(stage);
            String listStages = dto.getUnit().getLisOfStage();
            List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
            LOGGER.info("Check Abac");
            Map<String, Object> subject = new HashMap<>();
            subject.put("stages", stages);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("stages", myList);
            Map<String, Object> bodyParamABAC = new HashMap<>();
            bodyParamABAC.put("subject", subject);
            bodyParamABAC.put("attributes", attributes);
            ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParamABAC, "DELETE", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa chi tiết dò xe", null));
            } else {
                Response responseITSDELETE = itsRecognitionService.deleteRecognitionFromDBM(paramPath);
                response = new ResponseMessage(new MessageContent(responseITSDELETE.getStatus(), responseITSDELETE.getMessage(), null));
            }
        }

        return response;
    }

    public ResponseMessage deleteMultiRecognition(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            List<String> recognitionIdList = (List<String>) bodyParam.get("recognitionIdList");
            if (CollectionUtils.isEmpty(recognitionIdList)) {
                return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Vui lòng truyền danh sách phương tiện cần xóa", null));
            }
            Response responseITS = itsRecognitionService.getStageMultiEvent(recognitionIdList);
            List<String> stages = (List<String>) responseITS.getData();
            String listStages = dto.getUnit().getLisOfStage();
            List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
            LOGGER.info("Check Abac");
            Map<String, Object> subject = new HashMap<>();
            subject.put("stages", stages);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("stages", myList);
            Map<String, Object> bodyParamABAC = new HashMap<>();
            bodyParamABAC.put("subject", subject);
            bodyParamABAC.put("attributes", attributes);
            ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParamABAC, "DELETE", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa nhiều sự kiện sự kiện", null));
            } else {
                Response responseITSDELETE = itsRecognitionService.deleteMultiRecognition(recognitionIdList);
                response = new ResponseMessage(new MessageContent(responseITSDELETE.getStatus(), responseITSDELETE.getMessage(), null));
            }
        }

        return response;
    }
}
