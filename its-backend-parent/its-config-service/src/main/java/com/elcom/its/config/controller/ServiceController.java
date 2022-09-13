/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.controller;

import com.elcom.its.config.model.Services;
import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.service.ServiceManagerService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author Administrator
 */
@Controller
public class ServiceController extends BaseController {

    @Autowired
    private ServiceManagerService serviceMangManagerService;

    public ResponseMessage getServicesList(Map<String, String> headerParam, String requestPath, 
            String method, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, method, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                List<Services> servicesList = serviceMangManagerService.getAll();
                if (CollectionUtils.isEmpty(servicesList)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(),
                            HttpStatus.NOT_FOUND.toString(), null));
                } else {
                    response = new ResponseMessage(new MessageContent(servicesList.stream()
                            .filter(x -> x.getStatus() == 1).collect(Collectors.toList())));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách dịch vụ", null));
            }
        }
        return response;
    }

    public ResponseMessage getServicesById(Map<String, String> headerParam, String requestPath, 
            String method, String pathParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, method, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Optional<Services> services = serviceMangManagerService.findById(Long.valueOf(pathParam));
                if (services.isPresent()) {
                    response = new ResponseMessage(new MessageContent(services.get()));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(),
                            HttpStatus.NOT_FOUND.toString(), null));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách dịch vụ", null));
            }
        }
        return response;
    }
}
