/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vms.controller;

import com.elcom.its.vms.dto.AuthorizationResponseDTO;
import com.elcom.its.vms.dto.ABACResponseDTO;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.elcom.its.vms.dto.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import com.elcom.its.vms.service.VmsService;

/**
 *
 * @author Admin
 */
@Controller
public class VmsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VmsController.class);

    @Autowired
    private VmsService vmsService;

    public ResponseMessage getLiveImage(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, Object> body = new HashMap<String, Object>();
            ABACResponseDTO abacStatus = authorizeABAC(body, "DETAIL", dto.getUuid(), requestUrl);
            if (abacStatus.getStatus()) {
                Response liveImageResponse = vmsService.getLiveImage(urlParam);
                response = new ResponseMessage(new MessageContent(liveImageResponse.getStatus(),liveImageResponse.getMessage(),liveImageResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách thiết bị ",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách thiết bị ", null));
            }
        }
        return response;
    }

    public ResponseMessage cutVideo(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, Object> body = new HashMap<String, Object>();
            ABACResponseDTO abacStatus = authorizeABAC(body, "DETAIL", dto.getUuid(), requestUrl);
            if (abacStatus.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String startTime = params.get("startTime");
                String endTime = params.get("endTime");
                String cameraId = params.get("cameraId");
                Response cutVideoResponse = vmsService.cutVideo(startTime,endTime,cameraId);
                response = new ResponseMessage(new MessageContent(cutVideoResponse.getStatus(),cutVideoResponse.getMessage(),cutVideoResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem chi tiết sự kiện phát hiện",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem chi tiết sự kiện phát hiện", null));
            }
        }
        return response;
    }

}
