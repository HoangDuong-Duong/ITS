/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.DateUtil;
import com.elcom.its.utils.StringUtil;
import com.elcom.its.vds.config.ApplicationConfig;
import com.elcom.its.vds.enums.CameraLayoutType;
import com.elcom.its.vds.enums.DataStatus;
import com.elcom.its.vds.model.*;
import com.elcom.its.vds.model.dto.*;
import com.elcom.its.vds.service.*;
import com.elcom.its.vds.thread.ThreadManager;
import com.elcom.its.vds.validation.VdsValidation;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Admin
 */
@Controller
public class VdsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VdsController.class);

    @Autowired
    private ITSCoreCameraService cameraService;

    @Autowired
    private LayoutAreaService layoutAreaService;

    @Autowired
    private ProcessUnitService processUnitService;

    @Autowired
    private VdsService vdsService;

    @Autowired
    private ImageCameraService imgaCameraService;

    @Autowired
    private CameraLayoutService cameraLayoutService;

    @Autowired
    private ITSCoreVdsService itsCoreVdsService;

    @Autowired
    private ThreadManager threadManager;

    @Autowired
    private SiteService siteService;

    public ResponseMessage createVds(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                String vdsName = (String) bodyParam.get("vdsName");
                String cameraId = (String) bodyParam.get("cameraId");
                Map<String, Object> capability = (Map<String, Object>) bodyParam.get("capability");
                String processUnitId = (String) bodyParam.get("processUnitId");
                Integer layoutType = bodyParam.get("layoutType") != null ? (Integer) bodyParam.get("layoutType") : null;
                VdsDTO vdsDTO = VdsDTO.builder().cameraId(cameraId).capability(capability)
                        .layoutType(layoutType).processUnitId(processUnitId).vdsName(vdsName).build();

                //Validate
                String invalidData = new VdsValidation().validateInsertVds(vdsDTO);
                if (invalidData != null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                } else {
                    ProcessUnit processUnit = processUnitService.findById(processUnitId).orElse(null);
                    if (processUnit == null) {
                        invalidData = "Không tìm thấy thông tin ứng với processUnitId = " + processUnitId;
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                    } else {
                        if (layoutType == null || !layoutType.equals(processUnit.getPuType())) {
                            invalidData = "Loại layout và khối xử lý không cùng loại";
                            response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                        } else {
                            CameraDetailDTOMessage camerasResponse = cameraService.getCamerasByIdFromDBM(vdsDTO.getCameraId());
                            if (camerasResponse == null || camerasResponse.getData() == null) {
                                invalidData = "Không tìm thấy thông tin ứng với cameraId = " + cameraId;
                                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                            } else {
                                CameraDTO camera = camerasResponse.getData();
                                if (vdsService.existsByLayoutTypeAndCameraId(layoutType, cameraId)) {
                                    CameraLayoutType layoutTypeEnum = CameraLayoutType.parse(layoutType);
                                    return new ResponseMessage(new MessageContent(HttpStatus.CONFLICT.value(),
                                            "Đã tồn tại loại " + (layoutTypeEnum != null ? layoutTypeEnum.description() : "layout không xác định")
                                            + " cho camera " + camera.getName(), null));
                                }
                                //if (vdsService.existsByLayoutTypeAndCameraIdAndProcessUnitId(layoutType, cameraId, processUnitId)) {
                                //    return new ResponseMessage(new MessageContent(HttpStatus.CONFLICT.value(),
                                //            "Đã tồn tại loại layout và camera và khối xử lý", null));
                                //}

                                Vds entity = VdsDTO.toEntity(vdsDTO, processUnit, camera, dto.getUuid());
                                //Tạo ảnh live camera
                                Response urlResponse = cameraService.getLiveImageCamera(cameraId);
                                if (urlResponse == null || urlResponse.getStatus() != HttpStatus.OK.value()
                                        || urlResponse.getData() == null) {
                                    invalidData = "Lỗi cắt ảnh live cho camera : " + camera.getName();
                                    response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), invalidData, null));
                                } else {
                                    String liveImage = (String) urlResponse.getData();
                                    //Tạo image cameara
                                    ImageCamera imgCamera = new ImageCamera(UUID.randomUUID().toString(), cameraId, liveImage);
                                    imgCamera.setCreatedBy(dto.getUuid());
                                    imgCamera.setCreatedDate(new Date());
                                    imgCamera.setModifiedBy(dto.getUuid());
                                    imgCamera.setModifiedDate(new Date());
                                    imgaCameraService.create(imgCamera);

                                    //Tạo layout camera
                                    CameraLayouts cameraLayout = createLayout(camera, layoutType, liveImage, capability, dto.getUuid());
                                    if (cameraLayout == null) {
                                        invalidData = "Lỗi tạo layout cho camera : " + cameraId;
                                        response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), invalidData, null));
                                    } else {
                                        try {
                                            entity.setLayoutId(cameraLayout.getId());
                                            entity.setLayoutTypeName(cameraLayout.getName());
                                            entity.setCapability(capability);
                                            entity.setRenderVds(1);
                                            //Save vds
                                            entity = vdsService.save(entity);

                                            //Update json spec
                                            String jsonSpec = processUnitService.toJsonSpec(processUnit);
                                            processUnit.setSpec(jsonSpec);
                                            processUnitService.update(processUnit);

                                            //Image camera updateCameraThumnail
                                            threadManager.execute(() -> {
                                                Response updateThumnailResponse = cameraService.updateCameraThumnail(cameraId, liveImage);
                                                LOGGER.info("Update thumnail for camera id: {}, thumnail: {} => {}", cameraId, liveImage,
                                                        (updateThumnailResponse != null && updateThumnailResponse.getStatus() == HttpStatus.OK.value()));
                                            });

                                            response = new ResponseMessage(new MessageContent(HttpStatus.CREATED.value(),
                                                    HttpStatus.CREATED.toString(), entity));
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            LOGGER.error("Error to create vds >>> {}", ex.toString());
                                            response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                    HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền tạo VDS", null));
            }
        }
        return response;
    }

    private CameraLayouts createLayout(CameraDTO camera, Integer layoutType, String liveImage,
            Map<String, Object> capability, String userId) {
        try {
            String name = "Layout " + (CameraLayoutType.parse(layoutType).isGSANType()
                    ? "an ninh " : "giao thông ") + camera.getName() + " " + DateUtil.toString(new Date(), "yyyyMMddHHmmss");
            String usedCaptureImage = null;
            String cameraId = camera.getId();
            int widthImgScale = 0;
            int heightImgScale = 0;
            String sizedImg = null;
            String scaledFitImg = null;
            int originWidth = ((Number) capability.get("w")).intValue();
            int originHeight = ((Number) capability.get("h")).intValue();
            String layoutImage = liveImage;
            String defaultJson = "{\"objects\":[],\"background\":\"\",\"backgroundImage\":{\"type\":\"image\",\"originX\":\"left\",\"originY\":\"top\",\"left\":0,\"top\":0,\"width\":{width},\"height\":{height},\"fill\":\"rgb(0,0,0)\",\"strokeWidth\":1,\"strokeLineCap\":\"butt\",\"strokeLineJoin\":\"miter\",\"strokeMiterLimit\":10,\"scaleX\":1,\"scaleY\":1,\"angle\":0,\"flipX\":false,\"flipY\":false,\"opacity\":1,\"visible\":true,\"backgroundColor\":\"\",\"src\":\"{src}\",\"filters\":[],\"crossOrigin\":\"anonymous\"}}";
            defaultJson = defaultJson.replace("{width}", originWidth + "").replace("{height}", originHeight + "")
                    .replace("{src}", layoutImage);
            Map<String, Object> jsonCanvas = new Gson().fromJson(defaultJson, Map.class);
            String description = null;
            int version = 1;
            long modelProfileId = 0;
            Map<String, Object> layoutAreas = null;
            Map<String, Object> metaAttributes = null;
            CameraLayoutDTO cameraLayoutDto = new CameraLayoutDTO(name, usedCaptureImage,
                    cameraId, jsonCanvas, widthImgScale, heightImgScale, sizedImg,
                    scaledFitImg, originWidth, originHeight, layoutImage, description,
                    version, modelProfileId, layoutType, layoutAreas, DataStatus.ENABLE,
                    metaAttributes);
            CameraLayouts entity = CameraLayoutDTO.toEntity(cameraLayoutDto);
            entity.setStatus(DataStatus.ENABLE.code());
            entity.setCreatedBy(userId);
            entity.setCreatedDate(new Date());
            entity.setModifiedBy(userId);
            entity.setModifiedDate(new Date());
            return cameraLayoutService.create(entity);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ResponseMessage findVds(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValuesNoDecode(urlParam);
                Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                String siteId = params.get("siteId");
                String cameraId = params.get("cameraId");
                String processId = params.get("processId");
                String search = params.get("search");
                if (aBACResponseDTO.getAdmin() == null || !aBACResponseDTO.getAdmin()) {
                    //Nếu không phải là user admin
                    String stages = dto.getUnit().getLisOfStage();
                    SiteInfoNewResponseList siteInfoListResponse = siteService.getListSiteInListStage(stages);
                    if (siteInfoListResponse != null && (siteInfoListResponse.getStatus() == HttpStatus.OK.value()
                            || siteInfoListResponse.getStatus() == HttpStatus.CREATED.value())) {
                        //Lấy danh sách site id user được truy cập
                        List<String> siteIdList = siteInfoListResponse.getData().stream().map(info -> info.getId()).collect(Collectors.toList());
                        //Nếu tồn tại danh sách site id được truy cập mới có dữ liệu
                        if (siteIdList != null && !siteIdList.isEmpty()) {
                            if (StringUtil.isNullOrEmpty(siteId) || siteIdList.contains(siteId)) {
                                List<String> siteIdFilterList = siteIdList;
                                if (!StringUtil.isNullOrEmpty(siteId)) {
                                    siteIdFilterList = Arrays.asList(siteId);
                                }
                                Page<Vds> pagedResult = vdsService.findVds(siteIdFilterList, cameraId, processId, search, page, size);
                                if (pagedResult != null) {
                                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                                            HttpStatus.OK.toString(), pagedResult.getContent(), pagedResult.getTotalElements()));
                                } else {
                                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null, 0L));
                                }
                            } else {
                                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null, 0L));
                            }
                        } else {
                            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null, 0L));
                        }
                    } else {
                        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null, 0L));
                    }
                } else {
                    //Nếu là user admin
                    Page<Vds> pagedResult = vdsService.findVds(siteId, cameraId, processId, search, page, size);
                    if (pagedResult != null) {
                        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                                HttpStatus.OK.toString(), pagedResult.getContent(), pagedResult.getTotalElements()));
                    } else {
                        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null, 0L));
                    }
                }
            }
        }
        return response;
    }

    public ResponseMessage updateVds(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                String id = pathParam;
                Vds existVds = vdsService.findById(id);
                if (existVds == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(),
                            "Không tìm thấy thông tin VDS ứng với id = " + id, null));
                } else {
                    String vdsName = (String) bodyParam.get("vdsName");
                    String cameraId = (String) bodyParam.get("cameraId");
                    Map<String, Object> capability = (Map<String, Object>) bodyParam.get("capability");
                    String processUnitId = (String) bodyParam.get("processUnitId");
                    Integer layoutType = bodyParam.get("layoutType") != null ? (Integer) bodyParam.get("layoutType") : null;
                    VdsDTO vdsDTO = VdsDTO.builder().cameraId(cameraId).capability(capability)
                            .layoutType(layoutType).processUnitId(processUnitId).vdsName(vdsName).build();

                    //Validate
                    String invalidData = new VdsValidation().validateUpdateVds(id, vdsDTO);
                    if (invalidData != null) {
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                    } else {
                        ProcessUnit processUnit = processUnitService.findById(processUnitId).orElse(null);
                        if (processUnit == null) {
                            invalidData = "Không tìm thấy thông tin ứng với processUnitId = " + processUnitId;
                            response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                        } else {
                            CameraDetailDTOMessage camerasResponse = cameraService.getCamerasByIdFromDBM(vdsDTO.getCameraId());
                            if (camerasResponse == null || camerasResponse.getData() == null) {
                                invalidData = "Không tìm thấy thông tin ứng với cameraId = " + cameraId;
                                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                            } else {
                                CameraDTO camera = camerasResponse.getData();
                                if (!existVds.getCameraId().equals(cameraId) && !existVds.getProcessUnitId().equals(processUnitId)
                                        && vdsService.existsByLayoutTypeAndCameraIdAndProcessUnitId(layoutType, cameraId, processUnitId)) {
                                    return new ResponseMessage(new MessageContent(HttpStatus.CONFLICT.value(),
                                            "Đã tồn tại layout type và camera và processUnit", null));
                                }

                                Vds entity = VdsDTO.toEntity(existVds, vdsDTO, processUnit, camera, dto.getUuid());

                                //Nếu camera mới hoặc kiểu layout mới => Cần tạo layout mới
                                //Ngược lại vẫn camera cũ và kiểu layout cũ => Không cần tạo layout mới
                                Long layoutId = null;
                                String layoutName = null;
                                if (!existVds.getCameraId().equals(cameraId) || !existVds.getLayoutType().equals(layoutType)) {
                                    //Tạo ảnh live camera
                                    Response urlResponse = cameraService.getLiveImageCamera(cameraId);
                                    if (urlResponse == null || urlResponse.getStatus() != HttpStatus.OK.value()
                                            || urlResponse.getData() == null) {
                                        invalidData = "Lỗi tạo ảnh live cho camera : " + cameraId;
                                        return new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), invalidData, null));
                                    } else {
                                        String liveImage = (String) urlResponse.getData();
                                        //Tạo image cameara
                                        ImageCamera imgCamera = new ImageCamera(UUID.randomUUID().toString(), cameraId, liveImage);
                                        imgCamera.setCreatedBy(dto.getUuid());
                                        imgCamera.setCreatedDate(new Date());
                                        imgCamera.setModifiedBy(dto.getUuid());
                                        imgCamera.setModifiedDate(new Date());
                                        imgaCameraService.create(imgCamera);

                                        //Tạo layout camera
                                        CameraLayouts cameraLayout = createLayout(camera, layoutType, liveImage, capability, dto.getUuid());
                                        if (cameraLayout == null) {
                                            invalidData = "Lỗi tạo layout cho camera : " + cameraId;
                                            return new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), invalidData, null));
                                        } else {
                                            layoutId = cameraLayout.getId();
                                            layoutName = cameraLayout.getName();
                                        }
                                        
                                        //Image camera updateCameraThumnail
                                        threadManager.execute(() -> {
                                            Response updateThumnailResponse = cameraService.updateCameraThumnail(cameraId, liveImage);
                                            LOGGER.info("Update thumnail for camera id: {}, thumnail: {} => {}", cameraId, liveImage,
                                                    (updateThumnailResponse != null && updateThumnailResponse.getStatus() == HttpStatus.OK.value()));
                                        });
                                    }
                                } else {
                                    layoutId = existVds.getLayoutId();
                                    layoutName = existVds.getLayoutTypeName();
                                }
                                try {
                                    entity.setLayoutId(layoutId);
                                    entity.setLayoutTypeName(layoutName);
                                    entity.setCapability(capability);
                                    entity.setRenderVds(existVds.getRenderVds());
                                    entity = vdsService.save(entity);

                                    //Update json spec
                                    String jsonSpec = processUnitService.toJsonSpec(processUnit);
                                    processUnit.setSpec(jsonSpec);
                                    processUnitService.update(processUnit);

                                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                                            HttpStatus.OK.toString(), entity));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    LOGGER.error("Error to update vds >>> {}", ex.toString());
                                    response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                            HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
                                }
                            }
                        }
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền cập nhật VDS", null));
            }
        }
        return response;
    }

    public ResponseMessage detailVds(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "DETAIL", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                String invalidData = null;
                if (StringUtil.isNullOrEmpty(pathParam)) {
                    invalidData = "vds id không được để trống";
                    response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                            HttpStatus.OK.toString(), vdsService.findById(pathParam)));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem chi tiết VDS", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteVds(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Vds vds = vdsService.findById(pathParam);
                if (vds == null) {
                    return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy id tương ứng: " + pathParam, null));
                } else {
                    try {
                        vdsService.remove(vds);

                        //Update json spec
                        ProcessUnit processUnit = processUnitService.findById(vds.getProcessUnitId()).orElse(null);
                        if (processUnit != null) {
                            String jsonSpec = processUnitService.toJsonSpec(processUnit);
                            processUnit.setSpec(jsonSpec);
                            processUnitService.update(processUnit);
                        }

                        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                    } catch (Exception ex) {
                        LOGGER.error("Error to delete VDS >>> {}", ex.toString());
                        ex.printStackTrace();
                        response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa vds", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteMultiVds(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                List<String> vdsIdList = (List<String>) bodyParam.get("vdsIdList");
                if (CollectionUtils.isEmpty(vdsIdList)) {
                    return new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(),
                            "vdsIdList không được để trống", null));
                }
                for (String vdsId : vdsIdList) {
                    Vds vds = vdsService.findById(vdsId);
                    if (vds == null) {
                        return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy id tương ứng: " + vdsId, null));
                    } else {
                        try {
                            vdsService.remove(vds);

                            //Update json spec
                            ProcessUnit processUnit = processUnitService.findById(vds.getProcessUnitId()).orElse(null);
                            if (processUnit != null) {
                                String jsonSpec = processUnitService.toJsonSpec(processUnit);
                                processUnit.setSpec(jsonSpec);
                                processUnitService.update(processUnit);
                            }

                            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                        } catch (Exception ex) {
                            LOGGER.error("Error to delete multi VDS >>> {}", ex.toString());
                            ex.printStackTrace();
                            response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
                        }
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa vds", null));
            }
        }
        return response;
    }

    public ResponseMessage updateVdsStatus(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                String id = pathParam;
                Vds existVds = vdsService.findById(id);
                if (existVds == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(),
                            "Không tìm thấy thông tin VDS ứng với id = " + id, null));
                } else {
                    Integer status = (Integer) bodyParam.get("status");
                    String invalidData = null;
                    if (status == null || (status != 0 && status != 1)) {
                        invalidData = "Trạng thái truyền vào chỉ nhận giá trị 0 hoặc 1";
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                    } else {
                        if (vdsService.updateStatus(id, status, dto.getUuid())) {
                            //Update json spec
                            ProcessUnit processUnit = processUnitService.findById(existVds.getProcessUnitId()).orElse(null);
                            if (processUnit != null) {
                                String jsonSpec = processUnitService.toJsonSpec(processUnit);
                                processUnit.setSpec(jsonSpec);
                                processUnitService.update(processUnit);
                            }
                            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                        } else {
                            response = new ResponseMessage(new MessageContent(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.toString(), null));
                        }
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền cập nhật trạng thái VDS", null));
            }
        }
        return response;
    }

    public ResponseMessage getListEventByVds(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "DETAIL", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Vds vdsById = vdsService.findById(pathParam);
                if (vdsById == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy vds với id = " + pathParam, null));
                } else {
                    Map<String, String> urlMap = StringUtil.getUrlParamValues(urlParam);
                    int page = urlMap.get("page") != null ? Integer.parseInt(urlMap.get("page")) : 0;
                    int size = urlMap.get("size") != null ? Integer.parseInt(urlMap.get("size")) : 20;
                    String filterTimeLevel = urlMap.get("filterTimeLevel") != null ? urlMap.get("filterTimeLevel") : "day";
                    Response itscoreResponse = itsCoreVdsService.getListEventByVds(vdsById, page, size, filterTimeLevel);
                    response = new ResponseMessage(new MessageContent(itscoreResponse.getStatus(),
                            itscoreResponse.getMessage(), itscoreResponse.getData()));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem chi tiết VDS", null));
            }
        }
        return response;
    }

    public ResponseMessage getAggEventTypeByVds(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "DETAIL", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Vds vdsById = vdsService.findById(pathParam);
                if (vdsById == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy vds với id = " + pathParam, null));
                } else {
                    Map<String, String> urlMap = StringUtil.getUrlParamValues(urlParam);
                    String filterTimeLevel = urlMap.get("filterTimeLevel") != null ? urlMap.get("filterTimeLevel") : "day";
                    Response itscoreResponse = itsCoreVdsService.getAggEventTypeByVds(vdsById, filterTimeLevel);
                    response = new ResponseMessage(new MessageContent(itscoreResponse.getStatus(),
                            itscoreResponse.getMessage(), itscoreResponse.getData()));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem báo cáo sự kiện VDS", null));
            }
        }
        return response;
    }

    public ResponseMessage getTrafficflowDataByVds(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "DETAIL", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Vds vdsById = vdsService.findById(pathParam);
                if (vdsById == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy vds với id = " + pathParam, null));
                } else {
                    Map<String, String> urlMap = StringUtil.getUrlParamValues(urlParam);
                    String filterTimeLevel = urlMap.get("filterTimeLevel") != null ? urlMap.get("filterTimeLevel") : "day";
                    Response itscoreResponse = itsCoreVdsService.getTrafficflowDataByVds(vdsById, filterTimeLevel);
                    response = new ResponseMessage(new MessageContent(itscoreResponse.getStatus(),
                            itscoreResponse.getMessage(), itscoreResponse.getData()));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem báo cáo lưu lượng VDS", null));
            }
        }
        return response;
    }

    public ResponseMessage getTrafficFlowDataBySite(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                response = new ResponseMessage(new MessageContent(vdsService.getTrafficFlowBySite(urlParam)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo giám sát lưu lượng", null));
            }

        }
        return response;
    }

    public ResponseMessage getTrafficFlowDataBySiteReport(String requestPath, Map<String, String> headerParam,
                                                    Map<String, Object> bodyParam, String requestMethod, String pathParam, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                response = new ResponseMessage(new MessageContent(vdsService.getTrafficFlowBySiteReport(urlParam)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền", null));
            }

        }
        return response;
    }

    public ResponseMessage getCameraLayOutById(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (cameraLayoutService.findById(Long.valueOf(pathParam)) != null) {
                    response = new ResponseMessage(new MessageContent(cameraLayoutService.findById(Long.valueOf(pathParam)).get()));
                } else {
                    response = new ResponseMessage(new MessageContent(null));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền", null));
            }

        }
        return response;
    }

    public ResponseMessage getVdsByCamera(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (!CollectionUtils.isEmpty(vdsService.getListVdsByCamera(pathParam))) {
                    response = new ResponseMessage(new MessageContent(vdsService.getListVdsByCamera(pathParam).get(0)));
                } else {
                    response = new ResponseMessage(new MessageContent(null));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền", null));
            }

        }
        return response;
    }

    public ResponseMessage getLayoutAreasByLayoutId(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam, String urlParam) {

        ResponseMessage response = null;
        if (!CollectionUtils.isEmpty(layoutAreaService.getListByLayoutId(Long.valueOf(pathParam)))) {
            response = new ResponseMessage(new MessageContent(layoutAreaService.getListByLayoutId(Long.valueOf(pathParam))));
        } else {
            response = new ResponseMessage(new MessageContent(null));
        }

        return response;
    }

    public ResponseMessage updateVdsVideoThreads(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                String id = pathParam;
                Vds existVds = vdsService.findById(id);
                if (existVds == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(),
                            "Không tìm thấy thông tin VDS ứng với id = " + id, null));
                } else {
                    Map<String, Object> capability = (Map<String, Object>) bodyParam.get("capability");
                    Map<String, Object> detectors = (Map<String, Object>) bodyParam.get("detectors");
                    Map<String, Object> render = (Map<String, Object>) bodyParam.get("render");
                    VdsDTO vdsDTO = VdsDTO.builder().detectors(detectors).render(render).build();
                    String invalidData = new VdsValidation().validateUpdateVdsVideoThreads(id, vdsDTO);

                    if (invalidData != null) {
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                    } else {
                        existVds.setCapability(capability);
                        existVds.setDetectors(detectors);
                        existVds.setRender(render);
                        try {
                            existVds = vdsService.save(existVds);
                            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), existVds));

                            //Update link HLS Camera
                            CameraDTO camera = null;
                            CameraDetailDTOMessage camerasResponse = cameraService.getCamerasByIdFromDBM(existVds.getCameraId());
                            if (camerasResponse != null && camerasResponse.getData() != null) {
                                camera = camerasResponse.getData();
                            }
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
                                CameraDTO tmpCamera = camera;
                                threadManager.execute(() -> {
                                    CameraCreateUpdateDTO cameraRequest = new CameraCreateUpdateDTO(tmpCamera);
                                    if (!StringUtil.isNullOrEmpty(cameraRequest.getName())) {
                                        CameraUrls cameraUrl = cameraRequest.getUrls();
                                        if (cameraUrl != null) {
                                            cameraUrl.setHlsUrl(tmpHlsUrl);
                                            cameraRequest.setUrls(cameraUrl);

                                            //Update camera 2 Core
                                            CameraDetailDTOMessage responseDto = cameraService.updateCamera(cameraRequest, tmpCamera.getId());
                                            LOGGER.info("update HLS for camera id: {} => {}", tmpCamera.getId(),
                                                    (responseDto != null && responseDto.getStatus() == HttpStatus.OK.value()) ? "Success" : "Fail");
                                        }
                                    }
                                });
                            }

                            //Update process unit spec json
                            Optional<ProcessUnit> processUnit = processUnitService.findById(existVds.getProcessUnitId());
                            String jsonSpec = processUnitService.toJsonSpec(processUnit.get());
                            processUnit.get().setSpec(jsonSpec);
                            processUnitService.update(processUnit.get());
                            LOGGER.info("Generate successful new spec json for process unit id: {}", existVds.getProcessUnitId());
                        } catch (Exception ex) {
                            LOGGER.error("Error to update VDS Video Thread >>> {}", ex.toString());
                            ex.printStackTrace();
                            response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
                        }
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền cập nhật luồng video VDS", null));
            }
        }
        return response;
    }

    public ResponseMessage updateCameraStatus(Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        String cameraId = (String) bodyParam.get("cameraId");
        Integer status = bodyParam.get("status") != null ? (Integer) bodyParam.get("status") : null;

        //Validate
        String invalidData = new VdsValidation().validateUpdateCameraStatus(cameraId, status);

        if (invalidData != null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
        } else {
            try {
                vdsService.updateCameraStatus(cameraId, status);
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                        HttpStatus.OK.toString(), null));
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.error("Error to update camera status vds >>> {}", ex.toString());
                response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
            }
        }
        return response;
    }

    public ResponseMessage updateActiveVds(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                List<String> vdsIdList = (List<String>) bodyParam.get("vdsIdList");
                if (CollectionUtils.isEmpty(vdsIdList)) {
                    return new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(),
                            "vdsIdList không được để trống", null));
                } else {
                    try {
                        //Update list vds
                        vdsService.updateStatus(vdsIdList, 1, dto.getUuid());

                        //Update json spec
                        List<String> processUnitIdList = vdsService.findProcessUnitIdByVdsIdList(vdsIdList);
                        if (processUnitIdList != null && !processUnitIdList.isEmpty()) {
                            for (String processUnitId : processUnitIdList) {
                                ProcessUnit processUnit = processUnitService.findById(processUnitId).orElse(null);
                                if (processUnit != null) {
                                    String jsonSpec = processUnitService.toJsonSpec(processUnit);
                                    processUnit.setSpec(jsonSpec);
                                    processUnitService.update(processUnit);
                                }
                            }
                        } else {
                            LOGGER.info("No process unit id for update json spec");
                        }
                        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        LOGGER.error("Error to update active vds status >>> {}", ex.toString());
                        response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền cập nhật trạng thái VDS", null));
            }
        }
        return response;
    }

    public ResponseMessage updateInactiveVds(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                List<String> vdsIdList = (List<String>) bodyParam.get("vdsIdList");
                if (CollectionUtils.isEmpty(vdsIdList)) {
                    return new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(),
                            "vdsIdList không được để trống", null));
                } else {
                    try {
                        //Update list vds
                        vdsService.updateStatus(vdsIdList, 0, dto.getUuid());

                        //Update json spec
                        List<String> processUnitIdList = vdsService.findProcessUnitIdByVdsIdList(vdsIdList);
                        if (processUnitIdList != null && !processUnitIdList.isEmpty()) {
                            for (String processUnitId : processUnitIdList) {
                                ProcessUnit processUnit = processUnitService.findById(processUnitId).orElse(null);
                                if (processUnit != null) {
                                    String jsonSpec = processUnitService.toJsonSpec(processUnit);
                                    processUnit.setSpec(jsonSpec);
                                    processUnitService.update(processUnit);
                                }
                            }
                        } else {
                            LOGGER.info("No process unit id for update json spec");
                        }
                        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        LOGGER.error("Error to update inactive vds status >>> {}", ex.toString());
                        response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền cập nhật trạng thái VDS", null));
            }
        }
        return response;
    }

    public ResponseMessage findSite(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod, String urlParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                        HttpStatus.OK.toString(), vdsService.getSiteList()));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách vị trí trên VDS", null));
            }
        }
        return response;
    }
    
    public ResponseMessage updateActiveRenderVds(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                List<String> vdsIdList = (List<String>) bodyParam.get("vdsIdList");
                if (CollectionUtils.isEmpty(vdsIdList)) {
                    return new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(),
                            "vdsIdList không được để trống", null));
                } else {
                    try {
                        //Update list vds render status
                        vdsService.updateRenderStatus(vdsIdList, 1, dto.getUuid());

                        //Update json spec
                        List<String> processUnitIdList = vdsService.findProcessUnitIdByVdsIdList(vdsIdList);
                        if (processUnitIdList != null && !processUnitIdList.isEmpty()) {
                            for (String processUnitId : processUnitIdList) {
                                ProcessUnit processUnit = processUnitService.findById(processUnitId).orElse(null);
                                if (processUnit != null) {
                                    String jsonSpec = processUnitService.toJsonSpec(processUnit);
                                    processUnit.setSpec(jsonSpec);
                                    processUnitService.update(processUnit);
                                }
                            }
                        } else {
                            LOGGER.info("No process unit id for update json spec");
                        }
                        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        LOGGER.error("Error to update active render vds status >>> {}", ex.toString());
                        response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền cập nhật trạng thái luồng render VDS", null));
            }
        }
        return response;
    }
    
    public ResponseMessage updateInactiveRenderVds(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                List<String> vdsIdList = (List<String>) bodyParam.get("vdsIdList");
                if (CollectionUtils.isEmpty(vdsIdList)) {
                    return new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(),
                            "vdsIdList không được để trống", null));
                } else {
                    try {
                        //Update list vds render status
                        vdsService.updateRenderStatus(vdsIdList, 0, dto.getUuid());

                        //Update json spec
                        List<String> processUnitIdList = vdsService.findProcessUnitIdByVdsIdList(vdsIdList);
                        if (processUnitIdList != null && !processUnitIdList.isEmpty()) {
                            for (String processUnitId : processUnitIdList) {
                                ProcessUnit processUnit = processUnitService.findById(processUnitId).orElse(null);
                                if (processUnit != null) {
                                    String jsonSpec = processUnitService.toJsonSpec(processUnit);
                                    processUnit.setSpec(jsonSpec);
                                    processUnitService.update(processUnit);
                                }
                            }
                        } else {
                            LOGGER.info("No process unit id for update json spec");
                        }
                        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        LOGGER.error("Error to update inactive render vds status >>> {}", ex.toString());
                        response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền cập nhật trạng thái luồng render VDS", null));
            }
        }
        return response;
    }
}
