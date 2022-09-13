/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.map.controller;

import com.elcom.its.map.dto.AuthorizationResponseDTO;
import com.elcom.its.map.dto.ABACResponseDTO;
import com.elcom.its.map.dto.Response;
import com.elcom.its.map.service.MapService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Admin
 */
@Controller
public class MapController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapController.class);

    @Autowired
    private MapService mapService;

    public ResponseMessage getAllSystemDevices(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                if (resultCheckABAC.getAdmin() != null && resultCheckABAC.getAdmin()) {
                    response = new ResponseMessage(new MessageContent(mapService.getAllSystemDevicesForMap().getData()));
                } else {
                    String paramRequest = "stageIds=" + dto.getUnit().getLisOfStage();
                    response = new ResponseMessage(new MessageContent(mapService.getSystemDevicesInStagesForMap(paramRequest).getData()));
                }
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách thiết bị ",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách thiết bị ", null));
            }
        }
        return response;
    }

    public ResponseMessage getStageForMap(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                response = new ResponseMessage(new MessageContent(mapService.getStageForMap().getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách đoạn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách đoạn", null));
            }
        }
        return response;
    }

    public ResponseMessage getPageEventInStages(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> mapUrl = StringUtil.getUrlParamValues(urlParam);
            ABACResponseDTO resultCheckABAC = checkMapPermission(dto, "DETAIL", requestUrl, mapUrl.get("stageCode") != null ? mapUrl.get("stageCode") : "");
            if (resultCheckABAC.getStatus()) {
                response = new ResponseMessage(new MessageContent(mapService.getPageEventInStages(urlParam).getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách sự kiện trong đoạn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách sự kiện trong đoạn", null));
            }
        }
        return response;
    }

    public ResponseMessage getObjectTrackingHistoryInStagesForMap(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> mapUrl = StringUtil.getUrlParamValues(urlParam);
            ABACResponseDTO resultCheckABAC = checkMapPermission(dto, "DETAIL", requestUrl, mapUrl.get("stageCode") != null ? mapUrl.get("stageCode") : "");
            if (resultCheckABAC.getStatus()) {
                response = new ResponseMessage(new MessageContent(mapService.getObjectTrackingHistoryInStagesForMap(urlParam).getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách đối tượng theo dõi trong đoạn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách đối tượng theo dõi trong đoạn", null));
            }
        }
        return response;
    }

    public ResponseMessage getTrafficFlowInStageForMap(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> mapUrl = StringUtil.getUrlParamValues(urlParam);
            ABACResponseDTO resultCheckABAC = checkMapPermission(dto, "DETAIL", requestUrl, mapUrl.get("stageCode") != null ? mapUrl.get("stageCode") : "");
            if (resultCheckABAC.getStatus()) {
                response = new ResponseMessage(new MessageContent(mapService.getTrafficFlowInStageForMap(urlParam).getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy dữ liệu lưu lượng trong đoạn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy dữ liệu lưu lượng trong đoạn", null));
            }
        }
        return response;
    }

    public ResponseMessage getSystemDevicesInStagesForMap(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> mapUrl = StringUtil.getUrlParamValues(urlParam);
            ABACResponseDTO resultCheckABAC = checkMapPermission(dto, "DETAIL", requestUrl, mapUrl.get("stageCode") != null ? mapUrl.get("stageCode") : "");
            if (resultCheckABAC.getStatus()) {
                response = new ResponseMessage(new MessageContent(mapService.getSystemDevicesInStagesForMap(urlParam).getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách thiết bị trong đoạn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách thiết bị trong đoạn", null));
            }
        }
        return response;
    }

    public ResponseMessage getMapBetweenSite(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                response = new ResponseMessage(new MessageContent(mapService.getMapBetweenSite(urlParam).getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy dữ liệu bản đồ",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy dữ liệu bản đồ", null));
            }
        }
        return response;
    }

    public ResponseMessage getReportEventByTypeInStage(String requestUrl, String method, String urlParam, Map<String, String> headerMap)
            throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> mapUrl = StringUtil.getUrlParamValues(urlParam);
            ABACResponseDTO resultCheckABAC = checkMapPermission(dto, "DETAIL", requestUrl, mapUrl.get("stageCode") != null ? mapUrl.get("stageCode") : "");
            if (resultCheckABAC.getStatus()) {
                Response getReportResponse = mapService.getReportEventByTypeInStage(urlParam);
                response = new ResponseMessage(new MessageContent(getReportResponse.getStatus(),
                        getReportResponse.getMessage(), getReportResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy dữ liệu sự kiện của đoạn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy dữ liệu sự kiện của đoạn", null));
            }
        }
        return response;
    }

    public ResponseMessage getListNearbyCamera(String requestUrl, String method, String urlParam, Map<String, String> headerMap)
            throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> mapUrl = StringUtil.getUrlParamValues(urlParam);
            ABACResponseDTO resultCheckABAC = checkMapPermission(dto, "LIST", requestUrl, mapUrl.get("stageId"));
            if (resultCheckABAC.getStatus()) {
                Response getCameraResponse = mapService.getNearbyCamera(urlParam);
                response = new ResponseMessage(new MessageContent(getCameraResponse.getStatus(),
                        getCameraResponse.getMessage(), getCameraResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách camera lân cận",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách camera lân cận", null));
            }
        }
        return response;
    }

    public ResponseMessage getListCameraNearBySite(String requestUrl, String method, String urlParam, Map<String, String> headerMap)
            throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> mapUrl = StringUtil.getUrlParamValues(urlParam);
            ABACResponseDTO resultCheckABAC = checkMapPermission(dto, "LIST", requestUrl, mapUrl.get("stageId"));
            if (resultCheckABAC.getStatus()) {
                Response getCameraResponse = mapService.getListCameraNearBySite(urlParam);
                response = new ResponseMessage(new MessageContent(getCameraResponse.getStatus(),
                        getCameraResponse.getMessage(), getCameraResponse.getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách camera lân cận",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy danh sách camera lân cận", null));
            }
        }
        return response;
    }

    public ResponseMessage getStraightMapData(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> mapUrl = StringUtil.getUrlParamValues(urlParam);
            ABACResponseDTO resultCheckABAC = checkMapPermission(dto, "DETAIL", requestUrl, mapUrl.get("stageCode") != null ? mapUrl.get("stageCode") : "");
            if (resultCheckABAC.getStatus()) {
                response = new ResponseMessage(new MessageContent(mapService.getStraightMapData(urlParam).getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy thông tin đoạn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy thông tin đoạn", null));
            }
        }
        return response;
    }

    public ResponseMessage getStraightMapTrafficData(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> mapUrl = StringUtil.getUrlParamValues(urlParam);
            ABACResponseDTO resultCheckABAC = checkMapPermission(dto, "DETAIL", requestUrl, mapUrl.get("stageCode") != null ? mapUrl.get("stageCode") : "");
            if (resultCheckABAC.getStatus()) {
                response = new ResponseMessage(new MessageContent(mapService.getStraightMapTrafficData(urlParam).getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy thông tin đoạn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy thông tin đoạn", null));
            }
        }
        return response;
    }

    public ResponseMessage getLongLat(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            Map<String, String> mapUrl = StringUtil.getUrlParamValues(urlParam);
            ABACResponseDTO resultCheckABAC = checkMapPermission(dto, "DETAIL", requestUrl, mapUrl.get("stageCode") != null ? mapUrl.get("stageCode") : "");
            if (resultCheckABAC.getStatus()) {
                response = new ResponseMessage(new MessageContent(mapService.getLongLat(urlParam).getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy thông tin long lat",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy thông tin long lat", null));
            }
        }
        return response;
    }

    public ResponseMessage getMidPointInStages(String requestUrl, String method, String urlParam, Map<String, String> headerMap) throws ExecutionException, InterruptedException {
        ResponseMessage response;
        AuthorizationResponseDTO dto = authenToken(headerMap);
        if (dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestUrl);
            if (resultCheckABAC.getStatus()) {
                response = new ResponseMessage(new MessageContent(mapService.getMidPointInStages().getData()));
            } else {
                response = new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy thông tin đoạn",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền lấy thông tin đoạn", null));
            }
        }
        return response;
    }
}
