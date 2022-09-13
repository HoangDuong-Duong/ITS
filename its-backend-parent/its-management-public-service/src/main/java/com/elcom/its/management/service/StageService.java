package com.elcom.its.management.service;

import com.elcom.its.management.model.Job;

public interface StageService {

    public String findBySite(String siteId);
    String getStageContainSite(String siteId);

    String getStageHaveEvent(String eventId);

    String getStageByPositionM(Long positionM);
}
