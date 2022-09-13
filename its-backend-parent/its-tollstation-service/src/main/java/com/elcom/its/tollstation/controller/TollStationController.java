package com.elcom.its.tollstation.controller;

import com.elcom.its.tollstation.constant.Constant;
import com.elcom.its.tollstation.dto.*;
import com.elcom.its.tollstation.service.TollStationService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class TollStationController extends BaseController {
    @Autowired
    private TollStationService tollStationService;

    public ResponseMessage getListBOT(Map<String, String> headerParam, String requestPath,  String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách trạm cân",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách trạm cân", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String startDate = params.get("startDate");
                String endDate = params.get("endDate");
                TollStationResponseDTO data ;
                if(resultCheckABAC.getAdmin()!=null &&resultCheckABAC.getAdmin()){
                    data = tollStationService.getTollStation(null,true);
                }else {
                    data = tollStationService.getTollStation(dto.getUnit().getLisOfStage(),false);
                }
                response = new ResponseMessage(new MessageContent(data.getData(), data.getTotal()));
            }
        }

        return response;
    }

    public ResponseMessage getListBOTByDirectionCode(Map<String, String> headerParam, String requestPath,  String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách trạm cân",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách trạm cân", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String directionCode = params.get("directionCode");
                List<TollStationDTO> data;
                if(resultCheckABAC.getAdmin()!=null &&resultCheckABAC.getAdmin()){
                    data = tollStationService.getTollStationByDirectionCode(directionCode,null, true);
                }else {
                    data = tollStationService.getTollStationByDirectionCode(directionCode, dto.getUnit().getLisOfStage(),false);
                }
                response = new ResponseMessage(new MessageContent(data));
            }
        }

        return response;
    }

    public ResponseMessage createBOT(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath) throws ExecutionException, InterruptedException {
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
                    String code = (String) bodyParam.get("code");
                    String siteId = (String) bodyParam.get("siteId");
                    String directionCode = (String) bodyParam.get("directionCode");
                    Integer numberLanes = Integer.parseInt(bodyParam.get("numberLanes").toString());
                    String managementUnit = (String) bodyParam.get("managementUnit");
                    BotDTO botDTO = new BotDTO();
                    botDTO.setName(name);
                    botDTO.setCode(code);
                    botDTO.setSiteId(siteId);
                    botDTO.setNumberLanes(numberLanes);
                    botDTO.setDirectionCode(directionCode);
                    botDTO.setManagementUnit(managementUnit);
                    response = new ResponseMessage(new MessageContent(tollStationService.saveBot(botDTO)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền thêm mới trạm thu phí", null));
            }
        }
        return response;
    }

    public ResponseMessage updateBOT(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String pathParam) throws ExecutionException, InterruptedException {
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
                    String code = (String) bodyParam.get("code");
                    String siteId = (String) bodyParam.get("siteId");
                    String directionCode = (String) bodyParam.get("directionCode");
                    Integer numberLanes = Integer.parseInt(bodyParam.get("numberLanes").toString());
                    String managementUnit = (String) bodyParam.get("managementUnit");
                    BotDTO botDTO = new BotDTO();
                    botDTO.setName(name);
                    botDTO.setCode(code);
                    botDTO.setSiteId(siteId);
                    botDTO.setNumberLanes(numberLanes);
                    botDTO.setDirectionCode(directionCode);
                    botDTO.setManagementUnit(managementUnit);
                    response = new ResponseMessage(new MessageContent(tollStationService.updateBot(botDTO, pathParam)));

                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền cập nhật trạm thu phí", null));
            }
        }
        return response;
    }

    public ResponseMessage deleteBOT(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String pathParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                response = new ResponseMessage(new MessageContent(tollStationService.deleteBot(pathParam)));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xóa trạm thu phí", null));
            }
        }
        return response;
    }
}
