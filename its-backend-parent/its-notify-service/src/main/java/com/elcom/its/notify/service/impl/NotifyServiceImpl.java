/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.notify.service.impl;

import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.notify.constant.Constant;
import com.elcom.its.notify.enums.StatusView;
import com.elcom.its.notify.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.notify.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.notify.model.DeviceMap;
import com.elcom.its.notify.model.Notify;
import com.elcom.its.notify.model.NotifyPK;
import com.elcom.its.notify.model.dto.*;
import com.elcom.its.notify.repository.DeviceMapRepository;
import com.elcom.its.notify.repository.NotifyRepository;
import com.elcom.its.notify.service.NotifyService;
import com.elcom.its.notify.zalo.ZaloClient;
import com.elcom.its.notify.zalo.dto.FollowerDataResult;
import com.elcom.its.notify.zalo.dto.UploadResult;
import com.elcom.its.notify.zalo.dto.UserId;
import com.elcom.its.notify.zalo.dto.ZaloResponse;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;

/**
 * @author Admin
 */
@Service
public class NotifyServiceImpl implements NotifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyServiceImpl.class.getName());

    @Value("${socket.service.name}")
    private String serviceName;

    @Value("${socket_rpc.service.name}")
    private String socketRpcServiceName;

    @Value("${socket.emit.name}")
    private String emitName;

    @Value("${socket.emit.name.mobile}")
    private String emitNameMobile;

    @Value("${socket.emit.name.export}")
    private String emitNameExport;
    
    @Value("${socket.emit.name.import}")
    private String emitNameImport;

    @Value("${socket.worker.queue}")
    private String workerQueue;

    @Value("${fix-delay-noti}")
    private Long delayTime;

    @Autowired
    private NotifyRepository notifyRepository;

    @Autowired
    private DeviceMapRepository deviceMapRepository;

    @Autowired
    private RabbitMQClient rabbitMQClient;
    
    @Autowired
    private ZaloClient zaloClient;

    @Override
    public void save(Notify notify) {
        notifyRepository.save(notify);
    }

    @Override
    public Iterable<Notify> saveAll(List<Notify> notifyList) {
        return notifyRepository.saveAll(notifyList);
    }

    @Override
    public boolean update(Notify notify) {
        try {
            notifyRepository.save(notify);
        } catch (Exception ex) {
            LOGGER.error("Error to update notify >>> {}", ex.toString());
            return false;
        }
        return true;
    }

    @Override
    public void remove(Notify notify) {
        notifyRepository.delete(notify);
    }

    @Override
    public Page<Notify> findNotifyByUser(Integer page, Integer size, String keyword, String userId, long fromDate, long toDate, List<String> typeIcon, int type) {
        page = page != null && page > 0 ? page : 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by("timeSendNotify").descending());

        Page<Notify> notifyList = null;
        if (StringUtil.isNullOrEmpty(keyword) && (typeIcon == null || typeIcon.isEmpty())) {
            notifyList = notifyRepository.findByUserIdAndTypeAndTimeSendNotifyBetween(userId, type, fromDate, toDate, pageable);
        } else if (!StringUtil.isNullOrEmpty(keyword) && (typeIcon == null || typeIcon.isEmpty())) {
            keyword = StringUtil.replaceSpecialSQLCharacter(keyword);
            notifyList = notifyRepository.searchNotify(userId, type, keyword.toUpperCase(), fromDate, toDate, pageable);
        } else if (StringUtil.isNullOrEmpty(keyword) && (typeIcon != null && !typeIcon.isEmpty())) {
            notifyList = notifyRepository.findByUserIdAndIconAndTimeSendNotifyBetween(userId, type, typeIcon, fromDate, toDate, pageable);
        } else if (!StringUtil.isNullOrEmpty(keyword) && (typeIcon != null && !typeIcon.isEmpty())) {
            keyword = StringUtil.replaceSpecialSQLCharacter(keyword);
            notifyList = notifyRepository.searchNotifybyKeywordAndYypeIcon(userId, type, keyword.toUpperCase(), typeIcon, fromDate, toDate, pageable);
        }

        return notifyList;
    }

    @Override
    public boolean updateStatusView(String userId, int statusView) {
        return notifyRepository.updateStatusView(userId, statusView) > 0;
    }

    @Override
    public Notify updateStatusViewOne(String userId, String notiId, int statusView) {
        Notify notify = notifyRepository.findByNotifyPK_IdAndUserId(notiId, userId);
        if (notify != null) {
            notify.setStatusView(1);
        }
        return notifyRepository.save(notify);
    }

    @Override
    public long countNotifyNotRead(String userId, int statusView, long fromDate, long toDate, int type) {
        return notifyRepository.countByUserIdAndTypeAndStatusViewAndTimeSendNotifyBetween(userId, type, statusView, fromDate, toDate);
    }

    @Override
    public NotifyDTO transform(Notify x) {
        NotifyDTO notifyDto = NotifyDTO.builder()
                .id(x.getNotifyPK().getId())
                .title(x.getTitle())
                .content(x.getContent())
                .icon(x.getIcon())
                .url(x.getUrl())
                .statusView(x.getStatusView())
                .type(x.getType())
                .timeSendNotify(x.getTimeSendNotify())
                .objectId(x.getObjectId())
                .objectUuid(x.getObjectId())
                .startTime(x.getStartTime())
                .stageCode(x.getStageCode())
                .stageId(x.getStageId())
                .statusCode(x.getStatusCode())
                .jobId(x.getJobId())
                .build();

        return notifyDto;
    }

    @Override
    public ResponseMessage sendToMobileApp(NotifyRequestDTO notifyRequestDTO) {
        ResponseMessage response = null;

        switch (notifyRequestDTO.getObjectType()) {
            default:
                response = saveAndSendToOutbox(notifyRequestDTO);
        }
        return response;
    }

    @Override
    public ResponseMessage notifyExportFile(NotifyRequestDTO requestDTO) {
        SocketNotifyRequestDTO socketNotifyRequest = buildSocketNotifyRequest(requestDTO);
        boolean res = rabbitMQClient.callWorkerService(workerQueue, socketNotifyRequest.toJsonString());
        LOGGER.info("Socket work queue - Push to {} msg: {} => {}", workerQueue, socketNotifyRequest.toJsonString(), res);
        return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
    }

    public Notify toEntity(NotifyRequestDTO notifyDto) {
        Notify notify = new Notify();
        notify.setType(notifyDto.getType());
        notify.setTitle(notifyDto.getTitle());
        notify.setContent(notifyDto.getContent().toString());
        notify.setUrl(notifyDto.getUrl());
        return notify;
    }

    public ResponseMessage saveAndSendToOutbox(NotifyRequestDTO notifyRequestDTO) {
        //Convert Entity to MessageModel
        try {
            Instant current = Instant.now();
            String notifyId = UUID.randomUUID().toString();
            long now = System.currentTimeMillis() / 1000;
            Notify notify = toEntity(notifyRequestDTO);
            notify.setTimeSendNotify(now);
            notify.setStatusView(StatusView.NOT_SEEN.code());
            notify.setUserId(notifyRequestDTO.getUserId());
            notify.setIcon(notifyRequestDTO.getObjectType());
            notify.setObjectId(notifyRequestDTO.getObjectUuid());
            NotifyPK notifyPK = new NotifyPK(notifyId, now);
            notify.setNotifyPK(notifyPK);
            notify.setUpdatedAt(now);
            notifyRepository.save(notify);
            Boolean checkSocket = checkSocketOnline(notifyRequestDTO.getUserId());

            if (checkSocket == null) {
                return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
            } else if (checkSocket == true) {
                SocketNotifyRequestDTO socketNotifyRequest = buildSocketNotifyRequest(notifyRequestDTO, notifyId);
                boolean res = rabbitMQClient.callWorkerService(workerQueue, socketNotifyRequest.toJsonString());
                LOGGER.info("Socket work queue - Push to {} msg: {} => {}", workerQueue, socketNotifyRequest.toJsonString(), res);
            } else {
                DeviceMap deviceMap = deviceMapRepository.findByUserId(notifyRequestDTO.getUserId());
                MobileNotifyRequestDTO mobileNotifyRequestDTO = new MobileNotifyRequestDTO();
                mobileNotifyRequestDTO.setContent(notify.getContent());
                mobileNotifyRequestDTO.setTitle(notify.getTitle());
                mobileNotifyRequestDTO.setUserId(notifyRequestDTO.getUserId());
                mobileNotifyRequestDTO.setData(notifyRequestDTO.getObjectUuid());

                Message message = new Message();
                message.setType(Constant.NOTIFY_MOBILE);
                message.setData(mobileNotifyRequestDTO.toJsonString());
                if (deviceMap != null) {
                    Instant compare = current.minusSeconds(delayTime);
                    if (compare.isAfter(deviceMap.getLastTimeOnline())) {
                        boolean result = rabbitMQClient.callWorkerService(RabbitMQProperties.WORKER_QUEUE_MESSAGE, message.toJsonString());
                        LOGGER.info("Socket work queue - Push to {} msg: {} => {}", RabbitMQProperties.WORKER_QUEUE_MESSAGE, message.toJsonString(), result);
                        deviceMap.setPatrolViolation(false);
                        deviceMap.setLastTimeOnline(current);
                    } else {
                        deviceMap.setPatrolViolation(true);
                    }
                } else {
                    boolean result = rabbitMQClient.callWorkerService(RabbitMQProperties.WORKER_QUEUE_MESSAGE, message.toJsonString());
                    LOGGER.info("Socket work queue - Push to {} msg: {} => {}", RabbitMQProperties.WORKER_QUEUE_MESSAGE, message.toJsonString(), result);
                    deviceMap = new DeviceMap();
                    deviceMap.setId(UUID.randomUUID().toString());
                    deviceMap.setPatrolViolation(false);
                    deviceMap.setLastTimeOnline(current);
                    deviceMap.setUserId(mobileNotifyRequestDTO.getUserId());
                }
                deviceMapRepository.save(deviceMap);
            }
        } catch (Exception e) {
            LOGGER.error("error when save and send notify to message-queue");
        }
        return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
    }

    private SocketNotifyRequestDTO buildSocketNotifyRequest(NotifyRequestDTO notifyRequestDTO, String notifyId) {
        long timestamp = System.currentTimeMillis() / 1000;

        SocketNotifyRequestDTO request = new SocketNotifyRequestDTO();
        SocketNotifyDataDTO socketData = new SocketNotifyDataDTO();
        SocketNotifyMessageDTO socketMessage = new SocketNotifyMessageDTO();
        socketMessage.setContent(notifyRequestDTO.getContent());
        socketMessage.setType(notifyRequestDTO.getType());
        socketMessage.setTitle(notifyRequestDTO.getTitle());
        socketMessage.setContent(notifyRequestDTO.getContent());
        socketMessage.setUrl(notifyRequestDTO.getUrl());
        socketMessage.setTimeRequest(timestamp);
        socketMessage.setObjectType(notifyRequestDTO.getObjectType());
        socketMessage.setObjectUuid(notifyRequestDTO.getObjectUuid());
        socketMessage.setEmitName(emitNameMobile);

        List<SocketNotifyListIdDTO> socketListId = new ArrayList<>();
        socketListId.add(new SocketNotifyListIdDTO(notifyId, notifyRequestDTO.getUserId()));
        socketData.setListId(socketListId);

        request.setData(socketData);
        socketData.setMessage(socketMessage);
        request.setServiceName(serviceName);
        return request;
    }

    private SocketNotifyRequestDTO buildSocketNotifyRequest(NotifyRequestDTO notifyRequestDTO) {
        long timestamp = System.currentTimeMillis() / 1000;

        SocketNotifyRequestDTO request = new SocketNotifyRequestDTO();
        SocketNotifyDataDTO socketData = new SocketNotifyDataDTO();
        SocketNotifyMessageDTO socketMessage = new SocketNotifyMessageDTO();
        socketMessage.setContent(notifyRequestDTO.getContent());
        socketMessage.setType(notifyRequestDTO.getType());
        socketMessage.setTitle(notifyRequestDTO.getTitle());
        socketMessage.setContent(notifyRequestDTO.getContent());
        socketMessage.setUrl(notifyRequestDTO.getUrl());
        socketMessage.setTimeRequest(timestamp);
        socketMessage.setObjectType(notifyRequestDTO.getObjectType());
        socketMessage.setObjectUuid(notifyRequestDTO.getObjectUuid());
        socketMessage.setEmitName(emitNameExport);

        List<SocketNotifyListIdDTO> socketListId = new ArrayList<>();
        socketListId.add(new SocketNotifyListIdDTO(UUID.randomUUID().toString(), notifyRequestDTO.getUserId()));
        socketData.setListId(socketListId);

        request.setData(socketData);
        socketData.setMessage(socketMessage);
        request.setServiceName(serviceName);
        return request;
    }
    
    private SocketNotifyRequestDTO buildSocketWithEmitNameNotifyRequest(NotifyRequestDTO notifyRequestDTO, String emitName) {
        long timestamp = System.currentTimeMillis() / 1000;

        SocketNotifyRequestDTO request = new SocketNotifyRequestDTO();
        SocketNotifyDataDTO socketData = new SocketNotifyDataDTO();
        SocketNotifyMessageDTO socketMessage = new SocketNotifyMessageDTO();
        socketMessage.setContent(notifyRequestDTO.getContent());
        socketMessage.setType(notifyRequestDTO.getType());
        socketMessage.setTitle(notifyRequestDTO.getTitle());
        socketMessage.setContent(notifyRequestDTO.getContent());
        socketMessage.setUrl(notifyRequestDTO.getUrl());
        socketMessage.setTimeRequest(timestamp);
        socketMessage.setObjectType(notifyRequestDTO.getObjectType());
        socketMessage.setObjectUuid(notifyRequestDTO.getObjectUuid());
        socketMessage.setEmitName(emitName);

        List<SocketNotifyListIdDTO> socketListId = new ArrayList<>();
        socketListId.add(new SocketNotifyListIdDTO(UUID.randomUUID().toString(), notifyRequestDTO.getUserId()));
        socketData.setListId(socketListId);

        request.setData(socketData);
        socketData.setMessage(socketMessage);
        request.setServiceName(serviceName);
        return request;
    }

    private Boolean checkSocketOnline(String userId) throws JsonProcessingException {
        Boolean isOnline = null;

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("serviceName", socketRpcServiceName);
        bodyParam.put("data", data);

        //Authorize user action with api -> call rbac service
        ObjectMapper mapper = new ObjectMapper();
        String meg = mapper.writeValueAsString(bodyParam);
        try {
            String result = rabbitMQClient.callRpcService(RabbitMQProperties.SOCKET_RPC_EXCHANGE,
                    RabbitMQProperties.SOCKET_RPC_QUEUE, RabbitMQProperties.SOCKET_RPC_KEY, meg);
            if (result != null) {
                List<String> res = mapper.readValue(result, List.class);
                if (!Objects.isNull(res) && !res.isEmpty()) {
                    isOnline = true;
                } else {
                    isOnline = false;
                }
            }
        } catch (Exception e) {
            LOGGER.error("err call socket rpc {}", e);
        }
        return isOnline;
    }

    @Override
    public ResponseMessage sendNotifyZalo(NotifyRequestDTO requestDTO) {
        long start = System.currentTimeMillis();
        if (StringUtil.isNullOrEmpty(requestDTO.getUrl())) {
            return new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(),
                    "Link ảnh là bắt buộc", null));
        }
        ResponseMessage response = null;
        boolean result = false;
        String errorMsg = null;
        //Get list follower
        ZaloResponse followerResponse = zaloClient.getAllFollowers();
        if (followerResponse != null && followerResponse.getError() == ZaloResponse.SUCCESS) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);

            try {
                FollowerDataResult followerResult = mapper.readValue(mapper.writeValueAsString(followerResponse.getData()), FollowerDataResult.class);
                if (followerResult != null && followerResult.getTotal() > 0 && followerResult.getFollowers() != null) {
                    LOGGER.info("Starting send msg to {} zalo followers...", followerResult.getTotal());
                    //Upload image 2 zalo => Attachment_id
                    String image = requestDTO.getUrl();
                    String imageName = image.substring(image.lastIndexOf("/") + 1);
                    ZaloResponse uploadResponse = zaloClient.uploadImage(imageName, image);
                    if (uploadResponse != null && uploadResponse.getError() == ZaloResponse.SUCCESS) {
                        //
                        UploadResult uploadResult = mapper.readValue(mapper.writeValueAsString(uploadResponse.getData()), UploadResult.class);
                        if (uploadResult != null && !StringUtil.isNullOrEmpty(uploadResult.getAttachment_id())) {
                            List<UserId> zaloIdList = followerResult.getFollowers();
                            ZaloResponse sendMsgResult = null;

                            String message = requestDTO.getContent().toString();
                            for (UserId userId : zaloIdList) {
                                if(requestDTO.getUrlLink() != null && requestDTO.getUrlTitle() != null) {
                                    sendMsgResult = zaloClient.sendZaloMessage(userId.getUser_id(), message, uploadResult.getAttachment_id(),
                                        requestDTO.getUrlLink(), requestDTO.getUrlTitle());
                                } else {
                                    sendMsgResult = zaloClient.sendZaloMessage(userId.getUser_id(), message, uploadResult.getAttachment_id());
                                }
                                LOGGER.info("Send to zalo id: {}, message: {}: => {}",
                                        userId.getUser_id(), message, (sendMsgResult != null
                                        && sendMsgResult.getError() == ZaloResponse.SUCCESS ? "OK" : "Fail"));
                                if (sendMsgResult != null && sendMsgResult.getError() == ZaloResponse.SUCCESS) {
                                    result = true;
                                    errorMsg = null;
                                } else {
                                    result = false;
                                    errorMsg = sendMsgResult != null ? sendMsgResult.getMessage() : "Error to send zalo message";
                                }
                            }
                        } else {
                            LOGGER.info("No attachment_id for send msg report...");
                            errorMsg = "No attachment_id for send msg report...";
                        }
                    } else {
                        LOGGER.info("Upload image 2 zalo error...");
                        errorMsg = "Upload image 2 zalo error...";
                    }
                } else {
                    LOGGER.info("No zalo follower to send msg report...");
                    errorMsg = "No zalo follower to send msg report...";
                }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            LOGGER.info("Get zalo follower fail...");
            errorMsg = "Get zalo follower fail...";
        }
        if (result) {
            response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
        } else {
            response = new ResponseMessage(new MessageContent(HttpStatus.FAILED_DEPENDENCY.value(), errorMsg, null));
        }
        long end = System.currentTimeMillis();
        LOGGER.info("Total process send zalo in : {} ms", (end-start));
        return response;
    }
    
    @Override
    public ResponseMessage notifyImportFile(NotifyRequestDTO requestDTO) {
        SocketNotifyRequestDTO socketNotifyRequest = buildSocketWithEmitNameNotifyRequest(requestDTO, emitNameImport);
        boolean res = rabbitMQClient.callWorkerService(workerQueue, socketNotifyRequest.toJsonString());
        LOGGER.info("Socket work queue - Push to {} msg: {} => {}", workerQueue, socketNotifyRequest.toJsonString(), res);
        return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), null));
    }
}
