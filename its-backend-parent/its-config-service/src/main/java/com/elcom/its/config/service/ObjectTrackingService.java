package com.elcom.its.config.service;

import com.elcom.its.config.model.ObjectTracking;
import com.elcom.its.config.model.dto.ObjectTrackingCreateUpdateDto;
import com.elcom.its.config.model.dto.ObjectTrackingPaginationDTOMesage;
import com.elcom.its.config.model.dto.ObjectTrackingProcessDTO;
import com.elcom.its.config.model.dto.Response;

import java.util.List;

/**
 * @author ducduongn
 */
public interface ObjectTrackingService {

    List<ObjectTracking> getAll();

    Response getById(String id);

    ObjectTrackingPaginationDTOMesage getAll(Integer page, Integer size, String search, 
            String brand, String reason, String vehicleType, String recognitionFromDate,
            String recognitionToDate, String processFromDate, String processToDate, 
            String createFromDate, String createToDate);

    Response findById(String id);

    Response createObjectTracking(ObjectTrackingCreateUpdateDto objectTracking);

    Response updateObjectTracking(ObjectTrackingCreateUpdateDto objectTracking, String id);

    Response deleteObjectTracking(String id);

    Response deleteMultipleObjectTracking(List<String> ids);

    Response deleteUnitDataInObjectTracking(List<String> unitIds);

    Response findByIdentificationList(List<String> identificationList);
    
    Response updateObjectTrackingProcessed(ObjectTrackingProcessDTO objectTracking, String id);
}
