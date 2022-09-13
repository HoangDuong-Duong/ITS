package com.elcom.its.management.controller;

import com.elcom.its.management.constant.Constant;
import com.elcom.its.management.dto.*;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.management.model.Job;
import com.elcom.its.management.service.EventInfoFileService;
import com.elcom.its.management.service.EventInfoService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class EventInfoController extends BaseController {
    @Autowired
    private EventInfoService eventInfoService;
    @Autowired
    private EventInfoFileService eventInfoFileService;
    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Value("${info_worker_queue_event_accident}")
    private String queueExport;

    @Value("${info_worker_queue_event}")
    private String queueExportEvent;


    public ResponseMessage getDetailEventInfo(Map<String, String> headerParam, String requestPath, String method, String paramPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        Response eventInfoById = eventInfoService.getEventInfoById(paramPath);
        if (eventInfoById.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), eventInfoById.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(eventInfoById.getData()));
        }
        return response;
    }

    public ResponseMessage getEventInfoByEventId(Map<String, String> headerParam, String requestPath, String method, String paramPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        Response eventInfoById = eventInfoService.getEventInfoByEventId(paramPath);
        if (eventInfoById.getData() == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), eventInfoById.getMessage(), null));
        } else {
            response = new ResponseMessage(new MessageContent(eventInfoById.getData()));
        }
        return response;
    }

    public ResponseMessage createEventInfo(Map<String, String> headerParam, Map<String, Object> bodyParam, String requestPath, String requestMethod) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            if (bodyParam == null || bodyParam.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            } else {
                String eventType = (String) bodyParam.get("eventType");
                String siteId = (String) bodyParam.get("siteId");
                String endSiteId = (String) bodyParam.get("endSiteId");
                Integer startKm = (Integer) bodyParam.get("startKm");
                Integer startM = (Integer) bodyParam.get("startM");
                Integer startProvinceId = (Integer) bodyParam.get("startProvinceId");
                Integer startDistrictId = (Integer) bodyParam.get("startDistrictId");
                Integer startWardId = (Integer) bodyParam.get("startWardId");
                String startAddress = (String) bodyParam.get("startAddress");
                Integer endKm = (Integer) bodyParam.get("endKm");
                Integer endM = (Integer) bodyParam.get("endM");
                Integer endProvinceId = (Integer) bodyParam.get("endProvinceId");
                Integer endDistrictId = (Integer) bodyParam.get("endDistrictId");
                Integer endWardId = (Integer) bodyParam.get("endWardId");
                String endAddress = (String) bodyParam.get("endAddress");
                String siteCorrect = (String) bodyParam.get("siteCorrect");
                String direction = (String) bodyParam.get("direction");
                String source = (String) bodyParam.get("source");
                String reportMethod = (String) bodyParam.get("reportMethod");
                String priority = (String) bodyParam.get("priority");
                String info = (String) bodyParam.get("info");
                String reasonRoad = (String) bodyParam.get("reasonRoad");
                String reasonPerson = (String) bodyParam.get("reasonPerson");
                String reasonVehicle = (String) bodyParam.get("reasonVehicle");
                String reasonOther = (String) bodyParam.get("reasonOther");
                String reasonOrigin = (String) bodyParam.get("reasonOrigin");
                String objectName = (String) bodyParam.get("objectName");
                String eventKey = (String) bodyParam.get("eventKey");
                String reasonPolice = (String) bodyParam.get("reasonPolice");
                String state = (String) bodyParam.get("state");
                String content = (String) bodyParam.get("content");
                String title = (String) bodyParam.get("title");
                String email = (String) bodyParam.get("email");
                String dear = (String) bodyParam.get("dear");
                Integer numberDead = null;
                Integer lane = null;
                if (bodyParam.get("numberDead") != null) {
                    if (!StringUtil.isNullOrEmpty(bodyParam.get("numberDead").toString())) {
                        numberDead = Integer.parseInt(bodyParam.get("numberDead").toString());
                    }
                }

                Integer damageVehicle = null;
                if (bodyParam.get("damageVehicle") != null) {
                    damageVehicle = Integer.parseInt(bodyParam.get("damageVehicle").toString());
                }

                Integer damageStructure = null;
                if (bodyParam.get("damageStructure") != null) {
                    damageStructure = Integer.parseInt(bodyParam.get("damageStructure").toString());
                }

                Integer type = null;
                if (bodyParam.get("type") != null) {
                    type = Integer.parseInt(bodyParam.get("type").toString());
                }

                if (bodyParam.get("lane") != null) {
                    lane = Integer.parseInt(bodyParam.get("lane").toString());
                }

                Integer numberHurt = null;
                if (bodyParam.get("numberHurt") != null) {
                    if (!StringUtil.isNullOrEmpty(bodyParam.get("numberHurt").toString())) {
                        numberHurt = Integer.parseInt(bodyParam.get("numberHurt").toString());
                    }
                }

                Float fortuneRoad = null;
                if (bodyParam.get("fortuneRoad") != null) {
                    if (!StringUtil.isNullOrEmpty(bodyParam.get("fortuneRoad").toString())) {
                        fortuneRoad = Float.parseFloat(bodyParam.get("fortuneRoad").toString());
                    }
                }
                Float fortuneVehicle = null;
                if (bodyParam.get("fortuneVehicle") != null) {
                    if (!StringUtil.isNullOrEmpty(bodyParam.get("fortuneVehicle").toString())) {
                        fortuneVehicle = Float.parseFloat(bodyParam.get("fortuneVehicle").toString());
                    }
                }
                List<String> vehicle = (List<String>) bodyParam.get("vehicle");
                String road = (String) bodyParam.get("road");
                String trafficCondition = (String) bodyParam.get("trafficCondition");
                String note = (String) bodyParam.get("note");
                String process = (String) bodyParam.get("process");
                String videoUrl = (String) bodyParam.get("videoUrl");
                String imageUrl = (String) bodyParam.get("imageUrl");
                String eventId = (String) bodyParam.get("eventId");
                Date date = null;
                if (bodyParam.get("eventDate") != null) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        date = format.parse(bodyParam.get("eventDate").toString());
                    } catch (Exception e) {
                    }
                }

                Date date2 = null;
                if (bodyParam.get("dateReceived") != null) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        date2 = format.parse(bodyParam.get("dateReceived").toString());
                    } catch (Exception e) {
                    }
                }

                Date endTime = null;
                if (bodyParam.get("endTime") != null) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        endTime = format.parse(bodyParam.get("endTime").toString());
                    } catch (Exception e) {
                    }
                }

                EventInfoDTO eventInfoDTO = new EventInfoDTO();
                eventInfoDTO.setEventType(eventType);
                eventInfoDTO.setSiteId(siteId);
                eventInfoDTO.setSiteCorrect(siteCorrect);
                eventInfoDTO.setEndSiteId(endSiteId);
                eventInfoDTO.setDirection(direction);
                eventInfoDTO.setSource(source);
                eventInfoDTO.setEventDate(date);
                eventInfoDTO.setDateReceived(date2);
                eventInfoDTO.setReportMethod(reportMethod);
                eventInfoDTO.setPriority(priority);
                eventInfoDTO.setInfo(info);
                eventInfoDTO.setReasonRoad(reasonRoad);
                eventInfoDTO.setReasonPerson(reasonPerson);
                eventInfoDTO.setReasonVehicle(reasonVehicle);
                eventInfoDTO.setReasonOther(reasonOther);
                eventInfoDTO.setReasonOrigin(reasonOrigin);
                eventInfoDTO.setNumberDead(numberDead);
                eventInfoDTO.setNumberHurt(numberHurt);
                eventInfoDTO.setFortuneRoad(fortuneRoad);
                eventInfoDTO.setFortuneVehicle(fortuneVehicle);
                eventInfoDTO.setVehicle(vehicle);
                eventInfoDTO.setRoad(road);
                eventInfoDTO.setTrafficCondition(trafficCondition);
                eventInfoDTO.setNote(note);
                eventInfoDTO.setProcess(process);
                eventInfoDTO.setVideoUrl(videoUrl);
                eventInfoDTO.setImageUrl(imageUrl);
                eventInfoDTO.setEventId(eventId);
                eventInfoDTO.setType(type);
                eventInfoDTO.setEventKey(eventKey);
                eventInfoDTO.setObjectName(objectName);
                eventInfoDTO.setStartKm(Long.valueOf(startKm));
                eventInfoDTO.setStartM(Long.valueOf(startM));
                eventInfoDTO.setStartProvinceId(startProvinceId);
                eventInfoDTO.setStartDistrictId(startDistrictId);
                eventInfoDTO.setStartWardId(startWardId);
                eventInfoDTO.setStartAddress(startAddress);
                eventInfoDTO.setEndKm(endKm != null ? Long.valueOf(endKm) : null);
                eventInfoDTO.setEndM(endM != null ? Long.valueOf(endM) : null);
                eventInfoDTO.setEndProvinceId(endProvinceId);
                eventInfoDTO.setEndDistrictId(endDistrictId);
                eventInfoDTO.setEndWardId(endWardId);
                eventInfoDTO.setEndAddress(endAddress);
                eventInfoDTO.setState(state);
                eventInfoDTO.setEndTime(endTime);
                eventInfoDTO.setReasonPolice(reasonPolice);
                eventInfoDTO.setLane(lane);
                eventInfoDTO.setContent(content);
                eventInfoDTO.setDamageStructure(damageStructure);
                eventInfoDTO.setDamageVehicle(damageVehicle);
                eventInfoDTO.setDear(dear);
                eventInfoDTO.setTitle(title);
                eventInfoDTO.setEmail(email);

                response = new ResponseMessage(new MessageContent(eventInfoService.saveEventInfo(eventInfoDTO)));

            }
        }
        return response;
    }

    public ResponseMessage updateEventInfo(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam, String requestMethod, String pathParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "PUT", dto.getUuid(), requestPath);
            if (bodyParam == null || bodyParam.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, new MessageContent(HttpStatus.BAD_REQUEST.value(), Constant.VALIDATION_INVALID_PARAM_VALUE, null));
            } else {
                String eventType = (String) bodyParam.get("eventType");
                String siteId = (String) bodyParam.get("siteId");
                String endSiteId = (String) bodyParam.get("endSiteId");
                Integer startKm = (Integer) bodyParam.get("startKm");
                Integer startM = (Integer) bodyParam.get("startM");
                Integer startProvinceId = (Integer) bodyParam.get("startProvinceId");
                Integer startDistrictId = (Integer) bodyParam.get("startDistrictId");
                Integer startWardId = (Integer) bodyParam.get("startWardId");
                String startAddress = (String) bodyParam.get("startAddress");
                Integer endKm = (Integer) bodyParam.get("endKm");
                Integer endM = (Integer) bodyParam.get("endM");
                Integer endProvinceId = (Integer) bodyParam.get("endProvinceId");
                Integer endDistrictId = (Integer) bodyParam.get("endDistrictId");
                Integer endWardId = (Integer) bodyParam.get("endWardId");
                String endAddress = (String) bodyParam.get("endAddress");
                String siteCorrect = (String) bodyParam.get("siteCorrect");
                String direction = (String) bodyParam.get("direction");
                String source = (String) bodyParam.get("source");
                String reportMethod = (String) bodyParam.get("reportMethod");
                String priority = (String) bodyParam.get("priority");
                String info = (String) bodyParam.get("info");
                String reasonRoad = (String) bodyParam.get("reasonRoad");
                String reasonPerson = (String) bodyParam.get("reasonPerson");
                String reasonVehicle = (String) bodyParam.get("reasonVehicle");
                String reasonOther = (String) bodyParam.get("reasonOther");
                String reasonOrigin = (String) bodyParam.get("reasonOrigin");
                String objectName = (String) bodyParam.get("objectName");
                String eventKey = (String) bodyParam.get("eventKey");
                String reasonPolice = (String) bodyParam.get("reasonPolice");
                String state = (String) bodyParam.get("state");
                String content = (String) bodyParam.get("content");
                String title = (String) bodyParam.get("title");
                String email = (String) bodyParam.get("email");
                String dear = (String) bodyParam.get("dear");

                Date endTime = null;
                if (bodyParam.get("endTime") != null) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        endTime = format.parse(bodyParam.get("endTime").toString());
                    } catch (Exception e) {
                    }
                }
                Integer numberDead = null;
                if (bodyParam.get("numberDead") != null) {
                    numberDead = Integer.parseInt(bodyParam.get("numberDead").toString());
                }

                Integer numberHurt = null;
                if (bodyParam.get("numberHurt") != null) {
                    numberHurt = Integer.parseInt(bodyParam.get("numberHurt").toString());
                }

                Float fortuneRoad = null;
                if (bodyParam.get("fortuneRoad") != null) {
                    fortuneRoad = Float.parseFloat(bodyParam.get("fortuneRoad").toString());
                }
                Float fortuneVehicle = null;
                if (bodyParam.get("fortuneVehicle") != null) {
                    fortuneVehicle = Float.parseFloat(bodyParam.get("fortuneVehicle").toString());
                }
                Integer damageVehicle = null;
                if (bodyParam.get("damageVehicle") != null) {
                    damageVehicle = Integer.parseInt(bodyParam.get("damageVehicle").toString());
                }

                Integer damageStructure = null;
                if (bodyParam.get("damageStructure") != null) {
                    damageStructure = Integer.parseInt(bodyParam.get("damageStructure").toString());
                }
                Integer type = null;
                if (bodyParam.get("type") != null) {
                    numberDead = Integer.parseInt(bodyParam.get("type").toString());
                }
                Integer lane = null;
                if (bodyParam.get("lane") != null) {
                    lane = Integer.parseInt(bodyParam.get("lane").toString());
                }
                List<String> vehicle = (List<String>) bodyParam.get("vehicle");
                String road = (String) bodyParam.get("road");
                String trafficCondition = (String) bodyParam.get("trafficCondition");
                String note = (String) bodyParam.get("note");
                String process = (String) bodyParam.get("process");
                String videoUrl = (String) bodyParam.get("videoUrl");
                String imageUrl = (String) bodyParam.get("imageUrl");
                String eventId = (String) bodyParam.get("eventId");
                Date date = null;
                if (bodyParam.get("eventDate") != null) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        date = format.parse(bodyParam.get("eventDate").toString());
                    } catch (Exception e) {
                    }
                }
                Date date2 = null;
                if (bodyParam.get("dateReceived") != null) {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        date2 = format.parse(bodyParam.get("dateReceived").toString());
                    } catch (Exception e) {
                    }
                }

                EventInfoDTO eventInfoDTO = new EventInfoDTO();
                eventInfoDTO.setEventType(eventType);
                eventInfoDTO.setSiteId(siteId);
                eventInfoDTO.setEndSiteId(endSiteId);
                eventInfoDTO.setSiteCorrect(siteCorrect);
                eventInfoDTO.setDirection(direction);
                eventInfoDTO.setSource(source);
                eventInfoDTO.setEventDate(date);
                eventInfoDTO.setDateReceived(date2);
                eventInfoDTO.setReportMethod(reportMethod);
                eventInfoDTO.setPriority(priority);
                eventInfoDTO.setInfo(info);
                eventInfoDTO.setReasonRoad(reasonRoad);
                eventInfoDTO.setReasonPerson(reasonPerson);
                eventInfoDTO.setReasonVehicle(reasonVehicle);
                eventInfoDTO.setReasonOther(reasonOther);
                eventInfoDTO.setReasonOrigin(reasonOrigin);
                eventInfoDTO.setNumberDead(numberDead);
                eventInfoDTO.setNumberHurt(numberHurt);
                eventInfoDTO.setFortuneRoad(fortuneRoad);
                eventInfoDTO.setFortuneVehicle(fortuneVehicle);
                eventInfoDTO.setVehicle(vehicle);
                eventInfoDTO.setRoad(road);
                eventInfoDTO.setTrafficCondition(trafficCondition);
                eventInfoDTO.setNote(note);
                eventInfoDTO.setProcess(process);
                eventInfoDTO.setVideoUrl(videoUrl);
                eventInfoDTO.setImageUrl(imageUrl);
                eventInfoDTO.setEventId(eventId);
                eventInfoDTO.setType(type);
                eventInfoDTO.setEventKey(eventKey);
                eventInfoDTO.setObjectName(objectName);
                eventInfoDTO.setStartKm(Long.valueOf(startKm));
                eventInfoDTO.setStartM(Long.valueOf(startM));
                eventInfoDTO.setStartProvinceId(startProvinceId);
                eventInfoDTO.setStartDistrictId(startDistrictId);
                eventInfoDTO.setStartWardId(startWardId);
                eventInfoDTO.setStartAddress(startAddress);
                eventInfoDTO.setEndKm(endKm != null ? Long.valueOf(endKm) : null);
                eventInfoDTO.setEndM(endM != null ? Long.valueOf(endM) : null);
                eventInfoDTO.setEndProvinceId(endProvinceId);
                eventInfoDTO.setEndDistrictId(endDistrictId);
                eventInfoDTO.setEndWardId(endWardId);
                eventInfoDTO.setEndAddress(endAddress);
                eventInfoDTO.setState(state);
                eventInfoDTO.setEndTime(endTime);
                eventInfoDTO.setReasonPolice(reasonPolice);
                eventInfoDTO.setLane(lane);
                eventInfoDTO.setContent(content);
                eventInfoDTO.setDear(dear);
                eventInfoDTO.setTitle(title);
                eventInfoDTO.setEmail(email);
                eventInfoDTO.setDamageStructure(damageStructure);
                eventInfoDTO.setDamageVehicle(damageVehicle);

                response = new ResponseMessage(new MessageContent(eventInfoService.updateEventInfo(eventInfoDTO, pathParam)));

            }
        }
        return response;
    }

    public ResponseMessage getAllEventInfo(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                Integer size = Integer.parseInt(params.get("size"));
                Integer page = Integer.parseInt(params.get("page"));
                String startDate = params.get("startDate");
                String endDate = params.get("endDate");
                EventInfoPage data = eventInfoService.getAllEventInfo(size, page, startDate, endDate);
                response = new ResponseMessage(new MessageContent(data.getData(), data.getTotal()));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem thông tin sự kiện", null));
            }
        }

        return response;
    }

    public ResponseMessage getDataReport(Map<String, String> headerParam, String requestPath, String method, String urlParam, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
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
                EventInfoResponseDTO data = eventInfoService.getDataReportEventInfo(startDate, endDate);
                response = new ResponseMessage(new MessageContent(data.getData()));

            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem thông tin sự kiện", null));
            }
        }
        return response;
    }

    public ResponseMessage export(String requestPath, Map<String, Object> body, Map<String, String> headerParam) throws ExecutionException, InterruptedException, JsonProcessingException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            AccidentReportResponseDTO responseDTO = null;
