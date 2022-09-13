/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.cabinet.controller;

import com.elcom.its.cabinet.dto.AuthorizationResponseDTO;
import com.elcom.its.cabinet.dto.ABACResponseDTO;
import com.elcom.its.cabinet.dto.Response;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import com.elcom.its.cabinet.service.ElectricCabinetService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Admin
 */
@Controller
public class ElectricCabinetController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElectricCabinetController.class);

    @Autowired
    private ElectricCabinetService electricCabinetService;

    public ResponseMessage getListElectricCabinet(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                Response getListResponse = electricCabinetService.getListElectricCabinet(urlParam, method, resultCheckABAC.getAdmin() != null ? resultCheckABAC.getAdmin() : false);
                response = new ResponseMessage(new MessageContent(getListResponse.getStatus(), getListResponse.getMessage(), getListResponse.getData(), getListResponse.getTotal()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách tủ điẹn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách tủ điện", null));
            }
        }
        return response;
    }

    public ResponseMessage createElectricCabinet(String requestUrl, String method, String urlParam, Map<String, String> headerMap, Map<String, Object> bodyMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                bodyMap.put("createdBy", dto.getUuid());
                bodyMap.put("lastActionBy", dto.getUuid());
                bodyMap.put("id", UUID.randomUUID().toString());
                Response createCabinetResponse = electricCabinetService.createElectricCabinet(bodyMap);
                response = new ResponseMessage(new MessageContent(createCabinetResponse.getStatus(), createCabinetResponse.getMessage(), createCabinetResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thêm mới tủ điẹn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thêm mới tủ điện", null));
            }
        }
        return response;
    }

    public ResponseMessage updateElectricCabinet(String requestUrl, String method, String urlParam, Map<String, String> headerMap, Map<String, Object> bodyMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "PUT", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                bodyMap.put("lastActionBy", dto.getUuid());
                Response createCabinetResponse = electricCabinetService.updateElectricCabinet(bodyMap);
                response = new ResponseMessage(new MessageContent(createCabinetResponse.getStatus(), createCabinetResponse.getMessage(), createCabinetResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền chỉnh sửa tủ điẹn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền chỉnh sửa tủ điện", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteElectricCabinet(String requestUrl, String method, String urlParam, Map<String, String> headerMap, Map<String, Object> bodyMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DELETE", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                ObjectMapper mapper = new ObjectMapper();
                List<String> listIds = mapper.convertValue(bodyMap.get("listIds"), new TypeReference<List<String>>() {
                });
                Response deleteCabinetResponse = electricCabinetService.deleteElectricCabinet(listIds);
                response = new ResponseMessage(new MessageContent(deleteCabinetResponse.getStatus(), deleteCabinetResponse.getMessage(), deleteCabinetResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điẹn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điện", null));
            }
        }
        return response;
    }

    public ResponseMessage turnOnFanCabinet(String requestUrl, String method, String urlParam, Map<String, String> headerMap, Map<String, Object> bodyMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                ObjectMapper mapper = new ObjectMapper();
                List<String> listIds = mapper.convertValue(bodyMap.get("listIds"), new TypeReference<List<String>>() {
                });
                Response processCabinetResponse = electricCabinetService.processTurnOnFan(listIds);
                response = new ResponseMessage(new MessageContent(processCabinetResponse.getStatus(), processCabinetResponse.getMessage(), processCabinetResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điẹn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điện", null));
            }
        }
        return response;
    }

    public ResponseMessage turnOffFanCabinet(String requestUrl, String method, String urlParam, Map<String, String> headerMap, Map<String, Object> bodyMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                ObjectMapper mapper = new ObjectMapper();
                List<String> listIds = mapper.convertValue(bodyMap.get("listIds"), new TypeReference<List<String>>() {
                });
                Response processCabinetResponse = electricCabinetService.processTurnOffFan(listIds);
                response = new ResponseMessage(new MessageContent(processCabinetResponse.getStatus(), processCabinetResponse.getMessage(), processCabinetResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điẹn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điện", null));
            }
        }
        return response;
    }

    public ResponseMessage turnOnFireAlarmCabinet(String requestUrl, String method, String urlParam, Map<String, String> headerMap, Map<String, Object> bodyMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                ObjectMapper mapper = new ObjectMapper();
                List<String> listIds = mapper.convertValue(bodyMap.get("listIds"), new TypeReference<List<String>>() {
                });
                Response processCabinetResponse = electricCabinetService.processTurnOnFireAlarm(listIds);
                response = new ResponseMessage(new MessageContent(processCabinetResponse.getStatus(), processCabinetResponse.getMessage(), processCabinetResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điẹn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điện", null));
            }
        }
        return response;
    }

    public ResponseMessage turnOffFireAlarm(String requestUrl, String method, String urlParam, Map<String, String> headerMap, Map<String, Object> bodyMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                ObjectMapper mapper = new ObjectMapper();
                List<String> listIds = mapper.convertValue(bodyMap.get("listIds"), new TypeReference<List<String>>() {
                });
                Response processCabinetResponse = electricCabinetService.processTurnOffFireAlarm(listIds);
                response = new ResponseMessage(new MessageContent(processCabinetResponse.getStatus(), processCabinetResponse.getMessage(), processCabinetResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điẹn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điện", null));
            }
        }
        return response;
    }

    public ResponseMessage openDoor(String requestUrl, String method, String urlParam, Map<String, String> headerMap, Map<String, Object> bodyMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                ObjectMapper mapper = new ObjectMapper();
                List<String> listIds = mapper.convertValue(bodyMap.get("listIds"), new TypeReference<List<String>>() {
                });
                Response processCabinetResponse = electricCabinetService.processOpenDoor(listIds);
                response = new ResponseMessage(new MessageContent(processCabinetResponse.getStatus(), processCabinetResponse.getMessage(), processCabinetResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điẹn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điện", null));
            }
        }
        return response;
    }

    public ResponseMessage closeDoor(String requestUrl, String method, String urlParam, Map<String, String> headerMap, Map<String, Object> bodyMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                ObjectMapper mapper = new ObjectMapper();
                List<String> listIds = mapper.convertValue(bodyMap.get("listIds"), new TypeReference<List<String>>() {
                });
                Response processCabinetResponse = electricCabinetService.processCloseDoor(listIds);
                response = new ResponseMessage(new MessageContent(processCabinetResponse.getStatus(), processCabinetResponse.getMessage(), processCabinetResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điẹn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa tủ điện", null));
            }
        }
        return response;
    }

}
