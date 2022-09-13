package com.elcom.its.management.controller;

import com.elcom.its.management.dto.*;
import com.elcom.its.management.service.*;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.*;
import java.util.concurrent.ExecutionException;


@Controller
public class EventController extends BaseController {

    static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService itsCoreEventService;

    public ResponseMessage getEventMap(Map<String, String> headerParam, String requestPath, String urlParam) throws ExecutionException, InterruptedException {
        Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
        Response responseITS = itsCoreEventService.getEventMap(params.get("fromDate"), params.get("toDate"), "", true);
        ResponseMessage response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
        return response;
    }

    public ResponseMessage getDetail(Map<String, String> headerParam, String requestPath, String id, String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
        String startTime = params.get("startTime");
        Response responseITS = itsCoreEventService.getDetailEvent(id, startTime);
        response = new ResponseMessage(new MessageContent(responseITS.getStatus(), responseITS.getMessage(), responseITS.getData()));
        return response;
    }

}
