/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.rabbitmq;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.JSONConverter;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import elcom.com.its.notify.recevice.service.config.ApplicationConfig;
import elcom.com.its.notify.recevice.service.enums.EventStatus;
import elcom.com.its.notify.recevice.service.enums.EventType;
import elcom.com.its.notify.recevice.service.enums.JobType;
import elcom.com.its.notify.recevice.service.model.dto.*;
import elcom.com.its.notify.recevice.service.service.CategoryService;
import elcom.com.its.notify.recevice.service.service.ITSObjectTrackingService;
import elcom.com.its.notify.recevice.service.service.ITSSiteService;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.jgroups.util.UUID;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author admin
 */
@Component
public class WorkerServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerServer.class);

    @Value("${url.map}")
    private String urlMap;

    @Value("${url.schedule}")
    private String schedule;

    @Value("${url.center}")
    private String eventCenter;
    @Value("${url.base}")
    private String eventBase;

    @Value("${url.image}")
    private String urlImage;

    @Value("${its.event.info.worker.queue}")
    private String queueExport;

    @Value("${url.default}")
    private String urlImageDefault;

    @Value("${url.zalo}")
    private String urlZalo;

    @Autowired
    private WorkerConfig workerConfig;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private ITSObjectTrackingService itsObjectTrackingService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ITSSiteService itsSiteService;




    public WorkerServer() {
        LOGGER.info("WorkerServer constructor ...........");
    }

    @RabbitListener(id = "process", queues = {"#{workerConfig.getHeartbeatCamera()}"})
    public void workerReceiveHeartBeatCam(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
            if (request != null && request.getData() != null) {
                //Transform dto => model
                if ("heartbeat".equalsIgnoreCase(request.getType())) {
                    LOGGER.info(request.getData().toString());
                    String message = mapper.writeValueAsString(request.getData());
                    Heartbeat data = mapper.readValue(message, Heartbeat.class);
                    if (data != null) {
                        LOGGER.info(" [-------->] heartbeat data >>>" + JSONConverter.toJSON(data));
                        //List group camera id chua camera id
                        List<String> canAccessUserIdList = new ArrayList<>();
                        if(request.getSites() !=null) {
                            canAccessUserIdList = getCanAccessUserIdListFromIDService(request.getSites());
                            if(canAccessUserIdList == null){
                                canAccessUserIdList = new ArrayList<>();
                            }
                        }
                        List<String> adminUserIdList = getAdminUserIdListFromABACService();

                        if (canAccessUserIdList != null) {
                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                            set.addAll(adminUserIdList);
                            List<String> userIdCanReceiverList = new ArrayList<>(set);
                            sendNotify(userIdCanReceiverList, data);
                        }
                        Map<String, Object> bodyParam = putBodyParamChangeStatus(data);
                        handleChangeStatus(bodyParam);
                    }
                }
            } else {
                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @RabbitListener(id = "processVmsBoard", queues = {"#{workerConfig.getHeartbeatVmsBoard()}"})
    public void workerReceiveHeartBeatVmsBoard(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
            if (request != null && request.getData() != null) {
                //Transform dto => model
                if ("heartbeat".equalsIgnoreCase(request.getType())) {
                    LOGGER.info(request.getData().toString());
                    String message = mapper.writeValueAsString(request.getData());
                    Heartbeat data = mapper.readValue(message, Heartbeat.class);
                    if (data != null) {
                        LOGGER.info(" [-------->] heartbeat data >>>" + JSONConverter.toJSON(data));
                        //List group camera id chua camera id
                        List<String> canAccessUserIdList = new ArrayList<>();
                        if(request.getSites() !=null) {
                            canAccessUserIdList = getCanAccessUserIdListFromIDService(request.getSites());
                            if(canAccessUserIdList == null){
                                canAccessUserIdList = new ArrayList<>();
                            }
                        }
                        List<String> adminUserIdList = getAdminUserIdListFromABACService();

                        if (canAccessUserIdList != null) {
                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                            set.addAll(adminUserIdList);
                            List<String> userIdCanReceiverList = new ArrayList<>(set);
                            sendNotify(userIdCanReceiverList, data);
                        }
                        Map<String, Object> bodyParam = putBodyParamChangeStatus(data);
                        handleChangeStatus(bodyParam);
                    }
                }
            } else {
                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @RabbitListener(id = "processHeartbeatVms", queues = {"#{workerConfig.getHeartbeatVms()}"})
    public void workerReceiveHeartbeatVms(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
            if (request != null && request.getData() != null) {
                //Transform dto => model
                if ("heartbeat".equalsIgnoreCase(request.getType())) {
                    LOGGER.info(request.getData().toString());
                    String message = mapper.writeValueAsString(request.getData());
                    Heartbeat data = mapper.readValue(message, Heartbeat.class);
                    if (data != null) {
                        LOGGER.info(" [-------->] heartbeat data >>>" + JSONConverter.toJSON(data));
                        //List group camera id chua camera id
                        List<String> canAccessUserIdList = new ArrayList<>();
                        if(data.getSiteId() !=null) {
                            canAccessUserIdList = getCanAccessUserIdListFromIDService(data.getSiteId());
                            if(canAccessUserIdList == null){
                                canAccessUserIdList = new ArrayList<>();
                            }
                        }

                        List<String> adminUserIdList = getAdminUserIdListFromABACService();

                        if (canAccessUserIdList != null) {
                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                            set.addAll(adminUserIdList);
                            List<String> userIdCanReceiverList = new ArrayList<>(set);
                            sendNotify(userIdCanReceiverList, data);
                        }
                    }
                }
            } else {
                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @RabbitListener(id = "processNewsVmsBoardBoard", queues = {"#{workerConfig.getNewsVmsBoard()}"})
    public void workerReceiveNewsVmsBoard(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
            if (request != null && request.getData() != null) {
                ObjectMapper mapperCrowd = new ObjectMapper();
                mapperCrowd.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mapperCrowd.setDateFormat(df1);
                String message = mapper.writeValueAsString(request.getData());

                VMSBoardNotify data = mapperCrowd.readValue(message, VMSBoardNotify.class);
                if (data != null) {
                    LOGGER.info(" [-------->] CROWD FLOWS data >>>" + JSONConverter.toJSON(data));
                    List<String> canAccessUserIdList = new ArrayList<>();
                    if(data.getVmsBoard().getSiteId()!=null) {
                        canAccessUserIdList = getCanAccessUserIdListFromIDService(data.getVmsBoard().getSiteId().getId());
                        if(canAccessUserIdList == null){
                            canAccessUserIdList = new ArrayList<>();
                        }
                    }
                    List<String> adminUserIdList = getAdminUserIdListFromABACService();
                    if (canAccessUserIdList != null) {
                        Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                        set.addAll(adminUserIdList);
                        List<String> userIdCanReceiverList = new ArrayList<>(set);
                        sendVMSBoard(userIdCanReceiverList,data);
                    }

                    //For test Kien Giang => Ban zalo
                }
            } else {
                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @RabbitListener(id = "processDisplayVmsBoard", queues = {"#{workerConfig.getDisplayVmsBoard()}"})
    public void workerReceiveDisplayVmsBoard(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
            if (request != null && request.getData() != null) {
                ObjectMapper mapperCrowd = new ObjectMapper();
                mapperCrowd.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                mapperCrowd.setDateFormat(df1);
                String message = mapper.writeValueAsString(request.getData());

                DisplayVMSBoardNotify data = mapperCrowd.readValue(message, DisplayVMSBoardNotify.class);
                if (data != null) {
                    LOGGER.info(" [-------->] CROWD FLOWS data >>>" + JSONConverter.toJSON(data));
                    List<String> canAccessUserIdList = new ArrayList<>();
                    if(data.getVmsBoard().getSiteId()!=null) {
                        canAccessUserIdList = getCanAccessUserIdListFromIDService(data.getVmsBoard().getSiteId().getId());
                        if(canAccessUserIdList == null){
                            canAccessUserIdList = new ArrayList<>();
                        }
                    }
                    List<String> adminUserIdList = getAdminUserIdListFromABACService();
                    if (canAccessUserIdList != null) {
                        Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                        set.addAll(adminUserIdList);
                        List<String> userIdCanReceiverList = new ArrayList<>(set);
                        sendVMSBoardDisplay(userIdCanReceiverList,data);
                    }

                    //For test Kien Giang => Ban zalo
                }
            } else {
                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }


    @RabbitListener(id = "processObjectTracking", queues = {"#{workerConfig.getObjectTracking()}"})
    public void workerReceiveObjectTracking(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
            if (request != null && request.getData() != null) {
                //Transform dto => model
                if ("objecttracking".equalsIgnoreCase(request.getType())) {
                    LOGGER.info(request.getData().toString());
                    String message = mapper.writeValueAsString(request.getData());
                    RecognitionPlateObjectTrackingDTO data = mapper.readValue(message, RecognitionPlateObjectTrackingDTO.class);
                    if (data != null) {
                        LOGGER.info(" [-------->] heartbeat data >>>" + JSONConverter.toJSON(data));
                        //List group camera id chua camera id
                        List<String> canAccessUserIdList = new ArrayList<>();
                        if(data.getSite()!=null) {
                            canAccessUserIdList = getCanAccessUserIdListFromIDService(data.getSite().siteId);
                            if(canAccessUserIdList == null){
                                canAccessUserIdList = new ArrayList<>();
                            }
                        }

                        List<String> adminUserIdList = getAdminUserIdListFromABACService();
                        if(data.getListUnit() != null && !data.getListUnit().isEmpty()){
                            List<String> tmp = getCanAccessUserIdListFromUnits(data.getListUnit());
                            if(tmp != null){
                                canAccessUserIdList.addAll(tmp);
                            }
                        }

                        if (canAccessUserIdList != null) {
                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                            set.addAll(adminUserIdList);
                            List<String> userIdCanReceiverList = new ArrayList<>(set);
                            sendNotify(userIdCanReceiverList, data);
                        }
                    }
                }
            } else {
                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @RabbitListener(id = "processScheduleEvent", queues = {"#{workerConfig.getScheduleEvent()}"})
    public void workerReceiveScheduleEvent(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            ScheduleEventNotify request = mapper.readValue(json, ScheduleEventNotify.class);
            String now = df.format(new Date());
            if (request != null && request.getScheduledEvent() != null) {
                //Transform dto => model
                    LOGGER.info(request.getScheduledEvent().toString());
                    String message = mapper.writeValueAsString(request.getScheduledEvent());
                    ScheduledEvent data = mapper.readValue(message, ScheduledEvent.class);
                    if (data != null) {
                        LOGGER.info(" [-------->] heartbeat data >>>" + JSONConverter.toJSON(data));
                        //List group camera id chua camera id
                        List<String> canAccessUserIdList = new ArrayList<>();
                        canAccessUserIdList.add(data.getCreatedBy());
                        if(!StringUtil.isNullOrEmpty(data.getUsers())){
                            canAccessUserIdList = Arrays.asList(data.getUsers().split(","));
                        } else {
                            canAccessUserIdList = getUserFromGroupId(data.getGroupId());
                            List<String> listUser = getUserIdInShift(data.getSiteId(),now);
                            canAccessUserIdList = canAccessUserIdList.stream().filter(item->listUser.contains(item)).collect(Collectors.toList());
                        }

                        List<String> adminUserIdList = getAdminUserIdListFromABACService();

                        if (canAccessUserIdList != null) {
                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                            set.addAll(adminUserIdList);
                            if(!StringUtil.isNullOrEmpty(data.getCreatedBy()))
                                set.add(data.getCreatedBy());
                            List<String> userIdCanReceiverList = new ArrayList<>(set);
                            if ("created".equalsIgnoreCase(request.getActionCode())) {
                                sendNotify(userIdCanReceiverList, data,"Được tạo",schedule,0);
                            } else if ("updated".equalsIgnoreCase(request.getActionCode())) {
                                sendNotify(userIdCanReceiverList, data,"Được cập nhập",schedule,0);
                            } else if ("upcoming".equalsIgnoreCase(request.getActionCode())) {
                                sendNotify(userIdCanReceiverList, data,"Sắp diễn ra",schedule,0);
                            } else if ("started".equalsIgnoreCase(request.getActionCode())) {
                                Map<String, Object> bodyParamZalo = putBodyParamNotifyZalo("sdff",data,request,3);
                                handleSendNotify(bodyParamZalo);
                                userIdCanReceiverList.remove(data.getCreatedBy());
                                userIdCanReceiverList.removeAll(adminUserIdList);
                                sendNotify(userIdCanReceiverList, data,"Đang diễn ra", eventBase,1);
                                Set<String> setCenter = new LinkedHashSet<>(adminUserIdList);
                                setCenter.add(data.getCreatedBy());
                                List<String> userIdCanReceiverList2 = new ArrayList<>(setCenter);
                                sendNotify(userIdCanReceiverList2, data,"Đang diễn ra", eventCenter,1);
                                if(data.getJob().getJobType().equals("FORBIDDEN_WAY")){
                                    sendNotifyMapJob(userIdCanReceiverList2, data,eventBase, "BLOCKROAD");
                                } else if(data.getJob().getJobType().equals("CLOSE_LANE")){
                                    sendNotifyMapJob(userIdCanReceiverList2, data,eventBase, "BLOCKLANE");
                                } else if (data.getJob().getJobType().equals("CLOSE_OPEN_ENTRANCE_EXIT")) {
                                    sendNotifyMapJob(userIdCanReceiverList2, data,eventBase, "CLOSE_OPEN_ENTRANCE_EXIT");
                                } else if (data.getJob().getJobType().equals("LIMIT_SPEED")) {
                                    sendNotifyMapJob(userIdCanReceiverList2, data,eventBase, "LIMIT_SPEED");
                                }
                                if ("crowd".equalsIgnoreCase(data.getEventType()) ){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"10");
                                } else if("brokenvehicle".equalsIgnoreCase(data.getEventType())){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"2");
                                } else if("obstruction".equalsIgnoreCase(data.getEventType())){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"3");
                                } else if("accident".equalsIgnoreCase(data.getEventType())){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"1");
                                } else if("fire".equalsIgnoreCase(data.getEventType())){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"4");
                                } else if("barrier".equalsIgnoreCase(data.getEventType())){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"3");
                                } else if("construction".equalsIgnoreCase(data.getEventType())){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"9");
                                } else  if("rain".equalsIgnoreCase(data.getEventType())){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"5");
                                } else if("landslide".equalsIgnoreCase(data.getEventType())){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"6");
                                } else if("snow".equalsIgnoreCase(data.getEventType())){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"7");
                                } else if("fog".equalsIgnoreCase(data.getEventType())){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"8");
                                } else if("damaged_road".equalsIgnoreCase(data.getEventType())){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"22");
                                } else if("destruction".equalsIgnoreCase(data.getEventType())){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"21");
                                } else if("high_temperature".equalsIgnoreCase(data.getEventType())){
                                    sendNotifyMapEvent(userIdCanReceiverList, data,"20");
                                } else if("encroaching".equalsIgnoreCase(data.getEventType())) {
                                    sendNotifyMapEvent(userIdCanReceiverList, data, "25");
                                }
                            }
                        }
                    }
            } else {
                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }



    @RabbitListener(id = "job", queues = {"#{workerConfig.getJob()}"})
    public void workerReceiveJob(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();

            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            NotifyRequest request = mapper.readValue(json, NotifyRequest.class);
            String now = df.format(new Date());
            if (request != null) {
                //Transform dto => model
                if ("CREATE".equalsIgnoreCase(request.getActionCode()) || "UPDATE".equalsIgnoreCase(request.getActionCode()) ||
                        "REQUEST_REVERIFYCATION".equalsIgnoreCase(request.getActionCode()) || "FINISH".equalsIgnoreCase(request.getActionCode())
                        || "CANCEL".equalsIgnoreCase(request.getActionCode())) {
                    Job data = request.getJob();
                    if (data != null) {
                        LOGGER.info(" [-------->] heartbeat data >>>" + JSONConverter.toJSON(data));
                        //List group camera id chua camera id
                        List<String> canAccessUserIdList = new ArrayList<>();
                        if (StringUtil.isNullOrEmpty(data.getUserIds())) {
                            canAccessUserIdList = getUserFromGroupId(data.getGroupId());
                            if (canAccessUserIdList == null) {
                                canAccessUserIdList = new ArrayList<>();
                            } else {
                                List<String> listUser = getUserIdInShift(data.getStartSiteId(),now);
                                canAccessUserIdList = canAccessUserIdList.stream().filter(item->listUser.contains(item)).collect(Collectors.toList());
                            }
                        } else {
                            canAccessUserIdList = Arrays.asList(data.getUserIds().split(","));
                        }
                        if (canAccessUserIdList != null) {
                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                            List<String> userIdCanReceiverList = new ArrayList<>(set);
                            sendNotify(userIdCanReceiverList, data, request, eventBase);
                        }
                        if ("CREATE".equalsIgnoreCase(request.getActionCode()) || "FINISH".equalsIgnoreCase(request.getActionCode()) ||
                                "CANCEL".equalsIgnoreCase(request.getActionCode()) || "UPDATE".equalsIgnoreCase(request.getActionCode())  ) {
                        if (data.getJobType().equals("FORBIDDEN_WAY")) {
                            canAccessUserIdList = new ArrayList<>();
                            canAccessUserIdList = getUserFromGroupId(data.getGroupId());
                            if (canAccessUserIdList == null) {
                                canAccessUserIdList = new ArrayList<>();
                            }
                            List<String> adminUserIdList = getAdminUserIdListFromABACService();

                            if (canAccessUserIdList != null) {
                                Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                                set.addAll(adminUserIdList);
                                set.add(data.getCreatedBy());
                                List<String> userIdCanReceiverList = new ArrayList<>(set);
                                sendNotifyMapJob(userIdCanReceiverList, data, request, eventBase, "BLOCKROAD");
                            }
                        } else if (data.getJobType().equals("CLOSE_LANE")) {
                            canAccessUserIdList = new ArrayList<>();
                            canAccessUserIdList = getUserFromGroupId(data.getGroupId());
                            if (canAccessUserIdList == null) {
                                canAccessUserIdList = new ArrayList<>();
                            }
                            List<String> adminUserIdList = getAdminUserIdListFromABACService();

                            if (canAccessUserIdList != null) {
                                Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                                set.addAll(adminUserIdList);
                                set.add(data.getCreatedBy());
                                List<String> userIdCanReceiverList = new ArrayList<>(set);
                                sendNotifyMapJob(userIdCanReceiverList, data, request, eventBase, "BLOCKLANE");
                            }
                        } else if (data.getJobType().equals("CLOSE_OPEN_ENTRANCE_EXIT")) {
                            canAccessUserIdList = new ArrayList<>();
                            canAccessUserIdList = getUserFromGroupId(data.getGroupId());
                            if (canAccessUserIdList == null) {
                                canAccessUserIdList = new ArrayList<>();
                            }
                            List<String> adminUserIdList = getAdminUserIdListFromABACService();

                            if (canAccessUserIdList != null) {
                                Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                                set.addAll(adminUserIdList);
                                set.add(data.getCreatedBy());
                                List<String> userIdCanReceiverList = new ArrayList<>(set);
                                sendNotifyMapJobPlace(userIdCanReceiverList, data, request, eventBase, "CLOSE_OPEN_ENTRANCE_EXIT");
                            }
                        } else if (data.getJobType().equals("LIMIT_SPEED")) {
                            canAccessUserIdList = new ArrayList<>();
                            canAccessUserIdList = getUserFromGroupId(data.getGroupId());
                            if (canAccessUserIdList == null) {
                                canAccessUserIdList = new ArrayList<>();
                            }
                            List<String> adminUserIdList = getAdminUserIdListFromABACService();

                            if (canAccessUserIdList != null) {
                                Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                                set.addAll(adminUserIdList);
                                set.add(data.getCreatedBy());
                                List<String> userIdCanReceiverList = new ArrayList<>(set);
                                sendNotifyMapJob(userIdCanReceiverList, data, request, eventBase, "LIMIT_SPEED");
                            }
                        }
                    }
                    }
                } else if ("PROCESS".equalsIgnoreCase(request.getActionCode()) || "CONFIRM".equalsIgnoreCase(request.getActionCode())) {
                    Job data = request.getJob();
                    if (data != null) {
                        LOGGER.info(" [-------->] heartbeat data >>>" + JSONConverter.toJSON(data));
                        //List group camera id chua camera id
                        List<String> canAccessUserIdList = new ArrayList<>();
                        canAccessUserIdList.add(data.getCreatedBy());
                        if (canAccessUserIdList != null) {
                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                            List<String> userIdCanReceiverList = new ArrayList<>(set);
                            sendNotify(userIdCanReceiverList, data, request, eventCenter);
                        }
                    }
                }
            } else {
                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @RabbitListener(id = "eventEnd", queues = {"#{workerConfig.getFinishEvent()}"})
    public void workerReceiveEndEvent(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            NotifyEndEventRequest request = mapper.readValue(json, NotifyEndEventRequest.class);
            if (request != null) {
                List<String> canAccessUserIdList = new ArrayList<>();
                if(request.getSiteId()!=null) {
                    canAccessUserIdList = getCanAccessUserIdListFromIDService(request.getSiteId());
                    if(canAccessUserIdList == null){
                        canAccessUserIdList = new ArrayList<>();
                    }
                }

                List<String> adminUserIdList = getAdminUserIdListFromABACService();

                if (canAccessUserIdList != null) {
                    Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                    set.addAll(adminUserIdList);
                    List<String> userIdCanReceiverList = new ArrayList<>(set);
                    sendNotify(userIdCanReceiverList, request.getEventId());
                }

            } else {
                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }


    @RabbitListener(id = "processTraffic", queues = {"#{workerConfig.getTraffic()}"})
    public void workerReceiveTraffic(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);

            SiteInfo site = null;
            String sourceId = null;
            String sourceName = null;
            Date startTime = null;
            String violationId = null;
            String imagesObject = null;
            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
            if (request != null && request.getData() != null) {
                //Transform dto => model
                System.out.println(request.getType());
                 if ("traffic_status".equalsIgnoreCase(request.getType())) {
                    LOGGER.info(request.getData().toString());
                    ObjectMapper mapperCrowd = new ObjectMapper();
                    mapperCrowd.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    mapperCrowd.setDateFormat(df1);
                    String message = mapper.writeValueAsString(request.getData());
                    TrafficStatusNotify data = mapperCrowd.readValue(message, TrafficStatusNotify.class);
                    if (data != null) {
                        LOGGER.info(" [-------->] FIRE FLOWS data >>>" + JSONConverter.toJSON(data));
                        //List group camera id chua camera id
                        List<String> canAccessUserIdList = new ArrayList<>();
//                        if(data.getStage().getSiteStart()!=null) {
//                            canAccessUserIdList = getCanAccessUserIdListFromIDServiceStage(data.getStage().getCode());
//                            if(canAccessUserIdList == null){
//                                canAccessUserIdList = new ArrayList<>();
//                            }
//                        }

//                        List<String> adminUserIdList = getAdminUserIdListFromABACService();
                        Map<String,User> mapUser = getAllUser();
                        mapUser.forEach((k,v) ->{
                            canAccessUserIdList.add(k);

                        });

                        if (canAccessUserIdList != null) {
                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
//                            set.addAll(adminUserIdList);
                            List<String> userIdCanReceiverList = new ArrayList<>(set);
                            sendNotify(userIdCanReceiverList, data);
                        }
                    }
                }

            } else {
                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @RabbitListener(id = "processEventChange", queues = {"#{workerConfig.getEventChangeStatus()}"})
    public void workerEventChangeStatus(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            EventNotifyRequest request = mapper.readValue(json, EventNotifyRequest.class);
            if (request != null && request.getEvent() != null) {
                //Transform dto => model
                System.out.println(request.getEvent());
                if ("update".equalsIgnoreCase(request.getAction())) {
                    LOGGER.info(request.getEvent().toString());
                    ObjectMapper mapperCrowd = new ObjectMapper();
                    mapperCrowd.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    mapperCrowd.setDateFormat(df1);
                    String message = mapper.writeValueAsString(request.getEvent());
                    Event data = mapperCrowd.readValue(message, Event.class);
                    List<String> canAccessUserIdList = new ArrayList<>();
                    if(data.getSite()!=null) {
                        canAccessUserIdList = getCanAccessUserIdListFromIDService(data.getSite().getSiteId());
                        if(canAccessUserIdList == null){
                            canAccessUserIdList = new ArrayList<>();
                        }
                    }
                    List<String> adminUserIdList = getAdminUserIdListFromABACService();
                    if (canAccessUserIdList != null) {
                        Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                        set.addAll(adminUserIdList);
                        List<String> userIdCanReceiverList = new ArrayList<>(set);
                        if ("crowd".equalsIgnoreCase(data.getEventCode()) ){
                            sendSecurityMap(userIdCanReceiverList, data,"10");
                        } else if("brokenvehicle".equalsIgnoreCase(data.getEventCode())){
                            sendSecurityMap(userIdCanReceiverList, data,"2");
                        } else if("obstruction".equalsIgnoreCase(data.getEventCode())){
                            sendSecurityMap(userIdCanReceiverList, data,"18");
                        } else if("accident".equalsIgnoreCase(data.getEventCode())){
                            sendSecurityMap(userIdCanReceiverList, data,"1");
                        } else if("fire".equalsIgnoreCase(data.getEventCode())){
                            sendSecurityMap(userIdCanReceiverList, data,"4");
                        } else if("barrier".equalsIgnoreCase(data.getEventCode())){
                            sendSecurityMap(userIdCanReceiverList, data,"3");
                        } else if("construction".equalsIgnoreCase(data.getEventCode())){
                            sendSecurityMap(userIdCanReceiverList, data,"9");
                        } else if("rain".equalsIgnoreCase(data.getEventCode())){
                            sendNotifyMap(userIdCanReceiverList, data,"5");
                        } else if("landslide".equalsIgnoreCase(data.getEventCode())){
                            sendNotifyMap(userIdCanReceiverList, data,"6");
                        } else if("snow".equalsIgnoreCase(data.getEventCode())){
                            sendNotifyMap(userIdCanReceiverList, data,"7");
                        } else if("fog".equalsIgnoreCase(data.getEventCode())){
                            sendNotifyMap(userIdCanReceiverList, data,"8");
                        } else if("high_temperature".equalsIgnoreCase(data.getEventCode())){
                            sendNotifyMap(userIdCanReceiverList, data,"20");
                        } else if("damaged_road".equalsIgnoreCase(data.getEventCode())){
                            sendNotifyMap(userIdCanReceiverList, data,"22");
                        } if("destruction".equalsIgnoreCase(data.getEventCode())){
                            sendNotifyMap(userIdCanReceiverList, data,"21");
                        }
                    }
                }

            } else {
                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void createFile(String eventId){
        EventExport eventExport = new EventExport();
        eventExport.setEventId(eventId);
        eventExport.setUuid("àdsfsd");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String msg = mapper.writeValueAsString(eventExport);
            rabbitMQClient.callWorkerService(queueExport, msg);
        } catch (Exception ex){

        }

    }

    @RabbitListener(id = "processEvent", queues = {"#{workerConfig.getEvent()}"})
    public void workerReceiveEvent(String json){
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
            if (request != null && request.getData() != null) {
                if ("violation".equalsIgnoreCase(request.getType()) || "inversedirection".equalsIgnoreCase(request.getType())
                 || "restricted".equalsIgnoreCase(request.getType())
                || "wrongline".equalsIgnoreCase(request.getType()) || "speedlimit".equalsIgnoreCase(request.getType())
                || "noentry".equalsIgnoreCase(request.getType())) {
                    LOGGER.info(request.getData().toString());
                    ObjectMapper mapperCrowd = new ObjectMapper();
                    mapperCrowd.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    mapperCrowd.setDateFormat(df1);
                    String message = mapper.writeValueAsString(request.getData());
                    try {
                        ViolationDetail data = mapperCrowd.readValue(message, ViolationDetail.class);
                        if (data != null) {
//                            createFile(data.getEventId());
                            if(!StringUtil.isNullOrEmpty(data.getSourceId())){
                                sendViolation(data);
                            }
                            LOGGER.info(" [-------->] EVENT data >>>" + JSONConverter.toJSON(data));
                            //List group camera id chua camera id
                            List<String> canAccessUserIdList = new ArrayList<>();
                            if(request.getSites()!=null) {
                                canAccessUserIdList = getCanAccessUserIdListFromIDService(request.getSites());
                                if(canAccessUserIdList == null){
                                    canAccessUserIdList = new ArrayList<>();
                                }
                            }

                            List<String> adminUserIdList = getAdminUserIdListFromABACService();

//                            if (canAccessUserIdList != null & !canAccessUserIdList.isEmpty()) {
//                                Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
//                                set.addAll(adminUserIdList);
//                                List<String> userIdCanReceiverList = new ArrayList<>(set);
//                                sendNotify(userIdCanReceiverList, data);
//                            }
                        }
                    } catch (Exception ex){
                        Event data = mapperCrowd.readValue(message, Event.class);
//                        createFile(data.getParentId());
                        if (data != null) {
                            if(!StringUtil.isNullOrEmpty(data.getSourceId())){
                                sendViolation(data);
                            }
//                            LOGGER.info(" [-------->] EVENT data >>>" + JSONConverter.toJSON(data));
//                            //List group camera id chua camera id
//                            List<String> canAccessUserIdList = new ArrayList<>();
//                            if(request.getSites()!=null) {
//                                canAccessUserIdList = getCanAccessUserIdListFromIDService(request.getSites());
//                                if(canAccessUserIdList == null){
//                                    canAccessUserIdList = new ArrayList<>();
//                                }
//                            }
//
//                            List<String> adminUserIdList = getAdminUserIdListFromABACService();
//
//                            if (canAccessUserIdList != null & !canAccessUserIdList.isEmpty()) {
//                                Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
//                                set.addAll(adminUserIdList);
//                                List<String> userIdCanReceiverList = new ArrayList<>(set);
//                                sendNotify(userIdCanReceiverList, data);
//                            }
                        }
                    }


                } else if ("crowd".equalsIgnoreCase(request.getType()) || "brokenvehicle".equalsIgnoreCase(request.getType())
                || "obstruction".equalsIgnoreCase(request.getType()) || "accident".equalsIgnoreCase(request.getType() )
                        || "barrier".equalsIgnoreCase(request.getType())
                        || "fire".equalsIgnoreCase(request.getType()) || "construction".equalsIgnoreCase(request.getType())
                        || "illegalstop".equalsIgnoreCase(request.getType())) {
                    LOGGER.info(request.getData().toString());
                    ObjectMapper mapperCrowd = new ObjectMapper();
                    mapperCrowd.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    mapperCrowd.setDateFormat(df1);
                    String message = mapper.writeValueAsString(request.getData());
                    String now = df1.format(new Date());
                    try {
                        SecurityDTO data = mapperCrowd.readValue(message, SecurityDTO.class);
                        if (data != null) {

                            createFile(data.getEventId());
                            if(!StringUtil.isNullOrEmpty(data.getSourceId())){
                                sendSecurity(data);
                            }
                            LOGGER.info(" [-------->] CROWD FLOWS data >>>" + JSONConverter.toJSON(data));
                            List<String> canAccessUserIdList = new ArrayList<>();

                            if(data.getSite()!=null) {

                                canAccessUserIdList = getUserIdInShift(data.getSite().getSiteId(),now);
                                if(canAccessUserIdList == null){
                                    canAccessUserIdList = new ArrayList<>();
                                }
                            }

                            List<String> adminUserIdList = getAdminUserIdListFromABACService();
                            if (canAccessUserIdList != null) {
                                
                                Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                                set.addAll(adminUserIdList);
                                List<String> userIdCanReceiverList = new ArrayList<>(set);
                                if ("crowd".equalsIgnoreCase(request.getType()) ){
                                    sendSecurity(userIdCanReceiverList, data,"10");
                                } else if("brokenvehicle".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"2");
                                } else if("obstruction".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"3");
                                } else if("accident".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"1");
                                } else if("fire".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"4");
                                } else if("barrier".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"3");
                                } else if("construction".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"9");
                                } else if("illegalstop".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"25");
                                }
                            }

                            if(data.getSite()!=null) {
                                canAccessUserIdList = getCanAccessUserIdListFromIDService(data.getSite().getSiteId());
                                if(canAccessUserIdList == null){
                                    canAccessUserIdList = new ArrayList<>();
                                }
                            }

//                            List<String> adminUserIdList = getAdminUserIdListFromABACService();
                            if (canAccessUserIdList != null) {
                                Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                                set.addAll(adminUserIdList);
                                List<String> userIdCanReceiverList = new ArrayList<>(set);
                                if ("crowd".equalsIgnoreCase(request.getType()) ){
                                    sendSecurityMap(userIdCanReceiverList, data,"10");
                                } else if("brokenvehicle".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"2");
                                } else if("obstruction".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"3");
                                } else if("accident".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"1");
                                } else if("fire".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"4");
                                } else if("barrier".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"3");
                                } else if("construction".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"9");
                                } else if("illegalstop".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"25");
                                }
                            }

                            //For test Kien Giang => Ban zalo
                        }
                    } catch (Exception ex){
                        Event data = mapperCrowd.readValue(message, Event.class);
                        if (data != null) {
                            createFile(data.getParentId());
                            if(StringUtil.isNullOrEmpty(data.getSourceId())){
                                sendEvent(data);
                            }
                            LOGGER.info(" [-------->] CROWD FLOWS data >>>" + JSONConverter.toJSON(data));
                            List<String> canAccessUserIdList = new ArrayList<>();

                            if(data.getSite()!=null) {

                                canAccessUserIdList = getUserIdInShift(data.getSite().getSiteId(),now);
                                if(canAccessUserIdList == null){
                                    canAccessUserIdList = new ArrayList<>();
                                }
                            }
                            List<String> adminUserIdList = getAdminUserIdListFromABACService();
                            if (canAccessUserIdList != null) {
                                Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                                set.addAll(adminUserIdList);
                                List<String> userIdCanReceiverList = new ArrayList<>(set);
                                if ("crowd".equalsIgnoreCase(request.getType()) ){
                                    sendSecurity(userIdCanReceiverList, data,"10");
                                } else if("brokenvehicle".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"2");
                                } else if("obstruction".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"18");
                                } else if("accident".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"1");
                                } else if("fire".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"4");
                                } else if("barrier".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"3");
                                } else if("construction".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"9");
                                } else if("illegalstop".equalsIgnoreCase(request.getType())){
                                    sendSecurity(userIdCanReceiverList, data,"25");
                                }
                            }

                            if(data.getSite()!=null) {
                                canAccessUserIdList = getCanAccessUserIdListFromIDService(data.getSite().getSiteId());
                                if(canAccessUserIdList == null){
                                    canAccessUserIdList = new ArrayList<>();
                                }
                            }

