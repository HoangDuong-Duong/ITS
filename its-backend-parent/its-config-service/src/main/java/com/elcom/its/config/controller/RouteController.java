package com.elcom.its.config.controller;

import com.elcom.its.config.constant.Constant;
import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.Route;
import com.elcom.its.config.model.dto.*;
import com.elcom.its.config.service.RouteService;
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
public class RouteController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);
    @Autowired
    private RouteService routeService;

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

    public ResponseMessage getAllRoute(Map<String, String> headerParam, String requestPath,
                                       String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    response = new ResponseMessage(new MessageContent(routeService.getAllRoute()));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValuesNoDecode(urlParam);
                    Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                    Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                    String search = params.get("search");
                    if (page != null && size != null) {
                        Response routePaginationDTO = routeService.getAllRoute(page, size, search);
                        if (routePaginationDTO != null && routePaginationDTO.getStatus() == HttpStatus.OK.value()) {
                            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), routePaginationDTO.getData(), routePaginationDTO.getTotal()));
                        }
                    } else {
                        return new ResponseMessage(new MessageContent(routeService.getAllRoute()));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách tuyến đường", null));
            }

        }
        return response;
    }

    public ResponseMessage getRouteById(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response routeDetailDTO = routeService.getRouteById(paramPath);
                if (routeDetailDTO.getData() == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), routeDetailDTO.getMessage(), null));
                } else {
                    response = new ResponseMessage(new MessageContent(routeDetailDTO.getData()));
                }
            }else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem chi tiết tuyến đường", null));
            }
        }
        return response;
    }

    public ResponseMessage createRoute(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) {
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
                    String line = (String) bodyParam.get("line");
                    String code = (String) bodyParam.get("code");
                    Long numberLanes = null;
                    Long emergencyStop = null;
                    if (bodyParam.get("numberLanes") != null) {
                        numberLanes = Long.parseLong(bodyParam.get("numberLanes").toString());
                    }
                    if (bodyParam.get("numberLanes") != null) {
                        emergencyStop = Long.parseLong(bodyParam.get("emergencyStop").toString());
                    }

                    Route route = new Route();

                    route.setName(name);
                    route.setSiteStart(siteStart);
                    route.setSiteEnd(siteEnd);
                    route.setEmergencyStop(emergencyStop);
                    route.setNumberLanes(numberLanes);
                    route.setLine(line);
                    route.setCreatedBy(dto.getUuid());
                    route.setCreateDate(new Date());
                    route.setCode(code);

                    response = new ResponseMessage(new MessageContent(routeService.saveRoute(route)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền thêm mới tuyến đường", null));
            }
        }
        return response;
    }

    public ResponseMessage updateRoute(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
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
                    String line = (String) bodyParam.get("line");
                    String code = (String) bodyParam.get("code");
                    Long numberLanes = null;
                    Long emergencyStop = null;
                    if (bodyParam.get("numberLanes") != null) {
                        numberLanes = Long.parseLong(bodyParam.get("numberLanes").toString());
                    }
                    if (bodyParam.get("numberLanes") != null) {
                        emergencyStop = Long.parseLong(bodyParam.get("emergencyStop").toString());
                    }

                    Route route = new Route();

                    route.setId(pathParam);
                    route.setName(name);
                    route.setSiteStart(siteStart);
                    route.setSiteEnd(siteEnd);
                    route.setEmergencyStop(emergencyStop);
                    route.setNumberLanes(numberLanes);
                    route.setLine(line);
                    route.setCreatedBy(dto.getUuid());
                    route.setCreateDate(new Date());
                    route.setCode(code);

                    Response routeDetailDTO = routeService.getRouteById(pathParam);
                    if (routeDetailDTO != null) {
                        response = new ResponseMessage(new MessageContent(routeService.updateRoute(route)));
                    } else {
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy route tương ứng với id: " + pathParam, null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền chỉnh sửa tuyến đường", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteRoute(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response routeDetailDTO = routeService.getRouteById(pathParam);
                if (routeDetailDTO == null) {
                    return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy id tương ứng: "+ pathParam, null);
                }else{
                    response = new ResponseMessage(new MessageContent(routeService.deleteRoute(pathParam)));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa tuyến đường", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteMultiRoute(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
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
                response = new ResponseMessage(new MessageContent(routeService.deleteMultiRoute(listUuid)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa tuyến đường", null));
            }
        }
        return response;
    }
}
