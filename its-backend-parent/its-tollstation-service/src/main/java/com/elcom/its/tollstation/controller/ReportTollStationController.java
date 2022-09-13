package com.elcom.its.tollstation.controller;

import com.elcom.its.tollstation.dto.*;
import com.elcom.its.tollstation.service.TollStationService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class ReportTollStationController extends BaseController {
    @Autowired
    private TollStationService tollStationService;



    public ResponseMessage reportLaneStatus(Map<String, String> headerParam, String requestPath,  String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền báo cáo trạm cân",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền báo cáo trạm cân", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String tollStation = params.get("tollStationId");
                String startDate = params.get("startDate");
                String endDate = params.get("endDate");
                LanesResponseDTO data ;
                if(resultCheckABAC.getAdmin()!=null &&resultCheckABAC.getAdmin()){
                    data = tollStationService.reportLaneStatus(tollStation,null,true);
                }else {
                    data = tollStationService.reportLaneStatus(tollStation,dto.getUnit().getLisOfStage(),false);
                }

                response = new ResponseMessage(new MessageContent(data.getData()));
            }
        }

        return response;
    }

    public ResponseMessage reportLaneHistoryClose(Map<String, String> headerParam, String requestPath,  String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo trạm cân",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo trạm cân", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String tollStation = params.get("tollStationId");
                String startDate = params.get("startDate");
                String endDate = params.get("endDate");
                LanesHistoryResponseDTO data ;
                if(resultCheckABAC.getAdmin()!=null &&resultCheckABAC.getAdmin()){
                    data = tollStationService.getHistoryCloseLane(tollStation,startDate,endDate,null,true);
                }else {
                    data = tollStationService.getHistoryCloseLane(tollStation,startDate,endDate,dto.getUnit().getLisOfStage(),false);
                }
                response = new ResponseMessage(new MessageContent(data.getData()));
            }
        }

        return response;
    }

    public ResponseMessage reportTraffic(Map<String, String> headerParam, String requestPath,  String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo trạm cân",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo trạm cân", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String tollStation = params.get("tollStationId");
                String startDate = params.get("startDate");
                String endDate = params.get("endDate");
                Response data ;
                if(resultCheckABAC.getAdmin()!=null &&resultCheckABAC.getAdmin()){
                    data = tollStationService.report(tollStation,startDate,endDate,null,true);
                }else {
                    data = tollStationService.report(tollStation,startDate,endDate,dto.getUnit().getLisOfStage(),false);
                }
                response = new ResponseMessage(new MessageContent(data.getData()));
            }
        }

        return response;
    }

    public ResponseMessage reportTrafficLane(Map<String, String> headerParam, String requestPath,  String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo trạm cân",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo trạm cân", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String tollStation = params.get("tollStationId");
                String startDate = params.get("startDate");
                String endDate = params.get("endDate");
                Response data ;
                if(resultCheckABAC.getAdmin()!=null &&resultCheckABAC.getAdmin()){
                    data = tollStationService.reportLane(tollStation,startDate,endDate,null,true);
                }else {
                    data = tollStationService.reportLane(tollStation,startDate,endDate,dto.getUnit().getLisOfStage(),false);
                }
                response = new ResponseMessage(new MessageContent(data.getData()));
            }
        }

        return response;
    }

    public ResponseMessage reportTrafficLine(Map<String, String> headerParam, String requestPath,  String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo trạm cân",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem báo cáo trạm cân", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String tollStation = params.get("tollStationId");
                String startDate = params.get("startDate");
                String endDate = params.get("endDate");
                Response data ;
                if(resultCheckABAC.getAdmin()!=null &&resultCheckABAC.getAdmin()){
                    data = tollStationService.reportLine(tollStation,startDate,endDate,"day",null,true);
                }else {
                    data = tollStationService.reportLine(tollStation,startDate,endDate,"day",dto.getUnit().getLisOfStage(),false);
                }
                response = new ResponseMessage(new MessageContent(data.getData()));
            }
        }

        return response;
    }
}