//                            List<String> adminUserIdList = getAdminUserIdListFromABACService();
                            if (canAccessUserIdList != null) {
                                Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                                set.addAll(adminUserIdList);
                                List<String> userIdCanReceiverList = new ArrayList<>(set);
                                if ("crowd".equalsIgnoreCase(request.getType()) ){
                                    sendSecurityMap(userIdCanReceiverList, data,"10");
                                } else if("brokenvehicle".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"2");
                                } else if("obstruction".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"3");
                                } else if("accident".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"1");
                                } else if("fire".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"4");
                                } else if("barrier".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"3");
                                } else if("construction".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"9");
                                } else if("illegalstop".equalsIgnoreCase(request.getType())){
                                    sendSecurityMap(userIdCanReceiverList, data,"25");
                                }
                            }

                            //For test Kien Giang => Ban zalo
                        }
                    }



                }else if ("rain".equalsIgnoreCase(request.getType()) || "landslide".equalsIgnoreCase(request.getType())
                || "snow".equalsIgnoreCase(request.getType()) || "fog".equalsIgnoreCase(request.getType() )
                || "maintance".equalsIgnoreCase(request.getType()) || "brokendevice".equalsIgnoreCase(request.getType())
                || "damaged_road".equalsIgnoreCase(request.getType()) || "destruction".equalsIgnoreCase(request.getType())
                || "high_temperature".equalsIgnoreCase(request.getType())
                || "crowding".equalsIgnoreCase(request.getType()) || "illegalstop".equalsIgnoreCase(request.getType())
                        || "trafficjam".equalsIgnoreCase(request.getType()) || "finish_trafficjam".equalsIgnoreCase(request.getType())
                        || "explosion".equalsIgnoreCase(request.getType())
                        || "throw_stone".equalsIgnoreCase(request.getType()) || "theft".equalsIgnoreCase(request.getType())) {
                    LOGGER.info(request.getData().toString());
                    ObjectMapper mapperFobbiden = new ObjectMapper();
                    mapperFobbiden.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    mapperFobbiden.setDateFormat(df1);
                    String message = mapper.writeValueAsString(request.getData());
                    Event data = mapperFobbiden.readValue(message, Event.class);
                    String now = df1.format(new Date());
                    if (data != null) {
                        createFile(data.getParentId());
                        if(StringUtil.isNullOrEmpty(data.getSourceId())){
                            sendEvent(data);
                        }
                        LOGGER.info(" [-------->] Event Manual data >>>" + JSONConverter.toJSON(data));
                        List<String> canAccessUserIdList = new ArrayList<>();

                        //shift
                        if(data.getSite()!=null) {

                            canAccessUserIdList = getUserIdInShift(data.getSite().getSiteId(),now);
                            if(canAccessUserIdList == null){
                                canAccessUserIdList = new ArrayList<>();
                            }
                        }
                        List<String> adminUserIdList = getAdminUserIdListFromABACService();
                        if (canAccessUserIdList != null) {
                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                            set.addAll(adminUserIdList);
                            List<String> userIdCanReceiverList = new ArrayList<>(set);
                            if("rain".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"5");
                            } else if("landslide".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"6");
                            } else if("snow".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"7");
                            } else if("fog".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"8");
                            } else if("maintance".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"14");
                            } else if("brokendevice".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"24");
                            } else if("damaged_road".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"22");
                            } else if("destruction".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"21");
                            } else if("high_temperature".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"20");
                            }  else if("crowding".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"23");
                            } else if("illegalstop".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"25");
                            } else if("trafficjam".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"15");
                            } else if("finish_trafficjam".equalsIgnoreCase(request.getType())){
                                sendNotifyFinishTrafficJam(userIdCanReceiverList, data,"15");
                            } else if("explosion".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"29");
                            } else if("throw_stone".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"30");
                            }  else if("theft".equalsIgnoreCase(request.getType())){
                                sendNotify(userIdCanReceiverList, data,"31");
                            }

                        }

                        if(data.getSite()!=null) {
                            canAccessUserIdList = getCanAccessUserIdListFromIDService(data.getSite().getSiteId());
                            if(canAccessUserIdList == null){
                                canAccessUserIdList = new ArrayList<>();
                            }
                        }
