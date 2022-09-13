package com.elcom.its.config.service;
import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.Route;
import com.elcom.its.config.model.dto.*;

import java.util.List;

public interface RouteService {
    Response getAllRoute();
    Response getAllRoute(Integer page, Integer size, String search);
    Response getRouteById(String id);
    Response saveRoute(Route route);
    Response updateRoute(Route route);
    Response deleteRoute(String route);
    Response deleteMultiRoute(ListUuid route);
}
