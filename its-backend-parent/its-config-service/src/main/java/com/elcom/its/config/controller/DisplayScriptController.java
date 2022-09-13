package com.elcom.its.config.controller;

import com.elcom.its.config.constant.Constant;
import com.elcom.its.config.model.DisplayScript;
import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.DisplayScriptService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class DisplayScriptController extends BaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);
    @Autowired
    private DisplayScriptService displayScriptService;

    public ResponseMessage getAllDisplayScript(Map<String, String> headerParam, String requestPath,
                                               String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                if (StringUtil.isNullOrEmpty(urlParam)) {
                    response = new ResponseMessage(new MessageContent(displayScriptService.getDisplayScript()));
                } else {
                    Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                    Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                    Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                    String search = params.get("search");
                    if (page != null && size != null) {
                        Response displayScriptPaginationDTO = displayScriptService.getAllDisplayScript(page, size, search);
                        if (displayScriptPaginationDTO != null && displayScriptPaginationDTO.getStatus() == HttpStatus.OK.value()) {
                            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), displayScriptPaginationDTO.getData(), displayScriptPaginationDTO.getTotal()));
                        }
                    } else {
                        return new ResponseMessage(new MessageContent(displayScriptService.getDisplayScript()));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền", null));
            }

        }
        return response;
    }

    public ResponseMessage getDisplayScriptPlan(Map<String, String> headerParam, String requestPath,
                                               String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                    response = new ResponseMessage(new MessageContent(displayScriptService.getDisplayScriptPlan()));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền", null));
            }

        }
        return response;
    }

    public ResponseMessage getDisplayScriptById(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        Response displayScriptDetailDTO = displayScriptService.getDisplayScriptById(paramPath);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage createDisplayScript(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) {
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
                    String boardId = (String) bodyParam.get("boardId");
//                    String newsType = (String) bodyParam.get("newsType");
                    String newsletterId = (String) bodyParam.get("newsletterId");
                    String startTime = (String) bodyParam.get("startTime");
                    String endTime = (String) bodyParam.get("endTime");
//                    String origin = (String) bodyParam.get("origin");
//                    String now = (String) bodyParam.get("now");

                    Integer priority = null;
                    Integer repeat = null;
//                    Integer status = null;
                    if (bodyParam.get("priority") != null) {
                        priority = Integer.parseInt(bodyParam.get("priority").toString());
                    }
                    if (bodyParam.get("repeat") != null) {
                        repeat = Integer.parseInt(bodyParam.get("repeat").toString());
                    }
//                    if (bodyParam.get("status") != null) {
//                        status = Integer.parseInt(bodyParam.get("status").toString());
//                    }

                    DisplayScript displayScript = new DisplayScript();

                    displayScript.setName(name);
                    displayScript.setBoardId(boardId);
//                    displayScript.setNewsType(newsType);
                    displayScript.setNewsletterId(newsletterId);
                    displayScript.setStartTime(startTime);
                    displayScript.setEndTime(endTime);
//                    displayScript.setOrigin(origin);
//                    displayScript.setNow(now);
                    displayScript.setPriority(priority);
                    displayScript.setRepeat(repeat);
//                    displayScript.setStatus(status);


                    response = new ResponseMessage(new MessageContent(displayScriptService.saveDisplayScript(displayScript)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền Sửa danh mục", null));
            }
        }
        return response;
    }

    public ResponseMessage updateDisplayScript(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
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
                    String boardId = (String) bodyParam.get("boardId");
//                    String newsType = (String) bodyParam.get("newsType");
                    String newsletterId = (String) bodyParam.get("newsletterId");
                    String startTime = (String) bodyParam.get("startTime");
                    String endTime = (String) bodyParam.get("endTime");
//                    String origin = (String) bodyParam.get("origin");
//                    String now = (String) bodyParam.get("now");

                    Integer priority = null;
                    Integer repeat = null;
//                    Integer status = null;
                    if (bodyParam.get("priority") != null) {
                        priority = Integer.parseInt(bodyParam.get("priority").toString());
                    }
                    if (bodyParam.get("repeat") != null) {
                        repeat = Integer.parseInt(bodyParam.get("repeat").toString());
                    }
//                    if (bodyParam.get("status") != null) {
//                        status = Integer.parseInt(bodyParam.get("status").toString());
//                    }

                    DisplayScript displayScript = new DisplayScript();

                    displayScript.setId(pathParam);
                    displayScript.setName(name);
                    displayScript.setBoardId(boardId);
//                    displayScript.setNewsType(newsType);
                    displayScript.setNewsletterId(newsletterId);
                    displayScript.setStartTime(startTime);
//                    displayScript.setOrigin(origin);
//                    displayScript.setNow(now);
                    displayScript.setPriority(priority);
                    displayScript.setRepeat(repeat);
//                    displayScript.setStatus(status);
                    displayScript.setEndTime(endTime);

                    Response routeDetailDTO = displayScriptService.getDisplayScriptById(pathParam);

                    if (routeDetailDTO != null) {
                        response = new ResponseMessage(new MessageContent(displayScriptService.updateDisplayScript(displayScript)));
                    } else {
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy route tương ứng với id: " + pathParam, null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền sửa", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteDisplayScript(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response displayScriptDetailDTO = displayScriptService.getDisplayScriptById(pathParam);
                if (displayScriptDetailDTO == null) {
                    return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy id tương ứng: " + pathParam, null);
                } else {
                    response = new ResponseMessage(new MessageContent(displayScriptService.deleteDisplayScript(pathParam)));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa", null));
            }
        }
        return response;
    }
}
