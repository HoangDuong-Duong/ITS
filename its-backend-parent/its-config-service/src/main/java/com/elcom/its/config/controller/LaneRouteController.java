/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.controller;

import com.elcom.its.config.model.ImageCamera;
import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.LaneRouteService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author Admin
 */
@Controller
public class LaneRouteController extends BaseController {

    @Autowired
    private LaneRouteService laneRouteService;

    public ResponseMessage getAllLane(Map<String, String> headerParam, String requestPath,
            String method, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Response getLaneResponse = laneRouteService.getAllLane(urlParam);
                response = new ResponseMessage(new MessageContent(getLaneResponse.getStatus(), getLaneResponse.getMessage(), getLaneResponse.getData()));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem làn đường", null));
            }
        }
        return response;
    }

}
