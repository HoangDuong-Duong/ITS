/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.controller;

import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.service.PlaceService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class PlaceController extends BaseController {

    private Logger log = LoggerFactory.getLogger(PlaceController.class);

    @Autowired
    private PlaceService placeService;

    public ResponseMessage getAllPlace(String requestUrl, String method, String urlParam, Map<String, String> headerMap) {
        ResponseMessage response;

        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestUrl);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                boolean adminBackend = abacResponseDTO.getAdmin() != null ? abacResponseDTO.getAdmin() : false;
                String stageIds = dto.getUnit() != null ? dto.getUnit().getLisOfStage() : null;
                response = new ResponseMessage(new MessageContent(placeService.getAllPlace(urlParam, adminBackend, stageIds).getData()));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền lấy thông tin đối tượng theo dõi", null));
            }
        }
        return response;
    }
}
