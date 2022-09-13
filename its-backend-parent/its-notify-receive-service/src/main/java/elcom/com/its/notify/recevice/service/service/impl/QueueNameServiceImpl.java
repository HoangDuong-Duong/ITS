/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.service.impl;

import com.elcom.its.utils.StringUtil;
import elcom.com.its.notify.recevice.service.model.dto.*;
import elcom.com.its.notify.recevice.service.rabbitmq.RabbitMQClient;
import elcom.com.its.notify.recevice.service.service.*;
import elcom.com.its.notify.recevice.service.thread.ThreadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 */
@Service
public class QueueNameServiceImpl implements QueueNameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueNameServiceImpl.class);

    @Autowired
    private ThreadManager threadManager;

    @Autowired
    ITSCameraService cameraService;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private ITSSiteService itsSiteService;

    @Autowired
    private StageService stageService;

    @Autowired
    private VMSBoardService vmsBoardService;

    @Autowired
    private VMSService vmsService;

    @Autowired
    private ITSObjectTrackingService itsObjectTrackService;

    @Override
    public void save(String name) {
        QUEUE_NAME.add(name);
        threadManager.execute(() -> {
            rabbitMQClient.createQueue(name);
        });
    }

    @Override
    public void delete(String name) {
        QUEUE_NAME.remove(name);
        threadManager.execute(() -> {
            rabbitMQClient.deleteQueue(name);
        });
    }

    @Override
    public List<String> get() {
        if (QUEUE_NAME.isEmpty()) {
            List<String> queueNameList = new ArrayList<>();
            //Get from database vehicle track
//            List<String> queueNameList = itsObjectTrackService.getAllQueueName();
//            if(queueNameList == null) queueNameList = new ArrayList<>();

//            //Get from database violation track
//            List<String> queueNameListNM = violationTrackService.getAllQueueName();
//            if (queueNameListNM != null && !queueNameListNM.isEmpty()) {
//                queueNameList.addAll(queueNameListNM);
//            }
//            
            //Get from DBM camera list
            List<Camera> cameraDtoList = cameraService.getAll();
            if (cameraDtoList != null && !cameraDtoList.isEmpty()) {
                for (Camera dto : cameraDtoList) {
                    queueNameList.add("notify_trigger_cam_" + dto.getId());
                }
            }

            if (queueNameList != null && !queueNameList.isEmpty()) {
                queueNameList.stream().map((tmp) -> {
                    QUEUE_NAME.add(tmp);
                    return tmp;
                }).forEachOrdered((tmp) -> {
                    //threadManager.execute(() -> {
                    if (!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)) {
                        LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                        rabbitMQClient.createQueue(tmp);
                    }
                    //});
                });
            }
        }
        return QueueNameService.QUEUE_NAME;
    }

    @Override
    public List<String> getQueueEvent() {
        List<String> queueNameList = new ArrayList<>();
        List<SiteInfoConfig> sitesList = itsSiteService.getAll();
        if (sitesList != null && !sitesList.isEmpty()) {
            for (SiteInfoConfig dto : sitesList) {
                queueNameList.add("notify_trigger_event_" + dto.getSiteId());
            }
        }

        if (queueNameList != null && !queueNameList.isEmpty()) {
            queueNameList.stream().forEachOrdered((tmp) -> {
                if(!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)){
//                    LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                    rabbitMQClient.createQueue(tmp);
                }
            });
        }
        return queueNameList;
    }

    @Override
    public List<String> getQueueStage() {
        List<String> queueNameList = new ArrayList<>();
        queueNameList.add("notify_trigger_traffic_status");
        List<StageConfig> stageList = stageService.getAll();
        if (stageList != null && !stageList.isEmpty()) {
            for (StageConfig dto : stageList) {
                queueNameList.add("notify_trigger_trafficflow_" + dto.code);
            }
        }
        if (queueNameList != null && !queueNameList.isEmpty()) {
            queueNameList.stream().forEachOrdered((tmp) -> {
                if(!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)){
//                    LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                    rabbitMQClient.createQueue(tmp);
                }
            });
        }
        return queueNameList;
    }

    @Override
    public List<String> getQueueCam() {
        List<String> queueNameList = new ArrayList<>();
        List<Camera> cameraDtoList = cameraService.getAll();
        if (cameraDtoList != null && !cameraDtoList.isEmpty()) {
            for (Camera dto : cameraDtoList) {
                queueNameList.add("notify_trigger_cam_" + dto.getId());
            }
        }
        if (queueNameList != null && !queueNameList.isEmpty()) {
            queueNameList.stream().forEachOrdered((tmp) -> {
                if(!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)){
//                    LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                    rabbitMQClient.createQueue(tmp);
                }
            });
        }
        return queueNameList;
    }

    @Override
    public List<String> getQueueHeartBeatVms() {
        List<String> queueNameList = new ArrayList<>();
        List<Vms> vmsList = vmsService.getAll();
        if (vmsList != null && !vmsList.isEmpty()) {
            for (Vms dto : vmsList) {
                queueNameList.add("notify_trigger_heartbeat_vmscamera_" + dto.getId());
            }
        }
        if (queueNameList != null && !queueNameList.isEmpty()) {
            queueNameList.stream().forEachOrdered((tmp) -> {
                if(!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)){
//                    LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                    rabbitMQClient.createQueue(tmp);
                }
            });
        }
        return queueNameList;
    }

    @Override
    public List<String> getQueueHeartBeatCamera() {
        List<String> queueNameList = new ArrayList<>();
        List<Camera> cameraDtoList = cameraService.getAll();
        if (cameraDtoList != null && !cameraDtoList.isEmpty()) {
            for (Camera dto : cameraDtoList) {
                queueNameList.add("notify_trigger_heartbeat_cam_" + dto.getId());
            }
        }
        if (queueNameList != null && !queueNameList.isEmpty()) {
            queueNameList.stream().forEachOrdered((tmp) -> {
                if(!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)){
//                    LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                    rabbitMQClient.createQueue(tmp);
                }
            });
        }
        return queueNameList;
    }

    @Override
    public List<String> getQueueHeartBeatVMSBoard() {
        List<String> queueNameList = new ArrayList<>();
//        queueNameList.add("notify_trigger_heartbeat_vmsboard_");
        List<VmsBoard> vmsList = vmsBoardService.getAll();
        if (vmsList != null && !vmsList.isEmpty()) {
            for (VmsBoard dto : vmsList) {
                queueNameList.add("notify_trigger_heartbeat_vmsboard_" + dto.getId());
            }
        }
        if (queueNameList != null && !queueNameList.isEmpty()) {
            queueNameList.stream().forEachOrdered((tmp) -> {
                if(!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)){
//                    LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                    rabbitMQClient.createQueue(tmp);
                }
            });
        }
        return queueNameList;
    }

    @Override
    public List<String> getQueueNewsVMSBoard() {
        List<String> queueNameList = new ArrayList<>();
        queueNameList.add("vms_board_error_worker_queue");
//        List<Vms> vmsList = vmsService.getAll();
//        if (vmsList != null && !vmsList.isEmpty()) {
//            for (Vms dto : vmsList) {
//                queueNameList.add("notify_trigger_heartbeat_cam_" + dto.getId());
//            }
//        }
        if (queueNameList != null && !queueNameList.isEmpty()) {
            queueNameList.stream().forEachOrdered((tmp) -> {
                if(!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)){
//                    LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                    rabbitMQClient.createQueue(tmp);
                }
            });
        }
        return queueNameList;
    }

    @Override
    public List<String> getQueueDisplayVMSBoard() {
        List<String> queueNameList = new ArrayList<>();
        queueNameList.add("vms_board_display_worker_queue");
//        List<Vms> vmsList = vmsService.getAll();
//        if (vmsList != null && !vmsList.isEmpty()) {
//            for (Vms dto : vmsList) {
//                queueNameList.add("notify_trigger_heartbeat_cam_" + dto.getId());
//            }
//        }
        if (queueNameList != null && !queueNameList.isEmpty()) {
            queueNameList.stream().forEachOrdered((tmp) -> {
                if(!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)){
//                    LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                    rabbitMQClient.createQueue(tmp);
                }
            });
        }
        return queueNameList;
    }

    @Override
    public List<String> getQueueObjectTracking() {
        List<String> queueNameList= new ArrayList<>();
        queueNameList.add("notify_trigger_objecttracking");
        if (queueNameList != null && !queueNameList.isEmpty()) {
            queueNameList.stream().forEachOrdered((tmp) -> {
                if(!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)){
//                    LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                    rabbitMQClient.createQueue(tmp);
                }
            });
        }
        return queueNameList;
    }

    @Override
    public List<String> getQueueNameJob() {
        List<String> queueNameList= new ArrayList<>();
        queueNameList.add("its_notify_job_worker_queue");
        if (queueNameList != null && !queueNameList.isEmpty()) {
            queueNameList.stream().forEachOrdered((tmp) -> {
                if(!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)){
//                    LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                    rabbitMQClient.createQueue(tmp);
                }
            });
        }
        return queueNameList;
    }

    @Override
    public List<String> getQueueNameFinishEvent() {
        List<String> queueNameList= new ArrayList<>();
        queueNameList.add("its_notify_finish_event_worker_queue");
        if (queueNameList != null && !queueNameList.isEmpty()) {
            queueNameList.stream().forEachOrdered((tmp) -> {
                if(!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)){
//                    LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                    rabbitMQClient.createQueue(tmp);
                }
            });
        }
        return queueNameList;
    }

    @Override
    public List<String> getQueueEventChangeStatus() {
        List<String> queueNameList= new ArrayList<>();
        queueNameList.add("its_notify_event_change_status_worker_queue");
        if (queueNameList != null && !queueNameList.isEmpty()) {
            queueNameList.stream().forEachOrdered((tmp) -> {
                if(!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)){
//                    LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                    rabbitMQClient.createQueue(tmp);
                }
            });
        }
        return queueNameList;
    }

    @Override
    public List<String> getQueueNameScheduleEvent() {
        List<String> queueNameList= new ArrayList<>();
        queueNameList.add("its_notify_scheduled_event_worker_queue");
        if (queueNameList != null && !queueNameList.isEmpty()) {
            queueNameList.stream().forEachOrdered((tmp) -> {
                if(!StringUtil.isNullOrEmpty(tmp) && !"null".equals(tmp)){
//                    LOGGER.info("QueueNameServiceImpl tmp name: {}", tmp);
                    rabbitMQClient.createQueue(tmp);
                }
            });
        }
        return queueNameList;
    }


}
