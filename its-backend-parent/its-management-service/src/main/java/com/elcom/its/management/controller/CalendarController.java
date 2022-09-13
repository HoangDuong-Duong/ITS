package com.elcom.its.management.controller;

import com.elcom.its.management.dto.*;
import com.elcom.its.management.model.EventFile;
import com.elcom.its.management.service.CalendarService;
import com.elcom.its.management.service.EventFileService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.DateUtil;
import com.elcom.its.utils.DateUtils;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller
public class CalendarController extends BaseController{

    @Autowired
    private CalendarService calendarService;

    @Autowired
    EventFileService eventFileService;



    public ResponseMessage getMyJob(Map<String, String> headerParam, String requestPath, String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách công việc của tôi",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách công việc của tôi", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String key = params.get("key");
                Response responseITS;
                LocalDateTime time;
                LocalDateTime endTimeS = LocalDateTime.now();
                LocalDateTime expireStart;
                LocalDateTime expireEnd ;
                try {

                    expireStart = DateUtils.parse(params.get("expireStart"));
                    expireEnd = DateUtils.parse(params.get("expireEnd"));
                    time = DateUtils.parse(params.get("startTime"));
                    if(params.get("endTime")!= null)
                        endTimeS = DateUtils.parse(params.get("endTime"));

                } catch (Exception ex) {
                    return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endTime không đúng định dạng", null);
                }
                int status = !params.get("status").isEmpty()  ? Integer.parseInt(params.get("status")) : -1;
                int priority = !params.get("priority").isEmpty()  ? Integer.parseInt(params.get("priority")) : -1;
                List<String> jobType = StringUtil.isNullOrEmpty(params.get("jobType")) ? null: new ArrayList<String>(Arrays.asList(params.get("jobType").split(",")));
                List<String> siteIds = StringUtil.isNullOrEmpty(params.get("siteIds")) ? null:  new ArrayList<String>(Arrays.asList(params.get("siteIds").split(",")));
                List<EventJobDTO> result =  calendarService.getMyJob(DateUtil.fromLocalDateTime(time),DateUtil.fromLocalDateTime(endTimeS),jobType,priority,status,siteIds,params.get("eventCode"), dto.getUnit().getUuid(),dto.getUuid(), DateUtil.fromLocalDateTime(expireStart),DateUtil.fromLocalDateTime(expireEnd),key);
                response = new ResponseMessage(new MessageContent(result));
            }

        }

        return response;
    }
    public ResponseMessage getJobForReport(Map<String, String> headerParam, String requestPath, String urlParam) throws ExecutionException, InterruptedException {
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
                String key = params.get("key");
                LocalDateTime time;
                LocalDateTime endTimeS = LocalDateTime.now();
                try {
                    time = DateUtils.parse(params.get("startTime"));
                    if(params.get("endTime")!= null)
                        endTimeS = DateUtils.parse(params.get("endTime"));

                } catch (Exception ex) {
                    return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endTime không đúng định dạng", null);
                }
                List<EventJobDTO> result =  calendarService.getJobForReport(DateUtil.fromLocalDateTime(time),DateUtil.fromLocalDateTime(endTimeS),params.get("eventCode"),key);
                response = new ResponseMessage(new MessageContent(result));
            }

        }

        return response;
    }
}

