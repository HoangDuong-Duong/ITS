package elcom.com.its.notify.recevice.service.service;

import elcom.com.its.notify.recevice.service.model.dto.ViolationListResponse;

public interface NotifyViolationService {
    ViolationListResponse getListNotifyBySite(String camIds, int page, int size);
    ViolationListResponse getListNotify(int page, int size);
}
