package com.elcom.its.vmsboard.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.elcom.its.vmsboard.constant.Constant;
import com.elcom.its.vmsboard.dto.ABACResponseDTO;
import com.elcom.its.vmsboard.dto.AuthorizationResponseDTO;
import com.elcom.its.vmsboard.dto.Response;
import com.elcom.its.vmsboard.model.DisplayDetailDTO;
import com.elcom.its.vmsboard.model.DisplayScript;
import com.elcom.its.vmsboard.model.ScriptMaxPriority;
import com.elcom.its.vmsboard.service.DisplayScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class DisplayScriptController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayScriptController.class);
    @Autowired
    private DisplayScriptService displayScriptService;

    public ResponseMessage getAllDisplayScript(Map<String, String> headerParam, String requestPath, String method, String paramPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        Response displayScriptDetailDTO = displayScriptService.getAllDisplayScript(paramPath);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage getDetailScript(Map<String, String> headerParam, String requestPath, String method, String paramPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        Response displayScriptDetailDTO = displayScriptService.getDetailScript(paramPath);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage deleteDetailScript(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        Response displayScriptDetailDTO = displayScriptService.deleteDetailScript(pathParam);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage updateDetailScript(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
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
                    Object content = (Object) bodyParam.get("content");
                    Object source = (Object) bodyParam.get("source");
                    String preview = (String) bodyParam.get("preview");
                    Integer priority = null;

                    if (bodyParam.get("priority") != null) {
                        priority = Integer.parseInt(bodyParam.get("priority").toString());
                    }

                    DisplayDetailDTO displayScript = new DisplayDetailDTO();
                    displayScript.setName(name);
                    displayScript.setPriority(priority);
                    displayScript.setContent(content);
                    displayScript.setSource(source);
                    displayScript.setPreview(preview);
                    response = new ResponseMessage(new MessageContent(displayScriptService.updateDetailScript(displayScript, pathParam)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền sửa", null));
            }
        }
        return response;
    }

    public ResponseMessage getDisplayScriptPlan(Map<String, String> headerParam, String requestPath, String method, String paramPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        Response displayScriptDetailDTO = displayScriptService.getDisplayScriptPlan(paramPath);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage getDisplayScriptByParentId(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        Response displayScriptDetailDTO = displayScriptService.getDisplayScriptByParentId(paramPath);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage getDisplayScriptByBoardId(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        Response displayScriptDetailDTO = displayScriptService.getDisplayScriptByBoardId(paramPath);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage getTimeByBoardId(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        Response displayScriptDetailDTO = displayScriptService.getTimeByBoardId(paramPath);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage getTimeByBaseIdAndBoardId(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
        String baseId = params.get("baseId");
        String boardId = params.get("boardId");
        Response displayScriptDetailDTO = displayScriptService.getTimeByBaseAndBoardId(baseId,boardId);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage getDisplayScriptDefault(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        Response displayScriptDetailDTO = displayScriptService.getDisplayScriptDefault(paramPath);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage createDisplayScript(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) throws ExecutionException, InterruptedException {
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
                    String newsletterId = (String) bodyParam.get("newsletterId");
                    String startTime = (String) bodyParam.get("startTime");
                    String endTime = (String) bodyParam.get("endTime");
                    Object content = (Object) bodyParam.get("content");
                    Object source = (Object) bodyParam.get("source");
                    String preview = (String) bodyParam.get("preview");
                    String scripBaseId = (String) bodyParam.get("scripBaseId");
                    Boolean isDefaultDisplay = (Boolean) bodyParam.get("isDefaultDisplay");

                    Integer status = null;
                    Integer priority = null;
                    Integer repeat = null;
                    Date date = null;
                    if (bodyParam.get("status") != null) {
                        status = Integer.parseInt(bodyParam.get("status").toString());
                    }
                    if (bodyParam.get("priority") != null) {
                        priority = Integer.parseInt(bodyParam.get("priority").toString());
                    }
                    if (bodyParam.get("repeat") != null) {
                        repeat = Integer.parseInt(bodyParam.get("repeat").toString());
                    }
                    if (bodyParam.get("date") != null) {
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            date = format.parse(bodyParam.get("date").toString());
                        }catch (Exception e){}
                    }
                    DisplayScript displayScript = new DisplayScript();
                    displayScript.setName(name);
                    displayScript.setBoardId(boardId);
                    displayScript.setNewsletterId(newsletterId);
                    displayScript.setStartTime(startTime);
                    displayScript.setEndTime(endTime);
                    displayScript.setContent(content);
                    displayScript.setPreview(preview);
                    displayScript.setSource(source);
                    displayScript.setStatus(status);
                    displayScript.setIsDefaultDisplay(isDefaultDisplay);
                    displayScript.setPriority(priority);
                    displayScript.setRepeat(repeat);
                    displayScript.setDate(date);
                    displayScript.setScripBaseId(scripBaseId);
                    displayScript.setCreatedBy(dto.getUuid());


                    response = new ResponseMessage(new MessageContent(displayScriptService.saveDisplayScript(displayScript)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền Sửa danh mục", null));
            }
        }
        return response;
    }

    public ResponseMessage updateDisplayScript(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
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
                    String newsletterId = (String) bodyParam.get("newsletterId");
                    String startTime = (String) bodyParam.get("startTime");
                    String endTime = (String) bodyParam.get("endTime");
                    Object content = (Object) bodyParam.get("content");
                    Object source = (Object) bodyParam.get("source");
                    String preview = (String) bodyParam.get("preview");
                    String scripBaseId = (String) bodyParam.get("scripBaseId");
                    Boolean isDefaultDisplay = (Boolean) bodyParam.get("isDefaultDisplay");

                    Integer status = null;
                    Integer priority = null;
                    Integer repeat = null;
                    Date date = null;
                    if (bodyParam.get("status") != null) {
                        status = Integer.parseInt(bodyParam.get("status").toString());
                    }
                    if (bodyParam.get("priority") != null) {
                        priority = Integer.parseInt(bodyParam.get("priority").toString());
                    }
                    if (bodyParam.get("repeat") != null) {
                        repeat = Integer.parseInt(bodyParam.get("repeat").toString());
                    }
                    if (bodyParam.get("date") != null) {
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            date = format.parse(bodyParam.get("date").toString());
                        }catch (Exception e){}
                    }
                    DisplayScript displayScript = new DisplayScript();
                    displayScript.setName(name);
                    displayScript.setBoardId(boardId);
                    displayScript.setNewsletterId(newsletterId);
                    displayScript.setStartTime(startTime);
                    displayScript.setEndTime(endTime);
                    displayScript.setContent(content);
                    displayScript.setPreview(preview);
                    displayScript.setSource(source);
                    displayScript.setStatus(status);
                    displayScript.setIsDefaultDisplay(isDefaultDisplay);
                    displayScript.setPriority(priority);
                    displayScript.setRepeat(repeat);
                    displayScript.setDate(date);
                    displayScript.setScripBaseId(scripBaseId);
                    displayScript.setCreatedBy(dto.getUuid());
                    response = new ResponseMessage(new MessageContent(displayScriptService.updateDisplayScript(displayScript, pathParam)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền sửa", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteDisplayScript(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response displayScriptDetailDTO = displayScriptService.getDisplayScriptByParentId(pathParam);
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

    public ResponseMessage getScriptRunningInBoard(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        Response displayScriptDetailDTO = displayScriptService.getScriptRunningInBoard(paramPath);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage getNewsLetterRunningInBoard(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        Response displayScriptDetailDTO = displayScriptService.getNewsLetterRunningInBoard(paramPath);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }
    public ResponseMessage getNewsLetterByEventType(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
        String eventType = params.get("eventType");
        Response displayScriptDetailDTO = displayScriptService.getNewsLetterByEventType(eventType);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }


    public ResponseMessage createScriptMaxPriority(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) throws ExecutionException, InterruptedException {
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
                    String newsletterId = (String) bodyParam.get("newsletterId");
                    String endTime = (String) bodyParam.get("endTime");
                    Object content = (Object) bodyParam.get("content");
                    Object source = (Object) bodyParam.get("source");
                    String preview = (String) bodyParam.get("preview");
                    String createdBy = (String) bodyParam.get("createdBy");
                    Date date = null;
                    if (bodyParam.get("date") != null) {
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            date = format.parse(bodyParam.get("date").toString());
                        }catch (Exception e){}
                    }
                    ScriptMaxPriority displayScript = new ScriptMaxPriority();
                    displayScript.setName(name);
                    displayScript.setNewsLetterId(newsletterId);
                    displayScript.setBoardId(boardId);
                    displayScript.setEndTime(endTime);
                    displayScript.setContent(content);
                    displayScript.setSource(source);
                    displayScript.setPreview(preview);
                    displayScript.setDate(date);
                    if(!StringUtil.isNullOrEmpty(createdBy)){
                        displayScript.setCreatedBy(createdBy);
                    }else{
                        displayScript.setCreatedBy(dto.getUuid());
                    }

                    response = new ResponseMessage(new MessageContent(displayScriptService.saveScriptMaxPriority(displayScript)));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền Sửa danh mục", null));
            }
        }
        return response;
    }

    public ResponseMessage createScriptTopPriority(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) throws ExecutionException, InterruptedException {
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
                    String newsletterId = (String) bodyParam.get("newsletterId");
                    String createdBy = (String) bodyParam.get("createdBy");
                    String endTime = (String) bodyParam.get("endTime");
                    Object content = (Object) bodyParam.get("content");
                    Object source = (Object) bodyParam.get("source");
                    String preview = (String) bodyParam.get("preview");
                    Date date = null;
                    if (bodyParam.get("date") != null) {
                        try {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            date = format.parse(bodyParam.get("date").toString());
                        }catch (Exception e){}
                    }
                    ScriptMaxPriority displayScript = new ScriptMaxPriority();
                    displayScript.setName(name);
                    displayScript.setBoardId(boardId);
                    displayScript.setNewsLetterId(newsletterId);
                    displayScript.setEndTime(endTime);
                    displayScript.setContent(content);
                    displayScript.setSource(source);
                    displayScript.setPreview(preview);
                    displayScript.setDate(date);
                    if(!StringUtil.isNullOrEmpty(createdBy)){
                        displayScript.setCreatedBy(createdBy);
                    }else{
                        displayScript.setCreatedBy(dto.getUuid());
                    }
                    response = new ResponseMessage(new MessageContent(displayScriptService.saveScriptMaxPriority(displayScript)));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền Sửa danh mục", null));
            }
        }
        return response;
    }
}
