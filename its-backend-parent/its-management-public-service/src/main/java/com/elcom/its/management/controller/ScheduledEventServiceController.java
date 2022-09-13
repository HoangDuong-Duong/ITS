/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elcom.its.management.controller;

import com.elcom.its.management.dto.ABACResponseDTO;
import com.elcom.its.management.dto.AuthorizationResponseDTO;
import com.elcom.its.management.enums.JobStatus;
import com.elcom.its.management.enums.ScheduledEventStatus;
import com.elcom.its.management.model.Job;
import com.elcom.its.management.model.ScheduledEvent;
import com.elcom.its.management.service.ScheduledEventService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Acer
 */
@Controller
public class ScheduledEventServiceController extends BaseController {
    
    @Autowired
    private ScheduledEventService scheduledEventService;
    
    public ResponseMessage getScheduledEventForUser(Map<String, String> headerParam, String requestPath, String urlParam) throws ExecutionException, InterruptedException, ParseException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách lịch làm việc",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách lịch làm việc", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String startTime = params.get("startTime");
                String endTime = params.get("endTime");
                String keyword = params.get("keyword") != null ? params.get("keyword") : "";
                List<ScheduledEvent> listScheduledEvents;
                if (resultCheckABAC.getAdmin() == null || !resultCheckABAC.getAdmin()) {
                    listScheduledEvents = scheduledEventService.getListScheduledEventForUser(formatter.parse(startTime), formatter.parse(endTime), dto.getUnit().getUuid(), dto.getUuid(), keyword);
                } else {
                    listScheduledEvents = scheduledEventService.getScheduledEventByTime(formatter.parse(startTime), formatter.parse(endTime), keyword);
                }
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(),
                        listScheduledEvents));
            }
        }
        return response;
    }
    
    public ResponseMessage getJobByScheduledEvent(Map<String, String> headerParam, String requestPath, String pathParam)
            throws ExecutionException, InterruptedException, ParseException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem chi tiết lịch làm việc",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem chi tiết lịch làm việc", null));
            } else {
                ScheduledEvent scheduledEvent = scheduledEventService.findById(pathParam);
                if (scheduledEvent == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy sự kiện",
                            null));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(),
                            scheduledEvent.getJob()));
                    
                }
                
            }
        }
        return response;
    }
    
    public ResponseMessage createScheduledEvent(Map<String, String> headerParam, String requestPath,
            Map<String, Object> bodyMap) throws ExecutionException, InterruptedException, ParseException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tạo lịch làm việc",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền tạo lịch làm việc", null));
            } else {
                ScheduledEvent scheduledEvent = createdEventRecordFromBodyMap(bodyMap, null, dto);
                scheduledEvent.setCreatedBy(dto.getUuid());
                scheduledEvent.setCreatedDate(new Date());
                scheduledEventService.save(scheduledEvent);
                notifyScheduledEvent("CREATED", "Sự kiện được tạo", scheduledEvent);
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), scheduledEvent));
            }
        }
        return response;
    }
    
    public ResponseMessage updateScheduledEvent(Map<String, String> headerParam, String requestPath,
            String pathParam, Map<String, Object> bodyMap) throws ExecutionException, InterruptedException, ParseException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "PUT", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền chỉnh sửa lịch làm việc",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền chỉnh sửa lịch làm việc", null));
            } else {
                
                ScheduledEvent scheduledEventById = scheduledEventService.findById(pathParam);
                if (scheduledEventById == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy sự kiện",
                            null));
                } else {
                    ScheduledEvent scheduledEvent = createdEventRecordFromBodyMap(bodyMap, pathParam, dto);
                    scheduledEvent.setCreatedBy(scheduledEventById.getCreatedBy());
                    scheduledEvent.setCreatedDate(scheduledEventById.getCreatedDate());
                    scheduledEventService.save(scheduledEvent);
                    notifyScheduledEvent("UPDATED", "Sự kiện được cập nhật", scheduledEvent);
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), scheduledEvent));
                }
            }
        }
        return response;
    }
    
    public ResponseMessage deleteScheduledEvent(Map<String, String> headerParam, String requestPath,
            String pathParam) throws ExecutionException, InterruptedException, ParseException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DELETE", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa lịch làm việc",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xóa lịch làm việc", null));
            } else {
                
                ScheduledEvent scheduledEventById = scheduledEventService.findById(pathParam);
                if (scheduledEventById == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy sự kiện",
                            null));
                } else {
                    boolean deleteStatus = scheduledEventService.deleteScheduledEvent(scheduledEventById);
                    if (deleteStatus) {
                        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(),
                                null));
                    } else {
                        response = new ResponseMessage(new MessageContent(HttpStatus.CONFLICT.value(), "Có lỗi xảy ra",
                                null));
                    }
                }
            }
        }
        return response;
    }
    
    public ResponseMessage findScheduledEventById(Map<String, String> headerParam, String requestPath,
            String pathParam) throws ExecutionException, InterruptedException, ParseException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem chi tiết lịch làm việc",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem chi tiết lịch làm việc", null));
            } else {
                
                ScheduledEvent scheduledEventById = scheduledEventService.findById(pathParam);
                if (scheduledEventById == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Không tìm thấy sự kiện",
                            null));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(),
                            scheduledEventById));
                }
            }
        }
        return response;
    }
    
    private ScheduledEvent createdEventRecordFromBodyMap(Map<String, Object> bodyMap, String eventId, AuthorizationResponseDTO dto) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(formatter);
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        String title = (String) bodyMap.get("title");
        String eventType = (String) bodyMap.get("eventType");
        String siteId = (String) bodyMap.get("siteId");
        String siteName = (String) bodyMap.get("siteName");
        String description = (String) bodyMap.get("description");
        String directionCode = (String) bodyMap.get("directionCode");
        Job job = mapper.convertValue(bodyMap.get("job"), new TypeReference<Job>() {
        });
        if (job.getId() == null) {
            job.setId(UUID.randomUUID().toString());
        }
        Date startTime = formatter.parse((String) bodyMap.get("startTime"));
        Date endTime = formatter.parse((String) bodyMap.get("endTime"));
        if (eventId == null) {
            eventId = UUID.randomUUID().toString();
        }
        job.setCreatedBy(dto.getUuid());
        job.setCreatedDate(new Date());
        job.setStatus(JobStatus.WAIT);
        addActionHistory(job, "CREATE", "Tạo công việc", null, null, dto);
        if (job.getStartTime() == null) {
            job.setStartTime(startTime);
        }
        ScheduledEvent scheduledEvent = new ScheduledEvent();
        scheduledEvent.setTitle(title);
        scheduledEvent.setDescription(description);
        scheduledEvent.setEndTime(endTime);
        scheduledEvent.setEventType(eventType);
        scheduledEvent.setJob(job);
        scheduledEvent.setId(eventId);
        scheduledEvent.setStartTime(startTime);
        scheduledEvent.setSiteId(siteId);
        scheduledEvent.setSiteName(siteName);
        if (!StringUtil.isNullOrEmpty(job.getUserIds())) {
            scheduledEvent.setUsers(job.getUserIds());
        }
        scheduledEvent.setGroupId(job.getGroupId());
        scheduledEvent.setDirectionCode(directionCode);
        scheduledEvent.setStatus(ScheduledEventStatus.WAIT);
        
        return scheduledEvent;
        
    }
    
    public ResponseMessage getAggScheduledEvent(String urlParam)
            throws ExecutionException, InterruptedException, ParseException {
        Map<String, String> urlMap = StringUtil.getUrlParamValues(urlParam);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date shiftStartTime = dateFormat.parse(urlMap.get("shiftStartTime"));
        Date shiftEndTime = dateFormat.parse(urlMap.get("shiftEndTime"));
        Date nextShiftStartTime = dateFormat.parse(urlMap.get("nextShiftStartTime"));
        Date nextShiftEndTime = dateFormat.parse(urlMap.get("nextShiftEndTime"));
        String userId = urlMap.get("userId");
        ResponseMessage response = new ResponseMessage(new MessageContent(scheduledEventService.getAggScheduledEvent(userId, shiftStartTime, shiftEndTime, nextShiftStartTime, nextShiftEndTime)));
        return response;
    }
}
