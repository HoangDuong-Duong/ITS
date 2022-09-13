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
import com.elcom.its.shift.model.UserShift;
import com.elcom.its.shift.service.UserShiftService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import com.elcom.its.shift.dto.UserShiftGroupByStage;
import com.elcom.its.shift.dto.UserShiftGroupByUser;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Admin
 */
@Controller
public class UserShiftController extends BaseController {

    @Autowired
    private UserShiftService userShiftService;

    public ResponseMessage createUserShift(String requestUrl, String method, Map<String, String> headerMap, Map<String, Object> bodymap)
            throws ExecutionException, InterruptedException, ParseException {
        ResponseMessage response;
        AuthorizationResponseDTO userDTO = authenToken(headerMap);
        if (userDTO == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, method, userDTO.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = dateFormat.parse((String) bodymap.get("startTime"));
                Date endDate = dateFormat.parse((String) bodymap.get("endTime"));
                String groupCode =(String) bodymap.get("groupCode");
                userShiftService.deleteByGroupCodeAndDayBetween(groupCode,startDate, endDate);
                List<UserShift> userShiftRecord = createUserShiftRecord(bodymap, userDTO);
                userShiftService.save(userShiftRecord);
                response = new ResponseMessage(new MessageContent(userShiftRecord));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thêm ca trực ",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thêm ca trực ", null));
            }
        }
        return response;
    }
    

    public ResponseMessage getListUserShift(String requestUrl, String method, Map<String, String> headerMap, String urlParam)
            throws ExecutionException, InterruptedException, ParseException {
        ResponseMessage response;
        AuthorizationResponseDTO userDTO = authenToken(headerMap);
        if (userDTO == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", userDTO.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                Map<String, String> urlMap = StringUtil.getUrlParamValues(urlParam);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = dateFormat.parse((String) urlMap.get("startTime"));
                Date endDate = dateFormat.parse((String) urlMap.get("endTime"));
                String groupCode = urlMap.get("groupCode");
                response = new ResponseMessage(new MessageContent(userShiftService.getUserShiftResponse(startDate, endDate, groupCode)));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách ca trực ",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách ca trực ", null));
            }
        }
        return response;
    }

    public ResponseMessage getListUserOnShift(String requestUrl, String method, Map<String, String> headerMap, String urlParam)
            throws ExecutionException, InterruptedException, ParseException {
        Map<String, String> urlMap = StringUtil.getUrlParamValues(urlParam);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse((String) urlMap.get("date"));
        String siteId = (String) urlMap.get("siteId");
        return new ResponseMessage(new MessageContent(userShiftService.getListUserIdOnShift(date, siteId)));
    }

    private List<UserShift> createUserShiftRecord(Map<String, Object> bodyMap, AuthorizationResponseDTO userDTO) {
        ObjectMapper mapper = new ObjectMapper();
        List<UserShiftGroupByStage> listUserShiftGroupByStage = mapper.convertValue(bodyMap.get("listUserShiftGroupByStage"),
                new TypeReference< List<UserShiftGroupByStage>>() {
        }
        );
        String groupCode = (String) bodyMap.get("groupCode");
        List<UserShift> returnList = new ArrayList<>();
        Date now = new Date();
        for (UserShiftGroupByStage userShiftGroupByStage : listUserShiftGroupByStage) {
            for (UserShiftGroupByUser userShiftGroupByUser : userShiftGroupByStage.getListUserShiftGroupByUser()) {
                userShiftGroupByUser.getListUserShift().stream().forEach(
                        userShift -> {
                            userShift.setId(UUID.randomUUID().toString());
                            userShift.setUserId(userShiftGroupByUser.getUserId());
                            userShift.setUsername(userShiftGroupByUser.getUsername());
                            userShift.setGroupCode(groupCode);
                            userShift.setStageCode(userShiftGroupByStage.getStageCode());
                            userShift.setCreatedBy(userDTO.getUuid());
                            userShift.setCreatedDate(now);
                        }
                );
                returnList.addAll(userShiftGroupByUser.getListUserShift());
            }
        }
        return returnList;
    }
}
