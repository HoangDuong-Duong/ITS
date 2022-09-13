package com.elcom.its.report.service;

import com.elcom.its.report.model.dto.ObjectTrackingPaginationDTOMesage;

/**
 * @author ducduongn
 */
public interface ObjectTrackingService {

    ObjectTrackingPaginationDTOMesage findProcessedObjectTracking(Integer page, Integer size,
            String processFromDate, String processToDate);
}
