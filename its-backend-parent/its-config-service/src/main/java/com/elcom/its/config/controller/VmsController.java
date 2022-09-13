/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.controller;

import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.VmsService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class VmsController extends BaseController {

    @Autowired
    private VmsService vmsService;

    public ResponseMessage getAllVms(Map<String, String> headerParam, String requestPath,
            String method, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, Object> body = new HashMap<String, Object>();
            ABACResponseDTO abacStatus = authorizeABAC(body, "LIST", dto.getUuid(), requestPath);
            if (abacStatus.getStatus()) {
                Response getAllResponse = vmsService.getAllVms(urlParam);
                response = new ResponseMessage(new MessageContent(getAllResponse.getStatus(),
                        getAllResponse.getMessage(), getAllResponse.getData(),getAllResponse.getTotal()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách VMS",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách VMS", null));
            }
        }
        return response;
    }

    public ResponseMessage findVmsById(Map<String, String> headerParam, String requestPath,
            String method, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, Object> body = new HashMap<String, Object>();
            ABACResponseDTO abacStatus = authorizeABAC(body, "DETAIL", dto.getUuid(), requestPath);
            if (abacStatus.getStatus()) {
                Response getByIdResponse = vmsService.findVmsById(pathParam);
                response = new ResponseMessage(new MessageContent(getByIdResponse.getStatus(),
                        getByIdResponse.getMessage(), getByIdResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem thông tin VMS",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem thông tin VMS", null));
            }
        }
        return response;
    }

    public ResponseMessage createVms(Map<String, String> headerParam, String requestPath,
            String method, String pathParam, Map<String, Object> bodyMap) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, Object> body = new HashMap<String, Object>();
            ABACResponseDTO abacStatus = authorizeABAC(body, "POST", dto.getUuid(), requestPath);
            if (abacStatus.getStatus()) {
                Response createVmsresponse = vmsService.createVms(bodyMap, dto);
                response = new ResponseMessage(new MessageContent(createVmsresponse.getStatus(),
                        createVmsresponse.getMessage(), createVmsresponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thêm mới VMS",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thêm mới VMS", null));
            }
        }
        return response;
    }

    public ResponseMessage updateVms(Map<String, String> headerParam, String requestPath,
            String method, String pathParam, Map<String, Object> bodyMap) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, Object> body = new HashMap<String, Object>();
            ABACResponseDTO abacStatus = authorizeABAC(body, "PUT", dto.getUuid(), requestPath);
            if (abacStatus.getStatus()) {
                Response updateResponse = vmsService.updateVms(bodyMap, pathParam);
                response = new ResponseMessage(new MessageContent(updateResponse.getStatus(),
                        updateResponse.getMessage(), updateResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhật VMS",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhật VMS", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteVms(Map<String, String> headerParam, String requestPath,
            String method, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, Object> body = new HashMap<String, Object>();
            ABACResponseDTO abacStatus = authorizeABAC(body, "DELETE", dto.getUuid(), requestPath);
            if (abacStatus.getStatus()) {
                Response deleteResponse = vmsService.deleteVms(pathParam);
                response = new ResponseMessage(new MessageContent(deleteResponse.getStatus(),
                        deleteResponse.getMessage(), deleteResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa VMS",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa VMS", null));
            }
        }
        return response;
    }

    public ResponseMessage checkConnectionById(Map<String, String> headerParam, String requestPath,
            String method, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, Object> body = new HashMap<String, Object>();
            ABACResponseDTO abacStatus = authorizeABAC(body, "DETAIL", dto.getUuid(), requestPath);
            if (abacStatus.getStatus()) {
                Response checkConnectionResponse = vmsService.checkConnectionById(urlParam);
                response = new ResponseMessage(new MessageContent(checkConnectionResponse.getStatus(),
                        checkConnectionResponse.getMessage(), checkConnectionResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền kiểm tra kết nối VMS",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền kiểm tra kết nối VMS", null));
            }
        }
        return response;
    }

    public ResponseMessage checkConnectionByInfo(Map<String, String> headerParam, String requestPath,
            String method, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, Object> body = new HashMap<String, Object>();
            ABACResponseDTO abacStatus = authorizeABAC(body, "DETAIL", dto.getUuid(), requestPath);
            if (abacStatus.getStatus()) {
                
                Response checkConnectionResponse = vmsService.checkConnectionByInfo(urlParam);
                response = new ResponseMessage(new MessageContent(checkConnectionResponse.getStatus(),
                        checkConnectionResponse.getMessage(), checkConnectionResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền kiếm tra kết nối VMS",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền kiếm tra kết nối VMS", null));
            }
        }
        return response;
    }

    public ResponseMessage loadVmsCamera(Map<String, String> headerParam, String requestPath,
            String method, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, Object> body = new HashMap<String, Object>();
            ABACResponseDTO abacStatus = authorizeABAC(body, "DETAIL", dto.getUuid(), requestPath);
            if (abacStatus.getStatus()) {
                Response loadCameraResponse = vmsService.loadVmsCamera(urlParam);
                response = new ResponseMessage(new MessageContent(loadCameraResponse.getStatus(),
                        loadCameraResponse.getMessage(), loadCameraResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách VMS camera",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách VMS camera", null));
            }
        }
        return response;
    }

    public ResponseMessage addMultiCamera(Map<String, String> headerParam, String requestPath,
            String method, String pathParam, Map<String, Object> bodyMap) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, Object> body = new HashMap<String, Object>();
            ABACResponseDTO abacStatus = authorizeABAC(body, "POST", dto.getUuid(), requestPath);
            if (abacStatus.getStatus()) {
                Response addMultiCameraResponse = vmsService.addMultiCamera(bodyMap);
                response = new ResponseMessage(new MessageContent(addMultiCameraResponse.getStatus(),
                        addMultiCameraResponse.getMessage(), addMultiCameraResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thêm camera vào hệ thống",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy thêm camera vào hệ thống", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteMulti(Map<String, String> headerParam, String requestPath,
            String method, String pathParam, Map<String, Object> bodyMap) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, Object> body = new HashMap<String, Object>();
            ABACResponseDTO abacStatus = authorizeABAC(body, "DELETE", dto.getUuid(), requestPath);
            if (abacStatus.getStatus()) {
                Response addMultiCameraResponse = vmsService.deleteMulti(bodyMap);
                response = new ResponseMessage(new MessageContent(addMultiCameraResponse.getStatus(),
                        addMultiCameraResponse.getMessage(), addMultiCameraResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa VMS",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa VMS", null));
            }
        }
        return response;
    }
}
