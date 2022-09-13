package com.elcom.its.management.controller;

import com.elcom.its.management.dto.*;
import com.elcom.its.management.enums.EventStatus;
import com.elcom.its.management.enums.JobStatus;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.management.model.EventFile;
import com.elcom.its.management.service.*;
import com.elcom.its.management.validation.EventValidation;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.DateUtil;
import com.elcom.its.utils.DateUtils;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.elcom.its.management.model.Job;
import com.elcom.its.management.enums.JobStatus;
import com.elcom.its.management.enums.EventStatus;
import java.text.SimpleDateFormat;

@Controller
public class EventController extends BaseController {

    static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService itsCoreEventService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private JobService jobService;

    @Autowired
    private StageService stageService;

    @Autowired
    private EventFileService eventFileService;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private TrafficFlowService trafficFlowService;

    @Value("${its.event.export.worker.queue}")
    private String queueExport;

    @Value("${its.report.day.worker.queue}")
    private String queueDayReport;

    @Value("${its.report.event.daily.worker.queue}")
    private String queueDailyReportEvent;

    @Value("${its.event.info.update.worker.queue}")
    private String infoUpdate;

    public ResponseMessage findEvent(String requestPath,
            String requestMethod, String urlParam, Map<String, String> headerParam) throws ExecutionException, InterruptedException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện trung tâm",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện trung tâm", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                int currentPage = Integer.parseInt(params.get("page"));
                int rowsPerPage = Integer.parseInt(params.get("size"));
//                int eventStatus;
//                try {
//                    eventStatus = Integer.parseInt(params.get("eventStatus"));
//                }catch (Exception ex){
//                    eventStatus =-1;
//                }

                String startDate = params.get("fromDate");
                String endDate = params.get("toDate");
                String objectName = params.get("objectName");
                String eventCode = params.get("eventCode");
                String key = params.get("key");
                String plate = params.get("plate");
                String manual = params.get("manual");
                String directionCode = params.get("directionCode");
                String filterObjectType = params.get("filterObjectType");
                List<String> filterObjectIds = params.get("filterObjectIds") != null ? Arrays.asList(params.get("filterObjectIds").split(",")) : null;
                int eventStatus = !params.get("eventStatus").isEmpty() ? Integer.parseInt(params.get("eventStatus")) : -1;

