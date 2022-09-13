package com.elcom.its.report.messaging.rabbitmq;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.report.controller.EventReportController;
import com.elcom.its.report.controller.ObjectTrackingController;
import com.elcom.its.report.controller.TrafficFlowReportController;
import com.elcom.its.report.controller.TrafficJamHistoryController;
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
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author ducduongn
 */
public class RpcServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    @Autowired
    private TrafficFlowReportController trafficFlowReportController;

    @Autowired
    private TrafficJamHistoryController trafficJamHistoryController;

    @Autowired
    private EventReportController eventReportController;
    
    @Autowired
    private ObjectTrackingController objectTrackingController;

    @RabbitListener(queues = "${report.rpc.queue}")
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
                        if ("/report/traffic-flow".equalsIgnoreCase(requestPath)) {
                            response = trafficFlowReportController.getTrafficFlowReport(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        if ("/report/event/day".equalsIgnoreCase(requestPath)) {
                            response = eventReportController.getEventForReport(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/report/traffic-flow/site".equalsIgnoreCase(requestPath)) {
                            response = trafficFlowReportController.getTrafficFlowReportBySite(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/report/traffic-flow/chart".equalsIgnoreCase(requestPath)) {
                            response = trafficFlowReportController.getTrafficFlowReportByChart(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/report/event".equalsIgnoreCase(requestPath)) {
                            response = eventReportController.getEventReport(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/report/event/site".equalsIgnoreCase(requestPath)) {
                            response = eventReportController.getEventReportBySite(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/report/event/chart".equalsIgnoreCase(requestPath)) {
                            response = eventReportController.getEventReportByChart(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/report/traffic-jam".equalsIgnoreCase(requestPath)) {
                            response = trafficJamHistoryController.getTrafficFlowReport(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam, urlParam);
                        }
                        else if ("/report/timeLine/traffic-jam".equalsIgnoreCase(requestPath)) {
                            response = trafficJamHistoryController.getTrafficTimeLine(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam, urlParam);
                        }
                        else if ("/report/sort/traffic-jam".equalsIgnoreCase(requestPath)) {
                            response = trafficJamHistoryController.getTrafficSort(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam, urlParam);
                        } else if ("/report/traffic-jam-monitoring".equalsIgnoreCase(requestPath)) {
                            response = trafficJamHistoryController.getTrafficJamMonitoringData(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam, urlParam);
                        }
                        else if ("/report/traffic-jam-monitoring-all".equalsIgnoreCase(requestPath)) {
                            response = trafficJamHistoryController.getTrafficJamMonitoring(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam, urlParam);
                        }
                        else if ("/report/traffic-flow/list".equalsIgnoreCase(requestPath)) {
                            response = trafficFlowReportController.getTrafficFlowReportList(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        } else if ("/report/traffic-flow/fan-chart".equalsIgnoreCase(requestPath)) {
                            response = trafficFlowReportController.getTrafficFlowReportFanChart(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        } else if ("/report/traffic-flow/line-chart".equalsIgnoreCase(requestPath)) {
                            response = trafficFlowReportController.getTrafficFlowReportLineChart(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        } else   if ("/report/event/list".equalsIgnoreCase(requestPath)) {
                            response = eventReportController.getEventList(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        } else if ("/report/event/fan-chart".equalsIgnoreCase(requestPath)) {
                            response = eventReportController.getEventFanChart(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        } else if ("/report/event/line-chart".equalsIgnoreCase(requestPath)) {
                            response = eventReportController.getEventLineChart(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        } else if ("/report/object-tracking/processed".equalsIgnoreCase(requestPath)) {
                            response = objectTrackingController.getObjectTrackingProcessed(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        } else if ("/report/event/list-violation".equalsIgnoreCase(requestPath)) {
                            response = eventReportController.getEventViolation(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        } else if ("/report/event/list-violation/export".equalsIgnoreCase(requestPath)) {
                            response = eventReportController.exportViolation(requestPath, urlParam, headerParam);
                        }
                        else if ("/report/event/trouble".equalsIgnoreCase(requestPath)) {
                            response = eventReportController.getAggTroubleEventReport(headerParam, requestPath, request.getRequestMethod(), urlParam);
                        }else if ("/report/event/trouble/export".equalsIgnoreCase(requestPath)) {
                            response = eventReportController.exportAggTroubleEventReport(headerParam, requestPath, request.getRequestMethod(), urlParam);
                        }
                        break;
                    default:
                        break;
                }
            }
            LOGGER.info(" [<--] Server returned " + response != null ? response.toJsonString() : "null");
            return response != null ? response.toJsonString() : null;
        } catch (Exception ex) {
            LOGGER.error("Error to processService >>> {}", ExceptionUtils.getStackTrace(ex));
            ex.printStackTrace();
        }
        return null;
    }
}
