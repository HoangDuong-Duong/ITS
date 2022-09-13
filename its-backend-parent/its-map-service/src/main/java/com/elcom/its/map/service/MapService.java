/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.map.service;

import com.elcom.its.map.dto.Response;

/**
 *
 * @author Admin
 */
public interface MapService {

    public Response getStageForMap();

    public Response getTrafficFlowInStageForMap(String stageId);

    public Response getSystemDevicesInStagesForMap(String urlParam);

    public Response getLongLat(String urlParam);

    public Response getAllSystemDevicesForMap();

    public Response getObjectTrackingHistoryInStagesForMap(String urlParam);

    public Response getPageEventInStages(String urlParam);

    public Response getMapBetweenSite(String urlParam);

    public Response getReportEventByTypeInStage(String urlParam);

    public Response getNearbyCamera(String urlParam);

    public Response getStraightMapData(String urlParam);

    public Response getStraightMapTrafficData(String urlParam);

    public Response getMidPointInStages();

    public Response getListCameraNearBySite(String urlParam);
}
