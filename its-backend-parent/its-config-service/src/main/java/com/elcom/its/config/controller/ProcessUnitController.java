/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.controller;

import com.elcom.its.config.constant.Constant;
import com.elcom.its.config.exception.ResourceNotFoundException;
import com.elcom.its.config.model.ProcessUnit;
import com.elcom.its.config.model.Servers;
import com.elcom.its.config.model.dto.ABACResponseDTO;
import com.elcom.its.config.model.dto.AuthorizationResponseDTO;
import com.elcom.its.config.model.dto.ProcessUnitDTO;
import com.elcom.its.config.service.ProcessUnitService;
import com.elcom.its.config.service.ServerService;
import com.elcom.its.config.validation.ProcessUnitValidation;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Administrator
 */
@Controller
public class ProcessUnitController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessUnitController.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProcessUnitService processUnitService;

    @Autowired
    private ServerService serverService;

    public ResponseMessage getProcessUnitList(Map<String, String> headerParam, String requestPath,
                                              String method, String urlParam, Map<String, Object> bodyParam) throws UnsupportedEncodingException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Pageable pageable = null;
                String search = "";
                Integer puType = null;
                Page<ProcessUnit> processUnitPage = null;
                if (!StringUtil.isNullOrEmpty(urlParam)) {
                    Map<String, Object> query = StringUtil.getQueryMap(urlParam);
                    Integer page = query.get("page") != null ? Integer.parseInt((String) query.get("page")) : 0;
                    Integer size = query.get("size") != null ? Integer.parseInt((String) query.get("size")) : 10;
                    search = (String) query.get("search");
                    puType = query.get("puType") != null ? Integer.parseInt((String) query.get("puType")) : null;
                    pageable = PageRequest.of(page, size);
                }
                if (StringUtil.isNullOrEmpty(search) && puType == null) {
                    processUnitPage = processUnitService.getAllByPage(pageable);
                } else {
                    processUnitPage = processUnitService.findProcessUnit(puType, search != null ? URLDecoder.decode(search, StandardCharsets.UTF_8) : search, pageable);
                }
                if (CollectionUtils.isEmpty(processUnitPage.getContent())) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                            HttpStatus.OK.toString(), null));
                } else {
                    //Set server name
                    List<ProcessUnit> processUnitList = processUnitPage.getContent();
                    for (ProcessUnit entity : processUnitList) {
                        Servers servers = serverService.findById(entity.getServers());
                        if (servers != null) {
                            entity.setServerName(servers.getName());
                        }
                    }
                    //
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(),
                            HttpStatus.OK.toString(), processUnitList,
                            processUnitPage.getTotalElements()));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem danh sách khối xử lý", null));
            }
        }
        return response;
    }

    public ResponseMessage getProcessUnitById(Map<String, String> headerParam, String requestPath,
                                              String method, String pathParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, "DETAIL", dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                Optional<ProcessUnit> processUnit = processUnitService.findById(pathParam);
                if (processUnit.isPresent()) {
                    response = new ResponseMessage(new MessageContent(processUnit.get()));

                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(),
                            HttpStatus.NOT_FOUND.toString(), null));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền xem chi tiết khối xử lý", null));
            }
        }
        return response;
    }

    public ResponseMessage updateProcessUnit(String requestPath, Map<String, String> headerParam,
                                             Map<String, Object> bodyParam, String requestMethod, String pathParam) {
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
                    String id = pathParam;
                    String code = (String) bodyParam.get("code");
                    String name = (String) bodyParam.get("name");
                    String type = (String) bodyParam.get("type");
                    String note = (String) bodyParam.get("note");
                    String description = (String) bodyParam.get("description");
                    Map<String, Object> modelProfiles = (Map<String, Object>) bodyParam.get("modelProfiles");
                    Long serverId = bodyParam.get("serverId") != null ? Long.valueOf(bodyParam.get("serverId").toString()) : null;
                    Map<String, Object> appServices = (Map<String, Object>) bodyParam.get("appServices");
                    Map<String, Object> tvRenders = (Map<String, Object>) bodyParam.get("tvRenders");
                    Map<String, Object> eventNotifiers = (Map<String, Object>) bodyParam.get("eventNotifiers");
                    ProcessUnitDTO processUnitDto = new ProcessUnitDTO(code, name, type, note, description, modelProfiles, serverId, appServices, tvRenders, eventNotifiers, 1, 1, 0);
                    processUnitDto.setId(id);

                    String invalidData = new ProcessUnitValidation().validateInsertProcessUnit(processUnitDto);
                    if (invalidData != null) {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData,
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
                    } else {
                        ProcessUnit processUnitById = processUnitService.findById(id).orElseThrow(() -> new ResourceNotFoundException(ProcessUnit.class, id));
                        ProcessUnit processUnitByName = null;
                        try {
                            processUnitByName = processUnitService.findByName(processUnitDto.getName());
                        }catch (Exception e){
                            processUnitByName = null;
                        }
                        if (processUnitByName == null || (processUnitByName != null&&processUnitByName.getId().equalsIgnoreCase(id))) {
                            ProcessUnit entity = ProcessUnitDTO.toEntity(processUnitDto);
                            entity.setCreatedBy(processUnitById.getCreatedBy());
                            entity.setCreatedDate(processUnitById.getCreatedDate());
                            entity.setModifiedBy(dto.getUuid());
                            entity.setModifiedDate(new Date());
                            entity.setId(id);
                            entity.setPuType(processUnitById.getPuType());
                            try {
                                String jsonSpec = processUnitService.toJsonSpec(entity);
                                entity.setSpec(jsonSpec);
                                entity = processUnitService.update(entity);
                                //Server name
                                Servers servers = serverService.findById(serverId);
                                if (servers != null) {
                                    entity.setServerName(servers.getName());
                                }
                                //
                                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), entity));
                            } catch (Exception ex) {
                                response = new ResponseMessage(new MessageContent(HttpStatus.NOT_MODIFIED.value(),
                                        HttpStatus.NOT_MODIFIED.getReasonPhrase(), ex.toString()));
                                LOGGER.error("Error to update process unit >>> {}", ex.toString());
                                ex.printStackTrace();
                            }
                        }else{
                            response = new ResponseMessage(new MessageContent(HttpStatus.NOT_MODIFIED.value(),
                                    "Tên khối xử lý đã tồn tại", null));
                        }
                    }
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền sửa khối xử lý", null));
            }
        }
        return response;
    }

    public ResponseMessage createJsonSpec(String requestPath, Map<String, String> headerParam,
                                          String requestMethod, String pathParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check ABAC
            ABACResponseDTO aBACResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (aBACResponseDTO != null && aBACResponseDTO.getStatus() != null && aBACResponseDTO.getStatus()) {
                String idProcessUnit = pathParam;
                if (idProcessUnit == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
                } else {
                    Optional<ProcessUnit> processUnit = processUnitService.findById(idProcessUnit);
                    String jsonSpec = processUnitService.toJsonSpec(processUnit.get());
                    processUnit.get().setSpec(jsonSpec);
                    processUnitService.update(processUnit.get());
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), processUnit.get()));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền tạo json spec của khối xử lý", null));
            }
        }
        return response;
    }
}
