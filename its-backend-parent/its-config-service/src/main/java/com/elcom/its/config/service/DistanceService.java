package com.elcom.its.config.service;

import com.elcom.its.config.model.Distance;
import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.dto.*;

import java.util.List;

public interface DistanceService {
    Response getAllDistance();
    Response getAllDistance(Integer page, Integer size, String search);
    Response getDistanceById(String id);
    Response saveDistance(Distance distance);
    Response updateDistance(Distance distance);
    Response deleteDistance(String distance);
    Response deleteMultiDistance(ListUuid distance);
    Response filterDistance(Integer page,Integer size,String id,String name,String siteStart,String siteEnd);
}
