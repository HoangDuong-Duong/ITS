package com.elcom.its.config.service;

import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.service.impl.LaneRouteListMessage;

public interface LaneRouteService {

    LaneRouteListMessage getAllLaneRoutes();

    Response getAllLane(String urlParam);
}