//                            List<String> adminUserIdList = getAdminUserIdListFromABACService();
                        if (canAccessUserIdList != null) {
                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
                            set.addAll(adminUserIdList);
                            List<String> userIdCanReceiverList = new ArrayList<>(set);
                            if("rain".equalsIgnoreCase(request.getType())){
                                sendNotifyMap(userIdCanReceiverList, data,"5");
                            } else if("landslide".equalsIgnoreCase(request.getType())){
                                sendNotifyMap(userIdCanReceiverList, data,"6");
                            } else if("snow".equalsIgnoreCase(request.getType())){
                                sendNotifyMap(userIdCanReceiverList, data,"7");
                            } else if("fog".equalsIgnoreCase(request.getType())){
                                sendNotifyMap(userIdCanReceiverList, data,"8");
                            }
//                            else if("maintance".equalsIgnoreCase(request.getType())){
//                                sendNotify(userIdCanReceiverList, data,"14");
//                            }
                            else if("damaged_road".equalsIgnoreCase(request.getType())){
                                sendNotifyMap(userIdCanReceiverList, data,"22");
                            } else if("destruction".equalsIgnoreCase(request.getType())){
                                sendNotifyMap(userIdCanReceiverList, data,"21");
                            } else if("high_temperature".equalsIgnoreCase(request.getType())){
                                sendNotifyMap(userIdCanReceiverList, data,"20");
                            }  else if("crowding".equalsIgnoreCase(request.getType())){
                                sendNotifyMap(userIdCanReceiverList, data,"23");
                            } else if("illegalstop".equalsIgnoreCase(request.getType())) {
                                sendNotifyMap(userIdCanReceiverList, data, "25");
                            }
//                            } else if("trafficjam".equalsIgnoreCase(request.getType())){
//                                sendNotify(userIdCanReceiverList, data,"15");
//                            } else if("finish_trafficjam".equalsIgnoreCase(request.getType())){
//                                sendNotifyFinishTrafficJam(userIdCanReceiverList, data,"15");
//                            }
                        }
                    }
                }

            } else {
                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @RabbitListener(id = "processRecognition", queues = {"#{workerConfig.getRecognition()}"})
    public void workerReceiveRecognition(String json) {
        try {
            LOGGER.info(" [-->] Server received request for : {}", json);
            //Process here
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
            if (request != null && request.getData() != null) {
                if ("recognition".equalsIgnoreCase(request.getType())) {
                    LOGGER.info(request.getData().toString());
                    String message = mapper.writeValueAsString(request.getData());
                    RecognitionPlateDTO data = mapper.readValue(message, RecognitionPlateDTO.class);
                    if (data != null) {
                        LOGGER.info(" [-------->] RECOGNITION data >>>" + JSONConverter.toJSON(data));
                        sendRecognition(data);
                    }
                }else {
                    LOGGER.info("NotifyTriggerRequest request is null. No process here...");
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
    }


    private List<UserReceiverDTO> getUserListByGroupFromIDService(List<String> groupIdList) {
        //Body param
        Map<String, Object> bodyParamMap = new HashMap<>();
        bodyParamMap.put("groupUuids", groupIdList);

        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("POST");
        userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_INTERNAL_LIST_BYGROUP_URL);
        userRpcRequest.setBodyParam(bodyParamMap);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setHeaderParam(null);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
        LOGGER.info("getUserListByGroupFromIDService - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    List<UserReceiverDTO> userReceiverDTOList = mapper.readerFor(new TypeReference<List<UserReceiverDTO>>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return userReceiverDTOList;
                }
            } catch (Exception ex) {
                LOGGER.info("Error parse json in getUserListByGroupFromIDService from ID service: " + ex.toString());
                return null;
            }
        }
        return null;
    }

    private boolean checkAuthorizedSites(String userId, String siteId) {
        List<String> listUser = getUserListAuthorizedSites(siteId);
        System.out.println("userId = " + userId);
        if (listUser != null && !listUser.isEmpty()) {
            for (String s : listUser) {
                System.out.println("userIdInlist = " + s);
            }
            for (String userIdInList : listUser) {
                return userIdInList.equals(userId);
            }
        }
        return true;
    }

    private List<String> getUserListAuthorizedSites(String siteId) {
        //Body param
        Map<String, Object> bodyParamMap = new HashMap<>();
        bodyParamMap.put("siteId", siteId);
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("POST");
        userRpcRequest.setRequestPath("/user/authorizedSites");
        userRpcRequest.setBodyParam(bodyParamMap);
        userRpcRequest.setUrlParam("");
        userRpcRequest.setHeaderParam(null);
        userRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
        LOGGER.info("getUserListByGroupFromIDService - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    List<String> userReceiverDTOList = mapper.readerFor(new TypeReference<List<String>>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return userReceiverDTOList;
                }
            } catch (Exception ex) {
                LOGGER.info("Error parse json in getUserListByGroupFromIDService from ID service: " + ex.toString());
                return null;
            }
        }
        return null;
    }

    //Get list user admin id from rbac
    private List<String> getAdminUserIdListFromABACService() {
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("GET");
        rbacRpcRequest.setRequestPath(RabbitMQProperties.ABAC_RPC_ADMIN_URL);
        rbacRpcRequest.setBodyParam(null);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.ABAC_RPC_EXCHANGE,
                RabbitMQProperties.ABAC_RPC_QUEUE, RabbitMQProperties.ABAC_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("getAdminUserIdListFromABACService - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    List<String> adminUserIdDTOList = mapper.readerFor(new TypeReference<List<String>>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return adminUserIdDTOList;
                }
            } catch (Exception ex) {
                LOGGER.info("Error parse json in getAdminUserIdListFromABACService from ABAC service: " + ex.toString());
                return null;
            }
        }
        return null;
    }

    //Get list user admin id from rbac
    private List<String> getUserFromGroupId(String groupId) {
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("GET");
        rbacRpcRequest.setRequestPath("/v1.0/user/group/internal");
        rbacRpcRequest.setBodyParam(null);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setPathParam(groupId);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("getListFromABACService - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    List<String> adminUserIdDTOList = mapper.readerFor(new TypeReference<List<String>>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return adminUserIdDTOList;
                }
            } catch (Exception ex) {
                LOGGER.info("Error parse json in getAdminUserIdListFromABACService from ABAC service: " + ex.toString());
                return null;
            }
        }
        return null;
    }

    //Get can access camera user id from ID
    private List<String> getCanAccessUserIdListFromIDService(String siteId) {
        //Set body param
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("GET");
        rbacRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_SITE_LIST_URL);
        rbacRpcRequest.setBodyParam(null);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setPathParam(siteId);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("getCanAccessUserIdListFromIDService - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    List<String> canAccessUserIdDTOList = mapper.readerFor(new TypeReference<List<String>>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return canAccessUserIdDTOList;
                }
            } catch (Exception ex) {
                LOGGER.info("Error parse json in getCanAccessUserIdListFromIDService from ID service: " + ex.toString());
                return null;
            }
        }
        return null;
    }

    private List<String> getUserIdInShift(String siteId,String time) {
        //Set body param
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("GET");
        Map<String,String> tmp = new HashMap<>();
        tmp.put("siteId",siteId);
        tmp.put("date",time);
        String urlParam = StringUtil.generateMapString(tmp);
        rbacRpcRequest.setRequestPath("/v1.0/shift/user/list-user-on-shift");
        rbacRpcRequest.setBodyParam(null);
        rbacRpcRequest.setUrlParam(urlParam);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setPathParam(siteId);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.SHIFT_RPC_EXCHANGE,
                RabbitMQProperties.SHIFT_RPC_QUEUE, RabbitMQProperties.SHIFT_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("getCanAccessUserIdListFromIDService - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    List<String> canAccessUserIdDTOList = mapper.readerFor(new TypeReference<List<String>>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return canAccessUserIdDTOList;
                }
            } catch (Exception ex) {
                LOGGER.info("Error parse json in getCanAccessUserIdListFromIDService from ID service: " + ex.toString());
                return null;
            }
        }
        return null;
    }

    private List<String> getCanAccessUserIdListFromUnits(List<String> units) {
        //Set body param
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("POST");
        rbacRpcRequest.setRequestPath("/v1.0/user/listUnit/internal");
        Map<String,Object> body = new HashMap<>();
        body.put("units",units);
        rbacRpcRequest.setBodyParam(body);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setPathParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("getCanAccessUserIdListFromIDService - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    List<String> canAccessUserIdDTOList = mapper.readerFor(new TypeReference<List<String>>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return canAccessUserIdDTOList;
                }
            } catch (Exception ex) {
                LOGGER.info("Error parse json in getCanAccessUserIdListFromIDService from ID service: " + ex.toString());
                return null;
            }
        }
        return null;
    }

    private List<String> getCanAccessUserIdListFromIDServiceStage(String stageCode) {
        //Set body param
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("GET");
        rbacRpcRequest.setRequestPath("/v1.0/user/internal/stage/url");
        rbacRpcRequest.setBodyParam(null);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setPathParam(stageCode);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("getCanAccessUserIdListFromIDService - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    List<String> canAccessUserIdDTOList = mapper.readerFor(new TypeReference<List<String>>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return canAccessUserIdDTOList;
                }
            } catch (Exception ex) {
                LOGGER.info("Error parse json in getCanAccessUserIdListFromIDService from ID service: " + ex.toString());
                return null;
            }
        }
        return null;
    }

    private Map<String, Object> putBodyParamSecurity(String userId, SecurityDTO securityDTO, String type) {

        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        bodyParam.put("content", "Thời gian: "+  simpleDateFormat.format( securityDTO.getStartTime())+ "\n"  + "Vị trí: " + securityDTO.getSite().getSiteName() + " " + securityDTO.getDirectionCode());
        bodyParam.put("title", securityDTO.getEventName());
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bodyParam.put("startTime", simpleDateFormat.format( securityDTO.getStartTime()));
        bodyParam.put("objectType", type);
        bodyParam.put("objectUuid", securityDTO.getEventId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", securityDTO.getSite().getSiteId());
        return bodyParam;
    }

    private Map<String, Object> putBodyParamVMSBoard(String userId,VMSBoardNotify vmsBoardNotify) {

        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        bodyParam.put("content", "Tên thiết bị: " + vmsBoardNotify.getVmsBoard().getName()+ "\n"+ "Tên bản tin: " +vmsBoardNotify.getDisplayScriptName()+ "\n"+ "Thời gian: "+  simpleDateFormat.format( vmsBoardNotify.getTime())+ "\n"  + vmsBoardNotify.getErrorMessage() );
        bodyParam.put("title", "VMS - Cập nhập biển báo lỗi");
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bodyParam.put("startTime", simpleDateFormat.format( vmsBoardNotify.getTime()));
        bodyParam.put("objectType", "26");
        bodyParam.put("objectUuid", vmsBoardNotify.getVmsBoard().getId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", vmsBoardNotify.getVmsBoard().getSiteId().getId());
        return bodyParam;
    }

    private Map<String, Object> putBodyParamVMSBoardDisplay(String userId,DisplayVMSBoardNotify vmsBoardNotify) {

        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if(vmsBoardNotify.getStatus().equals("OUT_OF_DATE")){
            bodyParam.put("title", "VMS - Hết thời gian hiển thị bản tin");
            bodyParam.put("objectType", "27");
            bodyParam.put("content", "Tên thiết bị: " + vmsBoardNotify.getVmsBoard().getName()+ "\n"+ "Tên bản tin: " +vmsBoardNotify.getName()+ "\n"+ "Thời gian bắt đầu: "+  vmsBoardNotify.getStartTime()+ "\n"  + "Thời gian kết thúc: "+  vmsBoardNotify.getEndTime() );
        } else {
            bodyParam.put("content", "Tên thiết bị: " + vmsBoardNotify.getVmsBoard().getName()+ "\n"+ "Tên bản tin: " +vmsBoardNotify.getName() +"\n"+ "Thời gian : "+  simpleDateFormat.format(now));
            bodyParam.put("title", "VMS - Gia hạn thời gian hiển thị bản tin");
            bodyParam.put("objectType", "28");
        }

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bodyParam.put("startTime", simpleDateFormat.format( now));

        bodyParam.put("objectUuid", vmsBoardNotify.getVmsBoard().getId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", vmsBoardNotify.getVmsBoard().getSiteId().getId());
        return bodyParam;
    }

    private Map<String, Object> putBodyParamSecurity(String userId, Event securityDTO, String type) {

        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        bodyParam.put("content", "Thời gian: " +  simpleDateFormat.format( securityDTO.getStartTime())+ "\n"  + "Vị trí: "+  securityDTO.getSite().getSiteName() + " " + securityDTO.getDirectionCode());
        bodyParam.put("title", securityDTO.getEventName());
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bodyParam.put("startTime",simpleDateFormat.format( securityDTO.getStartTime()));
        bodyParam.put("objectType", type);
        bodyParam.put("objectUuid", securityDTO.getParentId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", securityDTO.getSite().getSiteId());
        return bodyParam;
    }

    private Map<String, Object> putBodyParamSecurityMap(String userId, SecurityDTO securityDTO, String type, Point point) {

        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        Map<String,Object> content = new HashMap<>();
        content.put("startTime",securityDTO.getStartTime());
        content.put("event","SECURITY");
        content.put("eventCode",securityDTO.getEventCode());
        content.put("eventIdString",securityDTO.getEventId());
        content.put("eventName",securityDTO.getEventName());
        content.put("eventStatus",securityDTO.getEventStatus());
        content.put("imageUrl",securityDTO.getImageUrl());
        content.put("site",securityDTO.getSite());
        content.put("sourceId",securityDTO.getSourceId());
        content.put("sourceName",securityDTO.getSourceName());
        content.put("longitude",point.getLongitude());
        content.put("latitude",point.getLatitude());
        content.put("startPositionM",securityDTO.getSite().getPositionM());
        content.put("directionCode",securityDTO.getDirectionCode());
        content.put("display",1);
        bodyParam.put("content", content);
        bodyParam.put("title", securityDTO.getEventName());
        bodyParam.put("startTime",securityDTO.getStartTime());
        bodyParam.put("objectType", type);
        bodyParam.put("objectUuid", securityDTO.getEventId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", securityDTO.getSite().getSiteId());
        bodyParam.put("position",1);

        return bodyParam;
    }

    private Map<String, Object> putBodyParamSecurityMap(String userId, Event securityDTO, String type, Point point) {

        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        Map<String,Object> content = new HashMap<>();
        content.put("startTime",securityDTO.getStartTime());
        content.put("event","SECURITY");
        content.put("eventCode",securityDTO.getEventCode());
        content.put("eventIdString",securityDTO.getParentId());
        content.put("eventName",securityDTO.getEventName());
        content.put("eventStatus",securityDTO.getEventStatus());
        content.put("imageUrl",securityDTO.getImageUrl());
        content.put("site",securityDTO.getSite());
        content.put("sourceId",securityDTO.getSourceId());
        content.put("sourceName",securityDTO.getSourceName());
        content.put("longitude",point.getLongitude());
        content.put("latitude",point.getLatitude());
        if(securityDTO.getEventStatus().code()==EventStatus.INCORRECT.code()){
        content.put("display",0);
        } else {
            content.put("display",1);
        }
        content.put("directionCode",securityDTO.getDirectionCode());
        content.put("startPositionM",securityDTO.getSite().getPositionM());
        bodyParam.put("content", content);
        bodyParam.put("title", securityDTO.getEventName());
        bodyParam.put("startTime",securityDTO.getStartTime());
        bodyParam.put("objectType", type);
        bodyParam.put("objectUuid", securityDTO.getParentId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", securityDTO.getSite().getSiteId());
        bodyParam.put("position",1);
        return bodyParam;
    }

    private Map<String, Object> putBodyParamSecurityMap(String userId, String eventId) {

        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        Map<String,Object> content = new HashMap<>();
        content.put("display",0);
        content.put("eventIdString",eventId);
        bodyParam.put("content", content);
        bodyParam.put("title", "Kết thúc sự kiện");
        bodyParam.put("objectUuid", eventId);
        bodyParam.put("userId", userId);
        bodyParam.put("objectType","5");
        bodyParam.put("position",1);
        return bodyParam;
    }

    private void sendSecurity(List<String> userIdList, SecurityDTO securityDTO, String type) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                Point point = itsSiteService.getPoint(securityDTO.getSite().getSiteId(),securityDTO.getDirectionCode());
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamSecurity(userId, securityDTO,type);
                    handleSendNotify(bodyParam);
                    bodyParam = putBodyParamSecurityMap(userId,securityDTO,type,point);
                    handleSendNotify(bodyParam);

                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendSecurityMap(List<String> userIdList, SecurityDTO securityDTO, String type) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                Point point = itsSiteService.getPoint(securityDTO.getSite().getSiteId(),securityDTO.getDirectionCode());
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamSecurityMap(userId,securityDTO,type,point);
                    handleSendNotify(bodyParam);

                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendVMSBoard(List<String> userIdList, VMSBoardNotify data) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamVMSBoard(userId,data);
                    handleSendNotify(bodyParam);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendVMSBoardDisplay(List<String> userIdList, DisplayVMSBoardNotify data) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamVMSBoardDisplay(userId,data);
                    handleSendNotify(bodyParam);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendSecurity(List<String> userIdList, Event securityDTO, String type) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                Point point = itsSiteService.getPoint(securityDTO.getSite().getSiteId(),securityDTO.getDirectionCode());
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamSecurity(userId, securityDTO,type);
                    handleSendNotify(bodyParam);
                    bodyParam = putBodyParamSecurityMap(userId,securityDTO,type,point);
                    handleSendNotify(bodyParam);

                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendSecurityMap(List<String> userIdList, Event securityDTO, String type) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                Point point = itsSiteService.getPoint(securityDTO.getSite().getSiteId(),securityDTO.getDirectionCode());
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamSecurityMap(userId,securityDTO,type,point);
                    handleSendNotify(bodyParam);

                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private List<RoleUserDTO> getUserByListRolesABAC(List<String> listRolesCode) {
        RequestMessage rbacRpcRequest = new RequestMessage();
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("roles", listRolesCode);
        rbacRpcRequest.setRequestMethod("POST");
        rbacRpcRequest.setRequestPath(RabbitMQProperties.ABAC_RPC_USES_BY_ROLES_URL);
        rbacRpcRequest.setBodyParam(bodyParam);
        rbacRpcRequest.setUrlParam("");
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.ABAC_RPC_EXCHANGE,
                RabbitMQProperties.ABAC_RPC_QUEUE, RabbitMQProperties.ABAC_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("rolesDTOList - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    List<RoleUserDTO> roleUserDTOs = mapper.readerFor(new TypeReference<List<RoleUserDTO>>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return roleUserDTOs;
                }
            } catch (Exception ex) {
                LOGGER.info("Error parse json in rolesDTOList from RBAC service: " + ex.toString());
                return null;
            }
        }
        return null;
    }

    public List<String> getListUserIdReceiverNotifiesConfig(List<String> listRolesNotifiesConfig) {
        // Lấy danh user theo nhóm danh sách quyền
        List<RoleUserDTO> roleUserReceiver = getUserByListRolesABAC(listRolesNotifiesConfig);

        if (roleUserReceiver != null && !roleUserReceiver.isEmpty()) {
            List<String> listUserIdReceiverNotifiesConfig = roleUserReceiver.stream().map(user -> user.getUuidUser()).collect(Collectors.toList());
            return listUserIdReceiverNotifiesConfig;
        }
        return null;
    }

    private void sendNotify(List<String> userIdList, SecurityDTO litteringFlowsDTO) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamNotify(userId, litteringFlowsDTO);
                    handleSendNotify(bodyParam);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendNotify(List<String> userIdList, ScheduledEvent event, String type, String url, Integer check) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamNotify(userId, event, type,url,check);
                    handleSendNotify(bodyParam);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }
//    private void sendNotifyMap(List<String> userIdList, ScheduledEvent event, String type) {
//        try {
//            if (userIdList != null && !userIdList.isEmpty()) {
//                for (String userId : userIdList) {
//                    Map<String, Object> bodyParam = putBodyParamNotify(userId, event, type);
//                    putBodyParamNotify(userId, event, type);
//                    handleSendNotify(bodyParam);
//                }
//            }
//        } catch (Exception ex) {
//            LOGGER.error(ex.toString());
//        }
//    }

    private void sendNotify(List<String> userIdList, String eventId) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamSecurityMap(userId,eventId);
                    handleSendNotify(bodyParam);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendNotify(List<String> userIdList, Job job, NotifyRequest request, String url) {
        try {
            if(request.getActionCode().equals("CREATE") || request.getActionCode().equals("REQUEST_REVERIFYCATION") || request.getActionCode().equals("UPDATE")){
                Map<String, Object> bodyParamZalo = putBodyParamNotifyZalo("sdff",request,job,3);
                handleSendNotify(bodyParamZalo);
            }

            if (userIdList != null && !userIdList.isEmpty()) {
                for (String userId : userIdList) {
                    if(job.getJobType().equals("FORBIDDEN_WAY")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    } else if(job.getJobType().equals("CLOSE_LANE")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    }else if(job.getJobType().equals("CLOSE_OPEN_ENTRANCE_EXIT")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    }else if(job.getJobType().equals("LIMIT_SPEED")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    } else if(request.getEventCode().equals("CROWD")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    }  else if(request.getEventCode().equals("BROKENVEHICLE")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    } else if(request.getEventCode().equals("INVERSEDIRECTION")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    } else if(request.getEventCode().equals("ILLEGALSTOP")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    } else if(request.getEventCode().equals("RESTRICTED")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    } else if(request.getEventCode().equals("WRONGLINE")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    } else if(request.getEventCode().equals("SPEEDLIMIT")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    } else if(request.getEventCode().equals("NOENTRY")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    }  else if(request.getEventCode().equals("BARRIER")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    }  else if(request.getEventCode().equals("FIRE")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    }  else if(request.getEventCode().equals("RAIN")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    }  else if(request.getEventCode().equals("LANDSLIDE")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    }  else if(request.getEventCode().equals("SNOW")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    }  else if(request.getEventCode().equals("FOG")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    } else if(request.getEventCode().equals("CONSTRUCTION")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    } else if(request.getEventCode().equals("ACCIDENT")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    }  else if(request.getEventCode().equals("BROKENDEVICE")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    } else if(request.getEventCode().equals("MAINTANCE")){
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    } else {
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, job,"19",request,url);
                        handleSendNotify(bodyParam);
                    }

                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendNotifyMapJob(List<String> userIdList, Job job, NotifyRequest request, String url, String code) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                Point point = itsSiteService.getPoint(request.getSiteId(), job.getDirection());
                Point point2 = new Point();
                if(!StringUtil.isNullOrEmpty(job.getEndSiteId())){
                    point2 = itsSiteService.getPoint(job.getEndSiteId(),job.getDirection());
                }
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamNotifyMap(userId,job,"19",request,point, code, point2, url);
                    handleSendNotify(bodyParam);

                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendNotifyMapJobPlace(List<String> userIdList, Job job, NotifyRequest request, String url, String code) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                Map<String, List<PointPlace>>  pointPlaces = itsSiteService.getPointPlace(job.getPlaceId());
                if(pointPlaces.get(job.getPlaceId())!=null && !pointPlaces.get(job.getPlaceId()).isEmpty()) {
                    PointPlace point = pointPlaces.get(job.getPlaceId()).get(0);
//
//                    Point point2 = new Point();
//                    if (!StringUtil.isNullOrEmpty(job.getEndSiteId())) {
//                        point2 = itsSiteService.getPoint(job.getEndSiteId(), job.getDirection());
//                    }
                    for (String userId : userIdList) {
                        Map<String, Object> bodyParam = putBodyParamNotifyMapPlace(userId, job, "19", request,point , code, url);
                        handleSendNotify(bodyParam);

                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendNotifyMapJob(List<String> userIdList, ScheduledEvent event, String url, String code) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                Point point = itsSiteService.getPoint(event.getJob().getStartSiteId(), event.getJob().getDirection());
                Point point2 = new Point();
                if(!StringUtil.isNullOrEmpty(event.getJob().getEndSiteId())){
                    point2 = itsSiteService.getPoint(event.getJob().getEndSiteId(),event.getJob().getDirection());
                }
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamNotifyMap(userId,event,"19",point, code, point2, url);
                    handleSendNotify(bodyParam);

                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendNotifyMapEvent(List<String> userIdList, ScheduledEvent event, String type) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                Point point = itsSiteService.getPoint(event.getSiteId(), event.getDirectionCode());
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamNotifyMapEventSchedule(userId,event,type,point);
                    handleSendNotify(bodyParam);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendNotify(List<String> userIdList, TrafficStatusNotify trafficStatusNotify) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                for (String userId : userIdList) {
//                    Map<String, Object> bodyParam = putBodyParamNotify(userId, trafficStatusNotify);
//                    handleSendNotify(bodyParam);
                    Map<String, Object>  bodyParam = putBodyParamNotifyMap(userId,trafficStatusNotify);
                    handleSendNotify(bodyParam);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendNotify(List<String> userIdList, ViolationDetail violationDetail) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamNotify(userId, violationDetail);
                    handleSendNotify(bodyParam);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

//    private void sendNotify(List<String> userIdList, Event violationDetail) {
//        try {
//            if (userIdList != null && !userIdList.isEmpty()) {
//                for (String userId : userIdList) {
//                    Map<String, Object> bodyParam = putBodyParamNotify(userId, violationDetail);
//                    handleSendNotify(bodyParam);
//                }
//            }
//        } catch (Exception ex) {
//            LOGGER.error(ex.toString());
//        }
//    }


    private void sendNotify(List<String> userIdList, RecognitionPlateObjectTrackingDTO recognitionPlateObjectTrackingDTO) {
            try {
                if (userIdList != null && !userIdList.isEmpty()) {
                    for (String userId : userIdList) {
                        Integer type = 3;
                        if(recognitionPlateObjectTrackingDTO.getAlertChannel()!= null) {
                            for (String tmp : recognitionPlateObjectTrackingDTO.getAlertChannel()
                            ) {
                                if (tmp.equalsIgnoreCase("SMS")) {
                                    type = 1;
                                    Map<String, Object> bodyParam = putBodyParamNotify(userId, recognitionPlateObjectTrackingDTO, type);
                                    handleSendNotify(bodyParam);
                                } else if (tmp.equalsIgnoreCase("ZALO")) {
                                    type = 6;
                                    Map<String, Object> bodyParam = putBodyParamNotify(userId, recognitionPlateObjectTrackingDTO, type);
                                    handleSendNotify(bodyParam);
                                } else if (tmp.equalsIgnoreCase("EMAIL")) {
                                    type = 2;
                                    Map<String, Object> bodyParam = putBodyParamNotify(userId, recognitionPlateObjectTrackingDTO, type);
                                    handleSendNotify(bodyParam);
                                }

                            }
                        }
                        Map<String, Object> bodyParam = putBodyParamNotify(userId, recognitionPlateObjectTrackingDTO,3);
                        handleSendNotify(bodyParam);

                    }
                }
            } catch (Exception ex) {
                LOGGER.error(ex.toString());
            }
        }

    private void sendNotify(List<String> userIdList, Heartbeat heartbeat) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamNotify(userId, heartbeat);
                    handleSendNotify(bodyParam);
                    bodyParam  = putBodyParamNotifyMap(userId,heartbeat);
                    handleSendNotify(bodyParam);


                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendNotify(List<String> userIdList, Event event, String type) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                Point point = itsSiteService.getPoint(event.getSite().getSiteId(),event.getDirectionCode());
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamNotify(userId, event,type);
                    handleSendNotify(bodyParam);
//                    bodyParam = putBodyParamNotifyMap(userId,event,type,point);
//                    handleSendNotify(bodyParam);

                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }


    private void sendNotifyFinishTrafficJam(List<String> userIdList, Event event, String type) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamNotifyFinishTrafficJam(userId, event,type);
                    handleSendNotify(bodyParam);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private void sendNotifyMap(List<String> userIdList, Event event, String type) {
        try {
            if (userIdList != null && !userIdList.isEmpty()) {
                Point point = itsSiteService.getPoint(event.getSite().getSiteId(),event.getDirectionCode());
                for (String userId : userIdList) {
                    Map<String, Object> bodyParam = putBodyParamNotifyMap(userId,event,type,point);
                    handleSendNotify(bodyParam);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.toString());
        }
    }

    private Map<String, Object> putBodyParamNotify(String userId, SecurityDTO litteringFlowsDTO) {
        LOGGER.info("Send web notify 2 user: {} => {}", userId, "Phát hiện Xả rác tại nút giao " + litteringFlowsDTO.getSite().getSiteAddress());
        Map<String, Object> bodyParam = new HashMap<>();
        String violationJson = new Gson().toJson(litteringFlowsDTO);
        bodyParam.put("type", 3);
        bodyParam.put("content", "Vị trí: " + litteringFlowsDTO.getSite().getSiteName());
        bodyParam.put("title", litteringFlowsDTO.getEventName());
        bodyParam.put("startTime",litteringFlowsDTO.getStartTime());
        bodyParam.put("url", litteringFlowsDTO.getEventId());
        bodyParam.put("objectType", litteringFlowsDTO.getEventCode()); // 17: Xả rác
        bodyParam.put("objectUuid", violationJson);
        bodyParam.put("userId", userId);
        return bodyParam;
    }

    private Map<String, Object> putBodyParamNotify(String userId, ScheduledEvent scheduledEvent, String type, String url, Integer check) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        bodyParam.put("content", "Sự kiện lên lịch : " + scheduledEvent.getTitle()+
                "\n" + "Vị trí: " + scheduledEvent.getSiteName() +
                "\n" + "Thời gian bắt đầu: " +
                "\n" + simpleDateFormat.format(scheduledEvent.getStartTime()) );
        bodyParam.put("title", "Sự kiện theo kế hoạch - " + type);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bodyParam.put("startTime",simpleDateFormat.format(scheduledEvent.getStartTime()));
        bodyParam.put("objectType", "13");
        Job job = scheduledEvent.getJob();
        if(job.getEventId()!=null) {
            bodyParam.put("objectUuid", job.getEventId());
            bodyParam.put("url",url);
        } else {
            bodyParam.put("objectUuid", scheduledEvent.getId());
            bodyParam.put("url", schedule);
        }
//        bodyParam.put("url",urlMap);
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", scheduledEvent.getSiteId());
        return bodyParam;
    }

//    private Map<String, Object> putBodyParamNotifyMap(String userId, ScheduledEvent scheduledEvent, String type) {
//        Point point = itsSiteService.getPoint(scheduledEvent.getSiteId(),scheduledEvent.get)
//        Map<String, Object> bodyParam = new HashMap<>();
//        bodyParam.put("type", 3);
//        Map<String,Object> content = new HashMap<>();
//        content.put("startTime",event.getStartTime());
//        content.put("event","MANUAL");
//        content.put("eventCode",event.getEventCode());
//        content.put("eventIdString",event.getParentId());
//        content.put("eventName",event.getEventName());
//        content.put("eventStatus",event.getEventStatus());
//        content.put("imageUrl",event.getImageUrl());
//        content.put("site",event.getSite());
//        content.put("sourceId",event.getSourceId());
//        content.put("sourceName",event.getSourceName());
//        content.put("longitude",point.getLongitude());
//        content.put("latitude",point.getLatitude());
//        content.put("display",1);
//        bodyParam.put("content", content);
//        bodyParam.put("title", event.getEventName());
//        bodyParam.put("startTime",event.getStartTime());
//        bodyParam.put("objectType", type);
//        bodyParam.put("objectUuid", event.getId());
//        bodyParam.put("userId", userId);
//        bodyParam.put("siteId", event.getSite().getSiteId());
//        bodyParam.put("position",1);
//        return bodyParam;
//    }

    private Map<String, Object> putBodyParamNotify(String userId, Job job, String type, NotifyRequest notifyRequest, String urlMap) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String content = "";
        content += "Tên công việc : " + notifyRequest.getJobName()+
                "\n" + "Vị trí: " + notifyRequest.getSiteName();
        if(!StringUtil.isNullOrEmpty(notifyRequest.getEndSiteName())){
            content += " - " + notifyRequest.getEndSiteName();
        }
        if(!StringUtil.isNullOrEmpty(job.getPlaceName())){
            content += " " + job.getPlaceName();
        }
        content +=  "\n" +"Thời gian: " + simpleDateFormat.format(notifyRequest.getActionTime()) + "\n"
                + "Người thực hiện : " + notifyRequest.getActor();

        bodyParam.put("content", content);
        bodyParam.put("title", notifyRequest.getEventName() +" - " +  notifyRequest.getActionName());
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bodyParam.put("startTime",simpleDateFormat.format(job.getEventStartTime()));
        bodyParam.put("objectType", type);
        bodyParam.put("objectUuid", job.getEventId());
        if(job.getJobType().equals("FORBIDDEN_WAY") || job.getJobType().equals("CLOSE_LANE")||
                job.getJobType().equals("CLOSE_OPEN_ENTRANCE_EXIT")||job.getJobType().equals("LIMIT_SPEED")) {
            bodyParam.put("jobId", job.getId());
        } else {
            bodyParam.put("jobId","1");
        }
        bodyParam.put("url",urlMap);
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", notifyRequest.getSiteId());
        return bodyParam;
    }

    private Map<String, Object> putBodyParamNotifyMap(String userId, Job job, String type, NotifyRequest notifyRequest, Point point, String code, Point point2 , String url) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        Map<String,Object> content = new HashMap<>();
        content.put("siteId1",job.getStartSiteId());
        content.put("siteId2",job.getEndSiteId());
        content.put("eventCode",code);
        content.put("jobId",job.getId());
        content.put("jobType",job.getJobType());
        content.put("longitude1",point.getLongitude());
        content.put("latitude1",point.getLatitude());
        content.put("longitude2",point2.getLongitude());
        content.put("latitude2",point2.getLatitude());
        content.put("startPositionM",notifyRequest.getStartPositionM());
        content.put("endPositionM",notifyRequest.getEndPositionM());
        content.put("placeId",job.getPlaceId());
        content.put("directionCode",job.getDirection());
        if(job.getJobType().equals("LIMIT_SPEED")){
            content.put("speedLimit",job.getLimitSpeed());
        }
        if("FINISH".equalsIgnoreCase(notifyRequest.getActionCode())
                || "CANCEL".equalsIgnoreCase(notifyRequest.getActionCode()))
            content.put("display",0);
        else
            content.put("display",1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        content.put("startTime",simpleDateFormat.format(job.getEventStartTime()));
        bodyParam.put("content", content);
        bodyParam.put("title", notifyRequest.getEventName() +" - " +  notifyRequest.getActionName());
        bodyParam.put("startTime",simpleDateFormat.format(job.getEventStartTime()));
        bodyParam.put("objectType", type);
        bodyParam.put("objectUuid",job.getEventId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", notifyRequest.getSiteId());
        bodyParam.put("url",url);
        bodyParam.put("position",1);
        return bodyParam;
    }

    private Map<String, Object> putBodyParamNotifyMapPlace(String userId, Job job, String type, NotifyRequest notifyRequest, PointPlace point, String code, String url) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        Map<String,Object> content = new HashMap<>();
        content.put("siteId1",job.getStartSiteId());
        content.put("siteId2",job.getEndSiteId());
        content.put("eventCode",code);
        content.put("jobId",job.getId());
        content.put("jobType",job.getJobType());
        content.put("longitude1",point.getLongitude());
        content.put("latitude1",point.getLatitude());
        content.put("startPositionM",notifyRequest.getStartPositionM());
        content.put("endPositionM",notifyRequest.getEndPositionM());
        content.put("placeId",job.getPlaceId());
//        content.put("longitude2",point2.getLongitude());
//        content.put("latitude2",point2.getLatitude());
        if("FINISH".equalsIgnoreCase(notifyRequest.getActionCode())
                || "CANCEL".equalsIgnoreCase(notifyRequest.getActionCode()))
            content.put("display",0);
        else
            content.put("display",1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        content.put("startTime",simpleDateFormat.format(job.getEventStartTime()));
        bodyParam.put("content", content);
        bodyParam.put("title", notifyRequest.getEventName() +" - " +  notifyRequest.getActionName());
        bodyParam.put("startTime",simpleDateFormat.format(job.getEventStartTime()));
        bodyParam.put("objectType", type);
        bodyParam.put("objectUuid",job.getEventId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", notifyRequest.getSiteId());
        bodyParam.put("url",url);
        bodyParam.put("position",1);
        return bodyParam;
    }

    private Map<String, Object> putBodyParamNotifyMap(String userId, ScheduledEvent scheduledEvent, String type, Point point, String code, Point point2 , String url) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        Map<String,Object> content = new HashMap<>();
        content.put("siteId1",scheduledEvent.getJob().getStartSiteId());
        content.put("siteId2",scheduledEvent.getJob().getEndSiteId());
        content.put("eventCode",code);
        content.put("jobId",scheduledEvent.getJob().getId());
        content.put("longitude1",point.getLongitude());
        content.put("latitude1",point.getLatitude());
        content.put("longitude2",point2.getLongitude());
        content.put("latitude2",point2.getLatitude());
        content.put("startPositionM",scheduledEvent.getJobStartPositionM());
        content.put("endPositionM",scheduledEvent.getJobEndPositionM());
        content.put("plateId",scheduledEvent.getJob().getPlaceId());
        content.put("directionCode",scheduledEvent.getJob().getDirection());
//        content.put("placeId",scheduledEvent.get);
        content.put("display",1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        content.put("startTime",simpleDateFormat.format(scheduledEvent.getStartTime()));
        bodyParam.put("content", content);
        bodyParam.put("title", "title");
        bodyParam.put("startTime",simpleDateFormat.format(scheduledEvent.getStartTime()));
        bodyParam.put("objectType", type);
        bodyParam.put("objectUuid",scheduledEvent.getJob().getEventId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId",scheduledEvent.getJob().getStartSiteId() );
        bodyParam.put("url",url);
        bodyParam.put("position",1);
        return bodyParam;
    }

    private Map<String, Object> putBodyParamNotifyMapEventSchedule(String userId, ScheduledEvent scheduledEvent, String type, Point point) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        Map<String,Object> content = new HashMap<>();
        content.put("startTime",scheduledEvent.getStartTime());
        content.put("event","SECURITY");
        content.put("eventCode",scheduledEvent.getEventType());
        content.put("eventIdString",scheduledEvent.getJob().getEventId());
        content.put("eventName","");
        content.put("eventStatus", "PROCESSING");
        content.put("imageUrl","sfd");
        content.put("site",scheduledEvent.getSiteId());
        content.put("longitude",point.getLongitude());
        content.put("latitude",point.getLatitude());
        content.put("display",1);
        content.put("startPositionM",scheduledEvent.getEventPositionM());
        content.put("directionCode",scheduledEvent.getDirectionCode());
        bodyParam.put("content", content);
        bodyParam.put("title", "ghjf");
        bodyParam.put("startTime",scheduledEvent.getStartTime());
        bodyParam.put("objectType", type);
        bodyParam.put("objectUuid", scheduledEvent.getJob().getEventId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", scheduledEvent.getSiteId());
        bodyParam.put("position",1);
        return bodyParam;
    }

    private Map<String, Object> putBodyParamNotify(String userId, RecognitionPlateObjectTrackingDTO data, Integer type) {
        Map<String, Object> bodyParam = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        bodyParam.put("type", type);
        bodyParam.put("content", "Thời gian: " + simpleDateFormat.format(data.getStartTime()) + "\n" + "Biển số: " + data.getPlate() + "\n" + "Vị trí: "+  data.getSite().getSiteName());
        bodyParam.put("title", "Phát hiện đối tượng theo dõi");
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bodyParam.put("startTime",simpleDateFormat.format(data.getStartTime()));
        bodyParam.put("objectType", "16");
        bodyParam.put("objectUuid", data.getId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", data.getSite().getSiteId());
        return bodyParam;
    }
    private Map<String, Object> putBodyParamNotifyZalo(String userId,NotifyRequest notifyRequest, Job data, Integer type) {
        Map<String, Object> bodyParam = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        bodyParam.put("type", 6);
        String content = notifyRequest.getActor() + " " + notifyRequest.getActionName()+ "\n"+
                "Thông tin sự kiện:\n" + "- Loại sự kiện: "+ notifyRequest.getEventName()+
                "\n"+
                "- Mã sự kiện: " + notifyRequest.getEventKey()+
                "\n"+
                "- Vị trí: " + notifyRequest.getEventSite() + " " + notifyRequest.getDirectionCode() + "\n- Vị trí chính xác: " ;
        if(!StringUtil.isNullOrEmpty(notifyRequest.getEventCorrectSite())){
            content += notifyRequest.getEventCorrectSite();
        }

        content += "\n- Thời gian: " + simpleDateFormat.format(data.getStartTime()) ;


        content += "\n- Mô tả: " ;
        if(!StringUtil.isNullOrEmpty(notifyRequest.getEventNote())){
            content+= notifyRequest.getEventNote();
        }
        content += "\nThông tin công việc: \n" + "- Loại công việc: " + JobType.parse(data.getJobType()).description()+ "\n"+
                "- Vị trí: " + notifyRequest.getSiteName() + " " + notifyRequest.getDirectionCode() ;
        if(!StringUtil.isNullOrEmpty(notifyRequest.getEndSiteName())){
            content += " - " + notifyRequest.getEndSiteName()+ " " +notifyRequest.getDirectionCode() ;
        }
        content +=  "\n- Đội: " + notifyRequest.getGroupName() ;
        if(StringUtil.isNullOrEmpty(notifyRequest.getListUsers())){
            content += "\n- Người xử lý: ";
        } else {
            content += "\n- Người xử lý: "+ notifyRequest.getListUsers();
        }
        content += "\n- Độ ưu tiên: " + data.getPriority().description() ;
//        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        if(data.getEndTime()!=null){
            content += "\n- Ngày hết hạn: " + simpleDateFormat.format(data.getEndTime()) ;
        } else {
            content += "\n- Ngày hết hạn: ";
        }
        if(StringUtil.isNullOrEmpty(data.getDescription())) {
            content += "\n- Mô tả: ";
        } else {
            Document doc = Jsoup.parse(data.getDescription());
            String text = doc.text();
            content += "\n- Mô tả: " + text;
        }
        bodyParam.put("content", content);
        bodyParam.put("title", "Phát hiện đối tượng theo dõi");
//        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bodyParam.put("startTime",simpleDateFormat.format(data.getStartTime()));
        bodyParam.put("objectType", "16");
        bodyParam.put("objectUuid", data.getId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", "sdfsdf");
        if(StringUtil.isNullOrEmpty(notifyRequest.getEventImage())){
            bodyParam.put("url",urlImageDefault+ "/img/no_preview.625d1aff.jpg");
        } else {
            String image;
            if(notifyRequest.getEventImage().indexOf(",")>0) {
                image = notifyRequest.getEventImage().substring(0, notifyRequest.getEventImage().indexOf(","));
            } else {
                image = notifyRequest.getEventImage();
            }
            if(image.indexOf("http") >=0){
                bodyParam.put("url",image);
            }else {
                bodyParam.put("url",urlImage +  image);
            }

        }
        bodyParam.put("urlTitle","Xử lý công việc");
        bodyParam.put("urlLink",urlZalo + data.getId());
        return bodyParam;
    }

    private Map<String, Object> putBodyParamNotifyZalo(String userId,ScheduledEvent scheduledEvent, ScheduleEventNotify notifyRequest, Integer type) {
        Map<String, Object> bodyParam = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        bodyParam.put("type", 6);
        String content = notifyRequest.getActor() + " " + notifyRequest.getActionName()+ "\n"+
                "Thông tin sự kiện:\n" + "- Loại sự kiện: "+ notifyRequest.getEventName()+
                "\n"+
                "- Mã sự kiện: " + notifyRequest.getEventKey()+
                "\n"+
                "- Vị trí: " + scheduledEvent.getSiteName() + " " + scheduledEvent.getDirectionCode() + "\n- Vị trí chính xác: " ;
//        if(!StringUtil.isNullOrEmpty(notifyRequest.getEventCorrectSite())){
//            content += notifyRequest.getEventCorrectSite();
//        }

        content += "\n- Thời gian: " + simpleDateFormat.format(scheduledEvent.getStartTime()) ;


        content += "\n- Mô tả: " ;
        if(!StringUtil.isNullOrEmpty(scheduledEvent.getDescription())){
            content+= scheduledEvent.getDescription();
        }
        content += "\nThông tin công việc: \n" + "- Loại công việc: " + JobType.parse(scheduledEvent.getJob().getJobType()).description()+ "\n"+
                "- Vị trí: " + notifyRequest.getJobStartSiteName() + " " + scheduledEvent.getDirectionCode() ;
        if(!StringUtil.isNullOrEmpty(notifyRequest.getJobEndSiteName())){
            content += " - " + notifyRequest.getJobEndSiteName()+ " " + scheduledEvent.getDirectionCode() ;
        }
        content +=  "\n- Đội: " + notifyRequest.getGroupName() ;
        if(StringUtil.isNullOrEmpty(notifyRequest.getListUsers())){
            content += "\n- Người xử lý: ";
        } else {
            content += "\n- Người xử lý: "+ notifyRequest.getListUsers();
        }
        content += "\n- Độ ưu tiên: " + scheduledEvent.getJob().getPriority().description() ;
//        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        if(scheduledEvent.getJob().getEndTime()!=null){
            content += "\n- Ngày hết hạn: " + simpleDateFormat.format(scheduledEvent.getJob().getEndTime()) ;
        } else {
            content += "\n- Ngày hết hạn: ";
        }
        if(StringUtil.isNullOrEmpty(scheduledEvent.getJob().getDescription())) {
            content += "\n- Mô tả: ";
        } else {
            Document doc = Jsoup.parse(scheduledEvent.getJob().getDescription());
            String text = doc.text();
            content += "\n- Mô tả: " + text;
        }
        bodyParam.put("content", content);
        bodyParam.put("title", "Phát hiện đối tượng theo dõi");
//        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bodyParam.put("startTime",simpleDateFormat.format(scheduledEvent.getJob().getStartTime()));
        bodyParam.put("objectType", "16");
        bodyParam.put("objectUuid", scheduledEvent.getJob().getId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", "sdfsdf");
        bodyParam.put("url",urlImageDefault+ "/img/no_preview.625d1aff.jpg");
//        if(StringUtil.isNullOrEmpty(notifyRequest.getEventImage())){
//            bodyParam.put("url",urlImageDefault+ "/img/no_preview.625d1aff.jpg");
//        } else {
//            String image;
//            if(notifyRequest.getEventImage().indexOf(",")>0) {
//                image = notifyRequest.getEventImage().substring(0, notifyRequest.getEventImage().indexOf(","));
//            } else {
//                image = notifyRequest.getEventImage();
//            }
//            if(image.indexOf("http") >=0){
//                bodyParam.put("url",image);
//            }else {
//                bodyParam.put("url",urlImage +  image);
//            }
//
//        }
        bodyParam.put("urlTitle","Xử lý công việc");
        bodyParam.put("urlLink",urlZalo + scheduledEvent.getJob().getId());
        return bodyParam;
    }


//    private Map<String, Object> putBodyParamNotify(String userId, TrafficStatusNotify trafficStatusNotify) {
//        Map<String, Object> bodyParam = new HashMap<>();
//        bodyParam.put("type", 3);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        Date date = new Date();
//        bodyParam.put("content","Trạng thái : "+  trafficStatusNotify.getNewTrafficStatus().getStatusName() + "\n " + "Thời gian: "+ simpleDateFormat.format(date)
//        + "\n " + "Đoạn đường : " + trafficStatusNotify.getStage().getName() + " "+ trafficStatusNotify.getStage().getDirectionCode());
//        bodyParam.put("title", "Mật độ giao thông");
//        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        bodyParam.put("startTime",simpleDateFormat.format(date));
//        bodyParam.put("objectType", "15");
//        bodyParam.put("status",trafficStatusNotify.getNewTrafficStatus().getStatusCode());
//        bodyParam.put("stageId",trafficStatusNotify.getStage().getId());
//        bodyParam.put("statusCode",trafficStatusNotify.getNewTrafficStatus().getStatusCode());
//        bodyParam.put("stageCode",trafficStatusNotify.getStage().getCode());
//
//        bodyParam.put("userId", userId);
//        if(!StringUtil.isNullOrEmpty(trafficStatusNotify.getEventId())){
//            bodyParam.put("startTime",simpleDateFormat.format(trafficStatusNotify.getStartTime()));
//            bodyParam.put("objectUuid", trafficStatusNotify.getEventId());
//            bodyParam.put("url","");
//        } else {
//            bodyParam.put("startTime",simpleDateFormat.format(date));
//            bodyParam.put("objectUuid", trafficStatusNotify.getStage().getId());
//            String url = "map-detail/ " + trafficStatusNotify.getStage().getId() + "?code=" + trafficStatusNotify.getStage().getCode();
//            bodyParam.put("url",url);
//        }
//
//        bodyParam.put("position",0);
//        return bodyParam;
//    }

    private Map<String, Object> putBodyParamNotifyMap(String userId, TrafficStatusNotify trafficStatusNotify) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        Map<String, Object> content = new HashMap<>();
        Date date = new Date();
        content.put("listSiteId",trafficStatusNotify.getListSiteId());
        content.put("directionCode",trafficStatusNotify.getDirectionCode());
        content.put("startPositionM",trafficStatusNotify.getStartPositionM());
        content.put("endPositionM",trafficStatusNotify.getEndPositionM());
        content.put("statusCode",trafficStatusNotify.getNewTrafficStatus().getStatusCode());
        bodyParam.put("content", content);
        bodyParam.put("title", trafficStatusNotify.getNewTrafficStatus().getStatusName());
        bodyParam.put("startTime",date);
        bodyParam.put("objectType", "15");
        bodyParam.put("url",urlMap);
        bodyParam.put("userId", userId);
        bodyParam.put("position",1);
        return bodyParam;
    }

    private Map<String, Object> putBodyParamNotify(String userId, ViolationDetail violationDetail) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        bodyParam.put("content", simpleDateFormat.format(violationDetail.getStartTime()) + "\n" + "Biển số: " + violationDetail.getPlate() + "\n" + violationDetail.getSite().getSiteName() + " " + violationDetail.getDirectionCode());
        bodyParam.put("title", violationDetail.getEventName());
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bodyParam.put("startTime",simpleDateFormat.format( violationDetail.getStartTime()));
        bodyParam.put("objectType", "5");
        bodyParam.put("objectUuid", violationDetail.getEventId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", violationDetail.getSite().getSiteId());
        return bodyParam;
    }


    private Map<String, Object> putBodyParamNotify(String userId, Heartbeat heartbeat) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        if(heartbeat.getStatus()==2){
            bodyParam.put("title", "Thiết bị kết nối lại");
            bodyParam.put("objectType", "18");
        } else if(heartbeat.getStatus()==1){
            bodyParam.put("title", "Thiết bị mất kết nối");
            bodyParam.put("objectType", "17");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        bodyParam.put("content","Thời gian: "+ simpleDateFormat.format(heartbeat.getStartTime()) + "\n"+ "Thiết bị : " + heartbeat.getDeviceName());
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bodyParam.put("startTime",simpleDateFormat.format( heartbeat.getStartTime()));
        bodyParam.put("url",urlMap);
        bodyParam.put("objectUuid", heartbeat.getDeviceId());
        bodyParam.put("userId", userId);
        return bodyParam;
    }

    private Map<String, Object> putBodyParamNotifyMap(String userId, Heartbeat heartbeat) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        Map<String,Object> content = new HashMap<>();
        if(heartbeat.getStatus()==2){
            bodyParam.put("title", "Thiết bị kết nối lại");
            bodyParam.put("objectType", "18");
        } else if(heartbeat.getStatus()==1){
            bodyParam.put("title", "Thiết bị mất kết nối");
            bodyParam.put("objectType", "17");
        }
        content.put("deviceId",heartbeat.getDeviceId());
        content.put("status",heartbeat.getStatus());
        bodyParam.put("content", content);
        bodyParam.put("startTime",heartbeat.getStartTime());
        bodyParam.put("url",urlMap);
        bodyParam.put("objectUuid", heartbeat.getDeviceId());
        bodyParam.put("userId", userId);
        bodyParam.put("position",1);
        return bodyParam;
    }

    private Map<String, Object> putBodyParamChangeStatus( Heartbeat heartbeat) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("cameraId", heartbeat.getDeviceId());
        bodyParam.put("status", heartbeat.getStatus());

        return bodyParam;
    }

    private Map<String, Object> putBodyParamNotify(String userId, Event event, String type ) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        bodyParam.put("content", "Thời gian: " +  simpleDateFormat.format(event.getStartTime()) + "\n" +"Vị trí: " + event.getSite().getSiteName() + " " + event.getDirectionCode());
        bodyParam.put("title", event.getEventName());
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bodyParam.put("startTime",simpleDateFormat.format( event.getStartTime()));
        bodyParam.put("objectType", type);
        bodyParam.put("objectUuid", event.getId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", event.getSite().getSiteId());
        return bodyParam;
    }

    private Map<String, Object> putBodyParamNotifyFinishTrafficJam(String userId, Event event, String type ) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        bodyParam.put("content", "Thời gian: " +  simpleDateFormat.format(event.getStartTime()) + "\n" +"Vị trí: " + event.getSite().getSiteName() + " " + event.getDirectionCode());
        bodyParam.put("title", "Hết ùn tắc");
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bodyParam.put("startTime",simpleDateFormat.format( event.getStartTime()));
        bodyParam.put("objectType", type);
        bodyParam.put("objectUuid", event.getParentId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", event.getSite().getSiteId());
        return bodyParam;
    }

    private Map<String, Object> putBodyParamNotifyMap(String userId, Event event, String type ,Point point) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", 3);
        Map<String,Object> content = new HashMap<>();
        content.put("startTime",event.getStartTime());
        content.put("event","MANUAL");
        content.put("eventCode",event.getEventCode());
        content.put("eventIdString",event.getParentId());
        content.put("eventName",event.getEventName());
        content.put("eventStatus",event.getEventStatus());
        content.put("imageUrl",event.getImageUrl());
        content.put("site",event.getSite());
        content.put("sourceId",event.getSourceId());
        content.put("sourceName",event.getSourceName());
        content.put("longitude",point.getLongitude());
        content.put("directionCode",event.getDirectionCode());
        content.put("latitude",point.getLatitude());
        content.put("display",1);
        content.put("startPositionM",event.getSite().getPositionM());
        bodyParam.put("content", content);
        bodyParam.put("title", event.getEventName());
        bodyParam.put("startTime",event.getStartTime());
        bodyParam.put("objectType", type);
        bodyParam.put("objectUuid", event.getId());
        bodyParam.put("userId", userId);
        bodyParam.put("siteId", event.getSite().getSiteId());
        bodyParam.put("position",1);
        return bodyParam;
    }

    private void handleSendNotify(Map<String, Object> bodyParam) {
        try {
            RequestMessage rbacRpcRequest = new RequestMessage();
            rbacRpcRequest.setRequestMethod("POST");
            rbacRpcRequest.setRequestPath(RabbitMQProperties.NOTIFY_RPC_URL);
            rbacRpcRequest.setBodyParam(bodyParam);
            rbacRpcRequest.setUrlParam(null);
            rbacRpcRequest.setHeaderParam(null);
            rbacRpcRequest.setVersion(ResourcePath.VERSION);
            String result = rabbitMQClient.callRpcService(RabbitMQProperties.NOTIFY_RPC_EXCHANGE,
                    RabbitMQProperties.NOTIFY_RPC_QUEUE, RabbitMQProperties.NOTIFY_RPC_KEY, rbacRpcRequest.toJsonString());
            LOGGER.info("getUserListFromIDService - result: " + result);
            if (result != null) {
                ObjectMapper mapper = new ObjectMapper();
                Response resultResponse = null;
                try {
                    resultResponse = mapper.readValue(result, Response.class);
                    if (resultResponse.getStatus() != HttpStatus.OK.value()) {
                        LOGGER.info("Error send notify to from report service");
                    } else {
                        LOGGER.info("Success send notify to from object-track service");
                    }
                } catch (Exception ex) {
                    LOGGER.info("Error parse json in handleSendNotifyToDevice from report service: " + ex.toString());
                }
            }
        } catch (Exception ex) {
            LOGGER.info("Error parse json in handleSendNotifyToDevice from report service: " + ex.toString());

        }
    }

    private void handleChangeStatus(Map<String, Object> bodyParam) {
        try {
            RequestMessage rbacRpcRequest = new RequestMessage();
            rbacRpcRequest.setRequestMethod("PUT");
            rbacRpcRequest.setRequestPath(RabbitMQProperties.SYSTEMCONFIG_CAMERA_STATUS);
            rbacRpcRequest.setBodyParam(bodyParam);
            rbacRpcRequest.setUrlParam(null);
            rbacRpcRequest.setHeaderParam(null);
            rbacRpcRequest.setVersion(ResourcePath.VERSION);
            String result = rabbitMQClient.callRpcService(RabbitMQProperties.SYSTEMCONFIG_RPC_EXCHANGE,
                    RabbitMQProperties.SYSTEMCONFIG_RPC_QUEUE, RabbitMQProperties.SYSTEMCONFIG_RPC_KEY, rbacRpcRequest.toJsonString());
            LOGGER.info("getUserListFromIDService - result: " + result);
            if (result != null) {
                ObjectMapper mapper = new ObjectMapper();
                Response resultResponse = null;
                try {
                    resultResponse = mapper.readValue(result, Response.class);
                    if (resultResponse.getStatus() != HttpStatus.OK.value()) {
                        LOGGER.info("Error send notify to from report service");
                    } else {
                        LOGGER.info("Success send notify to from object-track service");
                    }
                } catch (Exception ex) {
                    LOGGER.info("Error parse json in handleSendNotifyToDevice from report service: " + ex.toString());
                }
            }
        } catch (Exception ex) {
            LOGGER.info("Error parse json in handleSendNotifyToDevice from report service: " + ex.toString());

        }
    }

    private void sendMiddle(Map<String, Object> bodyParam) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            rabbitMQClient.callWorkerService(RabbitMQProperties.MIDDLE_QUEUE,mapper.writeValueAsString(bodyParam));
        } catch (Exception ex) {
            LOGGER.info("Error parse json in handleSendNotifyToDevice from report service: " + ex.toString());

        }
    }

    private void sendRecognition(RecognitionPlateDTO data) {
        Map<String, Object> bodyParam = putRecognition(data);
        sendMiddle(bodyParam);
    }

    private void sendEvent(Event data) {
        Map<String, Object> bodyParam = putEvent(data);
        sendMiddle(bodyParam);
    }

    private void sendViolation(ViolationDetail data) {
        Map<String, Object> bodyParam = putViolation(data);
        sendMiddle(bodyParam);
    }

    private void sendViolation(Event data) {
        Map<String, Object> bodyParam = putViolation(data);
        sendMiddle(bodyParam);
    }

    private void sendSecurity(SecurityDTO data) {
        Map<String, Object> bodyParam = putSecurity(data);
        sendMiddle(bodyParam);
    }




    private Map<String, Object> putRecognition(RecognitionPlateDTO data) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", "RECOGNITION");
        bodyParam.put("sourceId", data.getSourceId());
        bodyParam.put("sourceName", data.getSourceName());
        bodyParam.put("objectType",data.getObjectType());
        bodyParam.put("objectTypeName", data.getObjectName());
        bodyParam.put("eventTypeString", "RECOGNITION");
        bodyParam.put("eventTypeName", "Biển số");
        bodyParam.put("plate", data.getPlate());
        bodyParam.put("startTime", data.getStartTime());
        bodyParam.put("imageUrl", data.getImageUrl());
        bodyParam.put("speedOfVehicle", data.getSpeedOfVehicle());
        bodyParam.put("brand",data.getBrand());
        bodyParam.put("color",data.getColor());
        return bodyParam;
    }

    private Map<String, Object> putViolation(ViolationDetail data) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", "VIOLATION");
        bodyParam.put("sourceId", data.getSourceId());
        bodyParam.put("sourceName", data.getSourceName());
        bodyParam.put("objectType",data.getObjectType());
        bodyParam.put("objectTypeName", data.getObjectName());
        bodyParam.put("eventTypeString", data.getEventCode());
        bodyParam.put("eventTypeName", data.getEventName());
        bodyParam.put("plate", data.getPlate());
        bodyParam.put("startTime", data.getStartTime());
        bodyParam.put("imageUrl", data.getImageUrl());
        return bodyParam;
    }

    private Map<String, Object> putViolation(Event data) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", "VIOLATION");
        bodyParam.put("sourceId", data.getSourceId());
        bodyParam.put("sourceName", data.getSourceName());
        bodyParam.put("objectType",data.getObjectType());
        bodyParam.put("objectTypeName", data.getObjectType());
        bodyParam.put("eventTypeString", data.getParentId());
        bodyParam.put("eventTypeName", data.getEventName());
        bodyParam.put("startTime", data.getStartTime());
        bodyParam.put("imageUrl", data.getImageUrl());
        return bodyParam;
    }

    private Map<String, Object> putSecurity(SecurityDTO data) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", "SECURITY");
        bodyParam.put("sourceId", data.getSourceId());
        bodyParam.put("sourceName", data.getSourceName());
        bodyParam.put("eventTypeString", data.getEventCode());
        bodyParam.put("eventTypeName", data.getEventName());
        bodyParam.put("startTime", data.getStartTime());
        bodyParam.put("imageUrl", data.getImageUrl());
        return bodyParam;
    }
    private Map<String, Object> putEvent(Event data) {
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("type", "EVENT");
        bodyParam.put("sourceId", data.getSourceId());
        bodyParam.put("sourceName", data.getSourceName());
        bodyParam.put("eventTypeString", data.getEventCode());
        bodyParam.put("eventTypeName", data.getEventName());
        bodyParam.put("startTime", data.getStartTime());
        bodyParam.put("imageUrl", data.getImageUrl());
        return bodyParam;
    }

    private Map<String,User> getAllUser() {
        //Set body param
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("GET");
        rbacRpcRequest.setRequestPath("/v1.0/user/all/internal");
        rbacRpcRequest.setBodyParam(null);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setPathParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("[<--] Id return {}",result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    Object data = resultResponse.getData().getData();
                    Map<String, User> mapUser = mapper.convertValue(data, new TypeReference<Map<String,User>>() {
                    });
                    //OK
//                    JsonNode jsonNode = mapper.readTree(result);
//                    Map<String, User> mapUser = mapper.readerFor(new TypeReference<List<String>>() {
//                    }).readValue(jsonNode.get("data").get("data"));
                    return mapUser;
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }
}
