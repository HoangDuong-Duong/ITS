package com.elcom.its.report.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.report.model.dto.ABACResponseDTO;
import com.elcom.its.report.model.dto.AuthorizationResponseDTO;
import com.elcom.its.report.service.TrafficJamHistoryService;
import com.elcom.its.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class TrafficJamHistoryController extends BaseController {

    @Autowired
    TrafficJamHistoryService trafficJamHistoryService;

    public ResponseMessage getTrafficFlowReport(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                boolean adminBackend = abacResponseDTO.getAdmin() != null ? abacResponseDTO.getAdmin() : false;
                response = new ResponseMessage(new MessageContent(trafficJamHistoryService.getTrafficJamReport(urlParam,adminBackend, dto.getUnit().getLisOfStage())));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo điểm đen", null));
            }
        }
        return response;
    }

    public ResponseMessage getTrafficTimeLine(String requestPath, Map<String, String> headerParam,
                                                Map<String, Object> bodyParam, String requestMethod, String pathParam, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                boolean adminBackend = abacResponseDTO.getAdmin() != null ? abacResponseDTO.getAdmin() : false;
                response = new ResponseMessage(new MessageContent(trafficJamHistoryService.getTrafficTimeLine(urlParam,adminBackend, dto.getUnit().getLisOfStage())));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo điểm đen", null));
            }
        }
        return response;
    }

    public ResponseMessage getTrafficSort(String requestPath, Map<String, String> headerParam,
                                              Map<String, Object> bodyParam, String requestMethod, String pathParam, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                boolean adminBackend = abacResponseDTO.getAdmin() != null ? abacResponseDTO.getAdmin() : false;
                response = new ResponseMessage(new MessageContent(trafficJamHistoryService.getTrafficSort(urlParam,adminBackend, dto.getUnit().getLisOfStage())));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo điểm đen", null));
            }
        }
        return response;
    }

    public ResponseMessage getTrafficJamMonitoringData(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam, String urlParam) {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                boolean adminBackend = abacResponseDTO.getAdmin() != null ? abacResponseDTO.getAdmin() : false;
                response = new ResponseMessage(new MessageContent(trafficJamHistoryService.getTrafficJamMonitoringData(urlParam, adminBackend, dto.getUnit().getLisOfStage())));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem thông tin giám sát tắc đường", null));
            }
        }
        return response;
    }

    public ResponseMessage getTrafficJamMonitoring(String requestPath, Map<String, String> headerParam,
                                                       Map<String, Object> bodyParam, String requestMethod, String pathParam, String urlParam) {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        Integer page = 0;
        Integer size = 20;
        Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
        page = Integer.parseInt(params.get("page"));
        size = Integer.parseInt(params.get("size"));
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                boolean adminBackend = abacResponseDTO.getAdmin() != null ? abacResponseDTO.getAdmin() : false;
                response = new ResponseMessage(new MessageContent(trafficJamHistoryService.getTrafficJamMonitoring(urlParam, adminBackend, dto.getUnit().getLisOfStage(),size,page)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem thông tin giám sát tắc đường", null));
            }
        }
        return response;
    }
}
