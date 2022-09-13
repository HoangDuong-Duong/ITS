package com.elcom.its.map.service;

import com.elcom.its.map.dto.Response;
import com.elcom.its.map.service.impl.LaneRouteListMessage;

public interface LaneRouteService {

    LaneRouteListMessage getAllLaneRoutes();

    Response getAllLane(String urlParam);
}
