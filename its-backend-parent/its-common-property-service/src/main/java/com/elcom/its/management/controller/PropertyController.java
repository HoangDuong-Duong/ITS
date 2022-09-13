package com.elcom.its.management.controller;

import com.elcom.its.management.dto.*;
import com.elcom.its.management.enums.DeviceStatus;
import com.elcom.its.management.enums.PropertyType;
import com.elcom.its.management.enums.TypeEquipment;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.management.service.PropertyService;
import com.elcom.its.management.validation.PropertyValidation;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
public class PropertyController  extends BaseController{

    @Value("${its.property.export.worker.queue}")
    private String queueExport;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    public ResponseMessage findSearch(String requestPath, Map<String,Object> body, Map<String, String> headerParam) throws ExecutionException, InterruptedException {
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
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách tài sản",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách tài sản", null));
            } else {
                int currentPage = (int) body.get("page");
                int rowsPerPage = (int) body.get("size");
                String sort = (String) body.get("sort");
                String name = (String) body.get("name");
                String position = (String) body.get("position");
                String site = (String) body.get("site");
                String typeDate = (String) body.get("typeData");
                String directionCode = (String) body.get("directionCode");
                List<Integer> types = (List<Integer>) body.get("types");
                List<Integer> status = (List<Integer>) body.get("status");
//                List<String> type = params.get("types") != null ? Arrays.asList(params.get("types").split(",")) : null;
//                List<Integer> types = type.stream().map((item) -> Integer.valueOf(item)).collect(Collectors.toList());
//                List<String> status = params.get("status") != null ? Arrays.asList(params.get("status").split(",")) : null;
//                List<Integer> statusList = status.stream().map((item) -> Integer.valueOf(item)).collect(Collectors.toList());
                PropertyResponseDTO responseDTO = null;
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    String stageDto = dto.getUnit().getLisOfStage();
                    List<String> stages = Arrays.asList(stageDto.split(",")) ;
                    responseDTO = propertyService.findProperty(name,position,types,status,sort,currentPage,rowsPerPage, stages,false,site,directionCode,typeDate);
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                    } else {
                        response = new ResponseMessage(responseDTO.getStatus(),responseDTO.getMessage(),new MessageContent(responseDTO.getStatus(),responseDTO.getMessage(),null));
                    }

                } else {
                    responseDTO = propertyService.findProperty(name,position,types,status,sort,currentPage,rowsPerPage,null,true,site,directionCode,typeDate);
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                    } else {
                        response = new ResponseMessage(responseDTO.getStatus(),responseDTO.getMessage(),new MessageContent(responseDTO.getStatus(),responseDTO.getMessage(),null));
                    }
                }

            }
        }

        return response;
    }

    public ResponseMessage findFmsGroup(String requestPath, Map<String,Object> body, Map<String, String> headerParam) throws ExecutionException, InterruptedException {
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
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách tài sản",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách tài sản", null));
            } else {
                int currentPage = (int) body.get("page");
                int rowsPerPage = (int) body.get("size");
                String sort = (String) body.get("sort");
                String name = (String) body.get("name");
                String position = (String) body.get("position");
                String site = (String) body.get("site");
                String typeDate = (String) body.get("typeData");
                String directionCode = (String) body.get("directionCode");
                List<Integer> types = (List<Integer>) body.get("types");
                List<Integer> status = (List<Integer>) body.get("status");
                String listStage = (String) body.get("stages");
                List<String> stages = Arrays.asList(listStage.split(",")) ;
                PropertyResponseDTO responseDTO = null;
                responseDTO = propertyService.findProperty(name,position,types,status,sort,currentPage,rowsPerPage, stages,false,site,directionCode,typeDate);
                if (responseDTO != null && responseDTO.getData() != null) {
                    response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                } else {
                    response = new ResponseMessage(responseDTO.getStatus(),responseDTO.getMessage(),new MessageContent(responseDTO.getStatus(),responseDTO.getMessage(),null));
                }
            }
        }

        return response;
    }


    public ResponseMessage export(String requestPath, String urlParam, Map<String, String> headerParam) throws ExecutionException, InterruptedException, JsonProcessingException {
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
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách tài sản",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách tài sản", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                int currentPage = Integer.parseInt(params.get("page"));
                int rowsPerPage = Integer.parseInt(params.get("size"));
                String sort = params.get("sort");
                String name = params.get("name");
                String position = params.get("position");
                String types = params.get("types");
                String status = params.get("status");
//                List<String> type = params.get("types") != null ? Arrays.asList(params.get("types").split(",")) : null;
//                List<Integer> types = type.stream().map((item) -> Integer.valueOf(item)).collect(Collectors.toList());
//                List<String> status = params.get("status") != null ? Arrays.asList(params.get("status").split(",")) : null;
//                List<Integer> statusList = status.stream().map((item) -> Integer.valueOf(item)).collect(Collectors.toList());
                  PropertyResponseDTO responseDTO = null;
//                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
//                    String stages = dto.getUnit().getLisOfStage();
//                    EventExport eventExport = new EventExport();
//                    eventExport.setUuid(dto.getUuid());
//                    eventExport.setName(name);
//                    eventExport.setPosition(position);
//                    eventExport.setStatus(status);
//                    eventExport.setIsAdmin(false);
//                    eventExport.setPage(currentPage);
//                    eventExport.setSize(rowsPerPage);
//                    eventExport.setStages(stages);
//                    eventExport.setTypes(types);
//                    eventExport.setSort(sort);
//                    ObjectMapper mapper = new ObjectMapper();
//                    String msg = mapper.writeValueAsString(eventExport);
//                    rabbitMQClient.callWorkerService(queueExport, msg);
//                    return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "ok", null));
//
//                } else {
//                    EventExport eventExport = new EventExport();
//                    eventExport.setUuid(dto.getUuid());
//                    eventExport.setName(name);
//                    eventExport.setPosition(position);
//                    eventExport.setStatus(status);
//                    eventExport.setIsAdmin(true);
//                    eventExport.setPage(currentPage);
//                    eventExport.setSize(rowsPerPage);
//                    eventExport.setStages(null);
//                    eventExport.setTypes(types);
//                    eventExport.setSort(sort);
//                    ObjectMapper mapper = new ObjectMapper();
//                    String msg = mapper.writeValueAsString(eventExport);
//                    rabbitMQClient.callWorkerService(queueExport, msg);
//                    return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "ok", null));
//
//
//                }

            }
        }

        return response;
    }
    public ResponseMessage save(String requestPath, Map<String, String> headerParam, Map<String, Object> body) throws ExecutionException, InterruptedException, IOException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lưu tài sản",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lưu tài sản", null));
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                objectMapper.setDateFormat(df);
                List<CommonProperty> commonPropertyList = (List<CommonProperty>) body.get("data");
                String tmp = objectMapper.writeValueAsString(commonPropertyList);
                JsonNode jsonNode = objectMapper.readTree(tmp);
                commonPropertyList = objectMapper.readerFor(new TypeReference<List<CommonProperty>>() {
                }).readValue(jsonNode);
                for (CommonProperty item: commonPropertyList
                     ) {
                    String validate = new PropertyValidation().validateProperty(item);
                    if(validate!=null){
                        return new ResponseMessage(HttpStatus.FORBIDDEN.value(), validate,
                                new MessageContent(HttpStatus.FORBIDDEN.value(), validate, null));
                    }

                }
                List<CommonProperty> commonPropertyList1 = commonPropertyList.stream().map((item) ->{
                        Map<String,Object> data = (Map) item.getData();
                        data.replace("status", DeviceStatus.parse((Integer) data.get("status")));
                        if(PropertyType.AUXILIARY_EQUIPMENT.code() == item.getType().code()){
                            data.replace("type", TypeEquipment.of((Integer)data.get("type")));
                        }
                        item.setData(data);
                        return item;

                }).collect(Collectors.toList());
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    String stageDTO = dto.getUnit().getLisOfStage();
                    List<String> stages = Arrays.asList(stageDTO.split(","));
                    Response responseDTO = propertyService.saveListProperty(commonPropertyList1, dto.getUuid(),stages,false);
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData()));
                    } else {
                        response = new ResponseMessage(responseDTO.getStatus(),responseDTO.getMessage(),new MessageContent(responseDTO.getStatus(),responseDTO.getMessage(),null));
                    }

                } else {
                    Response responseDTO = propertyService.saveListProperty(commonPropertyList1, dto.getUuid(),null,true);
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData()));
                    } else {
                        response = new ResponseMessage(responseDTO.getStatus(),responseDTO.getMessage(),new MessageContent(responseDTO.getStatus(),responseDTO.getMessage(),null));
                    }
                }

            }
        }

        return response;
    }

    public ResponseMessage updateHistory(String requestPath, Map<String, String> headerParam, Map<String, Object> body) throws ExecutionException, InterruptedException, IOException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhập lịch sử tài sản",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhập lịch sử tài sản", null));
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                objectMapper.setDateFormat(df);
                String propertyId = (String) body.get("propertyId");
                String startTime = (String) body.get("startTime");
                String endTime = (String) body.get("endTime");
                String note = (String) body.get("note");
                String id = (String) body.get("id");
                if(StringUtil.isNullOrEmpty(id)){
                    return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Id không thể trống",
                            new MessageContent(HttpStatus.FORBIDDEN.value(), "Id không thể trống", null));
                }
                String validate = new PropertyValidation().validatePropertyHistory(startTime,endTime,propertyId);
                if(validate!=null){
                    return new ResponseMessage(HttpStatus.FORBIDDEN.value(), validate,
                            new MessageContent(HttpStatus.FORBIDDEN.value(), validate, null));
                }
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    String stageDTO = dto.getUnit().getLisOfStage();
                    List<String> stages = Arrays.asList(stageDTO.split(","));
                    Response responseDTO = propertyService.updateHistoryProperty(id,startTime,endTime,note,propertyId,dto.getUuid(),stages,false);
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData()));
                    } else {
                        response = new ResponseMessage(responseDTO.getStatus(),responseDTO.getMessage(),new MessageContent(responseDTO.getStatus(),responseDTO.getMessage(),null));
                    }

                } else {
                    Response responseDTO = propertyService.updateHistoryProperty(id,startTime,endTime,note,propertyId,dto.getUuid(),null,true);
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData()));
                    } else {
                        response = new ResponseMessage(responseDTO.getStatus(),responseDTO.getMessage(),new MessageContent(responseDTO.getStatus(),responseDTO.getMessage(),null));
                    }
                }

            }
        }

        return response;
    }

    public ResponseMessage saveHistory(String requestPath, Map<String, String> headerParam, Map<String, Object> body) throws ExecutionException, InterruptedException, IOException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lưu lịch sử tài sản",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lưu lịch sử tài sản", null));
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                objectMapper.setDateFormat(df);
                String propertyId = (String) body.get("propertyId");
                String startTime = (String) body.get("startTime");
                String endTime = (String) body.get("endTime");
                String note = (String) body.get("note");
                String validate = new PropertyValidation().validatePropertyHistory(startTime,endTime,propertyId);
                if(validate!=null){
                    return new ResponseMessage(HttpStatus.FORBIDDEN.value(), validate,
                            new MessageContent(HttpStatus.FORBIDDEN.value(), validate, null));
                }

                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    String stages = dto.getUnit().getLisOfStage();
                    Response responseDTO = propertyService.saveHistoryProperty(startTime,endTime,note,propertyId,dto.getUuid());
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData()));
                    } else {
                        response = new ResponseMessage(responseDTO.getStatus(),responseDTO.getMessage(),new MessageContent(responseDTO.getStatus(),responseDTO.getMessage(),null));
                    }

                } else {
                    Response responseDTO = propertyService.saveHistoryProperty(startTime,endTime,note,propertyId,dto.getUuid());
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData()));
                    } else {
                        response = new ResponseMessage(responseDTO.getStatus(),responseDTO.getMessage(),new MessageContent(responseDTO.getStatus(),responseDTO.getMessage(),null));
                    }
                }

            }
        }

        return response;
    }

    public ResponseMessage update(String requestPath, Map<String, String> headerParam, Map<String, Object> body) throws ExecutionException, InterruptedException, IOException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhập tài sản",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhập tài sản", null));
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                objectMapper.setDateFormat(df);
                List<CommonProperty> commonPropertyList = (List<CommonProperty>) body.get("data");
                String tmp = objectMapper.writeValueAsString(commonPropertyList);
                JsonNode jsonNode = objectMapper.readTree(tmp);
                commonPropertyList = objectMapper.readerFor(new TypeReference<List<CommonProperty>>() {
                }).readValue(jsonNode);
                List<CommonProperty> commonPropertyList1 = commonPropertyList.stream().map((item) ->{
                    Map<String,Object> data = (Map) item.getData();
                    data.replace("status", DeviceStatus.parse((Integer) data.get("status")));
                    if(PropertyType.AUXILIARY_EQUIPMENT.code() == item.getType().code()){
                        data.replace("type", TypeEquipment.of((Integer)data.get("type")));
                    }
                    item.setData(data);
                    return item;

                }).collect(Collectors.toList());
                for (CommonProperty commonProperty: commonPropertyList1
                     ) {
                    String validate = new PropertyValidation().validateProperty(commonProperty);
                    if(validate!=null){
                        return new ResponseMessage(HttpStatus.FORBIDDEN.value(), validate,
                                new MessageContent(HttpStatus.FORBIDDEN.value(), validate, null));
                    }
                    if(StringUtil.isNullOrEmpty(commonProperty.getId())){
                        return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Thiếu mã Id tài sản",
                                new MessageContent(HttpStatus.FORBIDDEN.value(), "Thiếu mã Id tài sản", null));
                    }

                }
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    String stages = dto.getUnit().getLisOfStage();
                    Response responseDTO = propertyService.updateListProperty(commonPropertyList1, dto.getUuid());
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData()));
                    } else {
                        response = new ResponseMessage(responseDTO.getStatus(),responseDTO.getMessage(),new MessageContent(responseDTO.getStatus(),responseDTO.getMessage(),null));
                    }

                } else {
                    Response responseDTO = propertyService.updateListProperty(commonPropertyList1, dto.getUuid());
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData()));
                    } else {
                        response = new ResponseMessage(responseDTO.getStatus(),responseDTO.getMessage(),new MessageContent(responseDTO.getStatus(),responseDTO.getMessage(),null));
                    }
                }

            }
        }

        return response;
    }

    public ResponseMessage deleteMulti(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            List<String> properties = (List<String>) bodyParam.get("properties");
            if (CollectionUtils.isEmpty(properties)) {
                return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Vui lòng truyền danh sách tài sản cần xóa", null));
            }
//            Response responseITS = itsRecognitionService.getStageMultiEvent(recognitionIdList);
//            List<String> stages = (List<String>) responseITS.getData();
//            String listStages = dto.getUnit().getLisOfStage();
//            List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
//            LOGGER.info("Check Abac");
//            Map<String, Object> subject = new HashMap<>();
//            subject.put("stages", stages);
//            Map<String, Object> attributes = new HashMap<>();
//            attributes.put("stages", myList);
//            Map<String, Object> bodyParamABAC = new HashMap<>();
//            bodyParamABAC.put("subject", subject);
//            bodyParamABAC.put("attributes", attributes);
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DELETE", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa danh sách tài sản",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa danh sách tài sản", null));
            } else {
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    String stage = dto.getUnit().getLisOfStage();
                    List<String> stages = Arrays.asList(stage.split(","));
                    Response responseITSDELETE = propertyService.deleteProperty(properties,stages,false);
                    response = new ResponseMessage(new MessageContent(responseITSDELETE.getStatus(), responseITSDELETE.getMessage(), null));
                    if (responseITSDELETE != null && responseITSDELETE.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseITSDELETE.getData(), responseITSDELETE.getTotal()));
                    } else {
                        response = new ResponseMessage(responseITSDELETE.getStatus(), responseITSDELETE.getMessage(), new MessageContent(responseITSDELETE.getStatus(), responseITSDELETE.getMessage(), null));
                    }

                } else {
                    String stages = dto.getUnit().getLisOfStage();
                    Response responseITSDELETE = propertyService.deleteProperty(properties,null,true);
                    response = new ResponseMessage(new MessageContent(responseITSDELETE.getStatus(), responseITSDELETE.getMessage(), null));
                    if (responseITSDELETE != null && responseITSDELETE.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseITSDELETE.getData(), responseITSDELETE.getTotal()));
                    } else {
                        response = new ResponseMessage(responseITSDELETE.getStatus(), responseITSDELETE.getMessage(), new MessageContent(responseITSDELETE.getStatus(), responseITSDELETE.getMessage(), null));
                    }
                }
            }
        }

        return response;
    }

    public ResponseMessage deleteMultiHistory(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            List<String> properties = (List<String>) bodyParam.get("ids");
            if (CollectionUtils.isEmpty(properties)) {
                return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Vui lòng truyền danh sách tài sản cần xóa", null));
            }
//            Response responseITS = itsRecognitionService.getStageMultiEvent(recognitionIdList);
//            List<String> stages = (List<String>) responseITS.getData();
//            String listStages = dto.getUnit().getLisOfStage();
//            List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
//            LOGGER.info("Check Abac");
//            Map<String, Object> subject = new HashMap<>();
//            subject.put("stages", stages);
//            Map<String, Object> attributes = new HashMap<>();
//            attributes.put("stages", myList);
//            Map<String, Object> bodyParamABAC = new HashMap<>();
//            bodyParamABAC.put("subject", subject);
//            bodyParamABAC.put("attributes", attributes);
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DELETE", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa lịch sử tài sản", null));
            } else {
                Response responseITSDELETE = propertyService.deleteHistoryProperty(properties);
                response = new ResponseMessage(new MessageContent(responseITSDELETE.getStatus(), responseITSDELETE.getMessage(), null));
            }
        }

        return response;
    }

    public ResponseMessage findSearchHistory(String requestPath, String urlParam, Map<String, String> headerParam) throws ExecutionException, InterruptedException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
            Integer currentPage = Integer.parseInt(params.get("page"));
            Integer rowsPerPage = Integer.parseInt(params.get("size"));
            String property = params.get("propertyId");
            String validate = new PropertyValidation().validateGetPropertyHistory(property,currentPage,rowsPerPage);
            if(validate!=null){
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), validate,
                        new MessageContent(HttpStatus.FORBIDDEN.value(), validate, null));
            }
            // Check ABAC
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem lịch sử tài sản",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem lịch sử tài sản", null));
            } else {
//                List<String> type = params.get("types") != null ? Arrays.asList(params.get("types").split(",")) : null;
//                List<Integer> types = type.stream().map((item) -> Integer.valueOf(item)).collect(Collectors.toList());
//                List<String> status = params.get("status") != null ? Arrays.asList(params.get("status").split(",")) : null;
//                List<Integer> statusList = status.stream().map((item) -> Integer.valueOf(item)).collect(Collectors.toList());
                HistoryPropertyResponseDTO responseDTO = null;
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    String stages = dto.getUnit().getLisOfStage();
                    responseDTO = propertyService.findHistory(property,currentPage,rowsPerPage);
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                    } else {
                        response = new ResponseMessage(responseDTO.getStatus(),responseDTO.getMessage(),new MessageContent(responseDTO.getStatus(),responseDTO.getMessage(),null));
                    }

                } else {
                    responseDTO = propertyService.findHistory(property,currentPage,rowsPerPage);
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                    } else {
                        response = new ResponseMessage(responseDTO.getStatus(),responseDTO.getMessage(),new MessageContent(responseDTO.getStatus(),responseDTO.getMessage(),null));
                    }
                }

            }
        }

        return response;
    }

}
