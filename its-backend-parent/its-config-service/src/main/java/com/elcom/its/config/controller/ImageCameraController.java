/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.controller;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.model.ImageCamera;
import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.CameraDetailDTOMessage;
import com.elcom.its.config.service.ITSCoreCameraService;
import com.elcom.its.config.service.ImageCameraService;
import com.elcom.its.config.thread.ThreadManager;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

/**
 *
 * @author Administrator
 */
@Controller
public class ImageCameraController extends BaseController {

    @Autowired
    private ImageCameraService imgaCameraService;

    @Autowired
    private ITSCoreCameraService cameraService;
    
    @Autowired
    private ThreadManager threadManager;

    public ResponseMessage getImageByCameraId(Map<String, String> headerParam, String requestPath,
            String method, String cameraId, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, method, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                List<ImageCamera> imageCameras = imgaCameraService.findByCameraId(cameraId);
                if (CollectionUtils.isEmpty(imageCameras)) {
                    return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                }
                response = new ResponseMessage(new MessageContent(imageCameras));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem Image Camera theo Camera Id", null));
            }
        }
        return response;
    }

    public ResponseMessage createImageByCameraId(Map<String, String> headerParam, Map<String, Object> bodyParam,
            String requestPath, String method) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, method, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                String urlImage = (String) bodyParam.get("urlImage");
                String cameraId = (String) bodyParam.get("cameraId");
                ImageCamera imgCamera = new ImageCamera(UUID.randomUUID().toString(), cameraId, urlImage);
                imgCamera.setCreatedBy(dto.getUuid());
                imgCamera.setCreatedDate(new Date());
                imgCamera.setModifiedBy(dto.getUuid());
                imgCamera.setModifiedDate(new Date());
                
                CameraDetailDTOMessage cameraDetailDTOMessage = cameraService.getCamerasByIdFromDBM(cameraId);
                if (cameraDetailDTOMessage == null || cameraDetailDTOMessage.getData() == null) {
                    return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy Camera", null));
                }
                imgCamera = imgaCameraService.create(imgCamera);
                threadManager.execute(() -> {
                    String cameraImage = ApplicationConfig.LAYOUT_ROOT_LINK + urlImage;
                    cameraService.updateImageUrlCamerasFromDBM(cameraId, cameraImage);
                });
                response = new ResponseMessage(new MessageContent(imgCamera));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền tạo Image Camera", null));
            }
        }
        return response;
    }
}
