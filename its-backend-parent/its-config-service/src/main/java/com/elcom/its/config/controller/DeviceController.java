/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.controller;

import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.elcom.its.config.service.DeviceService;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class DeviceController extends BaseController {

    @Autowired
    private DeviceService deviceService;

    public ResponseMessage getAllSystemDevices(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                if (resultCheckABAC.getAdmin() != null && resultCheckABAC.getAdmin()) {
                    response = new ResponseMessage(new MessageContent(deviceService.getAllSystemDevices().getData()));
                } else {
                    String stageIds = dto.getUnit().getLisOfStage();
                    response = new ResponseMessage(new MessageContent(deviceService.getSystemDevicesInStages(stageIds).getData()));
                }
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách thiết bị ",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách thiết bị ", null));
            }
        }
        return response;
    }
}