                EventResponseDTO responseDTO = null;
                String itsFilterObjectIds = filterObjectIds != null ? String.join(",", filterObjectIds) : null;
//                filterObjectType = "cam";
                // Neu khong phai la admin
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    String stages = dto.getUnit().getLisOfStage();
                    responseDTO = itsCoreEventService.findEventPage(stages, startDate, endDate, filterObjectType, itsFilterObjectIds,
                            objectName, eventCode, eventStatus,
                            directionCode, plate, key, currentPage, rowsPerPage, false, manual);
                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                    } else {
                        response = new ResponseMessage(new MessageContent(null, 0L));
                    }

                } else {
                    responseDTO = itsCoreEventService.findEventPage(null, startDate, endDate, filterObjectType,
                            itsFilterObjectIds, objectName, eventCode, eventStatus,
                            directionCode, plate, key, currentPage, rowsPerPage, true, manual);

                    if (responseDTO != null && responseDTO.getData() != null) {
                        response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                    } else {
                        response = new ResponseMessage(new MessageContent(null, 0L));
                    }
                }

            }
        }

        return response;
    }

    public ResponseMessage exportEvent(String requestPath, String pathParam, Map<String, String> headerParam) throws Exception {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tạo báo cáo sự kiện",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tạo báo cáo sự kiện", null));

            } else {
//                eventFileService.createReportEvent(pathParam,"gd");
                EventExport eventExport = new EventExport();
                eventExport.setEventId(pathParam);
                eventExport.setUuid(dto.getUuid());
                ObjectMapper mapper = new ObjectMapper();
                String msg = mapper.writeValueAsString(eventExport);
                rabbitMQClient.callWorkerService(queueExport, msg);
                return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "ok", null));
            }
        }

        return response;
    }

    public ResponseMessage getDataReport(Map<String, String> headerParam, String requestPath, String urlParam, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
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
                ReportEventDailyResponse data = itsCoreEventService.getReportDailyEvent(startDate, endDate);
                response = new ResponseMessage(new MessageContent(data.getData()));

            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem thông tin sự kiện", null));
            }
        }
        return response;
    }

    public ResponseMessage exportEventInfo(String requestPath, String pathParam, Map<String, String> headerParam) throws Exception {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tạo báo cáo sự kiện",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tạo báo cáo sự kiện", null));

            } else {
                eventFileService.createReportEvent(pathParam, dto.getUuid());
//                EventExport eventExport = new EventExport();
//                eventExport.setEventId(pathParam);
//                eventExport.setUuid(dto.getUuid());
//                ObjectMapper mapper = new ObjectMapper();
//                String msg = mapper.writeValueAsString(eventExport);
//                rabbitMQClient.callWorkerService(queueExport, msg);
                return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "ok", null));
            }
        }

        return response;
    }

    public ResponseMessage findEventCommon(String requestPath, String urlParam, Map<String, String> headerParam) throws ExecutionException, InterruptedException {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện hiện trường",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện hiện trường", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                int currentPage = Integer.parseInt(params.get("page"));
                int rowsPerPage = Integer.parseInt(params.get("size"));
                String startDate = params.get("fromDate");
                String endDate = params.get("toDate");
                String objectName = params.get("objectName");
                String manual = params.get("manual");
                List<String> eventCode = !StringUtil.isNullOrEmpty(params.get("eventCode")) ? Arrays.asList(params.get("eventCode").split(",")) : null;
                String directionCode = params.get("directionCode");
                String filterObjectType = params.get("filterObjectType");
                List<String> filterObjectIds = !StringUtil.isNullOrEmpty(params.get("filterObjectIds")) ? Arrays.asList(params.get("filterObjectIds").split(",")) : null;
                int eventStatus = !params.get("eventStatus").isEmpty() ? Integer.parseInt(params.get("eventStatus")) : -1;
                String key = params.get("key");
                String plate = params.get("plate");

                EventResponseDTO responseDTO = null;
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    try {
                        LocalDateTime localStartTime = DateUtils.parse(startDate);
                        LocalDateTime localEndTime = DateUtils.parse(endDate);
                        String stages = dto.getUnit().getLisOfStage();
                        List<String> stageList = stages != null ? Arrays.asList(stages.split(",")) : null;
                        List<String> parentId = jobService.getEvent(dto.getUnit().getUuid(), DateUtil.fromLocalDateTime(localStartTime), DateUtil.fromLocalDateTime(localEndTime));
//                        String parentIds = parentId != null ? String.join(",", parentId) : null;
                        if (parentId != null && !parentId.isEmpty()) {
                            responseDTO = itsCoreEventService.findEventPageCommon(stageList, parentId, startDate, endDate, filterObjectType, filterObjectIds, objectName, eventCode, eventStatus,
                                    directionCode, plate, key, currentPage, rowsPerPage, false, manual);
                            if (responseDTO != null && responseDTO.getData() != null) {
                                response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                            } else {
                                response = new ResponseMessage(new MessageContent(null, 0L));
                            }

                        } else {
                            response = new ResponseMessage(new MessageContent(null, 0L));
                        }

                    } catch (Exception e) {
                        System.out.println("Today's midnight applied.");
                        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Lỗi format Date",
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), "Lỗi format Date", null));
                    }
                } else {
                    LocalDateTime localStartTime = DateUtils.parse(startDate);
                    LocalDateTime localEndTime = DateUtils.parse(endDate);
                    List<String> parentId = jobService.getEventAdmin(DateUtil.fromLocalDateTime(localStartTime), DateUtil.fromLocalDateTime(localEndTime));
//                    String parentIds = parentId != null ? String.join(",", parentId) : null;
                    if (parentId != null && !parentId.isEmpty()) {
                        responseDTO = itsCoreEventService.findEventPageCommon(null, parentId, startDate, endDate, filterObjectType, filterObjectIds, objectName, eventCode, eventStatus,
                                directionCode, plate, key, currentPage, rowsPerPage, true, manual);
                        if (responseDTO != null && responseDTO.getData() != null) {
                            response = new ResponseMessage(new MessageContent(responseDTO.getData(), responseDTO.getTotal()));
                        } else {
                            response = new ResponseMessage(new MessageContent(null, 0L));
                        }

                    } else {
                        response = new ResponseMessage(new MessageContent(null, 0L));
                    }
                }

            }
        }

        return response;
    }

    public ResponseMessage findHistoryEvent(String urlParam, String requestPath,
            String requestMethod, String parentId) throws ExecutionException, InterruptedException {
        AuthorizationResponseDTO dto = getUrlParam(urlParam);
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        ResponseMessage response = null;
        if (dto == null) {
            return new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Lỗi lấy dữ liệu người dùng từ Urlparam",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Lỗi lấy dữ liệu người dùng từ Urlparam", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
//            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
//            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
//                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem lịch sử cập nhật sự kiện", null));
//            } else {

            EventResponseDTO eventResponseDTO = itsCoreEventService.historyEvent(parentId);
            if (eventResponseDTO != null && eventResponseDTO.getData() != null) {

                List<EventDTO> eventDTO = eventResponseDTO.getData();
                if (!eventDTO.isEmpty()) {
//                    String stages = stageService.findBySite(eventDTO.get(0).getSite().getSiteId());
                    String stages = stageService.getStageByPositionM(eventDTO.get(0).getSite().getPositionM());
                    List<String> stageIds = new ArrayList<>(Arrays.asList(stages.split(",")));
                    String listStages = dto.getUnit().getLisOfStage();
                    List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                    LOGGER.info("Check Abac");
                    Map<String, Object> subject = new HashMap<>();
                    subject.put("stage", stageIds);
                    Map<String, Object> attributes = new HashMap<>();
                    attributes.put("stage", myList);
                    Map<String, Object> bodyParam = new HashMap<>();
                    bodyParam.put("subject", subject);
                    bodyParam.put("attributes", attributes);
                    ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "DETAIL", dto.getUuid(), requestPath);
                    if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                        response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem lịch sử cập nhật sự kiện", null));
                    } else {
//                            eventDTO.stream().map(item -> {
//                                Map<String, Object> map = new HashMap<>();
//                                map.put("uuids", Arrays.asList(item.getModifiedBy()));
//                                AuthorizationResponseDTO userDTO = getUsernameByUserId(map);
//                                if (userDTO != null) {
//                                    item.setUserName(userDTO.getFullName());
//                                }
//                                return item;
//                            }).collect(Collectors.toList());
                        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), eventDTO, eventResponseDTO.getTotal()));
                    }
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), eventDTO, eventResponseDTO.getTotal()));
                }
            } else {
                response = new ResponseMessage(new MessageContent(null));
            }
