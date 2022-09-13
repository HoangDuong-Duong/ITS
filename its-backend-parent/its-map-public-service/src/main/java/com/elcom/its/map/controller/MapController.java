/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.map.controller;

import com.elcom.its.map.dto.AuthorizationResponseDTO;
import com.elcom.its.map.dto.ABACResponseDTO;
import com.elcom.its.map.dto.Response;
import com.elcom.its.map.service.MapService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class MapController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapController.class);

    @Autowired
    private MapService mapService;

    public ResponseMessage getAllSystemDevices(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response = new ResponseMessage(new MessageContent(mapService.getAllSystemDevicesForMap().getData()));
        return response;
    }

    public ResponseMessage getStageForMap(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response = new ResponseMessage(new MessageContent(mapService.getStageForMap().getData()));
        return response;
    }

    public ResponseMessage getPageEventInStages(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response = new ResponseMessage(new MessageContent(mapService.getPageEventInStages(urlParam).getData()));
        return response;
    }

    public ResponseMessage getObjectTrackingHistoryInStagesForMap(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response = new ResponseMessage(new MessageContent(mapService.getObjectTrackingHistoryInStagesForMap(urlParam).getData()));

        return response;
    }

    public ResponseMessage getTrafficFlowInStageForMap(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response = new ResponseMessage(new MessageContent(mapService.getTrafficFlowInStageForMap(urlParam).getData()));

        return response;
    }

    public ResponseMessage getSystemDevicesInStagesForMap(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response = new ResponseMessage(new MessageContent(mapService.getSystemDevicesInStagesForMap(urlParam).getData()));

        return response;
    }

    public ResponseMessage getMapBetweenSite(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response = new ResponseMessage(new MessageContent(mapService.getMapBetweenSite(urlParam).getData()));
        return response;
    }

    public ResponseMessage getReportEventByTypeInStage(String requestUrl, String method, String urlParam, Map<String, String> headerMap)
            throws ExecutionException, InterruptedException {
        ResponseMessage response;
        Response getReportResponse = mapService.getReportEventByTypeInStage(urlParam);
        response = new ResponseMessage(new MessageContent(getReportResponse.getStatus(),
                getReportResponse.getMessage(), getReportResponse.getData()));

        return response;
    }

    public ResponseMessage getMidPointInStages(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response = new ResponseMessage(new MessageContent(mapService.getMidPointInStages().getData()));
        return response;
    }

}
