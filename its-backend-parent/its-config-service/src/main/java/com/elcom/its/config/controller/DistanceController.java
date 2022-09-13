package com.elcom.its.config.controller;

import com.elcom.its.config.constant.Constant;
import com.elcom.its.config.model.Distance;
import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.dto.*;
import com.elcom.its.config.service.DistanceService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
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
public class DistanceController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceController.class);

    @Autowired
    private DistanceService distanceService;

    public static Map<String, String> getUrlParamValuesNoDecode(String url) {
        Map<String, String> paramsMap = new HashMap();
        String[] params = url.split("&");
        String[] var4 = params;
        int var5 = params.length;

        for(int var6 = 0; var6 < var5; ++var6) {
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

    public ResponseMessage getAllDistance(Map<String, String> headerParam, String requestPath,
                                          String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    response = new ResponseMessage(new MessageContent(distanceService.getAllDistance()));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValuesNoDecode(urlParam);
                    Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                    Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                    String search = params.get("search");
                    if (page != null && size != null) {
                        Response distancePaginationDTO = distanceService.getAllDistance(page, size, search);
                        if (distancePaginationDTO != null && distancePaginationDTO.getStatus() == HttpStatus.OK.value()) {
                            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), distancePaginationDTO.getData(), distancePaginationDTO.getTotal()));
                        }
                    } else {
                        return new ResponseMessage(new MessageContent(distanceService.getAllDistance()));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền", null));
            }

        }
        return response;
    }

    public ResponseMessage getDistanceById(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        Response distance = distanceService.getDistanceById(paramPath);
        if (distance.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), distance.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(distance.getData()));
        }
        return response;
    }

    public ResponseMessage createDistance(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "POST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (bodyParam == null || bodyParam.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    String name = (String) bodyParam.get("name");
                    String siteStart = (String) bodyParam.get("siteStart");
                    String siteEnd = (String) bodyParam.get("siteEnd");
                    String directionCode = (String) bodyParam.get("directionCode");
                    String directionString = (String) bodyParam.get("directionString");
                    String code = (String) bodyParam.get("code");

                    Distance distance = new Distance();

                    distance.setName(name);
                    distance.setSiteStart(siteStart);
                    distance.setSiteEnd(siteEnd);
                    distance.setDirectionCode(directionCode);
                    distance.setDirectionString(directionString);
                    distance.setCreatedBy(dto.getUuid());
                    distance.setCreatedDate(new Date());
                    distance.setCode(code);

                    response = new ResponseMessage(new MessageContent(distanceService.saveDistance(distance)));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền thêm mới quãng đường", null));
            }
        }
        return response;
    }

    public ResponseMessage updateDistance(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "PUT", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (bodyParam == null || bodyParam.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    String name = (String) bodyParam.get("name");
                    String siteStart = (String) bodyParam.get("siteStart");
                    String siteEnd = (String) bodyParam.get("siteEnd");
                    String directionCode = (String) bodyParam.get("directionCode");
                    String directionString = (String) bodyParam.get("directionString");
                    String code = (String) bodyParam.get("code");

                    Distance distance = new Distance();

                    distance.setId(pathParam);
                    distance.setName(name);
                    distance.setSiteStart(siteStart);
                    distance.setSiteEnd(siteEnd);
                    distance.setDirectionCode(directionCode);
                    distance.setDirectionString(directionString);
                    distance.setCreatedBy(dto.getUuid());
                    distance.setCreatedDate(new Date());
                    distance.setCode(code);

                    Response distanceDetailDTO = distanceService.getDistanceById(pathParam);
                    if (distanceDetailDTO != null) {
                        response = new ResponseMessage(new MessageContent(distanceService.updateDistance(distance)));
                    } else {
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy distan với id:" + pathParam, null));
                    }

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền sủa quãng đường", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteDistance(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response distanceDetailDTO = distanceService.getDistanceById(pathParam);
                if (distanceDetailDTO == null) {
                    return new ResponseMessage(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.toString(), null);
                }
                response = new ResponseMessage(new MessageContent(distanceService.deleteDistance(pathParam)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa quãng đường", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteMultiDistance(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
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
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                response = new ResponseMessage(new MessageContent(distanceService.deleteMultiDistance(listUuid)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa quãng đường", null));
            }
        }
        return response;
    }

    public ResponseMessage filterDistance(Map<String, String> headerParam, String requestPath,
                                          String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    response = new ResponseMessage(new MessageContent(distanceService.getAllDistance()));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                    Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                    String id = params.get("id");
                    String siteStart = params.get("siteStart");
                    String siteEnd = params.get("siteEnd");
                    String name = params.get("name");
                    if (page != null && size != null || id != null || name != null || siteStart != null || siteEnd != null) {
                        Response distancePaginationDTO = distanceService.filterDistance(page, size, id, name, siteStart, siteEnd);
                        if (distancePaginationDTO != null && distancePaginationDTO.getStatus() == HttpStatus.OK.value()) {
                            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), distancePaginationDTO.getData(), distancePaginationDTO.getTotal()));
                        }
                    } else {
                        return new ResponseMessage(new MessageContent(distanceService.getAllDistance()));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem", null));
            }

        }
        return response;
    }
}
