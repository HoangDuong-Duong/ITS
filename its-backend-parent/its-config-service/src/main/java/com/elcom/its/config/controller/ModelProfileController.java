/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.controller;

import com.elcom.its.config.model.ModelProfiles;
import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.service.ModelProfileService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author Administrator
 */
@Controller
public class ModelProfileController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelProfileController.class);
    
    @Autowired
    private ModelProfileService modelProfilesService;
    
    public ResponseMessage getModelProfilesList(Map<String, String> headerParam, String requestPath, 
            String method, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                List<ModelProfiles> processUnitList = modelProfilesService.getAll();
                if (CollectionUtils.isEmpty(processUnitList)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                            HttpStatus.OK.toString(), null));
                } else {
                    response = new ResponseMessage(new MessageContent(processUnitList));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách Model Profiles", null));
            }
        }
        return response;
    }

    public ResponseMessage getModelProfilesById(Map<String, String> headerParam, String requestPath, 
            String method, String pathParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "DETAIL", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Optional<ModelProfiles> processUnit = modelProfilesService.findById(Long.valueOf(pathParam));
                if (processUnit.isPresent()) {
                    response = new ResponseMessage(new MessageContent(processUnit.get()));

                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(),
                            HttpStatus.NOT_FOUND.toString(), null));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách Model Profiles", null));
            }
        }
        return response;
    }
}