//            }
        }

        return response;
    }

    public ResponseMessage getEventMap(Map<String, String> headerParam, String requestPath, String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                Response responseITS;
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    String stages = dto.getUnit().getLisOfStage();
                    responseITS = itsCoreEventService.getEventMap(params.get("fromDate"), params.get("toDate"), stages, false);
                } else {
                    responseITS = itsCoreEventService.getEventMap(params.get("fromDate"), params.get("toDate"), "", true);
                }

                response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
            }

        }

        return response;
    }

    public ResponseMessage getEventStraightMap(Map<String, String> headerParam, String requestPath, String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
            String stages = params.get("stageCode");
            String listStages = dto.getUnit().getLisOfStage();
            List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
            LOGGER.info("Check Abac");
            Map<String, Object> subject = new HashMap<>();
            subject.put("stage", stages);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("stage", myList);
            Map<String, Object> bodyParam = new HashMap<>();
            bodyParam.put("subject", subject);
            bodyParam.put("attributes", attributes);
            ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "DETAIL", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện", null));
            } else {
                Response responseITS;
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    responseITS = itsCoreEventService.getEventOnStraightMap(params.get("fromDate"), params.get("toDate"), stages, false);
                } else {
                    responseITS = itsCoreEventService.getEventOnStraightMap(params.get("fromDate"), params.get("toDate"), "", true);
                }

                response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
            }

        }

        return response;
    }

    public ResponseMessage getDetail(Map<String, String> headerParam, String requestPath, String id, String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
            String startTime = params.get("startTime");
            Response responseITS = itsCoreEventService.getDetailEvent(id, startTime);
            if (responseITS.getData() != null) {
                ObjectMapper oMapper = new ObjectMapper();
                Map<String, Object> map = oMapper.convertValue(responseITS.getData(), Map.class);
                SiteInfo siteInfo = oMapper.convertValue(map.get("site"), new TypeReference<SiteInfo>() {
                });
//                String stages = stageService.findBySite(siteInfo.getSiteId());
                String stages = stageService.getStageByPositionM(siteInfo.getPositionM());
                List<String> stageIds = new ArrayList<>(Arrays.asList(stages.split(",")));
                String listStages = dto.getUnit().getLisOfStage();
                List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                LOGGER.info("Check Abac");
                Map<String, Object> subject = new HashMap<>();
                subject.put("stage", stageIds);
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("stage", myList);
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("subject", subject);
                bodyParam.put("attributes", attributes);
                ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "DETAIL", dto.getUuid(), requestPath);
                if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem lịch sử cập nhật sự kiện", null));
                } else {
                    response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
                }

            } else {
                response = new ResponseMessage(new MessageContent(responseITS.getStatus(), "Không tìm thấy sự kiện", responseITS.getData()));
            }
        }

        return response;
    }

    public ResponseMessage deleteEvent(Map<String, String> headerParam, String requestPath, String paramPath, String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
            String startTime = params.get("startTime");
            Response responseITS = itsCoreEventService.getDetailEvent(paramPath, startTime);
            if (responseITS.getData() != null) {
                ObjectMapper oMapper = new ObjectMapper();
                Map<String, Object> map = oMapper.convertValue(responseITS.getData(), Map.class);
                SiteInfo siteInfo = oMapper.convertValue(map.get("site"), new TypeReference<SiteInfo>() {
                });
                String stage = stageService.findBySite(siteInfo.getSiteId());
                String listStages = dto.getUnit().getLisOfStage();
                List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                LOGGER.info("Check Abac");
                Map<String, Object> subject = new HashMap<>();
                List<String> stages = new ArrayList<>();
                stages.add(stage);
                subject.put("stages", stages);
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("stages", myList);
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("subject", subject);
                bodyParam.put("attributes", attributes);
                ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "DELETE", dto.getUuid(), requestPath);
                if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa sự kiện", null));
                } else {
                    responseITS = itsCoreEventService.deleteEvent(paramPath, startTime);
                    response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), null));
                }

            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy bản ghi", null));
            }
        }

        return response;
    }

    public ResponseMessage getAllFileEvent(Map<String, String> headerParam, String requestPath, String method, String paramPath) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            EventFileResponseDTO responseITS = itsCoreEventService.getFileEvent(paramPath);
            if (responseITS.getData() != null) {
                Response responseCheck = itsCoreEventService.getEvent(paramPath);
                if (responseCheck.getData() != null) {
                    ObjectMapper oMapper = new ObjectMapper();
                    Map<String, Object> map = oMapper.convertValue(responseCheck.getData(), Map.class);
                    SiteInfo siteInfo = oMapper.convertValue(map.get("site"), new TypeReference<SiteInfo>() {
                    });
//                    String stages = stageService.findBySite(siteInfo.getSiteId());
                    String stages = stageService.getStageByPositionM(siteInfo.getPositionM());
                    List<String> stageIds = new ArrayList<>(Arrays.asList(stages.split(",")));
                    String listStages = dto.getUnit().getLisOfStage();
                    List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                    LOGGER.info("Check Abac");
                    Map<String, Object> subject = new HashMap<>();
                    subject.put("stage", stageIds);
                    Map<String, Object> attributes = new HashMap<>();
                    attributes.put("stage", myList);
                    Map<String, Object> bodyParam = new HashMap<>();
                    bodyParam.put("subject", subject);
                    bodyParam.put("attributes", attributes);
                    ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "DETAIL", dto.getUuid(), requestPath);
                    if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                        response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem tài liệu sự kiện", null));
                    } else {
                        List<EventFile> eventFiles = responseITS.getData();
                        List<EventFile> fileJob = jobService.getListFileForEvent(paramPath);
                        eventFiles.addAll(fileJob);
                        response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), eventFiles));
                    }

                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy bản ghi", null));
                }
            } else {
                response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), null));
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
            List<String> eventIds = (List<String>) bodyParam.get("eventIds");
            if (CollectionUtils.isEmpty(eventIds)) {
                return new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Vui lòng truyền danh sách phương tiện cần xóa", null));
            }
            Response responseITS = itsCoreEventService.getMultiEvent(eventIds);
            List<String> stage = (List<String>) responseITS.getData();
            String listStages = dto.getUnit().getLisOfStage();
            List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
            LOGGER.info("Check Abac");
            Map<String, Object> subject = new HashMap<>();
            subject.put("stages", stage);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("stages", myList);
            Map<String, Object> bodyParamABAC = new HashMap<>();
            bodyParamABAC.put("subject", subject);
            bodyParamABAC.put("attributes", attributes);
            ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParamABAC, "DELETE", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa nhiều sự kiện", null));
            } else {
                Response responseITSDELETE = itsCoreEventService.deleteMultiEvent(eventIds);
                jobService.deleteByEventIds(eventIds);
                response = new ResponseMessage(new MessageContent(responseITSDELETE.getStatus(), responseITSDELETE.getMessage(), null));
            }
        }

        return response;
    }

    public ResponseMessage updateViolation(Map<String, Object> body, String requestPath, Map<String, String> headerParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            String id = (String) body.get("id");
            String sourceId = (String) body.get("sourceId");
            String plate = (String) body.get("plate");
            String startTime = (String) body.get("startTime");
            String endDate = (String) body.get("endDate");
            String objectType = (String) body.get("objectType");
            float speedOfVehicle;
            try {
                Integer a = (Integer) body.get("speedOfVehicle");
                speedOfVehicle = a.floatValue();
            } catch (Exception ex) {
                speedOfVehicle = (float) body.get("speedOfVehicle");
            }
            String laneId = (String) body.get("laneId");
            String eventCode = (String) body.get("eventCode");
            String objectName = (String) body.get("objectName");
            Integer eventStatus = (Integer) body.get("eventStatus");
            String imageUrl = (String) body.get("imageUrl");
            String videoUrl = (String) body.get("videoUrl");
            String invalidData = new EventValidation().updateViolation(id, startTime, sourceId, eventCode, plate, laneId, imageUrl, eventStatus);
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                Response responseStage = itsCoreEventService.getStageViolation(id, startTime);
                String stage = (String) responseStage.getData();
                String listStages = dto.getUnit().getLisOfStage();
                List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                LOGGER.info("Check Abac");
                Map<String, Object> subject = new HashMap<>();
                subject.put("stage", stage);
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("stage", myList);
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("subject", subject);
                bodyParam.put("attributes", attributes);
                ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "PUT", dto.getUuid(), requestPath);
                if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhật sự kiện", null));
                } else {
                    LocalDateTime time;
                    LocalDateTime endTime;
                    try {
                        time = DateUtils.parse(startTime);
                        endTime = DateUtils.parse(endDate);

                    } catch (Exception ex) {
                        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endDate không đúng định dạng", null);
                    }
                    Response responseITS = itsCoreEventService.updateViolation(id, sourceId, objectType, DateUtil.fromLocalDateTime(time), DateUtil.fromLocalDateTime(endTime), speedOfVehicle,
                            laneId, plate, eventCode, objectName, eventStatus, imageUrl, videoUrl, dto.getUuid());
                    response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
                }

            }

        }
        return response;
    }

    public ResponseMessage updateSecurity(Map<String, Object> body, String requestPath, Map<String, String> headerParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            String id = (String) body.get("id");
            String sourceId = (String) body.get("sourceId");
            String startTime = (String) body.get("startTime");
            String laneId = (String) body.get("laneId");
            String eventCode = (String) body.get("eventCode");
            Integer eventStatus = (Integer) body.get("eventStatus");
            String imageUrl = (String) body.get("imageUrl");
            String invalidData = new EventValidation().updateSecurity(id, startTime, sourceId, eventCode, laneId, imageUrl, eventStatus);
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                Response responseStage = itsCoreEventService.getStageViolation(id, startTime);
                String stage = (String) responseStage.getData();
                String listStages = dto.getUnit().getLisOfStage();
                List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                LOGGER.info("Check Abac");
                Map<String, Object> subject = new HashMap<>();
                subject.put("stage", stage);
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("stage", myList);
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("subject", subject);
                bodyParam.put("attributes", attributes);
                ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "PUT", dto.getUuid(), requestPath);
                if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhật sự kiện", null));
                } else {
                    LocalDateTime time;
                    LocalDateTime endTime;
                    try {
                        time = DateUtils.parse(startTime);

                    } catch (Exception ex) {
                        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endDate không đúng định dạng", null);
                    }
                    Response responseITS = itsCoreEventService.updateSecurity(id, sourceId, DateUtil.fromLocalDateTime(time),
                            laneId, eventCode, eventStatus, imageUrl, dto.getUuid());
                    response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
                }

            }

        }
        return response;
    }

    public ResponseMessage updateManual(Map<String, Object> body, String requestPath, Map<String, String> headerParam) throws ExecutionException, InterruptedException, JsonProcessingException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            String id = (String) body.get("id");
            String sourceId = (String) body.get("sourceId");
            String startTime = (String) body.get("startTime");
            String eventCode = (String) body.get("eventCode");
            Integer eventStatus = (Integer) body.get("eventStatus");
            String imageUrl = (String) body.get("imageUrl");
            Integer startKm = (Integer) body.get("startKm");
            Integer startM = (Integer) body.get("startM");
            Integer startProvinceId = (Integer) body.get("startProvinceId");
            Integer startDistrictId = (Integer) body.get("startDistrictId");
            Integer startWardId = (Integer) body.get("startWardId");
            String startAddress = (String) body.get("startAddress");
            Integer endKm = (Integer) body.get("endKm");
            Integer endM = (Integer) body.get("endM");
            Integer endProvinceId = (Integer) body.get("endProvinceId");
            Integer endDistrictId = (Integer) body.get("endDistrictId");
            Integer endWardId = (Integer) body.get("endWardId");
            String endAddress = (String) body.get("endAddress");
            String directionCode = (String) body.get("directionCode");
            String videoUrl = (String) body.get("videoUrl");
            String objectName = (String) body.get("objectName");
            String note = (String) body.get("note");
            String siteCorrect = (String) body.get("siteCorrect");
            String siteId = (String) body.get("siteId");
            String endSite = (String) body.get("endSite");
            String invalidData = new EventValidation().validateManual(id, startTime, eventStatus);
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                Response responseStage = itsCoreEventService.getStageManual(id, startTime);
                if (responseStage == null) {
                    return new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy sự kiện", null));
                }
                String stage = stageService.getStageByPositionM(Long.valueOf(startKm * 1000 + startM));
                List<String> stageId = new ArrayList<String>(Arrays.asList(stage.split(",")));
                String listStages = dto.getUnit().getLisOfStage();
                List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                LOGGER.info("Check Abac");
                Map<String, Object> subject = new HashMap<>();
                subject.put("stageId", stageId);
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("stageId", myList);
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("subject", subject);
                bodyParam.put("attributes", attributes);
                ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "PUT", dto.getUuid(), requestPath);
                if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhật sự kiện", null));
                } else {
                    LocalDateTime time;
                    LocalDateTime endTimeS;
                    try {
                        time = DateUtils.parse(startTime);
//                        endTimeS = DateUtils.parse(endTime);

                    } catch (Exception ex) {
                        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endDate không đúng định dạng", null);
                    }
                    EventManualDTO eventManualDTO = new EventManualDTO();
                    eventManualDTO.setEventCode(eventCode);
                    eventManualDTO.setImageUrl(imageUrl);
                    eventManualDTO.setStartKm(Long.valueOf(startKm));
                    eventManualDTO.setStartM(Long.valueOf(startM));
                    eventManualDTO.setStartProvinceId(startProvinceId);
                    eventManualDTO.setStartDistrictId(startDistrictId);
                    eventManualDTO.setStartWardId(startWardId);
                    eventManualDTO.setStartAddress(startAddress);
                    eventManualDTO.setStartTime(DateUtil.fromLocalDateTime(time));
                    eventManualDTO.setDirectionCode(directionCode);
                    eventManualDTO.setEndSite(endSite);
                    eventManualDTO.setObjectName(objectName);
                    eventManualDTO.setUuid(dto.getUuid());
                    eventManualDTO.setSourceId(sourceId);
                    eventManualDTO.setVideoUrl(videoUrl);
                    eventManualDTO.setSiteId(siteId);
                    eventManualDTO.setManualEvent(true);
                    eventManualDTO.setNote(note);
                    eventManualDTO.setSiteCorrect(siteCorrect);
                    if (endKm != null && endM != null) {
                        eventManualDTO.setEndKm(Long.valueOf(endKm));
                        eventManualDTO.setEndM(Long.valueOf(endM));
                        eventManualDTO.setEndProvinceId(endProvinceId);
                        eventManualDTO.setEndDistrictId(endDistrictId);
                        eventManualDTO.setEndWardId(endWardId);
                        eventManualDTO.setEndAddress(endAddress);
                    }
                    eventManualDTO.setUser(dto.getUuid());
                    Response responseITS = itsCoreEventService.updateManual(id, eventManualDTO);
                    response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
                    EventExport eventExport = new EventExport();
                    eventExport.setEventId(id);
                    eventExport.setUuid(dto.getUuid());
                    ObjectMapper mapper = new ObjectMapper();
                    String msg = mapper.writeValueAsString(eventExport);
                    rabbitMQClient.callWorkerService(infoUpdate, msg);
                }

            }

        }
        return response;
    }

    public ResponseMessage updateEvent(Map<String, Object> body, String requestPath, Map<String, String> headerParam) throws ExecutionException, InterruptedException, JsonProcessingException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            String id = (String) body.get("id");
            String eventCode = (String) body.get("eventCode");
            String startTime = (String) body.get("startTime");
            String siteCorrect = (String) body.get("siteCorrect");
            String note = (String) body.get("note");
            String videoUrl = (String) body.get("videoUrl");
            String imageUrl = (String) body.get("imageUrl");
            if (StringUtil.isNullOrEmpty(id)) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Id không thể trống", null));
            } else {
                Response responseStage = itsCoreEventService.getStageManual(id, startTime);
                if (responseStage == null) {
                    return new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy sự kiện", null));
                }
                String stage = (String) responseStage.getData();
                String listStages = dto.getUnit().getLisOfStage();
                List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                LOGGER.info("Check Abac");
                Map<String, Object> subject = new HashMap<>();
                subject.put("stageId", stage.split(","));
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("stageId", myList);
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("subject", subject);
                bodyParam.put("attributes", attributes);
                ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "PUT", dto.getUuid(), requestPath);
                if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhật sự kiện", null));
                } else {
                    Response responseITS = itsCoreEventService.updateEvent(id, eventCode, note, siteCorrect, imageUrl, videoUrl, dto.getUuid());
                    response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
                    EventExport eventExport = new EventExport();
                    eventExport.setEventId(id);
                    eventExport.setUuid(dto.getUuid());
                    ObjectMapper mapper = new ObjectMapper();
                    String msg = mapper.writeValueAsString(eventExport);
                    rabbitMQClient.callWorkerService(infoUpdate, msg);
                }

            }

        }
        return response;
    }

    public ResponseMessage createManual(Map<String, Object> body, String requestPath, Map<String, String> headerParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            String sourceId = (String) body.get("sourceId");
            String startTime = (String) body.get("startTime");
            String eventCode = (String) body.get("eventCode");
            String objectName = (String) body.get("objectName");
            String imageUrl = (String) body.get("imageUrl");
            String siteId = (String) body.get("siteId");
            Integer startKm = (Integer) body.get("startKm");
            Integer startM = (Integer) body.get("startM");
            Integer startProvinceId = (Integer) body.get("startProvinceId");
            Integer startDistrictId = (Integer) body.get("startDistrictId");
            Integer startWardId = (Integer) body.get("startWardId");
            String startAddress = (String) body.get("startAddress");
            String directionCode = (String) body.get("directionCode");
            String endSite = (String) body.get("endSite");
            Integer endKm = (Integer) body.get("endKm");
            Integer endM = (Integer) body.get("endM");
            Integer endProvinceId = (Integer) body.get("endProvinceId");
            Integer endDistrictId = (Integer) body.get("endDistrictId");
            Integer endWardId = (Integer) body.get("endWardId");
            String endAddress = (String) body.get("endAddress");
            String videoUrl = (String) body.get("videoUrl");
            String endTime = (String) body.get("endTime");
            String note = (String) body.get("note");
            String siteCorrect = (String) body.get("siteCorrect");
            String invalidData = new EventValidation().validateManual(startTime, eventCode, startKm, startM);
            if (invalidData != null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
            } else {
                String stage = stageService.getStageByPositionM(Long.valueOf(startKm * 1000 + startM));
                List<String> stageId = new ArrayList<String>(Arrays.asList(stage.split(",")));
                String listStages = dto.getUnit().getLisOfStage();
                List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                LOGGER.info("Check Abac");
                Map<String, Object> subject = new HashMap<>();
                subject.put("stageId", stageId);
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("stageId", myList);
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("subject", subject);
                bodyParam.put("attributes", attributes);
                ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "POST", dto.getUuid(), requestPath);
                if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tạo sự kiện", null));
                } else {
                    LocalDateTime time;
                    LocalDateTime endTimeS = LocalDateTime.now();
                    try {
                        time = DateUtils.parse(startTime);
                        if (endTime != null) {
                            endTimeS = DateUtils.parse(endTime);
                        }

                    } catch (Exception ex) {
                        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endTime không đúng định dạng", null);
                    }
                    EventManualDTO eventManualDTO = new EventManualDTO();
                    eventManualDTO.setEventCode(eventCode);
                    eventManualDTO.setImageUrl(imageUrl);
                    if (endTime != null) {
                        eventManualDTO.setEndTime(DateUtil.fromLocalDateTime(endTimeS));
                    }
                    eventManualDTO.setStartKm(Long.valueOf(startKm));
                    eventManualDTO.setStartM(Long.valueOf(startM));
                    eventManualDTO.setStartProvinceId(startProvinceId);
                    eventManualDTO.setStartDistrictId(startDistrictId);
                    eventManualDTO.setStartWardId(startWardId);
                    eventManualDTO.setStartAddress(startAddress);
                    eventManualDTO.setStartTime(DateUtil.fromLocalDateTime(time));
                    eventManualDTO.setDirectionCode(directionCode);
                    eventManualDTO.setEndSite(endSite);
                    eventManualDTO.setObjectName(objectName);
                    eventManualDTO.setUuid(dto.getUuid());
                    eventManualDTO.setSourceId(sourceId);
                    eventManualDTO.setVideoUrl(videoUrl);
                    eventManualDTO.setSiteId(siteId);
                    eventManualDTO.setManualEvent(true);
                    eventManualDTO.setNote(note);
                    eventManualDTO.setSiteCorrect(siteCorrect);
                    if (endKm != null && endM != null) {
                        eventManualDTO.setEndKm(Long.valueOf(endKm));
                        eventManualDTO.setEndM(Long.valueOf(endM));
                        eventManualDTO.setEndProvinceId(endProvinceId);
                        eventManualDTO.setEndDistrictId(endDistrictId);
                        eventManualDTO.setEndWardId(endWardId);
                        eventManualDTO.setEndAddress(endAddress);
                    }
                    eventManualDTO.setUser(dto.getUuid());
                    Response responseITS = itsCoreEventService.saveManual(eventManualDTO);
                    response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
                }

            }

        }
        return response;
    }

    public ResponseMessage confirmEvent(Map<String, Object> body, String requestPath, Map<String, String> headerParam) throws ExecutionException, InterruptedException, JsonProcessingException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            //Check RBAC quyền xử lý vi phạm
            String id = (String) body.get("id");
            String startTime = (String) body.get("startTime");
            Integer status = (Integer) body.get("status");
            if (StringUtil.isNullOrEmpty(id) || StringUtil.isNullOrEmpty(startTime)) {
                response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "id hoặc startTime không thể trống", null));
            } else {
                Response responseStage = itsCoreEventService.getStageManual(id, startTime);
                if (responseStage == null) {
                    return new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "Không tìm thấy bản ghi", null));
                }
                String stage = (String) responseStage.getData();
                String listStages = dto.getUnit().getLisOfStage();
                List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                LOGGER.info("Check Abac");
                Map<String, Object> subject = new HashMap<>();
                subject.put("stageId", new ArrayList<String>(Arrays.asList(stage.split(","))));
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("stageId", myList);
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("subject", subject);
                bodyParam.put("attributes", attributes);
                ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "PUT", dto.getUuid(), requestPath);
                if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền cập nhật sự kiện", null));
                } else {
                    LocalDateTime time;
                    LocalDateTime endTime;
                    try {
                        time = DateUtils.parse(startTime);

                    } catch (Exception ex) {
                        return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endDate không đúng định dạng", null);
                    }
                    Response responseITS = itsCoreEventService.updateStatus(id, DateUtil.fromLocalDateTime(time), status, dto.getUuid());
                    if (responseITS.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value()) {
                        notifyChangeEvent(responseITS.getData());
                    }
                    EventExport eventExport = new EventExport();
                    eventExport.setEventId(id);
                    eventExport.setUuid(dto.getUuid());
                    ObjectMapper mapper = new ObjectMapper();
                    String msg = mapper.writeValueAsString(eventExport);
                    rabbitMQClient.callWorkerService(infoUpdate, msg);


                    response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
                }

            }

        }
        return response;
    }

    public ResponseMessage finishEvent(Map<String, String> headerParam, String requestPath, String id, String urlParam) throws ExecutionException, InterruptedException, JsonProcessingException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
            String startTime = params.get("startTime");
            Response responseITS = itsCoreEventService.getDetailEvent(id, startTime);
            if (responseITS.getData() != null) {
                ObjectMapper oMapper = new ObjectMapper();
                Map<String, Object> map = oMapper.convertValue(responseITS.getData(), Map.class);
                SiteInfo siteInfo = oMapper.convertValue(map.get("site"), new TypeReference<SiteInfo>() {
                });
                String stages = stageService.findBySite(siteInfo.getSiteId());
                String listStages = dto.getUnit().getLisOfStage();
                List<String> myList = new ArrayList<String>(Arrays.asList(listStages.split(",")));
                LOGGER.info("Check Abac");
                Map<String, Object> subject = new HashMap<>();
                subject.put("stage", stages);
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("stage", myList);
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("subject", subject);
                bodyParam.put("attributes", attributes);
                ABACResponseDTO resultCheckABAC = authorizeABAC(bodyParam, "DETAIL", dto.getUuid(), requestPath);
                if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền kết thúc công việc", null));
                } else {
                    Response finishJobResponse = itsCoreEventService.updateEventStatus(id, EventStatus.PROCESSED, dto.getUuid());
                    notifyFinishEvent(id, siteInfo.getSiteId(), dto.getUuid());
                    List<Job> listJobs = jobService.findByEventId(id);
                    for (Job job : listJobs) {
                        finishJob(job, dto);
                    }
                    response = new ResponseMessage(new MessageContent(finishJobResponse.getStatus(), finishJobResponse.getMessage(), finishJobResponse.getData()));
                }

                EventExport eventExport = new EventExport();
                eventExport.setEventId(id);
                eventExport.setUuid(dto.getUuid());
                ObjectMapper mapper = new ObjectMapper();
                String msg = mapper.writeValueAsString(eventExport);
                rabbitMQClient.callWorkerService(infoUpdate, msg);
            } else {
                response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
            }
        }

        return response;
    }

    public ResponseMessage getEventHistory(Map<String, String> headerParam, String requestPath, String pathParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem lịch sử sự kiện", null));
            } else {
                Response responseITS = itsCoreEventService.getHistory(headerParam, pathParam);
                response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
            }

        }

        return response;
    }

    public ResponseMessage getTrafficJamDetailHistory(Map<String, String> headerParam, String requestPath, String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem diễn biến sự kiện", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                Response responseITS = itsCoreEventService.getTrafficJamDetailHistory(params.get("fromDate"), params.get("toDate"), params.get("eventId"));
                response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
            }
        }
        return response;
    }

    public ResponseMessage getDetailTrafficData(Map<String, String> headerParam, String requestPath, String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem diễn biến sự kiện", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String startTime = params.get("startTime");
                String endTime = params.get("endTime");
                String siteId = params.get("siteId");
                String directionCode = params.get("directionCode");
                Response responseITS = trafficFlowService.getDetailTrafficBySite(startTime, endTime, siteId, directionCode);
                response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
            }
        }
        return response;
    }

    public ResponseMessage exportDayReport(String requestPath, String pathParam, Map<String, String> headerParam, String urlParam) throws Exception {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ReportDayExport eventExport = new ReportDayExport();
            Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
            String startTime = params.get("startTime");
            String endTime = params.get("endTime");
            eventExport.setStartTime(startTime);
            eventExport.setEndTime(endTime);
            eventExport.setUuid(dto.getUuid());
            ObjectMapper mapper = new ObjectMapper();
            String msg = mapper.writeValueAsString(eventExport);
            rabbitMQClient.callWorkerService(queueDayReport, msg);
            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "ok", null));
        }

        return response;
    }

    public ResponseMessage exportDailyReport(String requestPath, String pathParam, Map<String, String> headerParam, String urlParam) throws Exception {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        Long start = System.currentTimeMillis();
        Long end = System.currentTimeMillis();
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ReportDayExport eventExport = new ReportDayExport();
            Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
            String startTime = params.get("startDate");
            String endTime = params.get("endDate");
//            eventFileService.createReportEventDaily(startTime,endTime,"adsdss");
            eventExport.setStartTime(startTime);
            eventExport.setEndTime(endTime);
            eventExport.setUuid(dto.getUuid());
            ObjectMapper mapper = new ObjectMapper();
            String msg = mapper.writeValueAsString(eventExport);
            rabbitMQClient.callWorkerService(queueDailyReportEvent, msg);
            return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "ok", null));
        }

        return response;
    }

    public ResponseMessage createEventFile(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyMap) throws Exception {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tạo tài liệu sự kiện", null));
            } else {
                EventFileDTO eventFileDTO = createEventFile(bodyMap, dto);
                Response responseITS = itsCoreEventService.createEventFile(eventFileDTO);
                response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
            }
        }

        return response;
    }

    private EventFileDTO createEventFile(Map<String, Object> bodyMap, AuthorizationResponseDTO userDTO) {
        Date now = new Date();
        ObjectMapper mapper = new ObjectMapper();
        EventFileDTO eventFile = mapper.convertValue(bodyMap, new TypeReference<EventFileDTO>() {
        });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(simpleDateFormat);
        Date eventStartTime = mapper.convertValue(bodyMap.get("eventStartTime"), new TypeReference<Date>() {
        });
        EventFilePK eventFilePK = new EventFilePK(UUID.randomUUID().toString(), eventStartTime);
        eventFile.setEventFilePK(eventFilePK);
        eventFile.setCreateBy(userDTO.getUuid());
        eventFile.setCreateDate(now);
        eventFile.setUploadTime(now);
        return eventFile;
    }

    private void finishJob(Job job, AuthorizationResponseDTO dto) {
        job.setStatus(JobStatus.COMPLETE);
        addActionHistory(job, "FINISH", "Kết thúc sự kiện", null, null, dto);
        jobService.save(job);
        notifyJobAction("FINISH", "Kết thúc  công việc", job, dto);
    }

    public ResponseMessage findSiteByKmAndM(Map<String, String> headerParam, String requestPath, String requestMethod, String urlParam, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(new MessageContent(siteService.getSiteByPositionM(urlParam)));
        }
        return response;
    }

    public ResponseMessage getPoint(Map<String, String> headerParam, String requestPath, String requestMethod, String urlParam, String pathParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            response = new ResponseMessage(new MessageContent(siteService.getPointByKmAndM(urlParam)));
        }
        return response;
    }

    public ResponseMessage updateReportStatusEvent(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyMap) throws Exception {
        AuthorizationResponseDTO dto = authenToken(headerParam);
        ResponseMessage response = null;
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền sửa sự kiện", null));
            } else {
                String eventId = (String) bodyMap.get("eventId");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String startTime = (String) bodyMap.get("startTime");
                Boolean reportStatus = Boolean.valueOf(bodyMap.get("reportStatus").toString());
                String modifiedBy = dto.getUuid();
                ReportStatusEvent reportStatusEvent = new ReportStatusEvent(eventId, startTime, reportStatus,modifiedBy);
                Response responseITS = itsCoreEventService.updateReportStatusEvent(reportStatusEvent);
                response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
            }
        }

        return response;
    }

}