//            Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
            String startDate = (String) body.get("startDate");
            String endDate = (String) body.get("endDate");
            String textHeader = (String) body.get("textHeader");
            AccidentEventExport export = new AccidentEventExport();
            export.setUuid(dto.getUuid());
            export.setEndDate(endDate);
            export.setStartDate(startDate);
            export.setTextHeader(textHeader);
            export.setUuid(dto.getUuid());

            ObjectMapper mapper = new ObjectMapper();
            String msg = mapper.writeValueAsString(export);
            rabbitMQClient.callWorkerService(queueExport, msg);
            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "ok", null));
        }

        return response;
    }

    public ResponseMessage exportEventInfo(String requestPath, Map<String, Object> body, Map<String, String> headerParam) throws ExecutionException, InterruptedException, JsonProcessingException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            AccidentReportResponseDTO responseDTO = null;
            String startDate = (String) body.get("startDate");
            String endDate = (String) body.get("endDate");
            String textHeader = (String) body.get("textHeader");
            Integer size = Integer.parseInt(body.get("size").toString());
            Integer page = Integer.parseInt(body.get("page").toString());
            EventInfoSend eventInfoSend = new EventInfoSend();
            eventInfoSend.setUuid(dto.getUuid());
            eventInfoSend.setPage(page);
            eventInfoSend.setSize(size);
            eventInfoSend.setEndDate(endDate);
            eventInfoSend.setStartDate(startDate);
            eventInfoSend.setTextHeader(textHeader);
            ObjectMapper mapper = new ObjectMapper();
            String msg = mapper.writeValueAsString(eventInfoSend);
            rabbitMQClient.callWorkerService(queueExportEvent, msg);
            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "ok", null));
        }

        return response;
    }
    public ResponseMessage exportHistory(String requestPath, Map<String, Object> body,String urlParam, Map<String, String> headerParam) throws ExecutionException, InterruptedException, JsonProcessingException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String vmsId = (String) params.get("vmsId");
        String link = eventInfoFileService.createHistoryDisplayFile(startDate,endDate,vmsId,dto.getUuid());
        return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "ok", link));
    }

    public ResponseMessage exportActionStatus(String requestPath, Map<String, Object> body,String urlParam, Map<String, String> headerParam) throws ExecutionException, InterruptedException, JsonProcessingException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
        String startDate = (String) params.get("startTime");
        String endDate = (String) params.get("endTime");
        String link = eventInfoFileService.createActionStatusFile(startDate,endDate,dto.getUuid());
        return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "ok", link));
    }


}
