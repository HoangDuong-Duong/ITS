package com.elcom.its.management.controller;

import com.elcom.its.management.constant.Constant;
import com.elcom.its.management.dto.*;
import com.elcom.its.management.enums.HotLineType;
import com.elcom.its.management.enums.ReportDeviceType;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.management.service.EventInfoFileService;
import com.elcom.its.management.service.HotlineService;
import com.elcom.its.management.service.ReportDeviceService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class ReportDeviceController extends BaseController {
    @Autowired
    private ReportDeviceService hotlineService;
    @Autowired
    private EventInfoFileService eventInfoFileService;
    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Value("${info_worker_queue_event_accident}")
    private String queueExport;

    @Value("${info_worker_queue_event}")
    private String queueExportEvent;



    public ResponseMessage createReport(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) throws Exception {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            if (bodyParam == null || bodyParam.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            } else {
                ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
                if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                    return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tạo thông tin báo cáo hằng ngày",
                            new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tạo thông tin báo cáo hằng ngày", null));
                } else {
                    ObjectMapper objectMapper = new ObjectMapper();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    objectMapper.setDateFormat(df);
                    List<ReportDeviceDTO> hotlineDTOS = (List<ReportDeviceDTO>)bodyParam.get("data");
                    String tmp = objectMapper.writeValueAsString(hotlineDTOS);
                    JsonNode jsonNode = objectMapper.readTree(tmp);
                    hotlineDTOS = objectMapper.readerFor(new TypeReference<List<ReportDeviceDTO>>() {
                    }).readValue(jsonNode);;
                    response = new ResponseMessage(new MessageContent(hotlineService.saveReport(hotlineDTOS)));
                }

            }
        }
        return response;
    }

    public ResponseMessage updateEventInfo(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền sửa thông tin báo cáo hằng ngày",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền sửa thông tin báo cáo hằng ngày", null));
            } else {
                String id = (String) bodyParam.get("id");
                Integer online = (Integer) bodyParam.get("online");
                Integer offline = (Integer) bodyParam.get("offline");
                Integer type = (Integer) bodyParam.get("type");
                Date startTime = null;
                if (bodyParam.get("startTime") != null) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        startTime = format.parse(bodyParam.get("startTime").toString());
                    } catch (Exception e) {
                    }
                }

                Date createdDate = null;
                ReportDeviceDTO reportDeviceDTO = new ReportDeviceDTO();
                reportDeviceDTO.setName(ReportDeviceType.parse(type).description());
                reportDeviceDTO.setType(ReportDeviceType.parse(type));
                reportDeviceDTO.setOffline(offline);
                reportDeviceDTO.setOnline(online);
                reportDeviceDTO.setStartTime(startTime);
                reportDeviceDTO.setId(id);
                response = new ResponseMessage(new MessageContent(hotlineService.updateHotline(reportDeviceDTO,id)));

            }
        }
        return response;
    }

    public ResponseMessage getReportHotline(Map<String, String> headerParam, String requestPath,  String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo hằng ngày",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo hằng ngày", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String startDate = params.get("startDate");
                String endDate = params.get("endDate");
                DeviceResponseDTO data = hotlineService.getReport(startDate, endDate);
                response = new ResponseMessage(new MessageContent(data.getData(), data.getTotal()));
            }
        }

        return response;
    }

    public ResponseMessage deleteMultiEvent(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo hằng ngày",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo hằng ngày", null));
            } else {
                List<String> eventIds = (List<String>) bodyParam.get("ids");
                if (CollectionUtils.isEmpty(eventIds)) {
                    return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Vui lòng truyền danh sách hotline cần xóa", null));
                }
                Response responseITS = hotlineService.delete(eventIds);
                response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), null));
            }

        }

        return response;
    }


}
