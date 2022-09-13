/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.map.controller;

import com.elcom.its.map.dto.Response;
import com.elcom.its.map.service.LaneRouteService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class LaneRouteController extends BaseController {

    @Autowired
    private LaneRouteService laneRouteService;

    public ResponseMessage getAllLane(Map<String, String> headerParam, String requestPath,
            String method, String urlParam) {
        Response getLaneResponse = laneRouteService.getAllLane(urlParam);
        ResponseMessage response = new ResponseMessage(new MessageContent(getLaneResponse.getStatus(), getLaneResponse.getMessage(), getLaneResponse.getData()));
        return response;
    }

}
