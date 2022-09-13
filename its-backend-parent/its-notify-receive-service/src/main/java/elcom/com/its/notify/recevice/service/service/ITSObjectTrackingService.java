/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcom.com.its.notify.recevice.service.service;

import elcom.com.its.notify.recevice.service.model.dto.ObjectTrackingDTO;
import elcom.com.its.notify.recevice.service.model.dto.ObjectTrackingResponse;
import elcom.com.its.notify.recevice.service.model.dto.ObjectTrackingResponseList;

import java.util.List;

/**
 *
 * @author Admin
 */
public interface ITSObjectTrackingService {
 
    ObjectTrackingResponse findByIdentification(String idenfitication);

    ObjectTrackingResponse createObjectTrackingFromDBM(ObjectTrackingDTO track);

    void deleteObjectTrackingFromDBM(String identification);

//    void deleteMultiObjectTrackingFromDBM(List<String> identificationObjectTrackList);
    ObjectTrackingResponse updateObjectTrackingFromDBM(String identification, ObjectTrackingDTO objectTrack);

    List<String> getAllQueueName();

    ObjectTrackingResponse findById(String id);

    ObjectTrackingResponseList findObjectTrackingAll(Integer page, Integer size, String sort, String fromDate, String toDate, String identification, String infoObject,
                                                     String model, String reason, String objectType, String typeList, String filterObjectType, List<String> filterObjectIds);

    ObjectTrackingResponseList findListByIdentification(String identification);
}
