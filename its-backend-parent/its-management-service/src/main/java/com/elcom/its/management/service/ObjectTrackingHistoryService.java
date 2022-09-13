package com.elcom.its.management.service;

import com.elcom.its.management.dto.Response;

public interface ObjectTrackingHistoryService {

    Response getObjectTrackingHistoryList(String stages, Boolean isAdmin, Long hourInterval);
}
