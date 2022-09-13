package com.elcom.its.shift.controller;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.shift.dto.AuthorizationResponseDTO;
import com.elcom.its.shift.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.shift.service.DailyLoginReportService;
import com.elcom.its.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class DailyLoginController extends BaseController {
    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private DailyLoginReportService dailyLoginReportService;

    public ResponseMessage getAllShift(String requestUrl, String method, Map<String, String> headerMap, String urlParam)
            throws ExecutionException, InterruptedException, ParseException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> urlMap = StringUtil.getUrlParamValues(urlParam);
            String startTime = urlMap.get("startTime");
            String endTime = urlMap.get("endTime");
            response = new ResponseMessage(new MessageContent(dailyLoginReportService.getDataReport(df.parse(startTime), df.parse(endTime))));
        }
        return response;
    }

    public ResponseMessage getAllShiftInternal(String requestUrl, String method, Map<String, String> headerMap, String urlParam)
            throws ExecutionException, InterruptedException, ParseException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> urlMap = StringUtil.getUrlParamValues(urlParam);
            String startTime = urlMap.get("startTime");
            String endTime = urlMap.get("endTime");
            response = new ResponseMessage(new MessageContent(dailyLoginReportService.getDataReport(df.parse(startTime), df.parse(endTime))));
        return response;
    }
}
