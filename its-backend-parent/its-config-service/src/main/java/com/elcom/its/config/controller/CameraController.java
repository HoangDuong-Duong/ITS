/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.controller;

import com.elcom.its.config.constant.Constant;
import com.elcom.its.config.model.Site;
import com.elcom.its.config.model.dto.*;
import com.elcom.its.config.service.ITSCoreCameraService;
import com.elcom.its.config.service.SiteService;
import com.elcom.its.config.service.StretchService;
import com.elcom.its.config.service.VdsService;
import com.elcom.its.config.tools.CameraDtoUtils;
import com.elcom.its.config.validation.CameraValidation;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

/**
 * @author Admin
 */
@Controller
public class CameraController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CameraController.class);

    @Autowired
    private ITSCoreCameraService cameraService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private StretchService stretchService;

    @Autowired
    private VdsService vdsService;

    /* public ResponseMessage getCameraList(Map<String, String> headerParam, String requestPath,
                                         String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                CameraResponseList camerasList = cameraService.getCamerasListFromDBM(urlParam);
                if (CollectionUtils.isEmpty(camerasList.getData())) {
                    return new ResponseMessage(new MessageContent(camerasList.getStatus(), camerasList.getMessage(), null));
                }
                Map<String, Object> query = StringUtil.getQueryMap(urlParam);
                String getfull = (String) query.get("getfull");
                if (abacResponseDTO.getAdmin() && !"true".equalsIgnoreCase(getfull)) {
                    List<String> cameraAllArr = new ArrayList<>();
                    Pageable pageable = null;
                    String search = "";
                    if (!StringUtil.isNullOrEmpty(urlParam)) {
                        Integer page = Integer.parseInt((String) query.get("page"));
                        Integer size = Integer.parseInt((String) query.get("size"));
                        search = (String) query.get("search");
                        pageable = PageRequest.of(page, size);
                    }
                    List<OldCameraDetailDTO> cameraListDto = camerasList.getData();

                    cameraListDto = cameraListDto.parallelStream().filter(camera -> cameraAllArr.contains(camera.getId())).collect(Collectors.toList());
                    if (pageable == null) {
                        return new ResponseMessage(new MessageContent(camerasList.getStatus(), camerasList.getMessage(), cameraListDto));
                    } else {
                        Page<OldCameraDetailDTO> cameraListDtoPage = toPage(cameraListDto, pageable);
                        return new ResponseMessage(new MessageContent(camerasList.getStatus(), camerasList.getMessage(), cameraListDtoPage.getContent(), cameraListDtoPage.getTotalElements()));
                    }
                } else {
                    response = new ResponseMessage(new MessageContent(camerasList.getStatus(), camerasList.getMessage(), camerasList.getData(), camerasList.getTotal()));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách camera", null));
            }
        }
        return response;
    }*/
    public ResponseMessage getAllCamera(Map<String, String> headerParam,
            String requestPath,
            String method,
            String urlParam,
            Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                    "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);

            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (abacResponseDTO.getAdmin() != null && abacResponseDTO.getAdmin()) {
                    if (StringUtil.isNullOrEmpty(urlParam)) {
                        responseMessage = new ResponseMessage(new MessageContent(cameraService.getAll()));
                    } else {
                        Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                        Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                        String search = params.get("search") != null ? (params.get("search")) : "";;
                        String stageCodes = params.get("stageCodes");
                        if (page != null && size != null) {
                            CameraPaginationDTO cameraPaginationDTO;
                            if (StringUtil.isNullOrEmpty(stageCodes)) {
                                cameraPaginationDTO = cameraService.getAll(page, size, search);
                            } else {
                                cameraPaginationDTO = cameraService.getAll(stageCodes, page, size, search);
                            }
                            if (cameraPaginationDTO != null && cameraPaginationDTO.getStatus() == HttpStatus.OK.value()) {
                                return new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                                        HttpStatus.OK.toString(),
                                        cameraPaginationDTO.getData(),
                                        cameraPaginationDTO.getTotal()));
                            }
                        } else {
                            return new ResponseMessage(new MessageContent(cameraService.getAll()));
                        }
                    }
                } else {
                    String stages = dto.getUnit().getLisOfStage();
                    if (StringUtil.isNullOrEmpty(urlParam)) {
                        return new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                                "Bạn chưa quản lý đoạn đường nào", null));
                    } else {
                        Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                        Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                        String search = params.get("search") != null ? (params.get("search")) : "";;
                        String stageCodes = params.get("stageCodes");
                        LOGGER.info("Page: " + page);
                        LOGGER.info("Size: " + size);
                        LOGGER.info("Search: " + search);
                        if (page != null && size != null) {
                            if (!StringUtil.isNullOrEmpty(stageCodes)) {
                                stages = filterListStage(stageCodes, stages);
                            }
                            CameraPaginationDTO cameraPaginationDTO = cameraService.getAll(stages, page, size, search);
                            if (cameraPaginationDTO != null && cameraPaginationDTO.getStatus() == HttpStatus.OK.value()) {
                                return new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                                        HttpStatus.OK.toString(),
                                        cameraPaginationDTO.getData(),
                                        cameraPaginationDTO.getTotal()));
                            }
                        } else {
                            return new ResponseMessage(new MessageContent(cameraService.getAll(stages, 0, 1000, "")));
                        }
                    }

                }
            } else {
                return new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách camera", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage getCameraBySiteIdList(Map<String, String> headerParam,
            String requestPath,
            String method,
            String urlParam,
            Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    responseMessage = new ResponseMessage(new MessageContent(cameraService.getAll()));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    String siteIds = params.get("siteIds");

                    if (siteIds != null) {
                        List<String> siteIdList = List.of(siteIds.split(","));
                        if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                            String stages = dto.getUnit().getLisOfStage();
                            List<SiteInfoDTO> sites = siteService.getAllSiteUser(stages, false);
                            List<String> checkSite = sites.stream().map((item) -> item.getSiteId()).collect(Collectors.toList());
                            List<String> tmp = siteIdList.stream().filter((item) -> checkSite.contains(item)).collect(Collectors.toList());
                            siteIdList = tmp;
                        }
                        Response response = cameraService.getCameraBySiteIds(siteIdList);

                        responseMessage = new ResponseMessage(new MessageContent(response.getData()));
                    } else {
                        if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                            List<String> stageIdList = List.of(dto.getUnit().getLisOfStage().split(","));
                            responseMessage = new ResponseMessage(new MessageContent(cameraService.getCameraByStageIds(stageIdList).getData()));
                        } else {
                            responseMessage = new ResponseMessage(new MessageContent(cameraService.getAll()));
                        }

                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền lấy danh sách camera theo danh sách camera theo vị trí", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage getCameraByStageIdList(Map<String, String> headerParam,
            String requestPath,
            String method,
            String urlParam,
            Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String siteIds = params.get("stageIds");

                if (siteIds != null) {
                    List<String> stageIdList = List.of(siteIds.split(","));
                    if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                        List<String> stages = List.of(dto.getUnit().getLisOfStage().split(","));
                        List<String> search = stageIdList.stream().filter((item) -> stages.contains(item)).collect(Collectors.toList());
                        stageIdList = search;
                    }
                    Response response = cameraService.getCameraByStageIds(stageIdList);

                    responseMessage = new ResponseMessage(new MessageContent(response.getData()));
                } else {
                    if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                        List<String> stages = List.of(dto.getUnit().getLisOfStage().split(","));
                        responseMessage = new ResponseMessage(new MessageContent(cameraService.getCameraByStageIds(stages)));
                    } else {
                        responseMessage = new ResponseMessage(new MessageContent(cameraService.getAll()));
                    }
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền lấy danh sách camera theo danh sách camera theo đoạn đường", null));
            }
        }

        return responseMessage;
    }

    public ResponseMessage getCameraType(Map<String, String> headerParam, String requestPath,
            String method, Map<String, Object> bodyParam) {
        long start = System.currentTimeMillis();
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        long end = System.currentTimeMillis();
        LOGGER.info("Authen token in: {}ms", (end - start));
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            end = System.currentTimeMillis();
            LOGGER.info("Authorize Role in: {}ms", (end - start));
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                CategoryResponseList categorys = cameraService.getCameraTypeFromDBM();
                if (CollectionUtils.isEmpty(categorys.getData())) {
                    return new ResponseMessage(new MessageContent(categorys.getStatus(), categorys.getMessage(), null));
                }
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), categorys.getData()));
                end = System.currentTimeMillis();
                LOGGER.info("Get data from DBM in: {}ms", (end - start));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem loại Camera", null));
            }
        }
        return response;
    }

    public ResponseMessage getCameraById(Map<String, String> headerParam, String requestPath,
            String method, String paramPath, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            CameraDetailDTOMessage cameraDetailDTOMessage = cameraService.getCamerasByIdFromDBM(paramPath);
            if (cameraDetailDTOMessage.getData() == null) {
                return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), cameraDetailDTOMessage.getMessage(), null));
            }
            String stages = null;
            try {
                stages = stretchService.findBySite(cameraDetailDTOMessage.getData().getSiteDTOForCameraId().getId());
            } catch (Exception e) {
            }
            String listStages = dto.getUnit().getLisOfStage();
            List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
            LOGGER.info("Check Abac");
            Map<String, Object> subject = new HashMap<>();
            subject.put("stage", stages);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("stage", myList);
            Map<String, Object> bodyParamCheck = new HashMap<>();
            bodyParamCheck.put("subject", subject);
            bodyParamCheck.put("attributes", attributes);
            // Check ABAC
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParamCheck, "DETAIL", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                response = new ResponseMessage(new MessageContent(cameraDetailDTOMessage.getData()));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem camera", null));
            }
        }
        return response;
    }

    public ResponseMessage createCamera(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) {
        LOGGER.info("Creating camera!");
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            if (bodyParam == null || bodyParam.isEmpty()) {
                responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            } else {
                CameraCreateUpdateDTO cameraDTO
                        = CameraDtoUtils.getCreateUpdateCameraDtoFromBodyParam(bodyParam);
                LOGGER.info("Camera: " + cameraDTO);
                String validatedData = new CameraValidation().validateCamera(cameraDTO);

                if (validatedData != null) {
                    responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), validatedData,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), validatedData, null));
                } else {
                    String stages = stretchService.findBySite(cameraDTO.getSiteId());
                    String listStages = dto.getUnit().getLisOfStage();
                    List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                    LOGGER.info("Check Abac");
                    Map<String, Object> subject = new HashMap<>();
                    subject.put("stage", stages);
                    Map<String, Object> attributes = new HashMap<>();
                    attributes.put("stage", myList);
                    Map<String, Object> bodyParamCheck = new HashMap<>();
                    bodyParamCheck.put("subject", subject);
                    bodyParamCheck.put("attributes", attributes);
                    // Check ABAC
                    ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParamCheck, "POST", dto.getUuid(), requestPath);
                    if (abacResponseDTO == null && !abacResponseDTO.getStatus()) {
                        return new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                                "Bạn không có quyền thêm camera", null));
                    }
                    CameraDetailDTOMessage message = cameraService.createCamera(cameraDTO);
                    LOGGER.info("Create camera succesfully: " + message.getData());
                    responseMessage = new ResponseMessage(new MessageContent(
                            HttpStatus.CREATED.value(),
                            HttpStatus.OK.toString(),
                            message.getData()
                    ));
                }
            }
        }
        return responseMessage;
    }

    public ResponseMessage updateCamera(String requestPath,
            Map<String, String> headerParam,
            Map<String, Object> bodyParam,
            String requestMethod,
            String pathParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            if (bodyParam == null || bodyParam.isEmpty()) {
                responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            } else {
                CameraCreateUpdateDTO cameraDTO
                        = CameraDtoUtils.getCreateUpdateCameraDtoFromBodyParam(bodyParam);

                String validatedData = new CameraValidation().validateCamera(cameraDTO);

                if (validatedData != null) {
                    responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(),
                            validatedData,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), validatedData, null));
                } else {
                    CameraDetailDTOMessage cameraDetailDTOMessage = cameraService.getCamerasByIdFromDBM(pathParam);
                    if (cameraDetailDTOMessage.getData() != null) {
                        String stages = stretchService.findBySite(cameraDTO.getSiteId());
                        String listStages = dto.getUnit().getLisOfStage();
                        List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                        LOGGER.info("Check Abac");
                        Map<String, Object> subject = new HashMap<>();
                        subject.put("stage", stages);
                        Map<String, Object> attributes = new HashMap<>();
                        attributes.put("stage", myList);
                        Map<String, Object> bodyParamCheck = new HashMap<>();
                        bodyParamCheck.put("subject", subject);
                        bodyParamCheck.put("attributes", attributes);
                        // Check ABAC
                        ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParamCheck, "PUT", dto.getUuid(), requestPath);
                        if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                            CameraDetailDTOMessage responseDto = cameraService.updateCamera(cameraDTO, pathParam);
                            if(cameraDTO.getSiteId() != null && !cameraDTO.getSiteId().isEmpty()){
                                vdsService.updateVdsSiteId(pathParam, cameraDTO.getSiteId(), responseDto.getData().getSiteDTOForCameraId().getName());
                            }
                            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), responseDto.getData()));
                        } else {
                            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                                    "Bạn không có quyền cập nhật camera", null));
                        }
                    } else {
                        responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                    }
                }
            }

        }
        return responseMessage;
    }

    public ResponseMessage deleteCamera(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        LOGGER.info("Delete camera!");
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            CameraDetailDTOMessage cameraDetailDTOMessage = cameraService.getCamerasByIdFromDBM(pathParam);
            if (cameraDetailDTOMessage.getData() == null) {
                return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy id tương ứng: " + pathParam, null);
            } else {
                String stages = null;
                if(cameraDetailDTOMessage.getData().getSiteDTOForCameraId() != null){
                    stages = stretchService.findBySite(cameraDetailDTOMessage.getData().getSiteDTOForCameraId().getId());
                }
                String listStages = dto.getUnit().getLisOfStage();
                List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                LOGGER.info("Check Abac");
                Map<String, Object> subject = new HashMap<>();
                subject.put("stage", stages);
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("stage", myList);
                Map<String, Object> bodyParamCheck = new HashMap<>();
                bodyParamCheck.put("subject", subject);
                bodyParamCheck.put("attributes", attributes);
                // Check ABAC
                ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParamCheck, "DELETE", dto.getUuid(), requestPath);
                if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                    Response response = cameraService.deleteCamerasFromDBM(pathParam);
                    responseMessage = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "Xoa thanh cong camera co Id: " + pathParam, response.getData()));
                } else {
                    responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa Camera", null));
                }
            }
        }
        return responseMessage;
    }

    public ResponseMessage deleteMultiCamera(Map<String, String> headerParam,
            String requestPath,
            String method,
            String urlParam,
            Map<String, Object> bodyParam) {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            if (bodyParam == null || bodyParam.isEmpty()) {
                responseMessage = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            } else {
                @SuppressWarnings("unchecked")
                List<String> cameraIdsDTO = (List<String>) bodyParam.get("cameraIds");

                if (cameraIdsDTO.size() == 0) {
                    responseMessage = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "Không có camera nào để xóa", null));
                } else {
                    CameraDetailDTOMessage cameraDetailDTOMessage = cameraService.getCamerasByIdFromDBM(cameraIdsDTO.get(0));
                    if (cameraDetailDTOMessage.getData() == null) {
                        return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy thông tin camera : ", null);
                    } else {
                        String stages = null;
                        if(cameraDetailDTOMessage.getData().getSiteDTOForCameraId() != null){
                            stages = stretchService.findBySite(cameraDetailDTOMessage.getData().getSiteDTOForCameraId().getId());
                        }
                        String listStages = dto.getUnit().getLisOfStage();
                        List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                        LOGGER.info("Check Abac");
                        Map<String, Object> subject = new HashMap<>();
                        subject.put("stage", stages);
                        Map<String, Object> attributes = new HashMap<>();
                        attributes.put("stage", myList);
                        Map<String, Object> bodyParamCheck = new HashMap<>();
                        bodyParamCheck.put("subject", subject);
                        bodyParamCheck.put("attributes", attributes);
                        // Check ABAC
                        ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParamCheck, "DELETE", dto.getUuid(), requestPath);
                        if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                            Response response = cameraService.deleteMultipleCameras(cameraIdsDTO);
                            responseMessage = new ResponseMessage(new MessageContent(response.getStatus(), response.getMessage(), response.getData()));
                        } else {
                            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa Camera", null));
                        }
                    }
                }
            }
        }

        return responseMessage;
    }

    public Page<OldCameraDetailDTO> toPage(List<OldCameraDetailDTO> list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = (int) ((pageable.getOffset() + pageable.getPageSize()) > list.size()
                ? list.size()
                : pageable.getOffset() + pageable.getPageSize());
        List<OldCameraDetailDTO> subList = list.subList(startIndex, endIndex);
        return new PageImpl<OldCameraDetailDTO>(subList, pageable, list.size());
    }

    private String filterListStage(String stageInParam, String stageInUnit) {
        List<String> listStageInParam = Arrays.asList(stageInParam.split(","));
        List<String> liststageInUnit = Arrays.asList(stageInUnit.split(","));

        List<String> listStageFilter = listStageInParam.stream().filter(x -> {
            return liststageInUnit.contains(x);
        }).collect(Collectors.toList());
        return String.join(",", listStageFilter);
    }

}
