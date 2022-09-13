/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.messaging.rabbitmq;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.vds.controller.VdsReportController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.elcom.its.vds.controller.VdsController;
import com.elcom.its.vds.controller.VdsEventController;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author Admin
 */
public class RpcServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    @Autowired
    private VdsController vdsController;

    @Autowired
    private VdsReportController vdsReportController;
    
    @Autowired
    private VdsEventController vdsEventController;

    @RabbitListener(queues = "${vds.rpc.queue}")
    public String processService(String json) {
        try {
            LOGGER.info(" [-->] Server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            RequestMessage request = mapper.readValue(json, RequestMessage.class);

            //Process here
            ResponseMessage response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            if (request != null) {
                String requestPath = request.getRequestPath().replace(request.getVersion() != null
                        ? request.getVersion() : ResourcePath.VERSION, "");
                String urlParam = URLDecoder.decode(request.getUrlParam(), StandardCharsets.UTF_8.name());
                String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();

                switch (request.getRequestMethod()) {
                    case "GET":
                        if ("/vds".equalsIgnoreCase(requestPath)) {
                            if (StringUtils.isNotBlank(pathParam)) {
                                response = vdsController.detailVds(requestPath, headerParam, bodyParam, requestPath, pathParam);
                            } else {
                                response = vdsController.findVds(requestPath, headerParam, bodyParam, request.getRequestMethod(), urlParam);
                            }
                        } else if ("/vds/cameraLayOut".equalsIgnoreCase(requestPath)) {
                            response = vdsController.getCameraLayOutById(requestPath, headerParam, bodyParam, requestPath, pathParam, urlParam);
                        }else if ("/vds/layoutAreas".equalsIgnoreCase(requestPath)) {
                            response = vdsController.getLayoutAreasByLayoutId(requestPath, headerParam, bodyParam, requestPath, pathParam, urlParam);
                        }
                        else if ("/vds/camera".equalsIgnoreCase(requestPath)) {
                            response = vdsController.getVdsByCamera(requestPath, headerParam, bodyParam, requestPath, pathParam, urlParam);
                        } else if ("/vds/event".equalsIgnoreCase(requestPath)) {
                            response = vdsController.getListEventByVds(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam, urlParam);
                        } else if ("/vds/report/event".equalsIgnoreCase(requestPath)) {
                            response = vdsController.getAggEventTypeByVds(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam, urlParam);
                        } else if ("/vds/report/traffic-flow".equalsIgnoreCase(requestPath)) {
                            response = vdsController.getTrafficflowDataByVds(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam, urlParam);
                        } else if ("/vds/traffic-flow".equalsIgnoreCase(requestPath)) {
                            response = vdsController.getTrafficFlowDataBySite(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam, urlParam);
                        }else if ("/vds/traffic-flow/report".equalsIgnoreCase(requestPath)) {
                            response = vdsController.getTrafficFlowDataBySiteReport(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam, urlParam);
                        } else if ("/vds/dashboard/event".equalsIgnoreCase(requestPath)) {
                            response = vdsReportController.getEventReport(
                                    headerParam,
                                    requestPath,
                                    request.getRequestMethod(),
                                    urlParam,
                                    bodyParam);
                        } else if ("/vds/dashboard/event/chart".equalsIgnoreCase(requestPath)) {
                            response = vdsReportController.getEventReportByChart(
                                    headerParam,
                                    requestPath,
                                    request.getRequestMethod(),
                                    urlParam,
                                    bodyParam);
                        } else if ("/vds/dashboard/event/site".equalsIgnoreCase(requestPath)) {
                            response = vdsReportController.getEventReportBySite(
                                    headerParam,
                                    requestPath,
                                    request.getRequestMethod(),
                                    urlParam,
                                    bodyParam);
                        } else if ("/vds/dashboard/traffic-flow".equalsIgnoreCase(requestPath)) {
                            response = vdsReportController.getTrafficFlowReport(
                                    headerParam,
                                    requestPath,
                                    request.getRequestMethod(),
                                    urlParam,
                                    bodyParam);
                        } else if ("/vds/dashboard/traffic-flow/chart".equalsIgnoreCase(requestPath)) {
                            response = vdsReportController.getTrafficFlowReportByChart(
                                    headerParam,
                                    requestPath,
                                    request.getRequestMethod(),
                                    urlParam,
                                    bodyParam);
                        } else if ("/vds/dashboard/traffic-flow/site".equalsIgnoreCase(requestPath)) {
                            response = vdsReportController.getTrafficFlowReportBySite(
                                    headerParam,
                                    requestPath,
                                    request.getRequestMethod(),
                                    urlParam,
                                    bodyParam);
                        } else if("/vds/event/center".equalsIgnoreCase(requestPath)){
                            response = vdsEventController.findVdsEvent(requestPath, headerParam, bodyParam, request.getRequestMethod(), urlParam);
                        } else if("/vds/site".equalsIgnoreCase(requestPath)){
                            response = vdsController.findSite(requestPath, headerParam, bodyParam, request.getRequestMethod(), urlParam);
                        }
                        break;
                    case "POST":
                        if ("/vds".equalsIgnoreCase(requestPath)) {
                            response = vdsController.createVds(requestPath, headerParam, bodyParam, request.getRequestMethod());
                        }
                        if ("/vds/download-file".equalsIgnoreCase(requestPath)) {
                            response = vdsEventController.downloadZipImageAndVideoViolation(requestPath, bodyParam, urlParam,headerParam);
                        }
                        break;
                    case "PUT":
                        if ("/vds".equalsIgnoreCase(requestPath)) {
                            response = vdsController.updateVds(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        } else if ("/vds/status".equalsIgnoreCase(requestPath)) {
                            response = vdsController.updateVdsStatus(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        } else if("/vds/video-threads".equalsIgnoreCase(requestPath)){
                            response = vdsController.updateVdsVideoThreads(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        } else if("/vds/camera-status/internal".equalsIgnoreCase(requestPath)){
                            response = vdsController.updateCameraStatus(bodyParam);
                        } else if ("/vds/status/active".equalsIgnoreCase(requestPath)) {
                            response = vdsController.updateActiveVds(requestPath, headerParam, bodyParam, request.getRequestMethod());
                        } else if ("/vds/status/inactive".equalsIgnoreCase(requestPath)) {
                            response = vdsController.updateInactiveVds(requestPath, headerParam, bodyParam, request.getRequestMethod());
                        } else if ("/vds/render/active".equalsIgnoreCase(requestPath)) {
                            response = vdsController.updateActiveRenderVds(requestPath, headerParam, bodyParam, request.getRequestMethod());
                        } else if ("/vds/render/inactive".equalsIgnoreCase(requestPath)) {
                            response = vdsController.updateInactiveRenderVds(requestPath, headerParam, bodyParam, request.getRequestMethod());
                        }
                        break;
                    case "DELETE":
                        if ("/vds".equalsIgnoreCase(requestPath)) {
                            if (StringUtils.isNotBlank(pathParam)) {
                                response = vdsController.deleteVds(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                            } else {
                                response = vdsController.deleteMultiVds(requestPath, headerParam, bodyParam, request.getRequestMethod());
                            }
                        }
                        break;
                    default:
                        break;
                }
                LOGGER.info(" [<--] Server returned {}", (response != null ? response.toJsonString() : "null"));
            }
            return response != null ? response.toJsonString() : null;
        } catch (Exception ex) {
            LOGGER.error("Error to processService >>> {}", ExceptionUtils.getStackTrace(ex));
            ex.printStackTrace();
        }
        return null;
    }
}
