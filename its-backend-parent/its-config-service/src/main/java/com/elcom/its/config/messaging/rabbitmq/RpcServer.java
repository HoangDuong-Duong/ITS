/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.messaging.rabbitmq;

import com.elcom.its.config.controller.*;
import com.elcom.its.config.controller.CameraController;
import com.elcom.its.config.controller.CameraLayoutController;
import com.elcom.its.config.controller.DistanceController;
import com.elcom.its.config.controller.RouteController;
import com.elcom.its.config.controller.StretchController;
import com.elcom.its.constant.ResourcePath;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author Admin
 */
public class RpcServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    @Autowired
    private DistanceController distanceController;

    @Autowired
    private StretchController stretchController;

    @Autowired
    private CameraController cameraController;

    @Autowired
    private RouteController routeController;
    
    @Autowired
    private CameraLayoutController cameraLayoutController;

    @Autowired
    private ObjectTrackingController objectTrackingController;

    @Autowired
    private SiteController siteController;
    
    @Autowired
    private ImageCameraController imageCameraController;
    
    @Autowired
    private LayoutAreaController layoutAreaController;
    
    @Autowired
    private ProcessUnitController processUnitController;

    @Autowired
    private VmsBoardController vmsBoardController;
    
    @Autowired
    private ModelProfileController modelProfileController;
    
    @Autowired
    private VdsController vdsController;

    @Autowired
    private DisplayScriptController displayScriptController;

    @Autowired
    private NewsLetterTemplateController newsLetterTemplateController;
    
    @Autowired
    private VmsController vmsController;

    @Autowired
    private CategoryController categoryController;

    @Autowired
    private CameraDirectionController cameraDirectionController;

    @Autowired
    private ServerController serverController;
    
    @Autowired
    private LaneRouteController laneRouteController;
    
    @Autowired
    private DeviceController deviceController;
    
    @Autowired
    private PlaceController placeController;

    @RabbitListener(queues = "${systemconfig.rpc.queue}")
    public String processService(String json) {
        try {
            LOGGER.info(" [-->] Server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            RequestMessage request = mapper.readValue(json, RequestMessage.class);

            //Process here
            ResponseMessage response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            if (request != null) {
                String requestPath = request.getRequestPath().replace(request.getVersion() != null
                        ? request.getVersion() : ResourcePath.VERSION, "");
                String urlParam = request.getUrlParam() != null ? URLDecoder.decode(request.getUrlParam(), StandardCharsets.UTF_8.name()) : request.getUrlParam();
                String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();

                switch (request.getRequestMethod()) {
                    case "GET":
                        if ("/systemconfig/distance".equalsIgnoreCase(requestPath)) {
                            if (!StringUtil.isNullOrEmpty(pathParam)) {
                                response = distanceController.getDistanceById(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            }else{
                                response = distanceController.getAllDistance(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                            }
                        }
                        else if("/systemconfig/distance/filter".equalsIgnoreCase(requestPath)) {
                            response = distanceController.filterDistance(headerParam, requestPath, urlParam, bodyParam );
                        }
                        else if("/systemconfig/stage".equalsIgnoreCase(requestPath)) {
                            if (!StringUtil.isNullOrEmpty(pathParam)) {
                                response = stretchController.getStretchById(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            }else{
                                response = stretchController.getAllStretch(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                            }
                        }
                        else if("/systemconfig/stage/list".equalsIgnoreCase(requestPath)) {
                            response = stretchController.getStretchByListCode(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if("/systemconfig/stage/filter".equalsIgnoreCase(requestPath)) {
                            response = stretchController.filterStretch(headerParam, requestPath, urlParam, bodyParam );
                        }
                        else if("/systemconfig/route".equalsIgnoreCase(requestPath)) {
                            if (!StringUtil.isNullOrEmpty(pathParam)) {
                                response = routeController.getRouteById(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            }else{
                                response = routeController.getAllRoute(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                            }
                        }
                        else if("/systemconfig/category".equalsIgnoreCase(requestPath)) {
                            if (!StringUtil.isNullOrEmpty(pathParam)) {
                                response = categoryController.getCategoryById(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            }else{
                                response = categoryController.getAllCategory(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                            }
                        }
                        else if ("/systemconfig/category/filter".equalsIgnoreCase(requestPath)) {
                            LOGGER.info("Categorty filter");
                            response = categoryController.getCategoryByType(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if("/systemconfig/site".equalsIgnoreCase(requestPath)) {
                            if (!StringUtil.isNullOrEmpty(pathParam)) {
                                response = siteController.getSiteById(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            }else{
                                response = siteController.getAllSite(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                            }
                        }
                        else if("/systemconfig/site/haveCamera".equalsIgnoreCase(requestPath)) {
                                response = siteController.getAllSiteHaveCamera(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/systemconfig/camera".equalsIgnoreCase(requestPath)) {
                            if (StringUtils.isNotBlank(pathParam)) { // Detail camera
                                response = cameraController.getCameraById(headerParam, requestPath, request.getRequestMethod(), pathParam, bodyParam);
                            } else { // List camera
                                response = cameraController.getAllCamera(headerParam, requestPath, request.getRequestMethod(), request.getUrlParam(), bodyParam);
                            }
                        }
                        else if ("/systemconfig/camera/camera-type".equalsIgnoreCase(requestPath)) {
                            response = cameraController.getCameraType(headerParam, requestPath, request.getRequestMethod(), bodyParam);
                        }
                        else if ("/systemconfig/camera/camera-layout".equalsIgnoreCase(requestPath)) {
                            if (StringUtils.isNotBlank(pathParam)) {
                                response = cameraLayoutController.getCameraLayoutsById(headerParam, requestPath, request.getRequestMethod(), pathParam, bodyParam);
                            } else {
                                response = cameraLayoutController.getCameraLayoutsList(headerParam, requestPath, request.getRequestMethod(), bodyParam);
                            }
                        }
                        else if ("/systemconfig/camera/camera-by-site-list".equalsIgnoreCase(requestPath)) {
                            response = cameraController.getCameraBySiteIdList(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/systemconfig/camera/camera-by-stage-list".equalsIgnoreCase(requestPath)) {
                            response = cameraController.getCameraByStageIdList(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/systemconfig/camera/camera-layout/layouts".equalsIgnoreCase(requestPath)) {
                            response = cameraLayoutController.getLayoutsByCameraId(headerParam, requestPath, request.getRequestMethod(), pathParam, urlParam, bodyParam);
                        }
                        else if ("/systemconfig/camera/image-camera/images".equalsIgnoreCase(requestPath)) {
                            response = imageCameraController.getImageByCameraId(headerParam, requestPath, request.getRequestMethod(), pathParam, bodyParam);
                        }
                        else if ("/systemconfig/camera/camera-layout/layout-area".equalsIgnoreCase(requestPath)) {
                            if (StringUtils.isNotBlank(pathParam)) {
                                response = layoutAreaController.getLayoutAreasById(headerParam, requestPath, request.getRequestMethod(), pathParam, bodyParam);
                            } else {
                                response = layoutAreaController.getLayoutAreasList(headerParam, requestPath, request.getRequestMethod(), bodyParam);
                            }
                        }
                        else if ("/systemconfig/camera/camera-layout/layout-area/areas".equalsIgnoreCase(requestPath)) {
                            response = layoutAreaController.getAreasByLayoutId(headerParam, requestPath, request.getRequestMethod(), pathParam, bodyParam);
                        }    
                        else if ("/systemconfig/object-tracking".equalsIgnoreCase(requestPath)) {

                            if(StringUtil.isNullOrEmpty(pathParam)){
                                response = objectTrackingController.getAllObjectTracking(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                            }
                            else{
                                 response = objectTrackingController.findById(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            }
                        }
                        else if ("/systemconfig/object-tracking/by-identification".equalsIgnoreCase(requestPath)) {
                            response = objectTrackingController.getObjectTrackingByIdentificationList(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/systemconfig/process-unit".equalsIgnoreCase(requestPath)) {
                            if (StringUtils.isNotBlank(pathParam)) {
                                response = processUnitController.getProcessUnitById(headerParam, requestPath, request.getRequestMethod(), pathParam, bodyParam);
                            } else {
                                response = processUnitController.getProcessUnitList(headerParam, requestPath, request.getRequestMethod(), request.getUrlParam(), bodyParam);
                            }
                        }
                        else if ("/systemconfig/vms-board".equalsIgnoreCase(requestPath)) {
                            if (!StringUtil.isNullOrEmpty(pathParam)) {
                                response = vmsBoardController.getVmsBoardById(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            }else{
                                response = vmsBoardController.getAllVmsBoard(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                            }
                        }
                        else if ("/systemconfig/vms-board/history".equalsIgnoreCase(requestPath)) {
                                response = vmsBoardController.getDisplayScript(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }

                        else if ("/systemconfig/process-unit/model-profiles".equalsIgnoreCase(requestPath)){
                            if (StringUtils.isNotBlank(pathParam)) {
                                response = modelProfileController.getModelProfilesById(headerParam, requestPath, request.getRequestMethod(), pathParam, bodyParam);
                            } else {
                                response = modelProfileController.getModelProfilesList(headerParam, requestPath, request.getRequestMethod(), bodyParam);
                            }
                        }
                        else if("/systemconfig/vds".equalsIgnoreCase(requestPath)){
                            if (StringUtils.isNotBlank(pathParam)) {
                                response = vdsController.detailVds(requestPath, headerParam, bodyParam, requestPath, pathParam);
                            } else {
                                response = vdsController.findVds(requestPath, headerParam, bodyParam, request.getRequestMethod(), urlParam);
                            }
                        }
                        else if("/systemconfig/site/provinces".equalsIgnoreCase(requestPath)) {
                                response = siteController.getAllProvinces(headerParam, requestPath, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/site/districts".equalsIgnoreCase(requestPath)) {
                                response = siteController.getAllDistrict(headerParam, requestPath, request.getRequestMethod(), pathParam,pathParam);
                        }
                        else if("/systemconfig/site/wards".equalsIgnoreCase(requestPath)) {
                            response = siteController.getAllWard(headerParam, requestPath, request.getRequestMethod(), pathParam,pathParam);
                        }
                        else if("/systemconfig/site/filter".equalsIgnoreCase(requestPath)) {
                            response = siteController.filterSite(headerParam, requestPath, urlParam, bodyParam );
                        }

                        else if("/systemconfig/displayscript".equalsIgnoreCase(requestPath)) {
                            if (!StringUtil.isNullOrEmpty(pathParam)) {
                                response = displayScriptController.getDisplayScriptById(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            }else{
                                response = displayScriptController.getAllDisplayScript(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                            }
                        }
                        else if("/systemconfig/displayscript/plan".equalsIgnoreCase(requestPath)) {
                                response = displayScriptController.getDisplayScriptPlan(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if("/systemconfig/news-letter".equalsIgnoreCase(requestPath)) {
                            if (!StringUtil.isNullOrEmpty(pathParam)) {
                                response = newsLetterTemplateController.getNewsLetterById(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            }else{
                                response = newsLetterTemplateController.getAllNewsLetterTemplate(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                            }
                        }
                        else if ("/systemconfig/camera/camera-layout/layout-area/id-max".equalsIgnoreCase(requestPath)) {
                            response = layoutAreaController.getLayoutsAreaIdMax(headerParam, requestPath, request.getRequestMethod(), bodyParam);
                        }
                        //VMS API 
                        else if ("/systemconfig/vms".equalsIgnoreCase(requestPath)) {
                            if (StringUtils.isNotBlank(pathParam)) {
                                response = vmsController.findVmsById(headerParam, requestPath, request.getRequestMethod(), pathParam);
                            } else {
                                response = vmsController.getAllVms(headerParam, requestPath, request.getRequestMethod(), urlParam);
                            }
                        }
                        else if ("/systemconfig/vms/camera".equalsIgnoreCase(requestPath)) {
                            response = vmsController.loadVmsCamera(headerParam, requestPath, request.getRequestMethod(), urlParam);
                        }
                        
                        else if ("/systemconfig/vms/connection".equalsIgnoreCase(requestPath)) {
                            response = vmsController.checkConnectionById(headerParam, requestPath, request.getRequestMethod(), urlParam);
                        }
                        
                        else if ("/systemconfig/vms/connection-by-info".equalsIgnoreCase(requestPath)) {
                            response = vmsController.checkConnectionByInfo(headerParam, requestPath, request.getRequestMethod(), urlParam);
                        }
                        else if ("/systemconfig/camera/direction".equalsIgnoreCase(requestPath)) {
                            response = cameraDirectionController.getAllCameraDirection(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        // ----------------------------
                        else if ("/systemconfig/process-unit/server".equalsIgnoreCase(requestPath)) {
                            response = serverController.getServerList(headerParam, requestPath, request.getRequestMethod(), bodyParam);
                        }
                        else if ("/systemconfig/lanes".equalsIgnoreCase(requestPath)) {
                            response = laneRouteController.getAllLane(headerParam, requestPath,  request.getRequestMethod(), urlParam);
                        }
                        else if ("/systemconfig/site/point".equalsIgnoreCase(requestPath)) {
                            response = siteController.getPointByPositionM(headerParam, requestPath, requestPath, urlParam, pathParam);
                        }
                        else if ("/systemconfig/devices".equalsIgnoreCase(requestPath)) {
                            response = deviceController.getAllSystemDevices(requestPath,  request.getRequestMethod(), urlParam, headerParam);
                        }
                        else if ("/systemconfig/places".equalsIgnoreCase(requestPath)) {
                            response = placeController.getAllPlace(requestPath,  request.getRequestMethod(), urlParam, headerParam);
                        }
                        break;
                    case "POST":
                        if ("/systemconfig/distance".equalsIgnoreCase(requestPath)) {
                            response = distanceController.createDistance(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if("/systemconfig/stage".equalsIgnoreCase(requestPath)){
                            response = stretchController.createStretch(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if("/systemconfig/route".equalsIgnoreCase(requestPath)){
                            response = routeController.createRoute(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if("/systemconfig/category".equalsIgnoreCase(requestPath)){
                            response = categoryController.createCategory(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if("/systemconfig/camera".equalsIgnoreCase(requestPath)) {
                            response = cameraController.createCamera(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if("/systemconfig/site".equalsIgnoreCase(requestPath)){
                            response = siteController.createSite(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if ("/systemconfig/camera/camera-layout".equalsIgnoreCase(requestPath)) {
                            response = cameraLayoutController.createCameraLayouts(requestPath, headerParam, bodyParam, request.getRequestMethod());
                        }
                        else if ("/systemconfig/camera/image-camera".equalsIgnoreCase(requestPath)) {
                            response = imageCameraController.createImageByCameraId(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if ("/systemconfig/camera/camera-layout/layout-area".equalsIgnoreCase(requestPath)) {
                            response = layoutAreaController.createLayoutAreas(requestPath, headerParam, bodyParam, request.getRequestMethod());
                        }
                        else if("/systemconfig/object-tracking".equalsIgnoreCase(requestPath)){
                            response = objectTrackingController.createObjectTracking(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if("/systemconfig/vms-board".equalsIgnoreCase(requestPath)){
                            response = vmsBoardController.createVmsBoard(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if("/systemconfig/vds".equalsIgnoreCase(requestPath)){
                            response = vdsController.createVds(requestPath, headerParam, bodyParam, request.getRequestMethod());
                        }
                        else if("/systemconfig/displayscript".equalsIgnoreCase(requestPath)){
                            response = displayScriptController.createDisplayScript(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if("/systemconfig/news-letter".equalsIgnoreCase(requestPath)){
                            response = newsLetterTemplateController.createNewsLetterTemplate(headerParam, bodyParam, requestPath, request.getRequestMethod());
                        }
                        else if("/systemconfig/site/test/data".equalsIgnoreCase(requestPath)){
                            response = siteController.createListSite(headerParam, requestPath, urlParam, bodyParam);
                        }
                        //VMS API 
                        else if ("/systemconfig/vms".equalsIgnoreCase(requestPath)) {
                                response = vmsController.createVms(headerParam, requestPath, request.getRequestMethod(),pathParam, bodyParam);
                        }
                        else if ("/systemconfig/vms/camera".equalsIgnoreCase(requestPath)) {
                            response = vmsController.addMultiCamera(headerParam, requestPath, request.getRequestMethod(),pathParam, bodyParam);
                        }                                             
                        // ----------------------------
                        else if ("/systemconfig/process-unit/json-spec".equalsIgnoreCase(requestPath)) {
                            response = processUnitController.createJsonSpec(requestPath, headerParam, request.getRequestMethod(), pathParam, bodyParam);
                        }
                        break;
                    case "PUT":
                        if ("/systemconfig/distance".equalsIgnoreCase(requestPath)) {
                            response = distanceController.updateDistance(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/stage".equalsIgnoreCase(requestPath)){
                            response = stretchController.updateStretch(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/route".equalsIgnoreCase(requestPath)){
                            response = routeController.updateRoute(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/category".equalsIgnoreCase(requestPath)){
                            response = categoryController.updateCategory(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/camera".equalsIgnoreCase(requestPath)) {
                            LOGGER.info("Update camera");
                            response = cameraController.updateCamera(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/site".equalsIgnoreCase(requestPath)){
                            response = siteController.updateRoute(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/systemconfig/camera/camera-layout".equalsIgnoreCase(requestPath) && !StringUtil.isNullOrEmpty(pathParam)){
                            response = cameraLayoutController.updateCameraLayouts(requestPath, headerParam, bodyParam, request.getRequestMethod(), Long.valueOf(pathParam));
                        }
                        else if ("/systemconfig/camera/camera-layout/layout-area".equalsIgnoreCase(requestPath) && !StringUtil.isNullOrEmpty(pathParam)) {
                            response = layoutAreaController.updateLayoutAreas(requestPath, headerParam, bodyParam, request.getRequestMethod(), Long.valueOf(pathParam));
                        }
                        else if("/systemconfig/object-tracking".equalsIgnoreCase(requestPath)){
                            response = objectTrackingController.updateObjectTracking(headerParam, bodyParam, requestPath, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/vms-board".equalsIgnoreCase(requestPath)){
                            response = vmsBoardController.updateVmsBoard(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/vds".equalsIgnoreCase(requestPath)){
                            response = vdsController.updateVds(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/systemconfig/process-unit".equalsIgnoreCase(requestPath) && pathParam != null && pathParam.length() > 0) {
                            response = processUnitController.updateProcessUnit(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }    
                        else if("/systemconfig/displayscript".equalsIgnoreCase(requestPath)){
                            response = displayScriptController.updateDisplayScript(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/news-letter".equalsIgnoreCase(requestPath)){
                            response = newsLetterTemplateController.updateNewsLetterTemplate(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }

                         //VMS API 
                        else if ("/systemconfig/vms".equalsIgnoreCase(requestPath)) {
                                response = vmsController.updateVms(headerParam, requestPath, request.getRequestMethod(),pathParam, bodyParam);
                        }                                 
                        // ----------------------------
                        else if("/systemconfig/vds/status".equalsIgnoreCase(requestPath)){
                            response = vdsController.updateVdsStatus(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/vds/video-threads".equalsIgnoreCase(requestPath)){
                            response = vdsController.updateVdsVideoThreads(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/vds/camera-status/internal".equalsIgnoreCase(requestPath)){
                            response = vdsController.updateCameraStatus(bodyParam);
                        }
                        else if("/systemconfig/object-tracking/processed".equalsIgnoreCase(requestPath)){
                            response = objectTrackingController.updateObjectTrackingProcessed(headerParam, bodyParam, requestPath, request.getRequestMethod(), pathParam);
                        }
                        break;
                    case "DELETE":
                        if ("/systemconfig/distance".equalsIgnoreCase(requestPath)) {
                            response = distanceController.deleteDistance(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        if ("/systemconfig/distance/multi-delete".equalsIgnoreCase(requestPath)) {
                            response = distanceController.deleteMultiDistance(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/systemconfig/stage".equalsIgnoreCase(requestPath)) {
                            response = stretchController.deleteStretch(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        if ("/systemconfig/stage/multi-delete".equalsIgnoreCase(requestPath)) {
                            response = stretchController.deleteMultiStretch(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/systemconfig/route".equalsIgnoreCase(requestPath)) {
                            response = routeController.deleteRoute(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        if ("/systemconfig/route/multi-delete".equalsIgnoreCase(requestPath)) {
                            response = routeController.deleteMultiRoute(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/systemconfig/category".equalsIgnoreCase(requestPath)) {
                            response = categoryController.deleteCategory(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/systemconfig/category/multi-delete".equalsIgnoreCase(requestPath)) {
                            response = categoryController.deleteMultiCategory(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/systemconfig/camera".equalsIgnoreCase(requestPath)) {
                            LOGGER.info("Delete camera");
                            response = cameraController.deleteCamera(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/systemconfig/camera/multi-delete".equalsIgnoreCase(requestPath)) {
                            LOGGER.info("Delete camera");
                            response = cameraController.deleteMultiCamera(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if ("/systemconfig/site".equalsIgnoreCase(requestPath)) {
                            response = siteController.deleteSite(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/systemconfig/site/multi-delete".equalsIgnoreCase(requestPath)) {
                            response = siteController.deleteMultiSite(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/systemconfig/camera/camera-layout".equalsIgnoreCase(requestPath)) {
                            if (StringUtils.isNotBlank(pathParam)) {
                                response = cameraLayoutController.deleteCameraLayouts(requestPath, headerParam, request.getRequestMethod(), Long.valueOf(pathParam), bodyParam);
                            } else {
                                response = cameraLayoutController.deleteMultiCameraLayouts(requestPath, headerParam, bodyParam, request.getRequestMethod());
                            }
                        }
                        else if ("/systemconfig/camera/camera-layout/layout-area".equalsIgnoreCase(requestPath)) {
                            response = layoutAreaController.deleteLayoutAreas(requestPath, headerParam, request.getRequestMethod(), Long.valueOf(pathParam), bodyParam);
                        }
                        else if("/systemconfig/object-tracking".equalsIgnoreCase(requestPath)){
                            response = objectTrackingController.deleteObjectTracking(headerParam, bodyParam, requestPath, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/object-tracking/multi-delete".equalsIgnoreCase(requestPath)){
                            response = objectTrackingController.deleteMultiObjectTracking(headerParam, requestPath, request.getRequestMethod(), urlParam, bodyParam);
                        }
                        else if("/systemconfig/vms-board".equalsIgnoreCase(requestPath)){
                            response = vmsBoardController.deleteVmsBoard(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/vms-board/multi-delete".equalsIgnoreCase(requestPath)){
                            response = vmsBoardController.deleteMultiVmsBoard(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if("/systemconfig/vds".equalsIgnoreCase(requestPath)){
                            if (StringUtils.isNotBlank(pathParam)) {
                                response = vdsController.deleteVds(requestPath, headerParam, bodyParam, request.getRequestMethod(), pathParam);
                            }   else {
                                response = vdsController.deleteMultiVds(requestPath, headerParam, bodyParam, request.getRequestMethod());
                            }
                        }
                        else if ("/systemconfig/displayscript".equalsIgnoreCase(requestPath)) {
                            response = displayScriptController.deleteDisplayScript(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/systemconfig/news-letter".equalsIgnoreCase(requestPath)) {
                            response = newsLetterTemplateController.deleteNewsLetterTemplate(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        else if ("/systemconfig/news-letter/multi-delete".equalsIgnoreCase(requestPath)) {
                            response = newsLetterTemplateController.deleteMultiNewsLetter(requestPath,headerParam, bodyParam, request.getRequestMethod(), pathParam);
                        }
                        //VMS API 
                        else if ("/systemconfig/vms".equalsIgnoreCase(requestPath)) {
                                response = vmsController.deleteVms(headerParam, requestPath, request.getRequestMethod(),pathParam);
                        } 
                        else if ("/systemconfig/vms/multi-delete".equalsIgnoreCase(requestPath)) {
                                response = vmsController.deleteMulti(headerParam, requestPath, request.getRequestMethod(),pathParam,bodyParam);
                        } 
                        // ----------------------------
                        
                        break;
                    default:
                        break;
                }
                LOGGER.info(" [<--] Server returned {}", (response != null ? response.toJsonString() : "null"));
            }
            return response != null ? response.toJsonString() : null;
        } catch (Exception ex) {
            LOGGER.error("Error to processService >>> " + ExceptionUtils.getStackTrace(ex));
            ex.printStackTrace();
        }
        return null;
    }
}
