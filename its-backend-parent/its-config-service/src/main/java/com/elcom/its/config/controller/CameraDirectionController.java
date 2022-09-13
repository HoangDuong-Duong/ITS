package com.elcom.its.config.controller;

import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.DirectionDto;
import com.elcom.its.config.service.LaneRouteService;
import com.elcom.its.config.service.impl.LaneRouteListMessage;
import com.elcom.its.config.tools.CameraDtoUtils;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Set;

@Controller
public class CameraDirectionController extends BaseController{
    private Logger logger = LoggerFactory.getLogger(CameraDirectionController.class);

    @Autowired
    private LaneRouteService laneRouterService;

    public ResponseMessage getAllCameraDirection(Map<String, String> headerParam,
                                                 String requestPath,
                                                 String method,
                                                 String urlParam,
                                                 Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                LaneRouteListMessage response = laneRouterService.getAllLaneRoutes();

                Set<DirectionDto> directionDtos = CameraDtoUtils.getDirections(response);

                logger.info("Lane route: " + directionDtos);

                responseMessage = new ResponseMessage(new MessageContent(
                        response.getStatus(),
                        response.getMessage(),
                        directionDtos
                ));
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền lấy danh sách hướng camera", null));
            }
        }

        return responseMessage;
    }
}
