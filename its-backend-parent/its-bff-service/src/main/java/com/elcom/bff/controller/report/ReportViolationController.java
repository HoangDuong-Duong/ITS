/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.bff.controller.report;

import com.elcom.bff.contants.ResourcePath;
import com.elcom.bff.controller.BaseController;
import com.elcom.bff.dto.Response;
import com.elcom.bff.service.ViolationReportService;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping(ResourcePath.ManagementReport)
public class ReportViolationController extends BaseController {

    @Autowired
    private ViolationReportService violationReportService;

    @GetMapping("/event/list-violation")
    public ResponseEntity<Response> findViolationReport(@RequestHeader Map<String, String> headerParam,
            @RequestParam Map<String, String> reqParam) {
        ResponseMessage responseMessage = authenToken(headerParam);
        if (responseMessage == null) {
            return new ResponseEntity<>(new Response(HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase(), null), HttpStatus.UNAUTHORIZED);
        } else if (responseMessage.getData().getStatus() != HttpStatus.OK.value()) {
            return new ResponseEntity<>(new Response(responseMessage.getData().getStatus(),
                    responseMessage.getData().getMessage(), null), HttpStatus.UNAUTHORIZED);
        } else {
            String urlParam = StringUtil.generateMapString(reqParam);
            // Gọi lấy dữ liệu từ Report
            Response reportServiceResponse = violationReportService.getViolationList(urlParam, headerParam);
            if (reportServiceResponse.getStatus() != HttpStatus.OK.value()) {
                return new ResponseEntity<>(reportServiceResponse, HttpStatus.OK);
            }
            return new ResponseEntity<>(new Response(HttpStatus.OK.value(), HttpStatus.OK.name(),
                    reportServiceResponse.getData(), reportServiceResponse.getTotal()), HttpStatus.OK);
        }
    }
}
