/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface QueueNameService {

    //static List<String> QUEUE_NAME = new ArrayList<>(Arrays.asList("queue1", "queue2", "queue3"));
    
    static List<String> QUEUE_NAME = new ArrayList<>();

    void save(String name);

    void delete(String name);

    List<String> get();

    List<String> getQueueEvent();

    List<String> getQueueStage();

    List<String> getQueueCam();

    List<String> getQueueHeartBeatVms();

    List<String> getQueueHeartBeatCamera();

    List<String> getQueueHeartBeatVMSBoard();

    List<String> getQueueNewsVMSBoard();
    List<String> getQueueDisplayVMSBoard();

    List<String> getQueueObjectTracking();

    List<String> getQueueNameJob();

    List<String> getQueueNameFinishEvent();

    List<String> getQueueEventChangeStatus();

    List<String> getQueueNameScheduleEvent();


}
