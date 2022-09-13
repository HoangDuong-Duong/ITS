/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.controller;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.constant.Constant;
import com.elcom.its.config.enums.CameraLayoutType;
import com.elcom.its.config.enums.DataStatus;
import com.elcom.its.config.model.CameraLayouts;
import com.elcom.its.config.model.LayoutAreas;
import com.elcom.its.config.model.ProcessUnit;
import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AIEngineJson;
import com.elcom.its.config.model.dto.AffectObject;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.CameraDetailDTOMessage;
import com.elcom.its.config.model.dto.CameraLayoutDTO;
import com.elcom.its.config.model.dto.CamerasResponse;
import com.elcom.its.config.model.dto.EncroachingInfo;
import com.elcom.its.config.model.dto.FallenObjectInfo;
import com.elcom.its.config.service.CameraLayoutService;
import com.elcom.its.config.service.ITSCoreCameraService;
import com.elcom.its.config.service.LayoutAreaService;
import com.elcom.its.config.service.ProcessUnitService;
import com.elcom.its.config.service.PuVideoThreadsService;
import com.elcom.its.config.service.StretchService;
import com.elcom.its.config.service.VdsService;
import com.elcom.its.config.validation.CameraLayoutValidation;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Administrator
 */
@RestController
public class CameraLayoutController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CameraLayoutController.class);

    @Value("${crowd.roitype}")
    private long crowdRoiType;

    @Value("${fire.roitype}")
    private long fireRoiType;

    @Value("${encroaching.roitype}")
    private long encroachingRoiType;

    @Value("${literring.roitype}")
    private long literringRoiType;

    @Value("${flooding.roitype}")
    private long floodingRoiType;

    @Value("${fobidden.roitype}")
    private long fobiddenRoiType;
    
    @Value("${fallenobject.roitype}")
    private long fallenobjectType;

    @Autowired
    private CameraLayoutService cameraLayoutService;

    @Autowired
    private ITSCoreCameraService cameraService;

    @Autowired
    private PuVideoThreadsService puVideoThreadsService;

    @Autowired
    private LayoutAreaService layoutAreaService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProcessUnitService processUnitService;

    @Autowired
    private VdsService vdsService;

    @Autowired
    private StretchService stretchService;

    public ResponseMessage getCameraLayoutsById(Map<String, String> headerParam, String requestPath,
            String method, String pathParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(),
                    "Bạn chưa đăng nhập", null));
        } else {
            Optional<CameraLayouts> services = cameraLayoutService.findById(Long.valueOf(pathParam));
            if (services.isPresent()) {
                CameraLayouts result = services.get();
                CameraDetailDTOMessage cameraDetailDTOMessage = cameraService.getCamerasByIdFromDBM(result.getCameraId());
                if (cameraDetailDTOMessage.getData() == null) {
                    return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), cameraDetailDTOMessage.getMessage(), null));
                }
                String stages = stretchService.findBySite(cameraDetailDTOMessage.getData().getSiteDTOForCameraId().getId());
                String listStages = dto.getUnit().getLisOfStage();
                List<String> myList = new ArrayList<>(Arrays.asList(listStages.split(",")));
                LOGGER.info("Check Abac");
                Map<String, Object> subject = new HashMap<>();
                subject.put("stage", stages);
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("stage", myList);
                Map<String, Object> bodyParamCheck = new HashMap<>();
                bodyParamCheck.put("subject", subject);
                bodyParamCheck.put("attributes", attributes);
                //Check ABAC
                ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParamCheck, "DETAIL", dto.getUuid(), requestPath);
                if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                    response = new ResponseMessage(new MessageContent(result));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                            "Bạn không có quyền xem danh sách Camera Layout", null));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.toString(), null));
            }
        }
        return response;
    }

    public ResponseMessage getCameraLayoutsList(Map<String, String> headerParam, String requestPath,
            String method, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                List<CameraLayouts> cameraLayoutses = cameraLayoutService.getAll();
                if (CollectionUtils.isEmpty(cameraLayoutses)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                            HttpStatus.OK.toString(), null));
                } else {
                    if (aBACResponseDTO.getAdmin() == null || !aBACResponseDTO.getAdmin()) {
                        //Truong hop ma co nhom camera => phan quyen theo nhom camera
                        //List<String> cameraIdsAllByUser = getAllCameraByUser(rBACResponseDTO, dto);
                        //cameraLayoutses = cameraLayoutses.stream().filter(item -> cameraIdsAllByUser.contains(item.getCameraId())).collect(Collectors.toList());
                    }
                    response = new ResponseMessage(new MessageContent(cameraLayoutses));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách Camera Layout", null));
            }
        }
        return response;
    }

    public ResponseMessage getLayoutsByCameraId(Map<String, String> headerParam, String requestPath,
            String method, String pathParam, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, method, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                String search = "";
                if (!StringUtil.isNullOrEmpty(urlParam)) {
                    Map<String, Object> query = StringUtil.getQueryMap(urlParam);
                    search = (String) query.get("search");
                }

                List<CameraLayouts> cameraLayoutses = cameraLayoutService.getLayoutsByCameraId(pathParam, search);
                if (CollectionUtils.isEmpty(cameraLayoutses)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                            HttpStatus.OK.toString(), null));
                } else {
                    if (CollectionUtils.isEmpty(cameraLayoutses)) {
                        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                                HttpStatus.OK.toString(), null));
                    } else {
                        if (aBACResponseDTO.getAdmin() == null || !aBACResponseDTO.getAdmin()) {
                            //Truong hop ma co nhom camera => phan quyen theo nhom camera
                            //List<String> cameraIdsAllByUser = getAllCameraByUser(rBACResponseDTO, dto);
                            //cameraLayoutses = cameraLayoutses.stream().filter(item -> cameraIdsAllByUser.contains(item.getCameraId())).collect(Collectors.toList());
                        }
                        response = new ResponseMessage(new MessageContent(cameraLayoutses));

                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách Camera Layout theo camera id", null));
            }
        }
        return response;
    }

    public ResponseMessage createCameraLayouts(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                String name = (String) bodyParam.get("name");
                String usedCaptureImage = (String) bodyParam.get("usedCaptureImage");
                String cameraId = (String) bodyParam.get("cameraId");
                Map<String, Object> jsonCanvas = (Map<String, Object>) bodyParam.get("jsonCanvas");
                int widthImgScale = ((Number) bodyParam.get("widthImgScale")).intValue();
                int heightImgScale = ((Number) bodyParam.get("heightImgScale")).intValue();
                String sizedImg = (String) bodyParam.get("sizedImg");
                String scaledFitImg = (String) bodyParam.get("scaledFitImg");
                int originWidth = ((Number) bodyParam.get("originWidth")).intValue();
                int originHeight = ((Number) bodyParam.get("originHeight")).intValue();
                String layoutImage = (String) bodyParam.get("layoutImage");
                String description = (String) bodyParam.get("description");
                int version = ((Number) bodyParam.get("version")).intValue();
                long modelProfileId = ((Number) bodyParam.get("modelProfileId")).longValue();
                int layoutType = ((Number) bodyParam.get("layoutType")).intValue();
                Map<String, Object> layoutAreas = (Map<String, Object>) bodyParam.get("layoutAreas");
                Map<String, Object> metaAttributes = (Map<String, Object>) bodyParam.get("metaAttributes");
                CameraLayoutDTO cameraLayoutDto = new CameraLayoutDTO(name, usedCaptureImage,
                        cameraId, jsonCanvas, widthImgScale, heightImgScale, sizedImg,
                        scaledFitImg, originWidth, originHeight, layoutImage, description,
                        version, modelProfileId, layoutType, layoutAreas, DataStatus.ENABLE,
                        metaAttributes);
                boolean checkUniqueName = cameraLayoutService.checkByName(cameraLayoutDto.getName());
                if (checkUniqueName) {
                    return new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Tên Camera Layout là duy nhất", null));
                }
                String invalidData = new CameraLayoutValidation().validateInsertCameraLayout(cameraLayoutDto);
                if (invalidData != null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                } else {
                    CameraDetailDTOMessage cameraDetailDTOMessage = cameraService.getCamerasByIdFromDBM(cameraLayoutDto.getCameraId());
                    if (cameraDetailDTOMessage == null || cameraDetailDTOMessage.getData() == null) {
                        return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy Camera", null));
                    }
                    CameraLayouts entity = CameraLayoutDTO.toEntity(cameraLayoutDto);
                    entity.setStatus(DataStatus.ENABLE.code());
                    entity.setCreatedBy(dto.getUuid());
                    entity.setCreatedDate(new Date());
                    entity.setModifiedBy(dto.getUuid());
                    entity.setModifiedDate(new Date());
                    entity = cameraLayoutService.create(entity);
                    response = new ResponseMessage(new MessageContent(HttpStatus.CREATED.value(), HttpStatus.CREATED.toString(), entity));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền thêm Camera Layout", null));
            }
        }
        return response;
    }

    public ResponseMessage updateCameraLayouts(String requestPath, Map<String, String> headerParam,
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
                    String name = (String) bodyParam.get("name");
                    String usedCaptureImage = (String) bodyParam.get("usedCaptureImage");
                    String cameraId = (String) bodyParam.get("cameraId");
                    Map<String, Object> jsonCanvas = (Map<String, Object>) bodyParam.get("jsonCanvas");
                    int widthImgScale = ((Number) bodyParam.get("widthImgScale")).intValue();
                    int heightImgScale = ((Number) bodyParam.get("heightImgScale")).intValue();
                    String sizedImg = (String) bodyParam.get("sizedImg");
                    String scaledFitImg = (String) bodyParam.get("scaledFitImg");
                    int originWidth = ((Number) bodyParam.get("originWidth")).intValue();
                    int originHeight = ((Number) bodyParam.get("originHeight")).intValue();
                    String layoutImage = (String) bodyParam.get("layoutImage");
                    String description = (String) bodyParam.get("description");
                    int version = ((Number) bodyParam.get("version")).intValue();
                    long modelProfileId = ((Number) bodyParam.get("modelProfileId")).longValue();
                    String layoutTypeStr = (String) bodyParam.get("layoutType");
                    int layoutType = 1;
                    if (!StringUtil.isNullOrEmpty(layoutTypeStr)) {
                        layoutType = Integer.parseInt(layoutTypeStr);
                    }
                    Map<String, Object> layoutAreas = (Map<String, Object>) bodyParam.get("layoutAreas");
                    Map<String, Object> metaAttributes = (Map<String, Object>) bodyParam.get("metaAttributes");
                    CameraLayoutDTO cameraLayoutDto = new CameraLayoutDTO(name, usedCaptureImage, cameraId, jsonCanvas, widthImgScale, heightImgScale, sizedImg, scaledFitImg, originWidth, originHeight, layoutImage, description, version, modelProfileId, layoutType, layoutAreas, DataStatus.ENABLE, metaAttributes);
                    cameraLayoutDto.setId(id);
                    String invalidData = new CameraLayoutValidation().validateInsertCameraLayout(cameraLayoutDto);
                    if (invalidData != null) {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                    } else {
                        Optional<CameraLayouts> cameraLayoutOptional = cameraLayoutService.findById(id);
                        if (cameraLayoutOptional.isEmpty()) {
                            return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Camera layout không tồn tại", null);
                        }
                        //Check exist camera
                        CameraDetailDTOMessage cameraDetailDTOMessage = cameraService.getCamerasByIdFromDBM(cameraLayoutDto.getCameraId());
                        if (cameraDetailDTOMessage == null || cameraDetailDTOMessage.getData() == null) {
                            return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy Camera", null));
                        }
                        CameraLayouts entity = CameraLayoutDTO.toEntity(cameraLayoutDto);
                        entity.setCreatedBy(cameraLayoutOptional.get().getCreatedBy());
                        entity.setCreatedDate(cameraLayoutOptional.get().getCreatedDate());
                        entity.setModifiedBy(dto.getUuid());
                        entity.setModifiedDate(new Date());
                        entity.setId(id);
                        try {
                            entity = cameraLayoutService.update(entity);

                            //Update json spec
                            List<String> processUnitIdList = vdsService.findProcessUnitIdByLayoutId(id);
                            if (processUnitIdList != null && !processUnitIdList.isEmpty()) {
                                for (String processUnitId : processUnitIdList) {
                                    ProcessUnit processUnit = processUnitService.findById(processUnitId).orElse(null);
                                    if (processUnit != null) {
                                        String jsonSpec = processUnitService.toJsonSpec(processUnit);
                                        processUnit.setSpec(jsonSpec);
                                        processUnitService.update(processUnit);
                                    }
                                }
                            }

                            // save ai_engine_json to cameras table
                            int gLayoutType = cameraLayoutOptional.get().getLayoutType();
                            if (gLayoutType == CameraLayoutType.LAYOUT_GSAN.layoutType()) { // layout camera an ninh
                                boolean result = saveAiEngineJson(cameraLayoutOptional.get().getCameraId(), id);
                                if (result) {
                                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), entity));
                                } else {
                                    response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lưu layout không thành công", null));
                                }
                            } else {
                                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), entity));
                            }
                        } catch (Exception ex) {
                            response = new ResponseMessage(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.getReasonPhrase(),
                                    new MessageContent(HttpStatus.NOT_MODIFIED.value(), HttpStatus.NOT_MODIFIED.getReasonPhrase(),
                                            ex.toString()));
                            LOGGER.error("Error to update camera layout >>> " + ex.toString());
                            ex.printStackTrace();
                        }
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền sửa camera layout", null));
            }
        }
        return response;
    }

    // save ai_engine_json to cameras table
    private boolean saveAiEngineJson(String cameraId, Long layoutId) {
        boolean result = false;
        try {
            List<AIEngineJson> aiEngineJsonLst = new ArrayList<>();
            List<LayoutAreas> layoutAreasLst = layoutAreaService.findByCameraLayoutId(layoutId);
            if (!CollectionUtils.isEmpty(layoutAreasLst)) {
                for (LayoutAreas area : layoutAreasLst) {
                    AIEngineJson aiEngineJson = new AIEngineJson();
                    if (area.getRoiType() == fobiddenRoiType) {
                        //
                    } else if (area.getRoiType() == encroachingRoiType) {
                        aiEngineJson.setEvent("encroaching");
                        aiEngineJson.setTime(((Number) area.getJsonDetailArea().get("time")).intValue());
                        aiEngineJson.setLinkAPI((String) area.getJsonDetailArea().get("linkAPI"));
                        aiEngineJson.setLiveImageUrl((String) area.getJsonDetailArea().get("liveImageUrl"));

                        // encroaching
                        List<EncroachingInfo> encroachingList = new ArrayList<>();

                        EncroachingInfo encroachingInfo = new EncroachingInfo();
                        encroachingInfo.setId(((Number) area.getJsonDetailArea().get("id")).longValue());
                        encroachingInfo.setZone((String) area.getJsonDetailArea().get("path"));

                        // affect_object
                        ObjectMapper objectMapper = new ObjectMapper();
                        String affectObject = objectMapper.writeValueAsString(area.getJsonDetailArea().get("affect_object"));
                        List<AffectObject> affectObjectLst = objectMapper.readValue(affectObject, new TypeReference<List<AffectObject>>() {
                        });
                        encroachingInfo.setAffect_object(affectObjectLst);

                        encroachingList.add(encroachingInfo);

                        aiEngineJson.setEncroaching(encroachingList);
                        aiEngineJsonLst.add(aiEngineJson);
                    } else if (area.getRoiType() == fallenobjectType) {
                        aiEngineJson.setEvent("fallenobject");
                        aiEngineJson.setTime(((Number) area.getJsonDetailArea().get("time")).intValue());
                        aiEngineJson.setLinkAPI((String) area.getJsonDetailArea().get("linkAPI"));
                        aiEngineJson.setLiveImageUrl((String) area.getJsonDetailArea().get("liveImageUrl"));

                        // fallenobject
                        List<FallenObjectInfo> fallenobjectList = new ArrayList<>();

                        FallenObjectInfo fallenobjectInfo = new FallenObjectInfo();
                        fallenobjectInfo.setId(((Number) area.getJsonDetailArea().get("id")).longValue());
                        fallenobjectInfo.setZone((String) area.getJsonDetailArea().get("path"));
                        
                        fallenobjectList.add(fallenobjectInfo);
                        aiEngineJson.setFallenobject(fallenobjectList);
                        aiEngineJsonLst.add(aiEngineJson);
                    } else {
                        String event = "";
                        if (area.getRoiType() == crowdRoiType) {
                            event = "crowd";
                        } else if (area.getRoiType() == fireRoiType) {
                            event = "fire";
                        } else if (area.getRoiType() == literringRoiType) {
                            event = "literring";
                        } else if (area.getRoiType() == floodingRoiType) {
                            event = "flooding";
                        }

                        aiEngineJson.setEvent(event);
                        aiEngineJson.setTime(((Number) area.getJsonDetailArea().get("time")).intValue());
                        aiEngineJson.setLinkAPI((String) area.getJsonDetailArea().get("linkAPI"));
                        aiEngineJson.setLiveImageUrl((String) area.getJsonDetailArea().get("liveImageUrl"));
                        aiEngineJsonLst.add(aiEngineJson);
                    }
                }
            }

            // save aiEngineJson
            String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/camera/ai-engine-json/" + cameraId;
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<AIEngineJson>> requestBody = new HttpEntity<>(aiEngineJsonLst, headers);
            ResponseEntity<CamerasResponse> response = restTemplate.exchange(urlRequest, HttpMethod.PUT, requestBody, CamerasResponse.class);
            CamerasResponse dto = response != null ? response.getBody() : null;
            if (dto != null && (dto.getStatus() == HttpStatus.OK.value())) {
                result = true;
            }
        } catch (Exception ex) {
            LOGGER.error("Error to save ai engine json >>> " + ex.toString());
            ex.printStackTrace();
        }
        return result;
    }

    public ResponseMessage deleteCameraLayouts(String requestPath, Map<String, String> headerParam,
            String requestMethod, Long layoutId, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                boolean checkExistVideoThread = puVideoThreadsService.existsByLayoutId(layoutId);
                if (checkExistVideoThread) {
                    return new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Không có quyền xóa Camera Layout do tồn tại trong Luồng video", null));
                }
                Optional<CameraLayouts> camearLayOptionalById = cameraLayoutService.findById(layoutId);
                if (camearLayOptionalById.isPresent()) {
                    return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy CameraLayouts", null);
                }
                cameraLayoutService.delete(camearLayOptionalById.get());
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));

            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền Xóa Camera Layout", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteMultiCameraLayouts(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                List<Integer> idCameraLayoutList = (List<Integer>) bodyParam.get("idCameraLayoutList");
                if (CollectionUtils.isEmpty(idCameraLayoutList)) {
                    return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Chưa có CameraLayout nào", null));
                }
                for (Integer idCameraLayout : idCameraLayoutList) {
                    Long idCameraLayoutLong = ((Number) idCameraLayout).longValue();
                    boolean checkExistVideoThread = puVideoThreadsService.existsByLayoutId(idCameraLayoutLong);
                    if (checkExistVideoThread) {
                        return new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Không có quyền xóa Camera Layout Id =" + idCameraLayout + "do tồn tại trong Luồng video", null));
                    }
                    Optional<CameraLayouts> camearLayOptionalById = cameraLayoutService.findById(idCameraLayoutLong);
                    if (camearLayOptionalById.isEmpty()) {
                        return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy CameraLayouts", null);
                    }
                    cameraLayoutService.delete(camearLayOptionalById.get());
                }

                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));

            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền Xóa Camera Layout", null));
            }
        }
        return response;
    }
}
