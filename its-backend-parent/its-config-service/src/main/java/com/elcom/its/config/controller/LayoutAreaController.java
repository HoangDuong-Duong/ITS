/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.controller;

import com.elcom.its.config.model.LayoutAreas;
import com.elcom.its.config.model.CameraLayouts;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.LayoutAreaDTO;
import com.elcom.its.config.service.CameraLayoutService;
import com.elcom.its.config.service.LayoutAreaService;
import com.elcom.its.config.validation.LayoutAreaValidation;
import com.elcom.its.message.MessageContent;
import com.elcom.its.config.constant.Constant;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.model.dto.RoisUpsertDTO;
import com.elcom.its.config.service.RoiService;
import java.util.Date;
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
public class LayoutAreaController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LayoutAreaController.class);

    @Autowired
    private LayoutAreaService layoutAreaService;

    @Autowired
    private CameraLayoutService cameraLayoutService;

    @Autowired
    private RoiService roiService;

    public ResponseMessage getLayoutAreasList(Map<String, String> headerParam, String requestPath,
            String method, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                List<LayoutAreas> layoutAreases = layoutAreaService.getAll();
                if (CollectionUtils.isEmpty(layoutAreases)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                            HttpStatus.OK.toString(), null));
                } else {
                    response = new ResponseMessage(new MessageContent(layoutAreases));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách Layout Areas", null));
            }
        }
        return response;
    }

    public ResponseMessage getLayoutAreasById(Map<String, String> headerParam, String requestPath,
            String method, String pathParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Optional<LayoutAreas> layOptional = layoutAreaService.findById(Long.valueOf(pathParam));
                if (layOptional.isPresent()) {
                    response = new ResponseMessage(new MessageContent(layOptional.get()));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(),
                            HttpStatus.NOT_FOUND.toString(), null));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách Layout Areas", null));
            }
        }
        return response;
    }

    public ResponseMessage createLayoutAreas(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Long cameraLayoutId = ((Number) bodyParam.get("layoutId")).longValue();
                Map<String, Object> jsonDetailArea = (Map<String, Object>) bodyParam.get("jsonDetailArea");
                long roiType = ((Number) bodyParam.get("roiType")).longValue();
                LayoutAreaDTO layoutAreaDto = new LayoutAreaDTO(cameraLayoutId, jsonDetailArea, roiType);

                String invalidData = new LayoutAreaValidation().validateInsertLayoutArea(layoutAreaDto);
                if (invalidData != null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                } else {
                    Optional<CameraLayouts> camOptional = cameraLayoutService.findById(layoutAreaDto.getCameraLayoutId());
                    if (camOptional == null || !camOptional.isPresent()) {
                        return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy Camera Layout", null));
                    }
                    LayoutAreas entity = LayoutAreaDTO.toEntity(layoutAreaDto);
                    entity.setCreatedBy(dto.getUuid());
                    entity.setCreatedDate(new Date());
                    entity.setModifiedBy(dto.getUuid());
                    entity.setModifiedDate(new Date());
                    entity = layoutAreaService.create(entity);
                    response = new ResponseMessage(new MessageContent(HttpStatus.CREATED.value(), HttpStatus.CREATED.toString(), entity));
                }

            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền thêm Layout Areas", null));
            }
        }
        return response;
    }

    public ResponseMessage updateLayoutAreas(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, Long pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                if (bodyParam == null || bodyParam.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    Long id = pathParam;
                    Long cameraLayoutId = ((Number) bodyParam.get("layoutId")).longValue();
                    Map<String, Object> jsonDetailArea = (Map<String, Object>) bodyParam.get("jsonDetailArea");
                    long roiType = ((Number) bodyParam.get("roiType")).longValue();
                    LayoutAreaDTO layoutAreaDto = new LayoutAreaDTO(cameraLayoutId, jsonDetailArea, roiType);

                    String invalidData = new LayoutAreaValidation().validateInsertLayoutArea(layoutAreaDto);
                    if (invalidData != null) {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                    } else {
                        Optional<LayoutAreas> cameraLayoutOptional = layoutAreaService.findById(id);
                        LayoutAreas existLayoutAreas = null;
                        if (cameraLayoutOptional.isEmpty()) {
                            return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy LayoutAreas", null);
                        } else {
                            existLayoutAreas = cameraLayoutOptional.get();
                        }

                        Optional<CameraLayouts> camOptional = cameraLayoutService.findById(layoutAreaDto.getCameraLayoutId());
                        if (camOptional.isEmpty()) {
                            return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy Camera Layout", null));
                        }
                        LayoutAreas entity = LayoutAreaDTO.toEntity(layoutAreaDto);
                        entity.setCreatedBy(cameraLayoutOptional.get().getCreatedBy());
                        entity.setCreatedDate(cameraLayoutOptional.get().getCreatedDate());
                        entity.setModifiedBy(dto.getUuid());
                        entity.setModifiedDate(new Date());
                        entity.setId(id);
                        try {
                            entity = layoutAreaService.update(entity);
                            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), entity));

                            //Truong hop co lane
                            Integer laneNumber = null;
                            if (jsonDetailArea != null && jsonDetailArea.containsKey("lane")) {
                                laneNumber = (Integer) jsonDetailArea.get("lane");
                            }
                            if (laneNumber != null) {
                                //Update 2 ITS Core Rois
                                RoisUpsertDTO roisUpsertDTO = RoisUpsertDTO.builder().number(laneNumber).roiId(id).build();
                                Response upsertResponse = roiService.save(roisUpsertDTO);
                                if (upsertResponse != null && upsertResponse.getStatus() == HttpStatus.OK.value()) {
                                    LOGGER.info("Upsert roi 2 ITS Core for : {} => Successful", roisUpsertDTO);
                                } else {
                                    LOGGER.info("Upsert error roi 2 ITS Core for : {}", roisUpsertDTO);
                                }
                            } else {
                                if (existLayoutAreas.getJsonDetailArea() != null && existLayoutAreas.getJsonDetailArea().containsKey("lane")) {
                                    laneNumber = (Integer) existLayoutAreas.getJsonDetailArea().get("lane");
                                    if (laneNumber != null) {
                                        //Remove 2 ITS Core Rois
                                        RoisUpsertDTO roisUpsertDTO = RoisUpsertDTO.builder().number(laneNumber).roiId(id).build();
                                        Response upsertResponse = roiService.remove(roisUpsertDTO);
                                        if (upsertResponse != null && upsertResponse.getStatus() == HttpStatus.OK.value()) {
                                            LOGGER.info("Remove roi 2 ITS Core for : {} => Successful", roisUpsertDTO);
                                        } else {
                                            LOGGER.info("Remove error roi 2 ITS Core for : {}", roisUpsertDTO);
                                        }
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            response = new ResponseMessage(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.getReasonPhrase(),
                                    new MessageContent(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.getReasonPhrase(),
                                            ex.toString()));
                            LOGGER.error("Error to update role >>> " + ex.toString());
                            ex.printStackTrace();
                        }
                    }
                }

            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền Sửa Layout Areas", null));
            }
        }
        return response;
    }

    public ResponseMessage getAreasByLayoutId(Map<String, String> headerParam, String requestPath,
            String method, String pathParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                List<LayoutAreas> layoutAreases = layoutAreaService.findByCameraLayoutId(Long.valueOf(pathParam));
                if (!CollectionUtils.isEmpty(layoutAreases)) {
                    response = new ResponseMessage(new MessageContent(layoutAreases));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                            HttpStatus.OK.toString(), null));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách Layout Areas", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteLayoutAreas(String requestPath, Map<String, String> headerParam,
            String requestMethod, Long pathParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Optional<LayoutAreas> camearLayOptionalById = layoutAreaService.findById(pathParam);
                if (camearLayOptionalById.isEmpty()) {
                    return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy LayoutAreas", null);
                }
                //Truong hop co lane
                LayoutAreas layoutAreas = camearLayOptionalById.get();
                if (layoutAreas.getJsonDetailArea() != null && layoutAreas.getJsonDetailArea().containsKey("lane")) {
                    Integer laneNumber = (Integer) layoutAreas.getJsonDetailArea().get("lane");
                    if (laneNumber != null) {
                        //Update 2 ITS Core Rois
                        RoisUpsertDTO roisUpsertDTO = RoisUpsertDTO.builder().number(laneNumber).roiId(layoutAreas.getId()).build();
                        Response upsertResponse = roiService.remove(roisUpsertDTO);
                        if (upsertResponse != null && upsertResponse.getStatus() == HttpStatus.OK.value()) {
                            LOGGER.info("Delete roi 2 ITS Core for : {} => Successful", roisUpsertDTO);
                        } else {
                            LOGGER.info("Delete error roi 2 ITS Core for : {}", roisUpsertDTO);
                        }
                    }
                }
                //Xoa area
                layoutAreaService.delete(camearLayOptionalById.get());

                //Response
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền Xóa Layout Areas", null));
            }
        }
        return response;
    }

    public ResponseMessage getLayoutsAreaIdMax(Map<String, String> headerParam, String requestPath,
            String method, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, method, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                LayoutAreas layoutAreas = layoutAreaService.findTop1ByOrderByIdDesc();
                if (layoutAreas != null) {
                    response = new ResponseMessage(new MessageContent(layoutAreas));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.toString(), null));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem Layout Areas", null));
            }
        }
        return response;
    }
}
