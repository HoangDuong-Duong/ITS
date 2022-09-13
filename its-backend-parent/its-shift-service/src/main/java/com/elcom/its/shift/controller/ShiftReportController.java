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
import com.elcom.its.shift.service.ShiftNotificationService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class ShiftReportController extends BaseController {

    @Autowired
    private ShiftNotificationService shiftNotificationService;

    public ResponseMessage getShiftNotification(Map<String, String> headerMap)
            throws ExecutionException, InterruptedException, ParseException {
        ResponseMessage response;
        AuthorizationResponseDTO userDTO = authenToken(headerMap);
        if (userDTO == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(new MessageContent(shiftNotificationService.getDailyEventReport(userDTO.getUuid(), userDTO.getUnit().getUuid())));
        }
        return response;
    }
}
