package com.elcom.its.vmsboard.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.elcom.its.vmsboard.constant.Constant;
import com.elcom.its.vmsboard.dto.ABACResponseDTO;
import com.elcom.its.vmsboard.dto.AuthorizationResponseDTO;
import com.elcom.its.vmsboard.dto.Response;
import com.elcom.its.vmsboard.model.ListUuid;
import com.elcom.its.vmsboard.model.ScriptBase;
import com.elcom.its.vmsboard.service.ScriptBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class ScriptBaseController extends BaseController {
    @Autowired
    private ScriptBaseService scriptBaseService;

    public ResponseMessage getAllScriptBase(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String startDate = params.get("startDate");
                String endDate = params.get("endDate");
                String name = params.get("name");
                String vmsIds = params.get("vmsIds");
                Integer status = params.get("status") != null ? Integer.parseInt(params.get("status")) : null;
                Response scriptBase = scriptBaseService.getAllScriptBase(startDate, endDate, name, status, vmsIds);
                if (scriptBase != null && scriptBase.getStatus() == HttpStatus.OK.value()) {
                    return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), scriptBase.getData(), scriptBase.getTotal()));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách kịch bản", null));
            }

        }
        return response;
    }

    public ResponseMessage activeScripBase(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String id = params.get("id");
                Integer status = Integer.parseInt(params.get("status").toString());
                Response scriptBase = scriptBaseService.activeScript(id, status);
                if (scriptBase != null && scriptBase.getStatus() == HttpStatus.OK.value()) {
                    return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), scriptBase.getData(), scriptBase.getTotal()));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền kích hoạt kịch bản", null));
            }

        }
        return response;
    }

    public ResponseMessage getScriptBaseById(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        Response routeDetailDTO = scriptBaseService.getDetailScriptBase(paramPath);
        if (routeDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), routeDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(routeDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage createScriptBase(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) throws ExecutionException, InterruptedException {
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
                    String startDate = (String) bodyParam.get("startDate");
                    String endDate = (String) bodyParam.get("endDate");
                    String vmsBoard = (String) bodyParam.get("vmsBoard");

                    ScriptBase scriptBase = new ScriptBase();
                    scriptBase.setName(name);
                    scriptBase.setStartDate(startDate);
                    scriptBase.setEndDate(endDate);
                    scriptBase.setVmsBoard(vmsBoard);
                    response = new ResponseMessage(new MessageContent(scriptBaseService.save(scriptBase)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền tạo mới mới kịch bản", null));
            }
        }
        return response;
    }

    public ResponseMessage updateScriptBase(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
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
                    String startDate = (String) bodyParam.get("startDate");
                    String endDate = (String) bodyParam.get("endDate");
                    String vmsBoard = (String) bodyParam.get("vmsBoard");
                    Integer status = (Integer) bodyParam.get("status");

                    ScriptBase scriptBase = new ScriptBase();
                    scriptBase.setId(pathParam);
                    scriptBase.setName(name);
                    scriptBase.setStartDate(startDate);
                    scriptBase.setEndDate(endDate);
                    scriptBase.setStatus(status);
                    scriptBase.setVmsBoard(vmsBoard);

                    Response vmsBoardDto = scriptBaseService.getDetailScriptBase(pathParam);
                    if (vmsBoardDto != null) {
                        response = new ResponseMessage(new MessageContent(scriptBaseService.update(scriptBase)));
                    } else {
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy kịch bản cần sửa", null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền chỉnh sửa kịch bản", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteScriptBase(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response vmsDetailDTO = scriptBaseService.getDetailScriptBase(pathParam);
                if (vmsDetailDTO == null) {
                    return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy kịch bản cần xóa", null);
                } else {
                    response = new ResponseMessage(new MessageContent(scriptBaseService.delete(pathParam)));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa kịch bản", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteMultiScriptBase(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
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
                response = new ResponseMessage(new MessageContent(scriptBaseService.multiDelete(listUuid)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa kịch bản", null));
            }
        }
        return response;
    }
}
