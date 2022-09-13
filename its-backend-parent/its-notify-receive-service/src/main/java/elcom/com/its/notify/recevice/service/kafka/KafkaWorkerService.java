//package elcom.com.its.notify.recevice.service.kafka;
//
//import com.elcom.its.constant.ResourcePath;
//import com.elcom.its.message.RequestMessage;
//import com.elcom.its.message.ResponseMessage;
//import com.elcom.its.utils.JSONConverter;
//import com.elcom.its.utils.StringUtil;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.stream.Collectors;
//
//import com.google.gson.Gson;
//import elcom.com.its.notify.recevice.service.config.ApplicationConfig;
//import elcom.com.its.notify.recevice.service.model.dto.*;
//import elcom.com.its.notify.recevice.service.rabbitmq.RabbitMQClient;
//import elcom.com.its.notify.recevice.service.rabbitmq.RabbitMQProperties;
//import elcom.com.its.notify.recevice.service.rabbitmq.WorkerConfig;
//import elcom.com.its.notify.recevice.service.rabbitmq.WorkerServer;
//import elcom.com.its.notify.recevice.service.service.CategoryService;
//import elcom.com.its.notify.recevice.service.service.ITSObjectTrackingService;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaWorkerService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaWorkerService.class);
//
//    @Value("url.map")
//    private String urlMap;
//
//    @Autowired
//    private RabbitMQClient rabbitMQClient;
//
//    @Autowired
//    private ITSObjectTrackingService itsObjectTrackingService;
//
//    @Autowired
//    private CategoryService categoryService;
//
////    @KafkaListener(topics = "#{workerConfig.getHeartbeatCamera()}")
////    public void workerReceiveHeartBeatCam(String json) {
////        try {
////            LOGGER.info(" [-->] Server received request for : {}", json);
////            //Process here
////            ObjectMapper mapper = new ObjectMapper();
////            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
////            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            mapper.setDateFormat(df);
////            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
////            if (request != null && request.getData() != null) {
////                //Transform dto => model
////                if ("heartbeat".equalsIgnoreCase(request.getType())) {
////                    LOGGER.info(request.getData().toString());
////                    Heartbeat data = mapper.readValue(request.getData().toString(), Heartbeat.class);
////                    if (data != null) {
////                        LOGGER.info(" [-------->] heartbeat data >>>" + JSONConverter.toJSON(data));
////                        //List group camera id chua camera id
////                        List<String> canAccessUserIdList = new ArrayList<>();
////                        if(request.getSites()!=null) {
////                            canAccessUserIdList = getCanAccessUserIdListFromIDService(request.getSites());
////                        }
////
////                        List<String> adminUserIdList = getAdminUserIdListFromABACService();
////
////                        if (canAccessUserIdList != null & !canAccessUserIdList.isEmpty()) {
////                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
////                            set.addAll(adminUserIdList);
////                            List<String> userIdCanReceiverList = new ArrayList<>(set);
////                            sendNotify(userIdCanReceiverList, data);
////                        }
////                    }
////                }
////            } else {
////                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.getMessage());
////            ex.printStackTrace();
////        }
////    }
////
////    @KafkaListener(topics = "#{workerConfig.getHeartbeatVms()}")
////    public void workerReceiveHeartBeatVms(String json) {
////        try {
////            LOGGER.info(" [-->] Server received request for : {}", json);
////            //Process here
////            ObjectMapper mapper = new ObjectMapper();
////            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
////            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            mapper.setDateFormat(df);
////            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
////            if (request != null && request.getData() != null) {
////                //Transform dto => model
////                if ("heartbeat".equalsIgnoreCase(request.getType())) {
////                    LOGGER.info(request.getData().toString());
////                    Heartbeat data = mapper.readValue(request.getData().toString(), Heartbeat.class);
////                    if (data != null) {
////                        LOGGER.info(" [-------->] heartbeat data >>>" + JSONConverter.toJSON(data));
////                        //List group camera id chua camera id
////                        List<String> canAccessUserIdList = new ArrayList<>();
////                        if(request.getSites()!=null) {
////                            canAccessUserIdList = getCanAccessUserIdListFromIDService(request.getSites());
////                        }
////
////                        List<String> adminUserIdList = getAdminUserIdListFromABACService();
////
////                        if (canAccessUserIdList != null & !canAccessUserIdList.isEmpty()) {
////                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
////                            set.addAll(adminUserIdList);
////                            List<String> userIdCanReceiverList = new ArrayList<>(set);
////                            sendNotify(userIdCanReceiverList, data);
////                        }
////                    }
////                }
////            } else {
////                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.getMessage());
////            ex.printStackTrace();
////        }
////    }
////
//////    @RabbitListener(id = "processHeartBeatVmsBoard", queues = {"#{workerConfig.getHeartbeatVmsBoard()}"})
//////    public void workerReceiveHeartBeatVmsBoard(String json) {
//////        try {
//////            LOGGER.info(" [-->] Server received request for : {}", json);
//////            //Process here
//////            ObjectMapper mapper = new ObjectMapper();
//////            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
//////            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//////            mapper.setDateFormat(df);
//////            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
//////            if (request != null && request.getData() != null) {
//////                //Transform dto => model
//////                if ("heartbeat".equalsIgnoreCase(request.getType())) {
//////                    LOGGER.info(request.getData().toString());
//////                    Heartbeat data = mapper.readValue(request.getData().toString(), Heartbeat.class);
//////                    if (data != null) {
//////                        LOGGER.info(" [-------->] heartbeat data >>>" + JSONConverter.toJSON(data));
//////                        //List group camera id chua camera id
//////                        List<String> canAccessUserIdList = new ArrayList<>();
//////                        if(request.getSites()!=null) {
//////                            canAccessUserIdList = getCanAccessUserIdListFromIDService(request.getSites());
//////                        }
//////
//////                        List<String> adminUserIdList = getAdminUserIdListFromABACService();
//////
//////                        if (canAccessUserIdList != null & !canAccessUserIdList.isEmpty()) {
//////                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
//////                            set.addAll(adminUserIdList);
//////                            List<String> userIdCanReceiverList = new ArrayList<>(set);
//////                            sendNotify(userIdCanReceiverList, data);
//////                        }
//////                    }
//////                }
//////            } else {
//////                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
//////            }
//////        } catch (Exception ex) {
//////            LOGGER.error(ex.getMessage());
//////            ex.printStackTrace();
//////        }
//////    }
////
////
////    @KafkaListener(topics = "#{workerConfig.getObjectTracking()}")
////    public void workerReceiveObjectTracking(String json) {
////        try {
////            LOGGER.info(" [-->] Server received request for : {}", json);
////            //Process here
////            ObjectMapper mapper = new ObjectMapper();
////            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
////            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            mapper.setDateFormat(df);
////            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
////            if (request != null && request.getData() != null) {
////                //Transform dto => model
////                if ("heartbeat".equalsIgnoreCase(request.getType())) {
////                    LOGGER.info(request.getData().toString());
////                    RecognitionPlateObjectTrackingDTO data = mapper.readValue(request.getData().toString(), RecognitionPlateObjectTrackingDTO.class);
////                    if (data != null) {
////                        LOGGER.info(" [-------->] heartbeat data >>>" + JSONConverter.toJSON(data));
////                        //List group camera id chua camera id
////                        List<String> canAccessUserIdList = new ArrayList<>();
////                        if(data.getSite()!=null) {
////                            canAccessUserIdList = getCanAccessUserIdListFromIDService(data.getSite().siteId);
////                        }
////
////                        List<String> adminUserIdList = getAdminUserIdListFromABACService();
////
////                        if (canAccessUserIdList != null & !canAccessUserIdList.isEmpty()) {
////                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
////                            set.addAll(adminUserIdList);
////                            List<String> userIdCanReceiverList = new ArrayList<>(set);
////                            sendNotify(userIdCanReceiverList, data);
////                        }
////                    }
////                }
////            } else {
////                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.getMessage());
////            ex.printStackTrace();
////        }
////    }
////
////    @KafkaListener(topics = "#{workerConfig.getTraffic()}")
////    public void workerReceiveTraffic(String json) {
////        try {
////            LOGGER.info(" [-->] Server received request for : {}", json);
////            //Process here
////            ObjectMapper mapper = new ObjectMapper();
////            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
////            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            mapper.setDateFormat(df);
////
////            SiteInfo site = null;
////            String sourceId = null;
////            String sourceName = null;
////            Date startTime = null;
////            String violationId = null;
////            String imagesObject = null;
////            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
////            if (request != null && request.getData() != null) {
////                //Transform dto => model
////                if ("traffic-status".equalsIgnoreCase(request.getType())) {
////                    LOGGER.info(request.getData().toString());
////                    ObjectMapper mapperCrowd = new ObjectMapper();
////                    mapperCrowd.setTimeZone(TimeZone.getTimeZone("GMT+7"));
////                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                    mapperCrowd.setDateFormat(df1);
////                    TrafficStatusNotify data = mapperCrowd.readValue(request.getData().toString(), TrafficStatusNotify.class);
////                    if (data != null) {
////                        LOGGER.info(" [-------->] FIRE FLOWS data >>>" + JSONConverter.toJSON(data));
////                        //List group camera id chua camera id
////                        List<String> canAccessUserIdList = new ArrayList<>();
////                        if(data.getStage().getSiteStart()!=null) {
////                            canAccessUserIdList = getCanAccessUserIdListFromIDService(data.getStage().getSiteStart());
////                        }
////
////                        List<String> adminUserIdList = getAdminUserIdListFromABACService();
////
////                        if (canAccessUserIdList != null & !canAccessUserIdList.isEmpty()) {
////                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
////                            set.addAll(adminUserIdList);
////                            List<String> userIdCanReceiverList = new ArrayList<>(set);
////                            sendNotify(userIdCanReceiverList, data);
////                        }
////                    }
////                }
////
////            } else {
////                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.getMessage());
////            ex.printStackTrace();
////        }
////    }
////
////    @KafkaListener(topics = "#{workerConfig.getEvent()}")
////    public void workerReceiveEvent(String json){
////        try {
////            LOGGER.info(" [-->] Server received request for : {}", json);
////            //Process here
////            ObjectMapper mapper = new ObjectMapper();
////            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
////            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            mapper.setDateFormat(df);
////            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
////            if (request != null && request.getData() != null) {
////                if ("violation".equalsIgnoreCase(request.getType())) {
////                    LOGGER.info(request.getData().toString());
////                    ObjectMapper mapperCrowd = new ObjectMapper();
////                    mapperCrowd.setTimeZone(TimeZone.getTimeZone("GMT+7"));
////                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                    mapperCrowd.setDateFormat(df1);
////                    ViolationDetail data = mapperCrowd.readValue(request.getData().toString(), ViolationDetail.class);
////                    if (data != null) {
////                        if(StringUtil.isNullOrEmpty(data.getSourceId())){
////                            sendViolation(data);
////                        }
////                        LOGGER.info(" [-------->] FIRE FLOWS data >>>" + JSONConverter.toJSON(data));
////                        //List group camera id chua camera id
////                        List<String> canAccessUserIdList = new ArrayList<>();
////                        if(request.getSites()!=null) {
////                            canAccessUserIdList = getCanAccessUserIdListFromIDService(request.getSites());
////                        }
////
////                        List<String> adminUserIdList = getAdminUserIdListFromABACService();
////
////                        if (canAccessUserIdList != null & !canAccessUserIdList.isEmpty()) {
////                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
////                            set.addAll(adminUserIdList);
////                            List<String> userIdCanReceiverList = new ArrayList<>(set);
////                            sendNotify(userIdCanReceiverList, data);
////                        }
////                    }
////                } else if ("crowd".equalsIgnoreCase(request.getType()) || "brokenvehicle".equalsIgnoreCase(request.getType())
////                        || "obstruction".equalsIgnoreCase(request.getType()) || "accident".equalsIgnoreCase(request.getType())
////                        || "fire".equalsIgnoreCase(request.getType())) {
////                    LOGGER.info(request.getData().toString());
////                    ObjectMapper mapperCrowd = new ObjectMapper();
////                    mapperCrowd.setTimeZone(TimeZone.getTimeZone("GMT+7"));
////                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                    mapperCrowd.setDateFormat(df1);
////                    SecurityDTO data = mapperCrowd.readValue(request.getData().toString(), SecurityDTO.class);
////                    if (data != null) {
////                        if(StringUtil.isNullOrEmpty(data.getSourceId())){
////                            sendSecurity(data);
////                        }
////                        LOGGER.info(" [-------->] CROWD FLOWS data >>>" + JSONConverter.toJSON(data));
////                        List<String> canAccessUserIdList = new ArrayList<>();
////                        if(data.getSite()!=null) {
////                            canAccessUserIdList = getCanAccessUserIdListFromIDService(data.getSite().getSiteId());
////                        }
////                        List<String> adminUserIdList = getAdminUserIdListFromABACService();
////                        if (canAccessUserIdList != null & !canAccessUserIdList.isEmpty()) {
////                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
////                            set.addAll(adminUserIdList);
////                            List<String> userIdCanReceiverList = new ArrayList<>(set);
////                            if ("crowd".equalsIgnoreCase(request.getType()) ){
////                                sendSecurity(userIdCanReceiverList, data,7);
////                            } else if("brokenvehicle".equalsIgnoreCase(request.getType())){
////                                sendSecurity(userIdCanReceiverList, data,17);
////                            } else if("obstruction".equalsIgnoreCase(request.getType())){
////                                sendSecurity(userIdCanReceiverList, data,18);
////                            } else if("accident".equalsIgnoreCase(request.getType())){
////                                sendSecurity(userIdCanReceiverList, data,19);
////                            } else if("fire".equalsIgnoreCase(request.getType())){
////                                sendSecurity(userIdCanReceiverList, data,20);
////                            }
////                        }
////
////                        //For test Kien Giang => Ban zalo
////                    }
////                }else if ("rain".equalsIgnoreCase(request.getType()) || "landslide".equalsIgnoreCase(request.getType())
////                        || "snow".equalsIgnoreCase(request.getType()) || "fog".equalsIgnoreCase(request.getType())) {
////                    LOGGER.info(request.getData().toString());
////                    ObjectMapper mapperFobbiden = new ObjectMapper();
////                    mapperFobbiden.setTimeZone(TimeZone.getTimeZone("GMT+7"));
////                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////                    mapperFobbiden.setDateFormat(df1);
////                    Event data = mapperFobbiden.readValue(request.getData().toString(), Event.class);
////                    if (data != null) {
////                        if(StringUtil.isNullOrEmpty(data.getSourceId())){
////                            sendEvent(data);
////                        }
////                        LOGGER.info(" [-------->] Event Manual data >>>" + JSONConverter.toJSON(data));
////                        List<String> canAccessUserIdList = new ArrayList<>();
////                        if(data.getSite()!=null) {
////                            canAccessUserIdList = getCanAccessUserIdListFromIDService(data.getSite().getSiteId());
////                        }
////                        List<String> adminUserIdList = getAdminUserIdListFromABACService();
////                        if (canAccessUserIdList != null & !canAccessUserIdList.isEmpty()) {
////                            Set<String> set = new LinkedHashSet<>(canAccessUserIdList);
////                            set.addAll(adminUserIdList);
////                            List<String> userIdCanReceiverList = new ArrayList<>(set);
////                            if("rain".equalsIgnoreCase(request.getType())){
////                                sendNotify(userIdCanReceiverList, data,21);
////                            } else if("landslide".equalsIgnoreCase(request.getType())){
////                                sendNotify(userIdCanReceiverList, data,22);
////                            } else if("snow".equalsIgnoreCase(request.getType())){
////                                sendNotify(userIdCanReceiverList, data,23);
////                            } else if("fog".equalsIgnoreCase(request.getType())){
////                                sendNotify(userIdCanReceiverList, data,24);
////                            }
////
////                        }
////                    }
////                }
////
////            } else {
////                LOGGER.info("NotifyTriggerRequest request is null. No process here...");
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.getMessage());
////            ex.printStackTrace();
////        }
////    }
////
////    @KafkaListener(topics = "#{workerConfig.getRecognition()}")
////    public void workerReceiveRecognition(String json) {
////        try {
////            LOGGER.info(" [-->] Server received request for : {}", json);
////            //Process here
////            ObjectMapper mapper = new ObjectMapper();
////            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
////            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            mapper.setDateFormat(df);
////            NotifyTriggerRequestDTO request = mapper.readValue(json, NotifyTriggerRequestDTO.class);
////            if (request != null && request.getData() != null) {
////                if ("recognition".equalsIgnoreCase(request.getType())) {
////                    LOGGER.info(request.getData().toString());
////                    RecognitionPlateDTO data = mapper.readValue(request.getData().toString(), RecognitionPlateDTO.class);
////                    if (data != null) {
////                        LOGGER.info(" [-------->] RECOGNITION data >>>" + JSONConverter.toJSON(data));
////                        sendRecognition(data);
////                    }
////                }else {
////                    LOGGER.info("NotifyTriggerRequest request is null. No process here...");
////                }
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.getMessage());
////            ex.printStackTrace();
////        }
////    }
////
////    private List<UserReceiverDTO> getUserListByGroupFromIDService(List<String> groupIdList) {
////        //Body param
////        Map<String, Object> bodyParamMap = new HashMap<>();
////        bodyParamMap.put("groupUuids", groupIdList);
////
////        RequestMessage userRpcRequest = new RequestMessage();
////        userRpcRequest.setRequestMethod("POST");
////        userRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_INTERNAL_LIST_BYGROUP_URL);
////        userRpcRequest.setBodyParam(bodyParamMap);
////        userRpcRequest.setUrlParam(null);
////        userRpcRequest.setHeaderParam(null);
////        userRpcRequest.setVersion(ResourcePath.VERSION);
////        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
////                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
////        LOGGER.info("getUserListByGroupFromIDService - result: " + result);
////        if (result != null) {
////            ObjectMapper mapper = new ObjectMapper();
////            ResponseMessage resultResponse = null;
////            try {
////                resultResponse = mapper.readValue(result, ResponseMessage.class);
////                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
////                    //OK
////                    JsonNode jsonNode = mapper.readTree(result);
////                    List<UserReceiverDTO> userReceiverDTOList = mapper.readerFor(new TypeReference<List<UserReceiverDTO>>() {
////                    }).readValue(jsonNode.get("data").get("data"));
////                    return userReceiverDTOList;
////                }
////            } catch (Exception ex) {
////                LOGGER.info("Error parse json in getUserListByGroupFromIDService from ID service: " + ex.toString());
////                return null;
////            }
////        }
////        return null;
////    }
////
////    private boolean checkAuthorizedSites(String userId, String siteId) {
////        List<String> listUser = getUserListAuthorizedSites(siteId);
////        System.out.println("userId = " + userId);
////        if (listUser != null && !listUser.isEmpty()) {
////            for (String s : listUser) {
////                System.out.println("userIdInlist = " + s);
////            }
////            for (String userIdInList : listUser) {
////                return userIdInList.equals(userId);
////            }
////        }
////        return true;
////    }
////
////    private List<String> getUserListAuthorizedSites(String siteId) {
////        //Body param
////        Map<String, Object> bodyParamMap = new HashMap<>();
////        bodyParamMap.put("siteId", siteId);
////        RequestMessage userRpcRequest = new RequestMessage();
////        userRpcRequest.setRequestMethod("POST");
////        userRpcRequest.setRequestPath("/user/authorizedSites");
////        userRpcRequest.setBodyParam(bodyParamMap);
////        userRpcRequest.setUrlParam("");
////        userRpcRequest.setHeaderParam(null);
////        userRpcRequest.setVersion(ResourcePath.VERSION);
////        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
////                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
////        LOGGER.info("getUserListByGroupFromIDService - result: " + result);
////        if (result != null) {
////            ObjectMapper mapper = new ObjectMapper();
////            ResponseMessage resultResponse = null;
////            try {
////                resultResponse = mapper.readValue(result, ResponseMessage.class);
////                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
////                    //OK
////                    JsonNode jsonNode = mapper.readTree(result);
////                    List<String> userReceiverDTOList = mapper.readerFor(new TypeReference<List<String>>() {
////                    }).readValue(jsonNode.get("data").get("data"));
////                    return userReceiverDTOList;
////                }
////            } catch (Exception ex) {
////                LOGGER.info("Error parse json in getUserListByGroupFromIDService from ID service: " + ex.toString());
////                return null;
////            }
////        }
////        return null;
////    }
////
////    //Get list user admin id from rbac
////    private List<String> getAdminUserIdListFromABACService() {
////        RequestMessage rbacRpcRequest = new RequestMessage();
////        rbacRpcRequest.setRequestMethod("GET");
////        rbacRpcRequest.setRequestPath(RabbitMQProperties.ABAC_RPC_ADMIN_URL);
////        rbacRpcRequest.setBodyParam(null);
////        rbacRpcRequest.setUrlParam(null);
////        rbacRpcRequest.setHeaderParam(null);
////        rbacRpcRequest.setVersion(ResourcePath.VERSION);
////        String result = rabbitMQClient.callRpcService(RabbitMQProperties.ABAC_RPC_EXCHANGE,
////                RabbitMQProperties.ABAC_RPC_QUEUE, RabbitMQProperties.ABAC_RPC_KEY, rbacRpcRequest.toJsonString());
////        LOGGER.info("getAdminUserIdListFromABACService - result: " + result);
////        if (result != null) {
////            ObjectMapper mapper = new ObjectMapper();
////            ResponseMessage resultResponse = null;
////            try {
////                resultResponse = mapper.readValue(result, ResponseMessage.class);
////                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
////                    //OK
////                    JsonNode jsonNode = mapper.readTree(result);
////                    List<String> adminUserIdDTOList = mapper.readerFor(new TypeReference<List<String>>() {
////                    }).readValue(jsonNode.get("data").get("data"));
////                    return adminUserIdDTOList;
////                }
////            } catch (Exception ex) {
////                LOGGER.info("Error parse json in getAdminUserIdListFromABACService from ABAC service: " + ex.toString());
////                return null;
////            }
////        }
////        return null;
////    }
////
////    //Get can access camera user id from ID
////    private List<String> getCanAccessUserIdListFromIDService(String siteId) {
////        //Set body param
////        Map<String, Object> bodyParam = new HashMap<>();
////        bodyParam.put("siteId", siteId);
////
////        RequestMessage rbacRpcRequest = new RequestMessage();
////        rbacRpcRequest.setRequestMethod("POST");
////        rbacRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_SITE_LIST_URL);
////        rbacRpcRequest.setBodyParam(bodyParam);
////        rbacRpcRequest.setUrlParam(null);
////        rbacRpcRequest.setHeaderParam(null);
////        rbacRpcRequest.setVersion(ResourcePath.VERSION);
////        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
////                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, rbacRpcRequest.toJsonString());
////        LOGGER.info("getCanAccessUserIdListFromIDService - result: " + result);
////        if (result != null) {
////            ObjectMapper mapper = new ObjectMapper();
////            ResponseMessage resultResponse = null;
////            try {
////                resultResponse = mapper.readValue(result, ResponseMessage.class);
////                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
////                    //OK
////                    JsonNode jsonNode = mapper.readTree(result);
////                    List<String> canAccessUserIdDTOList = mapper.readerFor(new TypeReference<List<String>>() {
////                    }).readValue(jsonNode.get("data").get("data"));
////                    return canAccessUserIdDTOList;
////                }
////            } catch (Exception ex) {
////                LOGGER.info("Error parse json in getCanAccessUserIdListFromIDService from ID service: " + ex.toString());
////                return null;
////            }
////        }
////        return null;
////    }
////
////    private Map<String, Object> putBodyParamSecurity(String userId, SecurityDTO securityDTO, Integer type) {
////
////        Map<String, Object> bodyParam = new HashMap<>();
////        bodyParam.put("type", 3);
////        bodyParam.put("content", securityDTO.getStartTime().toString() + "\n"  + securityDTO.getSite().getSiteName() + " " + securityDTO.getDirectionCode());
////        bodyParam.put("title", securityDTO.getEventName());
////        bodyParam.put("startTime",securityDTO.getStartTime());
////        bodyParam.put("objectType", type);
////        bodyParam.put("objectUuid", securityDTO.getEventId());
////        bodyParam.put("userId", userId);
////        bodyParam.put("siteId", securityDTO.getSite().getSiteId());
////        return bodyParam;
////    }
////
////    private void sendSecurity(List<String> userIdList, SecurityDTO crowdFlowsDTO, Integer type) {
////        try {
////            if (userIdList != null && !userIdList.isEmpty()) {
////                for (String userId : userIdList) {
////                    Map<String, Object> bodyParam = putBodyParamSecurity(userId, crowdFlowsDTO,type);
////                    handleSendNotify(bodyParam);
////                }
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.toString());
////        }
////    }
////
////    private NotifiesConfigDTO getNitifiesConfigByTypeNotifyName(String typeNotifyName) {
////        RequestMessage rbacRpcRequest = new RequestMessage();
////        Map<String, Object> bodyParam = new HashMap<>();
////        bodyParam.put("typeNotifyName", typeNotifyName);
////        rbacRpcRequest.setRequestMethod("POST");
////        rbacRpcRequest.setRequestPath(RabbitMQProperties.SYSTEMCONFIG_RPC_NOTIFIES_CONFIG_BY_TYPE_URL);
////        rbacRpcRequest.setBodyParam(bodyParam);
////        rbacRpcRequest.setUrlParam("");
////        rbacRpcRequest.setHeaderParam(null);
////        rbacRpcRequest.setVersion(ResourcePath.VERSION);
////        String result = rabbitMQClient.callRpcService(RabbitMQProperties.SYSTEMCONFIG_RPC_EXCHANGE,
////                RabbitMQProperties.SYSTEMCONFIG_RPC_QUEUE, RabbitMQProperties.SYSTEMCONFIG_RPC_KEY, rbacRpcRequest.toJsonString());
////        LOGGER.info("rolesDTOList - result: " + result);
////        if (result != null) {
////            ObjectMapper mapper = new ObjectMapper();
////            ResponseMessage resultResponse = null;
////            try {
////                resultResponse = mapper.readValue(result, ResponseMessage.class);
////                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
////                    //OK
////                    JsonNode jsonNode = mapper.readTree(result);
////                    NotifiesConfigDTO notifiesConfigDTO = mapper.readerFor(new TypeReference<NotifiesConfigDTO>() {
////                    }).readValue(jsonNode.get("data").get("data"));
////                    return notifiesConfigDTO;
////                }
////            } catch (Exception ex) {
////                LOGGER.info("Error parse json in notify from config service: " + ex.toString());
////                return null;
////            }
////        }
////        return null;
////    }
////
////    private List<RoleUserDTO> getUserByListRolesABAC(List<String> listRolesCode) {
////        RequestMessage rbacRpcRequest = new RequestMessage();
////        Map<String, Object> bodyParam = new HashMap<>();
////        bodyParam.put("roles", listRolesCode);
////        rbacRpcRequest.setRequestMethod("POST");
////        rbacRpcRequest.setRequestPath(RabbitMQProperties.ABAC_RPC_USES_BY_ROLES_URL);
////        rbacRpcRequest.setBodyParam(bodyParam);
////        rbacRpcRequest.setUrlParam("");
////        rbacRpcRequest.setHeaderParam(null);
////        rbacRpcRequest.setVersion(ResourcePath.VERSION);
////        String result = rabbitMQClient.callRpcService(RabbitMQProperties.ABAC_RPC_EXCHANGE,
////                RabbitMQProperties.ABAC_RPC_QUEUE, RabbitMQProperties.ABAC_RPC_KEY, rbacRpcRequest.toJsonString());
////        LOGGER.info("rolesDTOList - result: " + result);
////        if (result != null) {
////            ObjectMapper mapper = new ObjectMapper();
////            ResponseMessage resultResponse = null;
////            try {
////                resultResponse = mapper.readValue(result, ResponseMessage.class);
////                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
////                    //OK
////                    JsonNode jsonNode = mapper.readTree(result);
////                    List<RoleUserDTO> roleUserDTOs = mapper.readerFor(new TypeReference<List<RoleUserDTO>>() {
////                    }).readValue(jsonNode.get("data").get("data"));
////                    return roleUserDTOs;
////                }
////            } catch (Exception ex) {
////                LOGGER.info("Error parse json in rolesDTOList from RBAC service: " + ex.toString());
////                return null;
////            }
////        }
////        return null;
////    }
////
////    public List<String> getListUserIdReceiverNotifiesConfig(List<String> listRolesNotifiesConfig) {
////        // Lấy danh user theo nhóm danh sách quyền
////        List<RoleUserDTO> roleUserReceiver = getUserByListRolesABAC(listRolesNotifiesConfig);
////
////        if (roleUserReceiver != null && !roleUserReceiver.isEmpty()) {
////            List<String> listUserIdReceiverNotifiesConfig = roleUserReceiver.stream().map(user -> user.getUuidUser()).collect(Collectors.toList());
////            return listUserIdReceiverNotifiesConfig;
////        }
////        return null;
////    }
////
////    private void sendNotify(List<String> userIdList, SecurityDTO litteringFlowsDTO) {
////        try {
////            if (userIdList != null && !userIdList.isEmpty()) {
////                for (String userId : userIdList) {
////                    Map<String, Object> bodyParam = putBodyParamNotify(userId, litteringFlowsDTO);
////                    handleSendNotify(bodyParam);
////                }
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.toString());
////        }
////    }
////
////    private void sendNotify(List<String> userIdList, TrafficStatusNotify trafficStatusNotify) {
////        try {
////            if (userIdList != null && !userIdList.isEmpty()) {
////                for (String userId : userIdList) {
////                    Map<String, Object> bodyParam = putBodyParamNotify(userId, trafficStatusNotify);
////                    handleSendNotify(bodyParam);
////                }
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.toString());
////        }
////    }
////
////    private void sendNotify(List<String> userIdList, ViolationDetail violationDetail) {
////        try {
////            if (userIdList != null && !userIdList.isEmpty()) {
////                for (String userId : userIdList) {
////                    Map<String, Object> bodyParam = putBodyParamNotify(userId, violationDetail);
////                    handleSendNotify(bodyParam);
////                }
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.toString());
////        }
////    }
////
////
////    private void sendNotify(List<String> userIdList, RecognitionPlateObjectTrackingDTO recognitionPlateObjectTrackingDTO) {
////        try {
////            if (userIdList != null && !userIdList.isEmpty()) {
////                for (String userId : userIdList) {
////                    Integer type = 3;
////                    for (String tmp: recognitionPlateObjectTrackingDTO.getAlertChannel()
////                    ) {
////                        if(tmp.equalsIgnoreCase("SMS")){
////                            type = 1;
////                        } else  if(tmp.equalsIgnoreCase("ZALO")){
////                            type = 6;
////                        } else  if(tmp.equalsIgnoreCase("EMAIL")){
////                            type = 2;
////                        }
////                        Map<String, Object> bodyParam = putBodyParamNotify(userId, recognitionPlateObjectTrackingDTO,type);
////                        handleSendNotify(bodyParam);
////                    }
////                    Map<String, Object> bodyParam = putBodyParamNotify(userId, recognitionPlateObjectTrackingDTO,3);
////                    handleSendNotify(bodyParam);
////
////                }
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.toString());
////        }
////    }
////
////    private void sendNotify(List<String> userIdList, Heartbeat heartbeat) {
////        try {
////            if (userIdList != null && !userIdList.isEmpty()) {
////                for (String userId : userIdList) {
////                    Map<String, Object> bodyParam = putBodyParamNotify(userId, heartbeat);
////                    handleSendNotify(bodyParam);
////                }
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.toString());
////        }
////    }
////
////    private void sendNotify(List<String> userIdList, Event event, Integer type) {
////        try {
////            if (userIdList != null && !userIdList.isEmpty()) {
////                for (String userId : userIdList) {
////                    Map<String, Object> bodyParam = putBodyParamNotify(userId, event,type);
////                    handleSendNotify(bodyParam);
////                }
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.toString());
////        }
////    }
////
////    private Map<String, Object> putBodyParamNotify(String userId, SecurityDTO litteringFlowsDTO) {
////        LOGGER.info("Send web notify 2 user: {} => {}", userId, "Phát hiện Xả rác tại nút giao " + litteringFlowsDTO.getSite().getSiteAddress());
////        Map<String, Object> bodyParam = new HashMap<>();
////        String violationJson = new Gson().toJson(litteringFlowsDTO);
////        bodyParam.put("type", 3);
////        bodyParam.put("content", "Vị trí: " + litteringFlowsDTO.getSite().getSiteName());
////        bodyParam.put("title", litteringFlowsDTO.getEventName());
////        bodyParam.put("startTime",litteringFlowsDTO.getStartTime());
////        bodyParam.put("url", litteringFlowsDTO.getEventId());
////        bodyParam.put("objectType", litteringFlowsDTO.getEventCode()); // 17: Xả rác
////        bodyParam.put("objectUuid", violationJson);
////        bodyParam.put("userId", userId);
////        return bodyParam;
////    }
////
////    private Map<String, Object> putBodyParamNotify(String userId, RecognitionPlateObjectTrackingDTO data, Integer type) {
////        Map<String, Object> bodyParam = new HashMap<>();
////        bodyParam.put("type", type);
////        bodyParam.put("content", data.getStartTime().toString() + "\n" + "Biển số: " + data.getPlate() + "\n" + data.getSite().getSiteName());
////        bodyParam.put("title", "Phát hiện đối tượng theo dõi");
////        bodyParam.put("startTime",data.getStartTime());
////        bodyParam.put("objectType", 4);
////        bodyParam.put("objectUuid", data.getId());
////        bodyParam.put("userId", userId);
////        bodyParam.put("siteId", data.getSite().getSiteId());
////        return bodyParam;
////    }
////
////    private Map<String, Object> putBodyParamNotify(String userId, TrafficStatusNotify trafficStatusNotify) {
////        Map<String, Object> bodyParam = new HashMap<>();
////        bodyParam.put("type", 3);
////        bodyParam.put("content", trafficStatusNotify.getNewTrafficStatus().getStatusCode());
////        bodyParam.put("title", trafficStatusNotify.getNewTrafficStatus().getStatusName());
////        bodyParam.put("startTime",trafficStatusNotify.getNewTrafficStatus().getCreateDate());
////        bodyParam.put("objectType", 1);
////        bodyParam.put("objectUuid", trafficStatusNotify.getStage().getId());
////        bodyParam.put("userId", userId);
////        bodyParam.put("position",1);
////        return bodyParam;
////    }
////
////    private Map<String, Object> putBodyParamNotify(String userId, ViolationDetail violationDetail) {
////        Map<String, Object> bodyParam = new HashMap<>();
////        bodyParam.put("type", 3);
////        bodyParam.put("content", violationDetail.getStartTime().toString() + "\n" + "Biển số: " + violationDetail.getPlate() + "\n" + violationDetail.getSite().getSiteName() + " " + violationDetail.getDirectionCode());
////        bodyParam.put("title", violationDetail.getEventName());
////        bodyParam.put("startTime",violationDetail.getStartTime());
////        bodyParam.put("objectType", 5);
////        bodyParam.put("objectUuid", violationDetail.getEventId());
////        bodyParam.put("userId", userId);
////        bodyParam.put("siteId", violationDetail.getSite().getSiteId());
////        return bodyParam;
////    }
////
////    private Map<String, Object> putBodyParamNotify(String userId, Heartbeat heartbeat) {
////        Map<String, Object> bodyParam = new HashMap<>();
////        bodyParam.put("type", 3);
////        if(heartbeat.getStatus()==1){
////            bodyParam.put("title", "Thiết bị kết nối lại");
////            bodyParam.put("objectType", 3);
////        } else if(heartbeat.getStatus()==0){
////            bodyParam.put("title", "Thiết bị mất kết nối");
////            bodyParam.put("objectType", 2);
////        }
////        bodyParam.put("content", heartbeat.getStartTime().toString() + "\n"+ "Thiết bị : " + heartbeat.getDeviceName());
////        bodyParam.put("startTime",heartbeat.getStartTime());
////        bodyParam.put("url",urlMap);
////        bodyParam.put("objectUuid", heartbeat.getDeviceId());
////        bodyParam.put("userId", userId);
////        return bodyParam;
////    }
////
////    private Map<String, Object> putBodyParamNotify(String userId, Event event, Integer type ) {
////        Map<String, Object> bodyParam = new HashMap<>();
////        bodyParam.put("type", 3);
////        bodyParam.put("content", event.getStartTime().toString() + "\n" + event.getSite().getSiteName() + " " + event.getDirectionCode());
////        bodyParam.put("title", event.getEventName());
////        bodyParam.put("startTime",event.getStartTime());
////        bodyParam.put("objectType", type);
////        bodyParam.put("objectUuid", event.getId());
////        bodyParam.put("userId", userId);
////        bodyParam.put("siteId", event.getSite().getSiteId());
////        return bodyParam;
////    }
////
////    private void sendNotifyFire(List<String> userIdList, SecurityDTO fireFlowsDTO) {
////        try {
////            if (userIdList != null && !userIdList.isEmpty()) {
////                for (String userId : userIdList) {
////                    Map<String, Object> bodyParam = putBodyParamNotifyFireFlow(userId, fireFlowsDTO);
////                    handleSendNotify(bodyParam);
////                }
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.toString());
////        }
////    }
////
////    private void sendNotifyEncroaching(List<String> userIdList, SecurityDTO encroachingFlowsDTO) {
////        try {
////            if (userIdList != null && !userIdList.isEmpty()) {
////                for (String userId : userIdList) {
////                    Map<String, Object> bodyParam = putBodyParamNotifyEncroachingFlows(userId, encroachingFlowsDTO);
////                    handleSendNotify(bodyParam);
////                }
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.toString());
////        }
////    }
////
////    private Map<String, Object> putBodyParamNotifyFobidden(String userId, SecurityDTO fobiddenDTO) {
////        LOGGER.info("Send web notify 2 user: {} => {}", userId, "Phát hiện Xâm nhập vùng cấm tại nút giao " + fobiddenDTO.getSite().getSiteAddress());
////        Map<String, Object> bodyParam = new HashMap<>();
////        String violationJson = new Gson().toJson(fobiddenDTO);
////        bodyParam.put("type", 3);
////        bodyParam.put("content", "Vị trí: " + fobiddenDTO.getSite().getSiteName());
////        bodyParam.put("title", getEventTypeName("FOBIDDEN"));
////        bodyParam.put("url", fobiddenDTO.getEventId());
////        bodyParam.put("startTime",fobiddenDTO.getStartTime());
////        bodyParam.put("objectType", "fobidden"); // 17: Chasy
////        bodyParam.put("objectUuid", violationJson);
////        bodyParam.put("userId", userId);
////        return bodyParam;
////    }
////
////    private Map<String, Object> putBodyParamNotifyFireFlow(String userId, SecurityDTO fireFlowsDTO) {
////        LOGGER.info("Send web notify 2 user: {} => {}", userId, "Phát hiện Xả rác tại nút giao " + fireFlowsDTO.getSite().getSiteAddress());
////        Map<String, Object> bodyParam = new HashMap<>();
////        String violationJson = new Gson().toJson(fireFlowsDTO);
////        bodyParam.put("type", 3);
////        bodyParam.put("content", "Vị trí: " + fireFlowsDTO.getSite().getSiteName());
////        bodyParam.put("title", getEventTypeName("FIRE_FLOWS"));
////        bodyParam.put("url", fireFlowsDTO.getEventId());
////        bodyParam.put("startTime",fireFlowsDTO.getStartTime());
////        bodyParam.put("objectType", "fire"); // 17: Chasy
////        bodyParam.put("objectUuid", violationJson);
////        bodyParam.put("userId", userId);
////        return bodyParam;
////    }
////
////    private Map<String, Object> putBodyParamNotifyEncroachingFlows(String userId, SecurityDTO encroachingFlowsDTO) {
////        Map<String, Object> bodyParam = new HashMap<>();
////        String violationJson = new Gson().toJson(encroachingFlowsDTO);
////        bodyParam.put("type", 3);
////        bodyParam.put("content", "Vị trí: " + encroachingFlowsDTO.getSite().getSiteName());
////        bodyParam.put("title", getEventTypeName("ENCROACHING_FLOWS"));
////        //bodyParam.put("url", ApplicationConfig.URL_CROWD_FLOW + "?site_id=" + crowdFlowsDTO.getSite().getSiteId());
////        bodyParam.put("url", encroachingFlowsDTO.getEventId());
////        bodyParam.put("startTime",encroachingFlowsDTO.getStartTime());
////        bodyParam.put("objectType", "encroaching"); // 17: Chasy
////        bodyParam.put("objectUuid", violationJson);
////        bodyParam.put("userId", userId);
////        return bodyParam;
////    }
////
////    private Map<String, Object> putBodyParamNotifyTrafficIncident(String userId, SecurityDTO trafficIncidentDTO) {
////        Map<String, Object> bodyParam = new HashMap<>();
////        String violationJson = new Gson().toJson(trafficIncidentDTO);
////        bodyParam.put("type", 3);
////        bodyParam.put("content", "Vị trí: " + trafficIncidentDTO.getSite().getSiteName());
////        bodyParam.put("url", trafficIncidentDTO.getEventId());
////        bodyParam.put("startTime",trafficIncidentDTO.getStartTime());
////        bodyParam.put("title", getEventTypeName(trafficIncidentDTO.getEventCode()));
////        bodyParam.put("lane", trafficIncidentDTO.getLane());
////        bodyParam.put("objectUuid", violationJson);
////        bodyParam.put("userId", userId);
////        return bodyParam;
////    }
////
////    private String getEventTypeName(String code) {
////        return categoryService.findByCatType("EVENT").stream()
////                .filter(c -> Objects.equals(c.getCode(), code))
////                .findFirst()
////                .map(Category::getName)
////                .orElseGet(() -> "Không xác định");
////    }
////
////
////    private void sendNotifyFobidden(List<String> userIdList, SecurityDTO fobiddenDTO) {
////        try {
////            if (userIdList != null && !userIdList.isEmpty()) {
////                for (String userId : userIdList) {
////                    Map<String, Object> bodyParam = putBodyParamNotifyFobidden(userId, fobiddenDTO);
////                    handleSendNotify(bodyParam);
////                }
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.toString());
////        }
////    }
////
////
////    private void sendNotifyTrafficIncident(List<String> userIdList, SecurityDTO trafficIncidentDTO) {
////        try {
////            if (userIdList != null && !userIdList.isEmpty()) {
////                for (String userId : userIdList) {
////                    Map<String, Object> bodyParam = putBodyParamNotifyTrafficIncident(userId, trafficIncidentDTO);
////                    handleSendNotify(bodyParam);
////                }
////            }
////        } catch (Exception ex) {
////            LOGGER.error(ex.toString());
////        }
////    }
////
////    private void handleSendNotify(Map<String, Object> bodyParam) {
////        try {
////            RequestMessage rbacRpcRequest = new RequestMessage();
////            rbacRpcRequest.setRequestMethod("POST");
////            rbacRpcRequest.setRequestPath(RabbitMQProperties.NOTIFY_RPC_URL);
////            rbacRpcRequest.setBodyParam(bodyParam);
////            rbacRpcRequest.setUrlParam(null);
////            rbacRpcRequest.setHeaderParam(null);
////            rbacRpcRequest.setVersion(ResourcePath.VERSION);
////            String result = rabbitMQClient.callRpcService(RabbitMQProperties.NOTIFY_RPC_EXCHANGE,
////                    RabbitMQProperties.NOTIFY_RPC_QUEUE, RabbitMQProperties.NOTIFY_RPC_KEY, rbacRpcRequest.toJsonString());
////            LOGGER.info("getUserListFromIDService - result: " + result);
////            if (result != null) {
////                ObjectMapper mapper = new ObjectMapper();
////                Response resultResponse = null;
////                try {
////                    resultResponse = mapper.readValue(result, Response.class);
////                    if (resultResponse.getStatus() != HttpStatus.OK.value()) {
////                        LOGGER.info("Error send notify to from report service");
////                    } else {
////                        LOGGER.info("Success send notify to from object-track service");
////                    }
////                } catch (Exception ex) {
////                    LOGGER.info("Error parse json in handleSendNotifyToDevice from report service: " + ex.toString());
////                }
////            }
////        } catch (Exception ex) {
////            LOGGER.info("Error parse json in handleSendNotifyToDevice from report service: " + ex.toString());
////
////        }
////    }
////
////    private void sendMiddle(Map<String, Object> bodyParam) {
////        try {
////            RequestMessage message = new RequestMessage();
////            message.setRequestMethod("POST");
////            message.setRequestPath(RabbitMQProperties.NOTIFY_RPC_URL);
////            message.setBodyParam(bodyParam);
////            message.setUrlParam(null);
////            message.setHeaderParam(null);
////            message.setVersion(ResourcePath.VERSION);
////            rabbitMQClient.callWorkerService(RabbitMQProperties.MIDDLE_QUEUE,message.toJsonString());
////        } catch (Exception ex) {
////            LOGGER.info("Error parse json in handleSendNotifyToDevice from report service: " + ex.toString());
////
////        }
////    }
////
////    private void sendRecognition(RecognitionPlateDTO data) {
////        Map<String, Object> bodyParam = putRecognition(data);
////        sendMiddle(bodyParam);
////    }
////
////    private void sendEvent(Event data) {
////        Map<String, Object> bodyParam = putEvent(data);
////        sendMiddle(bodyParam);
////    }
////
////    private void sendViolation(ViolationDetail data) {
////        Map<String, Object> bodyParam = putViolation(data);
////        sendMiddle(bodyParam);
////    }
////
////    private void sendSecurity(SecurityDTO data) {
////        Map<String, Object> bodyParam = putSecurity(data);
////        sendMiddle(bodyParam);
////    }
////
////
////
////
////    private Map<String, Object> putRecognition(RecognitionPlateDTO data) {
////        Map<String, Object> bodyParam = new HashMap<>();
////        bodyParam.put("type", "RECOGNITION");
////        bodyParam.put("sourceId", data.getSourceId());
////        bodyParam.put("sourceName", data.getSourceName());
////        bodyParam.put("objectType",data.getObjectType());
////        bodyParam.put("objectTypeName", data.getObjectName());
////        bodyParam.put("eventTypeString", "RECOGNITION");
////        bodyParam.put("eventTypeName", "Biển số");
////        bodyParam.put("plate", data.getPlate());
////        bodyParam.put("startTime", data.getStartTime());
////        bodyParam.put("imageUrl", data.getImageUrl());
////        return bodyParam;
////    }
////
////    private Map<String, Object> putViolation(ViolationDetail data) {
////        Map<String, Object> bodyParam = new HashMap<>();
////        bodyParam.put("type", "VIOLATION");
////        bodyParam.put("sourceId", data.getSourceId());
////        bodyParam.put("sourceName", data.getSourceName());
////        bodyParam.put("objectType",data.getObjectType());
////        bodyParam.put("objectTypeName", data.getObjectName());
////        bodyParam.put("eventTypeString", data.getEventCode());
////        bodyParam.put("eventTypeName", data.getEventName());
////        bodyParam.put("plate", data.getPlate());
////        bodyParam.put("startTime", data.getStartTime());
////        bodyParam.put("imageUrl", data.getImageUrl());
////        return bodyParam;
////    }
////
////    private Map<String, Object> putSecurity(SecurityDTO data) {
////        Map<String, Object> bodyParam = new HashMap<>();
////        bodyParam.put("type", "SECURITY");
////        bodyParam.put("sourceId", data.getSourceId());
////        bodyParam.put("sourceName", data.getSourceName());
////        bodyParam.put("eventTypeString", data.getEventCode());
////        bodyParam.put("eventTypeName", data.getEventName());
////        bodyParam.put("startTime", data.getStartTime());
////        bodyParam.put("imageUrl", data.getImageUrl());
////        return bodyParam;
////    }
////    private Map<String, Object> putEvent(Event data) {
////        Map<String, Object> bodyParam = new HashMap<>();
////        bodyParam.put("type", "EVENT");
////        bodyParam.put("sourceId", data.getSourceId());
////        bodyParam.put("sourceName", data.getSourceName());
////        bodyParam.put("eventTypeString", data.getEventCode());
////        bodyParam.put("eventTypeName", data.getEventName());
////        bodyParam.put("startTime", data.getStartTime());
////        bodyParam.put("imageUrl", data.getImageUrl());
////        return bodyParam;
////    }
//
//
//
//
//
//}
