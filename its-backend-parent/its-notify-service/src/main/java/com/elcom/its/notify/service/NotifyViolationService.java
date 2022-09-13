package com.elcom.its.notify.service;

import com.elcom.its.notify.model.dto.ViolationListResponse;

public interface NotifyViolationService {
    ViolationListResponse getListNotifyBySite(String camIds, int page, int size);
    ViolationListResponse getListNotify(int page, int size);
}
