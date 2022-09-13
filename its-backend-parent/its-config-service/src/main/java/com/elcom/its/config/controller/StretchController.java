package com.elcom.its.config.controller;

import com.elcom.its.config.constant.Constant;
import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.Stretch;
import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.StretchService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StretchController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceController.class);
    @Autowired
    private StretchService stretchService;

    public static Map<String, String> getUrlParamValuesNoDecode(String url) {
        Map<String, String> paramsMap = new HashMap();
        String[] params = url.split("&");
        String[] var4 = params;
        int var5 = params.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            String param = var4[var6];
            String[] temp = param.split("=");

            try {
                paramsMap.put(temp[0], temp.length > 1 ? temp[1] : "");
            } catch (Exception var9) {
                var9.printStackTrace();
            }
        }

        return paramsMap;
    }

    public ResponseMessage getAllStretch(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                    String stages = dto.getUnit().getLisOfStage();
                    if (StringUtil.isNullOrEmpty(urlParam)) {
                        response = new ResponseMessage(new MessageContent(stretchService.getAllStretch(stages, false)));
                    } else {
                        Map<String, String> params = StringUtil.getUrlParamValuesNoDecode(urlParam);
                        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                        Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                        String search = params.get("search");
                        if (page != null && size != null) {
                            Response stretchPaginationDTO = stretchService.getAllStretch(stages, page, size, search, false);
                            if (stretchPaginationDTO != null && stretchPaginationDTO.getStatus() == HttpStatus.OK.value()) {
                                return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), stretchPaginationDTO.getData(), stretchPaginationDTO.getTotal()));
                            }
                        } else {
                            return new ResponseMessage(new MessageContent(stretchService.getAllStretch(stages, false)));
                        }
                    }
                } else {
                    if (StringUtil.isNullOrEmpty(urlParam)) {
                        response = new ResponseMessage(new MessageContent(stretchService.getAllStretch(null, true)));
                    } else {
                        Map<String, String> params = StringUtil.getUrlParamValuesNoDecode(urlParam);
                        Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                        Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                        String search = params.get("search");
                        if (page != null && size != null) {
                            Response stretchPaginationDTO = stretchService.getAllStretch(null, page, size, search, true);
                            if (stretchPaginationDTO != null && stretchPaginationDTO.getStatus() == HttpStatus.OK.value()) {
                                return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), stretchPaginationDTO.getData(), stretchPaginationDTO.getTotal()));
                            }
                        } else {
                            return new ResponseMessage(new MessageContent(stretchService.getAllStretch(null, true)));
                        }
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách đoạn đường", null));
            }

        }
        return response;
    }

    public ResponseMessage getStretchByListCode(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValuesNoDecode(urlParam);
                String listCode = (String) params.get("stageCodes");
                Response stretchPaginationDTO = stretchService.getStretchByListCodes(listCode);
                return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), stretchPaginationDTO));
            }else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách đoạn đường", null));
            }
        }
        return response;
    }

    public ResponseMessage getStretchById(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                    String stages = dto.getUnit().getLisOfStage();
                    Response stretch = stretchService.getStretchById(stages, paramPath, false);
                    response = new ResponseMessage(new MessageContent(stretch));
                } else {
                    Response stretch = stretchService.getStretchById(null, paramPath, true);
                    response = new ResponseMessage(new MessageContent(stretch));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem chi tiết đoạn đường", null));

            }
        }
        return response;
    }

    public ResponseMessage createStretch(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "POST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (bodyParam == null || bodyParam.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    String name = (String) bodyParam.get("name");
                    String siteStart = (String) bodyParam.get("siteStart");
                    String siteEnd = (String) bodyParam.get("siteEnd");
                    String code = (String) bodyParam.get("code");

                    Stretch stretch = new Stretch();

                    stretch.setName(name);
                    stretch.setSiteStart(siteStart);
                    stretch.setSiteEnd(siteEnd);
                    stretch.setCreatedBy(dto.getUuid());
                    stretch.setCreatedDate(new Date());
                    stretch.setCode(code);

                    response = new ResponseMessage(new MessageContent(stretchService.saveStretch(stretch)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền thêm mới đoạn đường", null));
            }
        }
        return response;
    }

    public ResponseMessage updateStretch(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "PUT", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (bodyParam == null || bodyParam.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    String name = (String) bodyParam.get("name");
                    String siteStart = (String) bodyParam.get("siteStart");
                    String siteEnd = (String) bodyParam.get("siteEnd");
                    String directionCode = (String) bodyParam.get("directionCode");
                    String directionString = (String) bodyParam.get("directionString");
                    String code = (String) bodyParam.get("code");

                    Stretch stretch = new Stretch();

                    stretch.setId(pathParam);
                    stretch.setName(name);
                    stretch.setSiteStart(siteStart);
                    stretch.setSiteEnd(siteEnd);
                    stretch.setDirectionCode(directionCode);
                    stretch.setDirectionString(directionString);
                    stretch.setCreatedBy(dto.getUuid());
                    stretch.setCreatedDate(new Date());
                    stretch.setCode(code);

                    Response stretchDetailDTO = stretchService.getStretchById(null, pathParam, true);
                    if (stretchDetailDTO != null) {
                        response = new ResponseMessage(new MessageContent(stretchService.updateStretch(stretch)));
                    } else {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền sửa đoạn đường", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteStretch(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws JsonProcessingException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            String stages;
            Boolean isAdmin;
            if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                stages = dto.getUnit().getLisOfStage();
                isAdmin = false;
            } else {
                stages = null;
                isAdmin = true;
            }

            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response stretchDetailDTO = stretchService.getStretchById(null, pathParam, true);
                if (stretchDetailDTO == null) {
                    return new ResponseMessage(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.toString(), null);
                }
                Response deleteResponse = stretchService.deleteStretch(stages, pathParam, isAdmin);
                response = new ResponseMessage(new MessageContent(deleteResponse));
                if (deleteResponse.getStatus() == 200) {
                    publishDeletedStage(pathParam);
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa đoạn đường", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteMultiStretch(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws JsonProcessingException {
        ResponseMessage response = null;

        List<String> uuid = (List<String>) bodyParam.get("ids");
        ListUuid listUuid = new ListUuid();
        listUuid.setIds(uuid);

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);

            String stages;
            Boolean isAdmin;
            if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                stages = dto.getUnit().getLisOfStage();
                isAdmin = false;
            } else {
                stages = null;
                isAdmin = true;
            }

            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response deleteResponse = stretchService.deleteMultiStage(stages, listUuid, isAdmin);
                response = new ResponseMessage(new MessageContent(deleteResponse));
                if (deleteResponse.getStatus() == 200) {
                    publishDeletedListStage(uuid);
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa đoạn đường", null));
            }
        }
        return response;
    }

    public ResponseMessage filterStretch(Map<String, String> headerParam, String requestPath, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);

            String stages;
            Boolean isAdmin;
            if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                stages = dto.getUnit().getLisOfStage();
                isAdmin = false;
            } else {
                stages = null;
                isAdmin = true;
            }

            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    response = new ResponseMessage(new MessageContent(stretchService.getAllStretch(stages, isAdmin)));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                    Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                    String id = params.get("id");
                    String siteStart = params.get("siteStart");
                    String siteEnd = params.get("siteEnd");
                    String name = params.get("name");
                    if (page != null && size != null || id != null || name != null || siteStart != null || siteEnd != null) {
                        Response stretchPaginationDTO = stretchService.filterStretch(stages, page, size, id, name, siteStart, siteEnd, isAdmin);
                        if (stretchPaginationDTO != null && stretchPaginationDTO.getStatus() == HttpStatus.OK.value()) {
                            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), stretchPaginationDTO.getData(), stretchPaginationDTO.getTotal()));
                        }
                    } else {
                        return new ResponseMessage(new MessageContent(stretchService.getAllStretch(stages, isAdmin)));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem", null));
            }

        }
        return response;
    }
}
