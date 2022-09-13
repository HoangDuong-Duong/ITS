/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.controller;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.enums.DataStatus;
import com.elcom.its.config.model.*;
import com.elcom.its.config.model.dto.*;
import com.elcom.its.config.service.ITSCoreCameraService;
import com.elcom.its.config.service.ProcessUnitService;
import com.elcom.its.config.service.PuVideoThreadsService;
import com.elcom.its.config.service.CameraLayoutService;
import com.elcom.its.config.validation.PuVideoThreadsValidation;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import com.elcom.its.config.exception.ResourceNotFoundException;
import com.elcom.its.config.thread.ThreadManager;

/**
 *
 * @author Administrator
 */
@Controller
public class PuVideoThreadsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PuVideoThreadsController.class);

    @Autowired
    private PuVideoThreadsService puVideoThreadssService;

    @Autowired
    private ITSCoreCameraService cameraService;

    @Autowired
    private ProcessUnitService processUnitService;

    @Autowired
    private CameraLayoutService cameraLayoutService;

    @Autowired
    private ThreadManager threadManager;

    public ResponseMessage getPuVideoThreadsByProcessUnitId(Map<String, String> headerParam,
            String requestPath, String method, String processUnitId, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, method, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                List<PuVideoThreads> puVideoThreadses = puVideoThreadssService.findByPuVideoThreadsByProcessUnitId(processUnitId);

                if (!CollectionUtils.isEmpty(puVideoThreadses)) {
                    List<PuVideoThreadsDTO> proDtos = new ArrayList<>();
                    for (PuVideoThreads puVideoThreads : puVideoThreadses) {
                        LOGGER.info("Convert PuVideoThreadsDto for puVideoThread: {}", puVideoThreads.getId());
                        PuVideoThreadsDTO proDto = PuVideoThreadsDTO.fromModel(puVideoThreads);
                        CameraDetailDTOMessage camerasResponse = cameraService.getCamerasByIdFromDBM(puVideoThreads.getCameraId());
                        if (camerasResponse != null && camerasResponse.getData() != null) {
                            proDto.setCameraName(camerasResponse.getData().getName());
                        }
                        proDtos.add(proDto);
                    }
                    response = new ResponseMessage(new MessageContent(proDtos));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                            HttpStatus.OK.toString(), null));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách Luồng video", null));
            }
        }
        return response;
    }

    public ResponseMessage createPuVideoThreads(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                String cameraId = (String) bodyParam.get("cameraId");
                Long layoutId = ((Number) bodyParam.get("layoutId")).longValue();
                Map<String, Object> capability = (Map<String, Object>) bodyParam.get("capability");
                Map<String, Object> detectors = (Map<String, Object>) bodyParam.get("detectors");
                String cacheUrl = (String) bodyParam.get("cacheUrl");
                int cacheLength = (int) bodyParam.get("cacheLength");
                int autoAdjust = (int) bodyParam.get("autoAdjust");
                Map<String, Object> render = (Map<String, Object>) bodyParam.get("render");
                String idProcessUnit = (String) bodyParam.get("idProcessUnit");
                PuVideoThreadsDTO puVideoThreadssDto = new PuVideoThreadsDTO(cameraId, layoutId, capability, detectors, cacheUrl, cacheLength, autoAdjust, render, DataStatus.ENABLE, idProcessUnit);

                String invalidData = new PuVideoThreadsValidation().validateInsertPuVideoThreads(puVideoThreadssDto);
                if (invalidData != null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                } else {
                    ProcessUnit processUnit = processUnitService.findById(idProcessUnit).orElseThrow(() -> new ResourceNotFoundException(ProcessUnit.class, idProcessUnit));
                    CameraDTO camera = null;
                    CameraDetailDTOMessage camerasResponse = cameraService.getCamerasByIdFromDBM(puVideoThreadssDto.getCameraId());
                    if (camerasResponse != null && camerasResponse.getData() != null) {
                        camera = camerasResponse.getData();
                    } else {
                        camera = new CameraDTO();
                    }

                    if (puVideoThreadssService.existsByCameraIdAndProcessId(cameraId, idProcessUnit)) {
                        return new ResponseMessage(new MessageContent(HttpStatus.CONFLICT.value(), "Đã tồn tại Camera này", null));
                    }
                    if (puVideoThreadssService.existsByLayoutIdAndCameraIdAndProcessId(layoutId, puVideoThreadssDto.getCameraId(), idProcessUnit)) {
                        return new ResponseMessage(new MessageContent(HttpStatus.CONFLICT.value(), "Đã tồn tại layout và camera", null));
                    }

                    CameraLayouts layout = null;
                    if (puVideoThreadssDto.getLayoutId() != 0) {
                        layout = cameraLayoutService.findById(puVideoThreadssDto.getLayoutId()).orElseThrow(() -> new ResourceNotFoundException(CameraLayouts.class, puVideoThreadssDto.getLayoutId()));
                    }
                    PuVideoThreads entity = toEntity(puVideoThreadssDto, idProcessUnit);
                    entity.setStatus(DataStatus.ENABLE.code());
                    entity.setCreatedBy(dto.getUuid());
                    entity.setCreatedDate(new Date());
                    entity.setModifiedBy(dto.getUuid());
                    entity.setModifiedDate(new Date());
                    entity = puVideoThreadssService.create(entity);
                    response = new ResponseMessage(new MessageContent(HttpStatus.CREATED.value(), HttpStatus.CREATED.toString(), entity));

                    String renderUrl = null;
                    if (render != null && render.containsKey("renderUrl")) {
                        renderUrl = (String) render.get("renderUrl");
                    }
                    if (camera != null && !StringUtil.isNullOrEmpty(renderUrl)) {
                        LOGGER.info("===> renderUrl: {}", renderUrl);
                        //renderUrl: rtmp://192.168.51.193/live/cgnb
                        //=> hlsUrl: http://192.168.51.193:8181/hls/cgnb.m3u8
                        //=> Index : hlsUrl: http://10.20.26.7:8181/hls/cgnb/index.m3u8
                        String hlsUrl = renderUrl.split("\\?")[0].replace("rtmp://", ApplicationConfig.HLS_PROTOCOL + "://")
                                .replace("rtsp://", ApplicationConfig.HLS_PROTOCOL + "://")
                                .replace("/live/", ":" + ApplicationConfig.HLS_PORT + "/hls/") + ".m3u8";
                        if ("index".equalsIgnoreCase(ApplicationConfig.HLS_TYPE)) {
                            hlsUrl = renderUrl.split("\\?")[0].replace("rtmp://", ApplicationConfig.HLS_PROTOCOL + "://")
                                    .replace("rtsp://", ApplicationConfig.HLS_PROTOCOL + "://")
                                    .replace("/live/", ":" + ApplicationConfig.HLS_PORT + "/hls/") + "/index.m3u8";
                        }
                        if (ApplicationConfig.HLS_DOMAIN_LIST != null && !ApplicationConfig.HLS_DOMAIN_LIST.isEmpty()) {
                            List<HlsDomainIp> domainIpList = ApplicationConfig.HLS_DOMAIN_LIST;
                            for (HlsDomainIp hlsDomainIp : domainIpList) {
                                hlsUrl = hlsUrl.replace(hlsDomainIp.getIp(), hlsDomainIp.getDomain());
                            }
                        }
                        LOGGER.info("===> hlsUrl: {}", hlsUrl);

                        String tmpHlsUrl = hlsUrl;
                        threadManager.execute(() -> {
                            /*CameraDTO cameraRequest = new CameraDto(camera);
                            if (cameraRequest != null && !StringUtil.isNullOrEmpty(cameraRequest.getName())) {
                                CameraUrl cameraUrl = cameraRequest.getUrls();
                                if (cameraUrl != null) {
                                    cameraUrl.setHlsUrl(tmpHlsUrl);
                                    cameraRequest.setUrls(cameraUrl);

                                    //Update camera 2 DBM
                                    CamerasResponse camerasResponse = cameraService.updateCamerasFromDBM(camera.getId(), cameraRequest);
                                    LOGGER.info("update HLS for camera id: {} => {}", camera.getId(),
                                            (camerasResponse != null && camerasResponse.getStatus() == HttpStatus.OK.value()) ? "Success" : "Fail");
                                }
                            }*/
                        });
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền thêm Luồng video", null));
            }
        }
        return response;
    }

    private PuVideoThreads toEntity(PuVideoThreadsDTO puVideoThreadssDto, String idProcessUnit) {
        PuVideoThreads puVideoThreadss = new PuVideoThreads();
        puVideoThreadss.setAutoAdjust(puVideoThreadssDto.getAutoAdjust());
        puVideoThreadss.setCacheLength(puVideoThreadssDto.getCacheLength());
        puVideoThreadss.setCacheUrl(puVideoThreadssDto.getCacheUrl());
        puVideoThreadss.setCameraId(puVideoThreadssDto.getCameraId());
        puVideoThreadss.setCapability(puVideoThreadssDto.getCapability());
        puVideoThreadss.setDetectors(puVideoThreadssDto.getDetectors());
        puVideoThreadss.setLayoutId(puVideoThreadssDto.getLayoutId());
        puVideoThreadss.setProcessUnit(processUnitService.findById(idProcessUnit).get());
        puVideoThreadss.setRender(puVideoThreadssDto.getRender());
        puVideoThreadss.setStatus(puVideoThreadssDto.getStatus().code());
        return puVideoThreadss;
    }
    
    public ResponseMessage deletePuVideoThreads(String requestPath, Map<String, String> headerParam, 
            String requestMethod, Long pathParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                PuVideoThreads puVideoThreadssById = puVideoThreadssService.findById(pathParam).orElseThrow(() -> new ResourceNotFoundException(PuVideoThreads.class, pathParam));
                puVideoThreadssService.delete(puVideoThreadssById);
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền Xóa Luồng video", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteMutltiPuVideoThreads(String requestPath, Map<String, String> headerParam, 
            Map<String, Object> bodyParam, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                List<Integer> idVideoThreadList = (List<Integer>) bodyParam.get("idVideoThreadList");
                if (CollectionUtils.isEmpty(idVideoThreadList)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không có Id Luồng video", null));
                }
                for (Integer idVideoThread : idVideoThreadList) {
                    Long idThreadLong = ((Number) idVideoThread).longValue();
                    PuVideoThreads puVideoThreads = puVideoThreadssService.findById(idThreadLong).orElseThrow(() -> new ResourceNotFoundException(ProcessUnit.class, idVideoThread));
                    puVideoThreadssService.delete(puVideoThreads);
                }
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền Xóa luồng video", null));
            }
        }
        return response;
    }
}
