/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.messaging.rabbitmq;

import com.elcom.its.management.controller.*;
import com.elcom.its.management.service.EventInfoService;
import com.elcom.its.management.service.ReportJobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * @author Admin
 */
public class RpcServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    @Autowired
    private JobController jobController;

    @Autowired
    private EventController eventController;

    @Autowired
    private RecognitionController recognitionController;

    @Autowired
    private ReportController reportController;
    
    @Autowired
    private ScheduledEventServiceController scheduledEventServiceController;

    @Autowired
    private HistoryStatusDeviceController historyStatusDeviceController;

    @Autowired
    private CalendarController calendarController;

    @Autowired
    private EventInfoController eventInfoController;

    @Autowired
    private JobBackupController jobBackupController;
    @Autowired
    private HotlineController hotlineController;
    @Autowired
    private ReportDeviceController reportDeviceController;




    @RabbitListener(queues = "${management.rpc.queue}")
    public String processService(String json) {
        long start = System.currentTimeMillis();
        try {
            LOGGER.info(" [-->] Server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            RequestMessage request = mapper.readValue(json, RequestMessage.class);
            ResponseMessage response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            if (request != null) {
                String requestPath = request.getRequestPath().replace(request.getVersion() != null
                        ? request.getVersion() : ResourcePath.VERSION, "");
                String urlParam = request.getUrlParam();
                String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();
                switch (request.getRequestMethod()) {
                    case "GET":
                        if ("/management/common/job".equalsIgnoreCase(requestPath)) {
                            if(StringUtil.isNullOrEmpty(pathParam)){
                            response = jobController.getJobByEvent(headerParam, request.getRequestPath(), pathParam, request.getRequestMethod(), urlParam);
                            }
                            else{
                                response = jobController.findJobById(headerParam, request.getRequestPath(), pathParam);
                            }
                        } else if ("/management/center/job/finish".equalsIgnoreCase(requestPath)) {
                            response = jobController.finishJob(headerParam, request.getRequestPath(), pathParam, request.getRequestMethod());
                        } else if ("/management/center/job/cancel".equalsIgnoreCase(requestPath)) {
                            response = jobController.cancelJob(headerParam, request.getRequestPath(), pathParam, request.getRequestMethod());
                        } else if ("/management/common/job/process-result".equalsIgnoreCase(requestPath)) {
                            response = jobController.getProcessResult(headerParam, request.getRequestPath(), pathParam, request.getRequestMethod());
                        } else if ("/management/center/event".equalsIgnoreCase(requestPath)) {
                            response = eventController.findEvent(request.getRequestPath(), request.getRequestMethod(),urlParam, headerParam);
                        } else if ("/management/base/event".equalsIgnoreCase(requestPath)) {
                            response = eventController.findEventCommon(request.getRequestPath(),urlParam, headerParam);
                        } else if ("/management/common/event/detail".equalsIgnoreCase(requestPath)) {
                            response = eventController.getDetail(headerParam, request.getRequestPath(), pathParam, urlParam);
                        } else if ("/management/common/event/file".equalsIgnoreCase(requestPath)) {
                            response = eventController.getAllFileEvent(headerParam, request.getRequestPath(), request.getRequestMethod(), pathParam);
                        } else if ("/management/common/event/history".equalsIgnoreCase(requestPath)) {
                            response = eventController.findHistoryEvent(urlParam, request.getRequestPath(), request.getRequestMethod(), pathParam);
                        } else if ("/management/base/jobs".equalsIgnoreCase(requestPath)) {
                            response = jobController.getJobsForUser(headerParam, request.getRequestPath(), urlParam, request.getRequestMethod());
                        } else if ("/management/recognition".equalsIgnoreCase(requestPath)) {
                            response = recognitionController.getDetail(headerParam,request.getRequestPath(),pathParam);
                        } else if ("/management/report/by-cam".equalsIgnoreCase(requestPath)) {
                            response = reportController.getReportTraffics(headerParam, request.getRequestPath(), request.getRequestMethod(), urlParam, bodyParam);
                        } else if ("/management/report/site-traffic-status-history".equalsIgnoreCase(requestPath)) {
                            response = reportController.getSiteTrafficStatusHistory(headerParam, request.getRequestPath(), request.getRequestMethod(), urlParam, bodyParam);
                        } else if ("/management/object-tracking-history/recently".equalsIgnoreCase(requestPath)) {
                            response = reportController.getObjectTrackingHistory(headerParam, request.getRequestPath(), request.getRequestMethod(), urlParam, bodyParam);
                        } else if ("/management/center/event/finish".equalsIgnoreCase(requestPath)) {
                            response = eventController.finishEvent(headerParam, request.getRequestPath(), pathParam, urlParam);
                        }  else if ("/management/center/event/map".equalsIgnoreCase(requestPath)) {
                            response = eventController.getEventMap(headerParam,request.getRequestPath(), urlParam);
                        }
                         else if ("/management/center/event/straight-map".equalsIgnoreCase(requestPath)) {
                            response = eventController.getEventStraightMap(headerParam,request.getRequestPath(), urlParam);
                        }
                        else if ("/management/common/job/map-notification".equalsIgnoreCase(requestPath)) {
                            response = jobController.getListJobNotifyInMap(headerParam, request.getRequestPath(), pathParam, request.getRequestMethod());
                        }else if ("/management/common/job/straight-map".equalsIgnoreCase(requestPath)) {
                            response = jobController.getListJobInStraightMap(headerParam, request.getRequestPath(), pathParam, request.getRequestMethod());
                        } 
                        else if ("/management/scheduled-event".equalsIgnoreCase(requestPath)) {
                            if(StringUtil.isNullOrEmpty(pathParam)){
                            response = scheduledEventServiceController.getScheduledEventForUser(headerParam,  request.getRequestPath(), urlParam);
                            }
                            else{
                                response = scheduledEventServiceController.findScheduledEventById(headerParam,  request.getRequestPath(), pathParam);
                            }
                            
                        } else if ("/management/scheduled-event/job".equalsIgnoreCase(requestPath)) {
                            response = scheduledEventServiceController.getJobByScheduledEvent(headerParam,  request.getRequestPath(), pathParam);
                        } else if ("/management/base/event/calender".equalsIgnoreCase(requestPath)) {
                            response = calendarController.getMyJob(headerParam,request.getRequestPath(),urlParam);
                        } else if ("/management/base/event/report".equalsIgnoreCase(requestPath)) {
                            response = calendarController.getJobForReport(headerParam,request.getRequestPath(),urlParam);
                        }
                        else if ("/management/center/event/export".equalsIgnoreCase(requestPath)) {
                            response = eventController.exportEvent(request.getRequestPath(),pathParam,headerParam);
                        }  else if ("/management/report-job/group".equalsIgnoreCase(requestPath)) {
                            response = reportController.reportJobGroup(headerParam,request.getRequestPath(),urlParam);
                        }  else if ("/management/report-job/status".equalsIgnoreCase(requestPath)) {
                            response = reportController.reportJobStatus(headerParam,request.getRequestPath(),urlParam);
                        }  else if ("/management/report-job/event".equalsIgnoreCase(requestPath)) {
                            response = reportController.reportJobEvent(headerParam,request.getRequestPath(),urlParam);
                        } else if ("/management/center/event/export/info".equalsIgnoreCase(requestPath)) {
                            response = eventController.exportEventInfo(request.getRequestPath(),pathParam,headerParam);
                        } else if ("/management/center/event/report/daily".equalsIgnoreCase(requestPath)) {
                            response = eventController.getDataReport(headerParam,request.getRequestPath(),urlParam,bodyParam);
                        }
                        else if ("/management/base/event/history".equalsIgnoreCase(requestPath)) {
                            response = eventController.getEventHistory(headerParam, request.getRequestPath(), pathParam);
                        }
                        else if("/management/report-job/job-type".equalsIgnoreCase(requestPath)){
                            response = reportController.getReportOnlineStatus(headerParam, request.getRequestPath(), request.getRequestMethod(), urlParam);
                        }
                        else if ("/management/common/event/traffic-jam/detail".equalsIgnoreCase(requestPath)) {
                            response = eventController.getTrafficJamDetailHistory(headerParam, request.getRequestPath(), urlParam);
                        } else if ("/management/common/event/traffic-jam/detail/site".equalsIgnoreCase(requestPath)) {
                            response = eventController.getDetailTrafficData(headerParam, request.getRequestPath(), urlParam);
                        }
                        else if ("/management/center/event/info".equalsIgnoreCase(requestPath)) {
                            if (!StringUtil.isNullOrEmpty(pathParam)) {
                                response = eventInfoController.getDetailEventInfo(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            }else{
                                response = eventInfoController.getAllEventInfo(headerParam, request.getRequestPath(), request.getRequestMethod(), urlParam, bodyParam);
                            }
                        }
                        else if ("/management/center/event/info/eventId".equalsIgnoreCase(requestPath)) {
                            if (!StringUtil.isNullOrEmpty(pathParam)) {
                                response = eventInfoController.getEventInfoByEventId(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            }
                        }
                        else if ("/management/center/event/info/eventId".equalsIgnoreCase(requestPath)) {
                            response = eventController.getTrafficJamDetailHistory(headerParam, request.getRequestPath(), urlParam);
                        }
                        else if ("/management/center/event/info/report".equalsIgnoreCase(requestPath)) {
                            response = eventInfoController.getDataReport(headerParam, request.getRequestPath(), request.getRequestMethod(), urlParam, bodyParam);
                        } else if ("/management/site/position".equalsIgnoreCase(requestPath)) {
                            response = eventController.findSiteByKmAndM(headerParam, requestPath, request.getRequestMethod(), urlParam, pathParam);
                        } else if ("/management/site/point".equalsIgnoreCase(requestPath)) {
                            response = eventController.getPoint(headerParam, requestPath, request.getRequestMethod(), urlParam, pathParam);
                        }
                        else if ("/management/center/day/export".equalsIgnoreCase(requestPath)) {
                            response = eventController.exportDayReport(request.getRequestPath(),pathParam,headerParam,urlParam);
                        }
                        else if ("/management/scheduled-event/shift-report".equalsIgnoreCase(requestPath)) {
                            response = scheduledEventServiceController.getAggScheduledEvent(urlParam);
                        }
                        else if ("/management/common/job/report/job-by-status-internal".equalsIgnoreCase(requestPath)) {
                            response = reportController.getAggJobByStatus(urlParam);
                        }
                        else if ("/management/display/history".equalsIgnoreCase(requestPath)) {
                            response = eventInfoController.exportHistory(request.getRequestPath(), bodyParam, urlParam,headerParam);
                        }else if ("/management/action-status".equalsIgnoreCase(requestPath)) {
                            response = eventInfoController.exportActionStatus(request.getRequestPath(), bodyParam, urlParam,headerParam);
                        }else if ("/management/job/backup".equalsIgnoreCase(requestPath)) {
                            response = jobBackupController.jobBackup(pathParam);
                        }else if ("/management/center/event/hotline".equalsIgnoreCase(requestPath)) {
                            response = hotlineController.getReportHotline(headerParam,request.getRequestPath(),urlParam);
                        }else if ("/management/center/event/device".equalsIgnoreCase(requestPath)) {
                            response = reportDeviceController.getReportHotline(headerParam,request.getRequestPath(),urlParam);
                        }else if ("/management/center/event/daily".equalsIgnoreCase(requestPath)) {
                            response = eventController.exportDailyReport(request.getRequestPath(),pathParam,headerParam,urlParam);
                        }

                        break;
                    case "POST":
                        if ("/management/center/job".equalsIgnoreCase(requestPath)) {
                            response = jobController.createJob(headerParam, request.getRequestPath(), request.getRequestMethod(), bodyParam);
                        } else if ("/management/base/job/confirm".equalsIgnoreCase(requestPath)) {
                            response = jobController.confirmJob(headerParam, request.getRequestPath(), pathParam, request.getRequestMethod(), bodyParam);
                        } else if ("/management/base/job/process".equalsIgnoreCase(requestPath)) {
                            response = jobController.processJob(headerParam, request.getRequestPath(), pathParam, request.getRequestMethod(), bodyParam);
                        } else if ("/management/base/job/process-vms-job".equalsIgnoreCase(requestPath)) {
                            response = jobController.processVmsJob(headerParam, request.getRequestPath(), pathParam, request.getRequestMethod(), bodyParam);
                        } else if ("/management/base/job/process-repair-job".equalsIgnoreCase(requestPath)) {
                            response = jobController.processRepairJob(headerParam, request.getRequestPath(), pathParam, request.getRequestMethod(), bodyParam);
                        } else if ("/management/common/job/comment".equalsIgnoreCase(requestPath)) {
                            response = jobController.commentInJob(headerParam, request.getRequestPath(), pathParam, request.getRequestMethod(), bodyParam);
                        } else if ("/management/center/event/manual".equalsIgnoreCase(requestPath)) {
                            response = eventController.createManual(bodyParam, request.getRequestPath(), headerParam);
                        } else if ("/management/recognition".equalsIgnoreCase(requestPath)) {
                            response = recognitionController.findRecognition(bodyParam, request.getRequestPath(), headerParam);
                        } else if ("/management/recognition/statistic".equalsIgnoreCase(requestPath)) {
                            response = recognitionController.findHistoryStatistic(bodyParam, request.getRequestPath(), headerParam);
                        } else if ("/management/recognition/history".equalsIgnoreCase(requestPath)) {
                            response = recognitionController.findHistoryRecognition(bodyParam, request.getRequestPath(), headerParam);
                        } else if ("/management/center/job/reverification-request".equalsIgnoreCase(requestPath)) {
                            response = jobController.requestReverification(headerParam, request.getRequestPath(), pathParam, request.getRequestMethod(), bodyParam);
                        } else if ("/management/scheduled-event".equalsIgnoreCase(requestPath)) {
                            response = scheduledEventServiceController.createScheduledEvent(headerParam, request.getRequestPath(), bodyParam);
                        }  else if ("/management/device-status".equalsIgnoreCase(requestPath)) {
                            response = historyStatusDeviceController.createDeviceStatus(bodyParam,request.getRequestPath(), headerParam );
                        }
                        else if ("/management/center/event/info".equalsIgnoreCase(requestPath)) {
                            response = eventInfoController.createEventInfo(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if ("/management/center/event/info/export/accident".equalsIgnoreCase(requestPath)) {
                            response = eventInfoController.export( requestPath,  bodyParam, headerParam);
                        }
                        else if ("/management/device-status/history".equalsIgnoreCase(requestPath)) {
                            response = historyStatusDeviceController.findHistory(request.getRequestPath(),bodyParam, headerParam );
                        } else if ("/management/device-status/report".equalsIgnoreCase(requestPath)) {
                            response = historyStatusDeviceController.findReport(request.getRequestPath(),bodyParam, headerParam );
                        } else if ("/management/device-status/report/disconnect".equalsIgnoreCase(requestPath)) {
                            response = historyStatusDeviceController.findReportDisconnect(request.getRequestPath(),bodyParam, headerParam );
                        }
                        else if ("/management/center/event/info/export".equalsIgnoreCase(requestPath)) {
                            response = eventInfoController.exportEventInfo( requestPath,  bodyParam, headerParam);
                        }
                        else if ("/management/common/job/vms-board-suggestion".equalsIgnoreCase(requestPath)) {
                            response = jobController.suggestVmsJob(headerParam, request.getRequestPath(),  request.getRequestMethod(), bodyParam);
                        } 
                        else if ("/management/common/event/event-file".equalsIgnoreCase(requestPath)) {
                            response = eventController.createEventFile(request.getRequestPath(), headerParam, bodyParam);
                        }else if ("/management/center/event/hotline".equalsIgnoreCase(requestPath)) {
                            response = hotlineController.createHotline(headerParam,bodyParam,request.getRequestPath(),request.getRequestMethod());
                        }else if ("/management/center/event/device".equalsIgnoreCase(requestPath)) {
                            response = reportDeviceController.createReport(headerParam,bodyParam,request.getRequestPath(),request.getRequestMethod());
                        }
                        else if ("/management/common/event/report-status".equalsIgnoreCase(requestPath)) {
                            response = eventController.updateReportStatusEvent(request.getRequestPath(), headerParam, bodyParam);
                        }
                        break;
                    case "PUT":
                        if ("/management/center/job".equalsIgnoreCase(requestPath)) {
                            response = jobController.updateJob(headerParam, request.getRequestPath(), pathParam, request.getRequestMethod(), bodyParam);
                        } else if ("/management/center/event/violation".equalsIgnoreCase(requestPath)) {
                            response = eventController.updateViolation(bodyParam, request.getRequestPath(), headerParam);
                        } else if ("/management/center/event/manual".equalsIgnoreCase(requestPath)) {
                            response = eventController.updateManual(bodyParam, request.getRequestPath(), headerParam);
                        }else if ("/management/center/event".equalsIgnoreCase(requestPath)) {
                            response = eventController.updateEvent(bodyParam, request.getRequestPath(), headerParam);
                        } else if ("/management/center/event/security".equalsIgnoreCase(requestPath)) {
                            response = eventController.updateSecurity(bodyParam, request.getRequestPath(), headerParam);
                        } else if ("/management/center/event/confirm".equalsIgnoreCase(requestPath)) {
                            response = eventController.confirmEvent(bodyParam, request.getRequestPath(), headerParam);
                        } else if ("/management/recognition".equalsIgnoreCase(requestPath)) {
                            response = recognitionController.correctData(headerParam, request.getRequestPath(), bodyParam,pathParam);
                        } else if ("/management/scheduled-event".equalsIgnoreCase(requestPath)) {
                            response = scheduledEventServiceController.updateScheduledEvent(headerParam, request.getRequestPath(),pathParam, bodyParam);
                        }  else if ("/management/device-status".equalsIgnoreCase(requestPath)) {
                            response = historyStatusDeviceController.updateDeviceStatus(bodyParam,request.getRequestPath(), headerParam,pathParam);
                        }
                        else if ("/management/center/event/info".equalsIgnoreCase(requestPath)) {
                            response = eventInfoController.updateEventInfo(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }else if ("/management/center/event/hotline".equalsIgnoreCase(requestPath)) {
                            response = hotlineController.updateEventInfo(request.getRequestPath(),headerParam,bodyParam);
                        }else if ("/management/center/event/device".equalsIgnoreCase(requestPath)) {
                            response = reportDeviceController.updateEventInfo(request.getRequestPath(),headerParam,bodyParam);
                        }
                        break;
                    case "PATCH":
                        break;
                    case "DELETE":
                        if ("/management/center/event".equalsIgnoreCase(requestPath)) {
                            response = eventController.deleteEvent(headerParam, request.getRequestPath(), pathParam, urlParam);
                        } else if ("/management/center/event/multi".equalsIgnoreCase(requestPath)) {
                            response = eventController.deleteMultiEvent(headerParam, bodyParam, request.getRequestPath());
                        } else if ("/management/recognition".equalsIgnoreCase(requestPath)) {
                            response = recognitionController.deleteRecognition(headerParam, request.getRequestPath(), pathParam);
                        } else if ("/management/recognition/multi".equalsIgnoreCase(requestPath)) {
                            response = recognitionController.deleteMultiRecognition(headerParam, bodyParam, request.getRequestPath());
                        }
                        else if ("/management/scheduled-event".equalsIgnoreCase(requestPath)) {
                            response = scheduledEventServiceController.deleteScheduledEvent(headerParam, request.getRequestPath(), pathParam);
                        }else if ("/management/center/event/hotline".equalsIgnoreCase(requestPath)) {
                            response = hotlineController.deleteMultiEvent(headerParam,bodyParam,request.getRequestPath());
                        }else if ("/management/center/event/device".equalsIgnoreCase(requestPath)) {
                            response = reportDeviceController.deleteMultiEvent(headerParam,bodyParam,request.getRequestPath());
                        }
                        break;                                                                                                                                                                                                         
                    default:
                        break;
                }
            }
            LOGGER.info(" [<--] Server returned " + response.toJsonString());
            long end = System.currentTimeMillis();
            LOGGER.info("[RpcServer] ================> Time to process data : " + (end - start) + " miliseconds");
            return response.toJsonString();
        } catch (Exception ex) {
            LOGGER.error("Error to process request >>> " + ex.toString());
            ex.printStackTrace();
        }
        return null;
    }
}
