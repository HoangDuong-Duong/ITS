package com.elcom.its.management.controller;

import com.elcom.its.management.dto.*;
import com.elcom.its.management.model.Job;
import com.elcom.its.management.repository.ReportJobCustomize;
import com.elcom.its.management.service.*;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.DateUtil;
import com.elcom.its.utils.DateUtils;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author ducduongn
 */
@Controller
public class ReportController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @Autowired
    private ObjectTrackingHistoryService objectTrackingHistoryService;

    @Autowired
    private ReportJobService reportJobService;

    @Autowired
    private ReportJobCustomize reportJobCustomize;

    @Autowired
    private EventService eventService;

    @Autowired
    private SiteService siteService;

    public ResponseMessage getReportTraffics(Map<String, String> headerParam, String requestPath,
            String method, String urlParam, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);

                String filterObjectType = params.get("filterObjectType");
                String filterTimeLevel = params.get("filterTimeLevel");
                List<String> filterObjectIds = params.get("filterObjectIds") != null ? Arrays.asList(params.get("filterObjectIds").split(",")) : null;
                String itsFilterObjectIds = filterObjectIds != null ? String.join(",", filterObjectIds) : null;
                if (StringUtil.isNullOrEmpty(itsFilterObjectIds)) {
                    filterObjectType = null;
                }
                Integer isAdminBackEnd = 1;
                String stages = null;
                //Nếu không phải admin => Chỉ thống kê trên đoạn đường của đội user quản lý
                if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                    isAdminBackEnd = 0;
                    stages = dto.getUnit().getLisOfStage();
                }
                Response reportResponseDTO = reportService.getReportMetrics(stages, filterObjectType, filterTimeLevel, itsFilterObjectIds, isAdminBackEnd);
                if (reportResponseDTO != null && reportResponseDTO.getData() != null) {
                    responseMessage = new ResponseMessage(new MessageContent(reportResponseDTO.getData()));
                } else {
                    responseMessage = new ResponseMessage(new MessageContent(null));
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền lấy danh sách thống kê lưu lượng và vi phạm", null));
            }
        }

        return responseMessage;
    }

    public ResponseMessage getReportOnlineStatus(Map<String, String> headerParam, String requestPath,
            String method, String urlParam) throws ExecutionException, InterruptedException, ParseException, FileNotFoundException, JsonProcessingException {
        ResponseMessage responseMessage;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                int currentPage = Integer.parseInt(params.get("page"));
                int rowsPerPage = Integer.parseInt(params.get("size"));
                String startTime = params.get("startTime");
                String endTime = params.get("endTime");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Pageable pageable = PageRequest.of(currentPage, rowsPerPage, Sort.by("startTime").descending());
                Page<ReportOnlineStatusDTO> result = reportJobService.reportJobOnlineStatus(dateFormat.parse(startTime), dateFormat.parse(endTime), pageable);
                responseMessage = new ResponseMessage(new MessageContent(result.getContent(), result.getTotalElements()));
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền lấy báo cáo tình trạng hoạt động tuyến", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage getObjectTrackingHistory(Map<String, String> headerParam,
            String requestPath, String method, String urlParam, Map<String, Object> bodyParam)
            throws ExecutionException, InterruptedException {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                Long hourInterval = Long.parseLong(params.get("hourInterval"));

                String stages = null;
                Boolean isAdmin = true;
                if (abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin()) {
                    stages = dto.getUnit().getLisOfStage();
                    isAdmin = false;
                }
                Response objectTrackingHistoryResponse = objectTrackingHistoryService.getObjectTrackingHistoryList(
                        stages, isAdmin, hourInterval);
                if (objectTrackingHistoryResponse != null && objectTrackingHistoryResponse.getData() != null) {
                    responseMessage = new ResponseMessage(new MessageContent(objectTrackingHistoryResponse.getData()));
                } else {
                    responseMessage = new ResponseMessage(new MessageContent(null));
                }
            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền lấy danh sách lịch sử đối tượng theo dõi", null));
            }
        }
        return responseMessage;
    }

    public ResponseMessage getSiteTrafficStatusHistory(Map<String, String> headerParam,
            String requestPath, String method, String urlParam, Map<String, Object> bodyParam)
            throws ExecutionException, InterruptedException {
        ResponseMessage responseMessage = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO abacResponseDTO = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (abacResponseDTO != null && abacResponseDTO.getStatus()) {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String filterTimeLevel = params.get("filterTimeLevel");
                Integer isAdminBackEnd = abacResponseDTO.getAdmin() == null || !abacResponseDTO.getAdmin() ? 0 : 1;
                Response objectTrackingHistoryResponse = reportService.getSiteTrafficStatusHistory(
                        filterTimeLevel, dto.getUnit().getLisOfStage(), isAdminBackEnd);
                if (objectTrackingHistoryResponse != null && objectTrackingHistoryResponse.getData() != null) {
                    responseMessage = new ResponseMessage(new MessageContent(objectTrackingHistoryResponse.getData()));
                } else {
                    responseMessage = new ResponseMessage(new MessageContent(null));
                }

            } else {
                responseMessage = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(),
                        "Bạn không có quyền lấy danh sách lịch sử lưu lượng", null));
            }
        }

        return responseMessage;
    }

    public ResponseMessage reportJobEvent(Map<String, String> headerParam, String requestPath, String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                Response responseITS;
                LocalDateTime time;
                LocalDateTime endTimeS = LocalDateTime.now();
                try {
                    time = DateUtils.parse(params.get("startTime"));
                    if (params.get("endTime") != null) {
                        endTimeS = DateUtils.parse(params.get("endTime"));
                    }

                } catch (Exception ex) {
                    return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endTime không đúng định dạng", null);
                }
                String status = params.get("status");
                List<String> groups = StringUtil.isNullOrEmpty(params.get("groups")) ? null : new ArrayList<String>(Arrays.asList(params.get("groups").split(",")));
                List<ReportEventJobDTO> result = reportJobService.reportJobEvent(DateUtil.fromLocalDateTime(time), DateUtil.fromLocalDateTime(endTimeS), status, params.get("eventCode"), groups);
                response = new ResponseMessage(new MessageContent(result));
            }

        }

        return response;
    }

    public ResponseMessage reportJobStatus(Map<String, String> headerParam, String requestPath, String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String key = params.get("key");
                Response responseITS;
                LocalDateTime time;
                LocalDateTime endTimeS = LocalDateTime.now();
                try {
                    time = DateUtils.parse(params.get("startTime"));
                    if (params.get("endTime") != null) {
                        endTimeS = DateUtils.parse(params.get("endTime"));
                    }

                } catch (Exception ex) {
                    return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endTime không đúng định dạng", null);
                }
                List<ReportStatisticResponse> result = reportJobService.reportJobStatus(DateUtil.fromLocalDateTime(time), DateUtil.fromLocalDateTime(endTimeS));
                response = new ResponseMessage(new MessageContent(result));
            }
        }

        return response;
    }

    public ResponseMessage reportJobGroup(Map<String, String> headerParam, String requestPath, String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn chưa đăng nhập", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                return new ResponseMessage(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện",
                        new MessageContent(HttpStatus.FORBIDDEN.value(), "Bạn không có quyền xem danh sách sự kiện", null));
            } else {
                Map<String, String> params = StringUtil.getUrlParamValues(urlParam);
                String key = params.get("key");
                Response responseITS;
                LocalDateTime time;
                LocalDateTime endTimeS = LocalDateTime.now();
                try {
                    time = DateUtils.parse(params.get("startTime"));
                    if (params.get("endTime") != null) {
                        endTimeS = DateUtils.parse(params.get("endTime"));
                    }

                } catch (Exception ex) {
                    return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "startTime hoặc endTime không đúng định dạng", null);
                }
                List<String> groups = StringUtil.isNullOrEmpty(params.get("groups")) ? null : new ArrayList<String>(Arrays.asList(params.get("groups").split(",")));
                List<ReportStatisticResponse> result = reportJobService.reportJobGroup(DateUtil.fromLocalDateTime(time), DateUtil.fromLocalDateTime(endTimeS), groups);
                response = new ResponseMessage(new MessageContent(result));
            }
        }

        return response;
    }

    public ResponseMessage getAggJobByStatus(String urlParam) throws ExecutionException, InterruptedException, ParseException {
        Map<String, String> urlMap = StringUtil.getUrlParamValues(urlParam);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = simpleDateFormat.parse(urlMap.get("startTime"));
        Date endDate = simpleDateFormat.parse(urlMap.get("endTime"));
        String groupId = urlMap.get("groupId");
        ResponseMessage response = new ResponseMessage(new MessageContent(reportJobService.getAggJobByStatus(groupId, startDate, endDate)));
        return response;
    }
}
