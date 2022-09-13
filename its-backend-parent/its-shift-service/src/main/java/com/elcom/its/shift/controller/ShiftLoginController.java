/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.shift.dto.AuthorizationResponseDTO;
import com.elcom.its.shift.model.LoginHistory;
import com.elcom.its.shift.model.LoginHistoryPK;
import com.elcom.its.shift.service.LoginHistoryService;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class ShiftLoginController extends BaseController {
    
    @Autowired
    private LoginHistoryService loginHistoryService;
    
    public ResponseMessage processLogin(Map<String, String> headerMap)
            throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO userDTO = authenToken(headerMap);
        if (userDTO == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            LoginHistory loginHistory = loginHistoryService.findLastLoginHistory(userDTO.getUuid());
            if (loginHistory == null || loginHistory.isExpired()) {
                LoginHistory newLoginHistoryRecord = createLoginHistoryRecord(userDTO);
                loginHistoryService.save(newLoginHistoryRecord);
            } else {
                loginHistory.setLastRequest(new Date());
                loginHistory.setUserFullName(userDTO.getFullName());
                loginHistoryService.save(loginHistory);
            }
            response = new ResponseMessage(new MessageContent("OK"));
        }
        return response;
    }
    
    private LoginHistory createLoginHistoryRecord(AuthorizationResponseDTO userDTO) {
        LoginHistory loginHistory = new LoginHistory();
        Date now = new Date();
        loginHistory.setLoginHistoryPK(new LoginHistoryPK(UUID.randomUUID().toString(), now));
        loginHistory.setLastRequest(now);
        loginHistory.setUserId(userDTO.getUuid());
        loginHistory.setUsername(userDTO.getUserName());
        loginHistory.setUserFullName(userDTO.getFullName());
        return loginHistory;
    }
    
}
