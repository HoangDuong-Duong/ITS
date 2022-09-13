/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.shift.dto.ABACResponseDTO;
import com.elcom.its.shift.dto.AuthorizationResponseDTO;
import com.elcom.its.shift.model.Shift;
import com.elcom.its.shift.service.ShiftService;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * @author Admin
 */
@Controller
public class ShiftController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiftController.class);

    @Autowired
    private ShiftService shiftService;

    public ResponseMessage getAllShift(String requestUrl, String method, Map<String, String> headerMap, String urlParam)
            throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                Map<String, String> urlMap = StringUtil.getUrlParamValues(urlParam);

                String month = urlMap.get("month") != null ? (urlMap.get("month")) : getMonth(new Date());
                response = new ResponseMessage(new MessageContent(shiftService.findAll(month)));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách ca trực ",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách ca trực ", null));
            }
        }
        return response;
    }

    public ResponseMessage getAllShiftInternal(String requestUrl, String method, Map<String, String> headerMap, String urlParam)
            throws ExecutionException, InterruptedException {
        ResponseMessage response;
        Map<String, String> urlMap = StringUtil.getUrlParamValues(urlParam);

        String month = urlMap.get("month") != null ? (urlMap.get("month")) : getMonth(new Date());
        response = new ResponseMessage(new MessageContent(shiftService.findAll(month)));
        return response;
    }

    public ResponseMessage createShift(String requestUrl, String method, Map<String, String> headerMap, Map<String, Object> bodymap)
            throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO userDTO = authenToken(headerMap);
        if (userDTO == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, method, userDTO.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                String month = bodymap.get("month") != null ? (String) bodymap.get("month") : getMonth(new Date());
                List<Shift> listShiftRecord = createListShiftRecord(bodymap, userDTO);
                shiftService.deleteAllShift(month);
                shiftService.save(listShiftRecord);
                response = new ResponseMessage(new MessageContent(listShiftRecord));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thêm ca trực ",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thêm ca trực ", null));
            }
        }
        return response;
    }

    public ResponseMessage updateShift(String requestUrl, String method, Map<String, String> headerMap, String pathParam, Map<String, Object> bodymap)
            throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO userDTO = authenToken(headerMap);
        if (userDTO == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, method, userDTO.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                Shift shiftById = shiftService.findById(pathParam);
                if (shiftById == null) {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy ca trực với id " + pathParam,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy ca trực với id " + pathParam, null));
                } else {

                    Shift shiftRecord = createShiftRecord(bodymap);
                    shiftRecord.setId(pathParam);
                    shiftRecord.setModifiedBy(userDTO.getUuid());
                    shiftRecord.setModifiedDate(new Date());
                    shiftRecord.setStatus(shiftById.getStatus());
                    shiftService.save(shiftRecord);
                    response = new ResponseMessage(new MessageContent(shiftRecord));
                }

            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhật ca trực ",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhật ca trực ", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteShift(String requestUrl, String method, Map<String, String> headerMap, String pathParam)
            throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO userDTO = authenToken(headerMap);
        if (userDTO == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, method, userDTO.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                Shift shiftById = shiftService.findById(pathParam);
                if (shiftById == null) {
                    response = new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy ca trực với id " + pathParam,
                            new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy ca trực với id " + pathParam, null));
                } else {
                    shiftService.delete(shiftById);
                    response = new ResponseMessage(new MessageContent(shiftById));
                }

            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa ca trực ",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa ca trực ", null));
            }
        }
        return response;
    }

    private Shift createShiftRecord(Map<String, Object> bodyMap) {
        ObjectMapper mapper = new ObjectMapper();
        Shift shift = mapper.convertValue(bodyMap, new TypeReference<Shift>() {
        });
        shift.setId(UUID.randomUUID().toString());
        shift.setStartTimeFloat();
        shift.setEndTimeFloat();
        return shift;
    }

    private List<Shift> createListShiftRecord(Map<String, Object> bodyMap, AuthorizationResponseDTO userDTO) {
        ObjectMapper mapper = new ObjectMapper();
        List<Shift> listShifts = mapper.convertValue(bodyMap.get("listShifts"), new TypeReference<List<Shift>>() {
        });
        Date now = new Date();
        String month = bodyMap.get("month") != null ? (String) bodyMap.get("month") : getMonth(now);
        for (Shift shift : listShifts) {
            shift.setId(UUID.randomUUID().toString());
            shift.setStartTimeFloat();
            shift.setEndTimeFloat();
            shift.setCreatedBy(userDTO.getUuid());
            shift.setCreatedDate(now);
            shift.setStatus(1);
            shift.setMonth(month);
        }
        return listShifts;
    }

    private String getMonth(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
        return simpleDateFormat.format(date);
    }
}
