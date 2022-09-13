/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.notify.controller;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.notify.enums.StatusView;
import com.elcom.its.notify.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.notify.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.notify.model.Notify;
import com.elcom.its.notify.model.NotifyPK;
import com.elcom.its.notify.model.dto.*;
import com.elcom.its.notify.service.NotifyService;
import com.elcom.its.notify.validation.NotifyValidation;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Admin
 */
@Controller
public class NotifyController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyController.class);

    @Value("${socket.worker.queue}")
    private String workerQueue;

    @Value("${socket.service.name}")
    private String serviceName;

    @Value("${socket.emit.name}")
    private String emitName;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    public ResponseMessage getNotifyByUser(Map<String, String> headerParam,
            String requestPath, String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                long now = System.currentTimeMillis() / 1000;

                Integer page = 0;
                Integer size = 20;
                String keyword = "";
                String typeIcon = "";
                Integer type = 3;
                long fromDate = now - (30 * 24 * 60 * 60);
                long toDate = now;
                if (StringUtils.isNotBlank(urlParam)) {
                    Map<String, Object> query = getQueryMap(urlParam);
                    page = query.get("page") != null ? Integer.parseInt((String) query.get("page")) : page;
                    size = query.get("size") != null ? Integer.parseInt((String) query.get("size")) : size;
                    keyword = query.get("keyword") != null ? (String) query.get("keyword") : "";
                    type = query.get("type") != null ? Integer.parseInt((String) query.get("type")) : type;
                    if (!StringUtil.isNullOrEmpty(keyword)) {
                        try {
                            keyword = URLDecoder.decode(keyword, "UTF-8");
                        } catch (UnsupportedEncodingException ex) {
                            LOGGER.error(ex.toString());
                        }
                    }
                    fromDate = query.get("fromDate") != null ? Long.parseLong((String) query.get("fromDate")) : fromDate;
                    toDate = query.get("toDate") != null ? Long.parseLong((String) query.get("toDate")) : toDate;
                    typeIcon = query.get("typeIcon") != null ? (String) query.get("typeIcon") : "";
                }
                LOGGER.info("search keyword: {}", keyword);
                List<String> typeIconList = StringUtils.isNotBlank(typeIcon) ? Arrays.asList(typeIcon.split("-")) : null;

                Page<Notify> pagedResult = notifyService.findNotifyByUser(page, size, keyword, dto.getUuid(), fromDate, toDate, typeIconList, type);
                if (pagedResult != null && !pagedResult.isEmpty()) {
                    List<NotifyDTO> results = pagedResult.getContent()
                            .parallelStream()
                            .map(notifyService::transform)
                            .collect(Collectors.toList());

                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), results, pagedResult.getTotalElements()));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                }

            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách thông báo", null));
            }
        }

        return response;
    }

    public ResponseMessage getNumberNotifyByUser(Map<String, String> headerParam, String requestPath,
            String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                long now = System.currentTimeMillis() / 1000;
                long fromDate = now - (7 * 24 * 60 * 60);
                long toDate = now;
                long countNotify = notifyService.countNotifyNotRead(dto.getUuid(), StatusView.NOT_SEEN.code(), fromDate, toDate, 3);
                Map<String, Object> map = new HashMap<>();
                map.put("countNotiNotRead", countNotify);
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), map));
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem số lượng thông báo", null));
            }
        }

        return response;
    }

    public ResponseMessage readAllNotify(String requestPath, Map<String, String> headerParam,
            Map<String, Object> bodyParam, String requestMethod) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, requestMethod, dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                String userId = (String) bodyParam.get("userId");
                boolean result = notifyService.updateStatusView(userId, StatusView.SEEN.code());
                if (result) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền đánh dấu đã đọc thông báo", null));
            }
        }

        return response;
    }

    public ResponseMessage readOneNotify(Map<String, String> headerParam, String requestPath,
            String method, String urlParam, Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            // Check ABAC
            ABACResponseDTO abacResponseDTO = authorizeABAC(bodyParam, method, dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Map<String, Object> query = StringUtil.getQueryMap(urlParam);
                String userId = (String) query.get("userId");
                String notiId = (String) query.get("notiId");
                
                if(bodyParam != null){
                    userId = (String) bodyParam.get("userId");
                    notiId = (String) bodyParam.get("notiId");
                }
                Notify result = notifyService.updateStatusViewOne(userId, notiId, StatusView.SEEN.code());
                if (result != null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), result));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
                }
            } else {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền đọc thông báo", null));
            }
        }

        return response;
    }

    public ResponseMessage sendNotify(Map<String, Object> bodyParam) {
        ResponseMessage response = null;
        int type = (int) bodyParam.get("type");
        String title = (String) bodyParam.get("title");
        Object content = (Object) bodyParam.get("content");
        String url = (String) bodyParam.get("url");
        String objectType = (String) bodyParam.get("objectType");
        String objectUuid = (String) bodyParam.get("objectUuid");
        String userId = (String) bodyParam.get("userId");
        String siteId = (String) bodyParam.get("siteId");
        String sourceId = (String) bodyParam.get("sourceId");
        //Position cho các vị trí trên web (mặc định có quả chuông, 1 : trên bình đồ)
        Integer position = (Integer) bodyParam.get("position");
        String startTime = (String) bodyParam.get("startTime");
        //Stage id, code, status
        String stageCode = (String) bodyParam.get("stageCode");
        String stageId = (String) bodyParam.get("stageId");
        String statusCode = (String) bodyParam.get("statusCode");
        //Url link, url title
        String urlLink = (String) bodyParam.get("urlLink");
        String urlTitle = (String) bodyParam.get("urlTitle");
        //JobId
        String jobId = (String) bodyParam.get("jobId");

        NotifyRequestDTO notifyDto = new NotifyRequestDTO(type, title, content, url,
                objectType, objectUuid, userId, siteId, position, startTime, stageCode,
                stageId, statusCode, urlLink, urlTitle, sourceId, jobId);
        String invalidData = new NotifyValidation().validate(notifyDto);
        if (invalidData != null) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), invalidData, new MessageContent(HttpStatus.BAD_REQUEST.value(), invalidData, null));
        } else {
            switch (type) {
                case 1:
                    // SMS
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                    break;
                case 2:
                    // Email
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
                    break;
                case 3:
                    // Notify Web
                    long start = System.currentTimeMillis();
                    response = sendNotifyInApp(notifyDto, userId);
                    long end = System.currentTimeMillis();
                    LOGGER.info("===> Total time process sendNotifyInApp: {}ms", (end - start));
                    break;
                case 4:
                    //Mobile App
                    response = notifyService.sendToMobileApp(notifyDto);
                    break;
                case 5:
                    //Export file
                    response = notifyService.notifyExportFile(notifyDto);
                    break;
                case 6:
                    //Zalo
                    response = notifyService.sendNotifyZalo(notifyDto);
                    break;
                case 7:
                    //Import file
                    response = notifyService.notifyImportFile(notifyDto);    
                    break;
                default:
                    response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), "type is not support", null));
            }
        }

        return response;
    }

    private ResponseMessage sendNotifyInApp(NotifyRequestDTO notifyDto, String userId) {
        ResponseMessage response = null;

        ObjectMapper mapper = new ObjectMapper();
        List<UserReceiverDTO> usersList = getUserListFromIDService();
        usersList = mapper.convertValue(usersList, new TypeReference<List<UserReceiverDTO>>() {
        });
        if (usersList != null && !usersList.isEmpty()) {
            if (!StringUtils.isBlank(userId)) {
                usersList = usersList.stream().filter(user -> user.getId().equals(userId)).collect(Collectors.toList());
            }
            List<String> userIds = usersList.stream().map(item -> item.getId()).collect(Collectors.toList());
            // Push msg to socket work queue
            SocketNotifyRequestDTO socketNotifyRequest = buildSocketNotifyRequest(notifyDto, userIds);
            boolean result = rabbitMQClient.callWorkerService(workerQueue, socketNotifyRequest.toJsonString());
            LOGGER.info("Socket work queue - Push to {} msg: {} => {}", workerQueue, socketNotifyRequest.toJsonString(), result);
            if (result) {
                if (notifyDto.getPosition() == null || notifyDto.getPosition() == 0) {
                    long timestamp = System.currentTimeMillis() / 1000;
                    // message
                    SocketNotifyMessageDTO socketMessage = socketNotifyRequest.getData().getMessage();
                    Notify entity = toEntity(notifyDto);
                    entity.setUpdatedAt(timestamp);
                    entity.setIcon(socketMessage.getIcon());
                    entity.setTimeSendNotify(socketMessage.getTimeRequest());
                    entity.setStatusView(StatusView.NOT_SEEN.code());
                    entity.setStartTime(notifyDto.getStartTime());

                    // listId
                    List<Notify> notifyList = new ArrayList<>();
                    List<SocketNotifyListIdDTO> socketListId = socketNotifyRequest.getData().getListId();
                    for (SocketNotifyListIdDTO item : socketListId) {
                        NotifyPK notifyPK = new NotifyPK(item.getNotifyId(), timestamp);
                        Notify notifyNew = toEntity(notifyDto);
                        notifyNew.setUpdatedAt(timestamp);
                        notifyNew.setIcon(socketMessage.getIcon());
                        notifyNew.setTimeSendNotify(socketMessage.getTimeRequest());
                        notifyNew.setStatusView(StatusView.NOT_SEEN.code());
                        notifyNew.setNotifyPK(notifyPK);
                        notifyNew.setUserId(item.getUserId());
                        notifyNew.setObjectId(entity.getObjectId());
                        notifyNew.setJobId(entity.getJobId());
                        notifyList.add(notifyNew);
                    }
                    // Save data to DB
                    try {
                        notifyService.saveAll(notifyList);
                    } catch (Exception ex) {
                        LOGGER.error("Error save notify >>> {}", ex.toString());
                    }
                } else {
                    LOGGER.info("No send to bell position => No save database notify!");
                }
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
            } else {
                LOGGER.error("sendNotifyInApp >>> push msg to socket is failed!");
                response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
            }
        } else {
            LOGGER.error("sendNotifyInApp >>> usersList is null");
            response = new ResponseMessage(new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), null));
        }

        return response;
    }

    private SocketNotifyRequestDTO buildSocketNotifyRequest(NotifyRequestDTO notifyDto, List<String> userIds) {
        long timestamp = System.currentTimeMillis() / 1000;

        // message
        String objectType = notifyDto.getObjectType();
        SocketNotifyMessageDTO notifyMsg = new SocketNotifyMessageDTO();
        notifyMsg.setType(notifyDto.getType());
        notifyMsg.setTitle(notifyDto.getTitle());
        notifyMsg.setContent(notifyDto.getContent());
        notifyMsg.setUrl(notifyDto.getUrl());
        notifyMsg.setTimeRequest(timestamp);
        notifyMsg.setObjectType(objectType);
        notifyMsg.setObjectUuid(notifyDto.getObjectUuid());
        notifyMsg.setEmitName(emitName);
        notifyMsg.setPosition(notifyDto.getPosition());
        notifyMsg.setStartTime(notifyDto.getStartTime());
        notifyMsg.setStageCode(notifyDto.getStageCode());
        notifyMsg.setStageId(notifyDto.getStageId());
        notifyMsg.setStatusCode(notifyDto.getStatusCode());
        notifyMsg.setSiteId(notifyDto.getSiteId());
        notifyMsg.setSourceId(notifyDto.getSourceId());
        notifyMsg.setJobId(notifyDto.getJobId());

        String icon = "";
        switch (objectType) {
            case "1": // Tai nạn
                icon = "icon1";
                break;
            case "2": // Xe hỏng
                icon = "icon2";
                break;
            case "3": // Vật cản
                icon = "icon3";
                break;
            case "4": // Hoả hoạn
                icon = "icon4";
                break;
            case "5": // Mưa to
                icon = "icon5";
                break;
            case "6": // Sạt lở
                icon = "icon6";
                break;
            case "7": // Tuyết rơi
                icon = "icon7";
                break;
            case "8": // Sương mù
                icon = "icon8";
                break;
            case "9": // Đường đang thi công
                icon = "icon9";
                break;
            case "10": // Biểu tình đám đông
                icon = "icon10";
                break;
            case "11": // Đóng làn
                icon = "icon11";
                break;
            case "12": // Cấm đường
                icon = "icon12";
                break;
            case "13": // Sự kiện theo kế hoạch
                icon = "icon13";
                break;
            case "14": // Sự kiện bảo trì, bảo dưỡng
                icon = "icon14";
                break;
            case "15": // Trạng thái giao thông, ùn tắc
                icon = "icon15";
                break;
            case "16": // Phát hiện đối tượng theo dõi
                icon = "icon16";
                break;
            case "17": // Thiết bị mất kết nối
                icon = "icon17";
                break;    
            case "18": // Thiết bị kết nối lại
                icon = "icon18";
                break;
            case "19": // Xử lý công việc
                icon = "icon19";
                break;    
            case "20": // Sự kiện nhiệt độ cao
                icon = "icon20";    
                break;
            case "21": // Sự kiện phá hoại
                icon = "icon21";    
                break;
            case "22": // Sự kiện đường hư hỏng
                icon = "icon22";    
                break;
            case "23": // Sự kiện va chạm giao thông
                icon = "icon23";    
                break;
            case "24": // Sự kiện thiết bị gặp sự cố
                icon = "icon24";    
                break;
            case "25": // Sự kiện đậu đỗ trái phép (sai quy định)
                icon = "icon25";    
                break;    
            case "26": // Sự kiện vms board
                icon = "icon26";    
                break; 
            case "27": // Hết giờ bản tin vms board
                icon = "icon27";    
                break;
            case "28": // Gia hạn bản tin vms board
                icon = "icon28";    
                break;
            case "29": // Nổ lốp
                icon = "icon29";    
                break; 
            case "30": // Ném đá
                icon = "icon30";    
                break;
            case "31": // Mất trộm tài sản
                icon = "icon31";    
                break;    
        }
        notifyMsg.setIcon(icon);

        // listId
        List<SocketNotifyListIdDTO> notifyListIds = new ArrayList<>();
        userIds.forEach((item) -> {
            SocketNotifyListIdDTO notifyListId = new SocketNotifyListIdDTO();
            notifyListId.setNotifyId(UUID.randomUUID().toString());
            notifyListId.setUserId(item);

            notifyListIds.add(notifyListId);
        });

        // data
        SocketNotifyDataDTO socketNotifyDataDTO = new SocketNotifyDataDTO();
        socketNotifyDataDTO.setMessage(notifyMsg);
        socketNotifyDataDTO.setListId(notifyListIds);

        // notify request
        SocketNotifyRequestDTO socketNotifyRequestDto = new SocketNotifyRequestDTO();
        socketNotifyRequestDto.setServiceName(serviceName);
        socketNotifyRequestDto.setData(socketNotifyDataDTO);

        return socketNotifyRequestDto;
    }

    private Notify toEntity(NotifyRequestDTO notifyDto) {
        Notify notify = new Notify();
        notify.setType(notifyDto.getType());
        notify.setTitle(notifyDto.getTitle());
        notify.setContent(notifyDto.getContent().toString());
        notify.setUrl(notifyDto.getUrl());
        notify.setObjectId(notifyDto.getObjectUuid());
        notify.setStartTime(notifyDto.getStartTime());
        notify.setStageCode(notifyDto.getStageCode());
        notify.setStageId(notifyDto.getStageId());
        notify.setStatusCode(notifyDto.getStatusCode());
        notify.setJobId(notifyDto.getJobId());
        return notify;
    }

    public Map<String, Object> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, Object> map = new HashMap<>();
        String[] paramsList = params;
        for (String param : paramsList) {
            List<String> paramSplit = Arrays.asList(param.split("="));
            if (paramSplit.size() > 1) {
                String name = paramSplit.get(0);
                Object value = paramSplit.get(1);
                map.put(name, value);
            } else {
                String name = paramSplit.get(0);
                map.put(name, null);
            }

        }
        return map;
    }

    private List<UserReceiverDTO> getUserListFromIDService() {
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("GET");
        rbacRpcRequest.setRequestPath(RabbitMQProperties.USER_RPC_INTERNAL_LIST_URL);
        rbacRpcRequest.setBodyParam(null);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, rbacRpcRequest.toJsonString());
        LOGGER.info("getUserListFromIDService - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    List<UserReceiverDTO> userReceiverDTOList = mapper.treeToValue(jsonNode.get("data").get("data"), List.class);
                    return userReceiverDTOList;
                }
            } catch (Exception ex) {
                LOGGER.info("Error parse json in getUserListFromIDService from ID service: " + ex.toString());
                return null;
            }
        }
        return null;
    }
}
