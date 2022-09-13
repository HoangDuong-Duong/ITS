/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vmsboard.controller;

import com.elcom.its.vmsboard.constant.Constant;
import com.elcom.its.vmsboard.dto.AuthorizationResponseDTO;
import com.elcom.its.vmsboard.dto.ABACResponseDTO;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.elcom.its.vmsboard.dto.Response;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.elcom.its.vmsboard.model.ListUuid;
import com.elcom.its.vmsboard.model.VmsBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import com.elcom.its.vmsboard.service.VmsBoardService;

/**
 *
 * @author Admin
 */
@Controller
public class VmsBoardController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VmsBoardController.class);
    @Autowired
    private VmsBoardService vmsBoardService;

    public ResponseMessage getAllVmsBoard(Map<String, String> headerParam, String requestPath,
            String method, String urlParam, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String name = params.get("name");
                String siteId = params.get("siteId");
                String typeBoard = params.get("typeBoard");
                boolean isAdmin;
                if(abacResponseDTO.getAdmin() == null){
                    isAdmin = false;
                }else{
                    isAdmin = abacResponseDTO.getAdmin();
                }
                String stageCodes = dto.getUnit().getLisOfStage();
                Integer status = params.get("status") != null ? Integer.parseInt(params.get("status")) : null;
                Response scriptBase = vmsBoardService.getAllVmsBoard(name, siteId, typeBoard, status,isAdmin, stageCodes);
                if (scriptBase != null && scriptBase.getStatus() == HttpStatus.OK.value()) {
                    return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), scriptBase.getData(), scriptBase.getTotal()));
                }

            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách biển báo", null));
            }

        }
        return response;
    }

    public ResponseMessage getDisplayScript(Map<String, String> headerParam, String requestPath,
            String method, String urlParam, Map<String, Object> bodyParam) throws UnsupportedEncodingException, ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                Integer page = params.get("page") != null ? Integer.parseInt(params.get("page")) : null;
                Integer size = params.get("size") != null ? Integer.parseInt(params.get("size")) : null;
                String deviceId = params.get("deviceId");
                String keyword = params.get("keyword");
                String startDate = params.get("fromDate");
                String toDate = params.get("toDate");

                Response vmsPaginationDTO = vmsBoardService.getDisplayScript(page, size, keyword, deviceId, startDate, toDate);
                if (vmsPaginationDTO != null && vmsPaginationDTO.getStatus() == HttpStatus.OK.value()) {
                    return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), vmsPaginationDTO.getData(), vmsPaginationDTO.getTotal()));
                }

            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem lịch sử hiển thị biển báo", null));
            }

        }
        return response;
    }

    public ResponseMessage getVmsBoardById(Map<String, String> headerParam, String requestPath, String method, String paramPath) {
        ResponseMessage response = null;
        Response routeDetailDTO = vmsBoardService.getVmsBoardById(paramPath);
        if (routeDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), routeDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(routeDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage getVmsBoardByDirection(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
        String direction = params.get("direction");
        Response displayScriptDetailDTO = vmsBoardService.getVmsBoardByDirection(direction);
        if (displayScriptDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), displayScriptDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(displayScriptDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage getHistoryByVms(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
        String startTime = params.get("startTime");
        String endTime = params.get("endTime");
        String vmsId = params.get("vmsId");
        Response routeDetailDTO = vmsBoardService.getHistoryDisplayScript(startTime, endTime, vmsId);
        if (routeDetailDTO.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), routeDetailDTO.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(routeDetailDTO.getData()));
        }
        return response;
    }

    public ResponseMessage getCurrentDisplay(Map<String, String> headerParam, String requestPath, String method, String urlParam) {
        ResponseMessage response = null;
        Response coreResponse = vmsBoardService.getCurrentDisplay(urlParam);
        response = new ResponseMessage(new MessageContent(coreResponse.getData()));
        return response;
    }

    public ResponseMessage createVmsBoard(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) throws ExecutionException, InterruptedException {
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
                    String site = (String) bodyParam.get("site");
                    String positionM = (String) bodyParam.get("positionM");
                    String typeBoard = (String) bodyParam.get("typeBoard");
                    String newsType = (String) bodyParam.get("newsType");
                    String scriptId = (String) bodyParam.get("scriptId");
                    String model = (String) bodyParam.get("model");
                    String language = (String) bodyParam.get("language");
                    String ip = (String) bodyParam.get("ip");
                    String realId = (String) bodyParam.get("realId");
                    String direction = (String) bodyParam.get("direction");
                    String manufacturer = (String) bodyParam.get("manufacturer");
                    String content = (String) bodyParam.get("content");
                    String timeOn = (String) bodyParam.get("onTime");
                    String timeOff = (String) bodyParam.get("offTime");
                    Float longitude = null;
                    Float latitude = null;
                    Float length = null;
                    Float width = null;

                    if (bodyParam.get("longitude") != null) {
                        longitude = Float.parseFloat(bodyParam.get("longitude").toString());
                    }
                    if (bodyParam.get("latitude") != null) {
                        latitude = Float.parseFloat(bodyParam.get("latitude").toString());
                    }
                    if (bodyParam.get("length") != null) {
                        length = Float.parseFloat(bodyParam.get("length").toString());
                    }
                    if (bodyParam.get("width") != null) {
                        width = Float.parseFloat(bodyParam.get("width").toString());
                    }

                    VmsBoard vmsBoard = new VmsBoard();

                    vmsBoard.setName(name);
                    vmsBoard.setSite(site);
                    vmsBoard.setPositionM(positionM);
                    vmsBoard.setTypeBoard(typeBoard);
                    vmsBoard.setModel(model);
                    vmsBoard.setLanguage(language);
                    vmsBoard.setLatitude(latitude);
                    vmsBoard.setLongitude(longitude);
                    vmsBoard.setLength(length);
                    vmsBoard.setWidth(width);
                    vmsBoard.setDirection(direction);
                    vmsBoard.setIp(ip);
                    vmsBoard.setManufacturer(manufacturer);
                    vmsBoard.setContent(content);
                    vmsBoard.setOffTime(timeOff);
                    vmsBoard.setOnTime(timeOn);
                    vmsBoard.setScriptId(scriptId);
                    vmsBoard.setNewsType(newsType);
                    vmsBoard.setRealId(realId);
                    vmsBoard.setInstaller(dto.getUuid());

                    response = new ResponseMessage(new MessageContent(vmsBoardService.saveVmsBoard(vmsBoard)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền thêm mới thiết bị biển báo", null));
            }
        }
        return response;
    }

    public ResponseMessage updateVmsBoard(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
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
                    String site = (String) bodyParam.get("site");
                    String positionM = (String) bodyParam.get("positionM");
                    String typeBoard = (String) bodyParam.get("typeBoard");
                    String model = (String) bodyParam.get("model");
                    String language = (String) bodyParam.get("language");
                    String ip = (String) bodyParam.get("ip");
                    String direction = (String) bodyParam.get("direction");
                    String manufacturer = (String) bodyParam.get("manufacturer");
                    String content = (String) bodyParam.get("content");
                    String timeOn = (String) bodyParam.get("onTime");
                    String timeOff = (String) bodyParam.get("offTime");
                    String newsType = (String) bodyParam.get("newsType");
                    String scriptId = (String) bodyParam.get("scriptId");
                    String realId = (String) bodyParam.get("realId");

                    Float longitude = null;
                    Float latitude = null;
                    Float length = null;
                    Float width = null;

                    if (bodyParam.get("longitude") != null) {
                        longitude = Float.parseFloat(bodyParam.get("longitude").toString());
                    }
                    if (bodyParam.get("latitude") != null) {
                        latitude = Float.parseFloat(bodyParam.get("latitude").toString());
                    }
                    if (bodyParam.get("length") != null) {
                        length = Float.parseFloat(bodyParam.get("length").toString());
                    }
                    if (bodyParam.get("width") != null) {
                        width = Float.parseFloat(bodyParam.get("width").toString());
                    }

                    VmsBoard vmsBoard = new VmsBoard();

                    vmsBoard.setId(pathParam);
                    vmsBoard.setName(name);
                    vmsBoard.setSite(site);
                    vmsBoard.setPositionM(positionM);
                    vmsBoard.setTypeBoard(typeBoard);
                    vmsBoard.setModel(model);
                    vmsBoard.setLanguage(language);
                    vmsBoard.setLatitude(latitude);
                    vmsBoard.setLongitude(longitude);
                    vmsBoard.setLength(length);
                    vmsBoard.setWidth(width);
                    vmsBoard.setDirection(direction);
                    vmsBoard.setIp(ip);
                    vmsBoard.setManufacturer(manufacturer);
                    vmsBoard.setContent(content);
                    vmsBoard.setOffTime(timeOff);
                    vmsBoard.setOnTime(timeOn);
                    vmsBoard.setScriptId(scriptId);
                    vmsBoard.setNewsType(newsType);
                    vmsBoard.setRealId(realId);
                    vmsBoard.setInstaller(dto.getUuid());

                    Response vmsBoardDto = vmsBoardService.getVmsBoardById(pathParam);
                    if (vmsBoardDto != null) {
                        response = new ResponseMessage(new MessageContent(vmsBoardService.updateVmsBoard(vmsBoard)));
                    } else {
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy vmsBoard tương ứng với id: " + pathParam, null));
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền chỉnh sửa thiết bị", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteVmsBoard(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Response vmsDetailDTO = vmsBoardService.getVmsBoardById(pathParam);
                if (vmsDetailDTO == null) {
                    return new ResponseMessage(HttpStatus.NOT_FOUND.value(), "Không tìm thấy id tương ứng: " + pathParam, null);
                } else {
                    response = new ResponseMessage(new MessageContent(vmsBoardService.deleteVmsBoard(pathParam)));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa thiết bị", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteMultiVmsBoard(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
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
                response = new ResponseMessage(new MessageContent(vmsBoardService.multiDelete(listUuid)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa thiết bị", null));
            }
        }
        return response;
    }

    public ResponseMessage getAllVmsByGroup(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;

        String stageCode = (String) bodyParam.get("ids");
        List<String> uuid = Arrays.asList(stageCode.split(","));
        ListUuid listUuid = new ListUuid();
        listUuid.setIds(uuid);

        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                response = new ResponseMessage(new MessageContent(vmsBoardService.getAllVmsByGroup(listUuid)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa thiết bị", null));
            }
        }
        return response;
    }
}
