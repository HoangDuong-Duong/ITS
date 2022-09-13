package com.elcom.its.management.service.impl;

import com.elcom.its.constant.ResourcePath;
import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.*;
import com.elcom.its.management.enums.JobStatus;
import com.elcom.its.management.enums.JobType;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQClient;
import com.elcom.its.management.messaging.rabbitmq.RabbitMQProperties;
import com.elcom.its.management.model.ActionHistory;
import com.elcom.its.management.model.Job;
import com.elcom.its.management.repository.JobRepository;
import com.elcom.its.management.repository.ReportJobCustomize;
import com.elcom.its.management.service.EventService;
import com.elcom.its.management.service.JobService;
import com.elcom.its.management.service.ReportJobService;
import com.elcom.its.management.service.SiteService;
import com.elcom.its.message.RequestMessage;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Converter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReportJobServiceImpl implements ReportJobService {

    private static Logger LOGGER = LoggerFactory.getLogger(ReportJobServiceImpl.class);

    @Autowired
    private ReportJobCustomize reportJobCustomize;

    @Autowired
    private RabbitMQClient rabbitMQClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EventService eventService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private JobService jobService;

    @Override
    public List<ReportStatisticResponse> reportJobGroup(Date fromDate, Date toDate, List<String> groupIds) {
        List<MapperJobStatusReport> result = reportJobCustomize.reportStatusByGroup(fromDate, toDate, groupIds);
        List<ReportStatisticResponse> eventReportResponses = null;
        List<Unit> units = getAllGroup(groupIds);
        eventReportResponses = this.processFilterJobByGroup(units, result);
        return eventReportResponses;
    }

    @Override
    public List<ReportStatisticResponse> reportJobStatus(Date fromDate, Date toDate) {
        List<MapperJobStatusReport> result = reportJobCustomize.reportJobGroup(fromDate, toDate);
        List<ReportStatisticResponse> eventReportResponses = null;
        eventReportResponses = this.processFilterJob(result);
        return eventReportResponses;
    }

    @Override
    public List<ReportEventJobDTO> reportJobEvent(Date startDate, Date endDate, String status, String eventCodes, List<String> groupId) {
        List<Job> jobs = reportJobCustomize.getMyJob(startDate, endDate, status, groupId);
        Map<String, List<Job>> groupByJob = jobs.parallelStream().collect(Collectors.groupingBy(Job::getGroupId));
        List<Unit> units = getAllGroup(groupId);
        List<ReportEventJobDTO> result = new ArrayList<>();
        List<String> parentIds = jobs.stream().map((item) -> item.getEventId()).collect(Collectors.toList());
        Set<String> eventIds = new HashSet<>();
        eventIds.addAll(parentIds);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (endDate == null) {
            endDate = new Date();
        }
        if (eventIds.isEmpty()) {
            return new ArrayList<>();
        }
        String events = String.join(",", eventIds);
        EventResponseDTO dto = findMyEvent(df.format(startDate), df.format(endDate), eventCodes, events, "");
        Map<String, EventDTO> eventDTOMap = new HashMap<>();
        List<EventDTO> eventDTOS;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            eventDTOS = dto.getData();
        } else {
            return new ArrayList<>();
        }
        if (eventDTOS == null || eventDTOS.isEmpty()) {
            return new ArrayList<>();
        } else {
            for (EventDTO event : eventDTOS) {
                eventDTOMap.put(event.getParentId(), event);
            }
        }
        for (Unit group : units) {
            ReportEventJobDTO reportEventJobDTO = new ReportEventJobDTO();
            reportEventJobDTO.setGroupId(group.getUuid());
            reportEventJobDTO.setGroupName(group.getName());
            List<ReportJobDTO> data = new ArrayList<>();
            if (groupByJob.get(group.getUuid()) != null) {
                for (Job item : groupByJob.get(group.getUuid())) {
                    EventDTO eventDTO = eventDTOMap.get(item.getEventId());
                    if (eventDTO != null) {
                        ReportJobDTO reportJobDTO = new ReportJobDTO();
                        reportJobDTO.setJobCode(item.getJobType());
                        reportJobDTO.setJobName(JobType.valueOf(item.getJobType()).description());
                        reportJobDTO.setKey(eventDTO.getKey());
                        reportJobDTO.setEventCode(eventDTO.getEventCode());
                        reportJobDTO.setEventName(eventDTO.getEventName());
                        reportJobDTO.setStartTime(item.getStartTime());
                        reportJobDTO.setUpdateTime(item.getActionHistory().get(item.getActionHistory().size() - 1).getActionTime());
                        reportJobDTO.setStatusName(item.getStatus().description());
                        data.add(reportJobDTO);
                    }
                }
            }
            reportEventJobDTO.setData(data);
            result.add(reportEventJobDTO);
        }
        return result;
    }

    @Override
    public Page<ReportOnlineStatusDTO> reportJobOnlineStatus(Date startTime, Date endTime, Pageable pageable) {
        List<String> filterJobType = new ArrayList<>(Arrays.asList(
                JobType.CLOSE_LANE.code(), JobType.FORBIDDEN_WAY.code(), JobType.LIMIT_SPEED.code(), JobType.CLOSE_OPEN_ENTRANCE_EXIT.code()));
        Page<Job> jobPage = jobService.findByJobTypeAndStartTimeBetween(filterJobType, startTime, endTime, pageable);
        List<ReportOnlineStatusDTO> reportOnlineStatusDTOList = getReportOnlineStatusFromJobList(jobPage);
        return new PageImpl<>(reportOnlineStatusDTOList, pageable, jobPage.getTotalElements());
    }

    public List<ReportOnlineStatusDTO> getReportOnlineStatusFromJobList(Page<Job> jobList){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ReportOnlineStatusDTO> reportOnlineStatusDTOList = new ArrayList<>();
        for(Job job : jobList) {
            ReportOnlineStatusDTO report = new ReportOnlineStatusDTO();
            List<ActionHistory> actionHistoryList = job.getActionHistory();
            List<ActionHistory> actionFinish = actionHistoryList.stream().filter(actionHistory -> actionHistory.getActionCode().equals("FINISH")).collect(Collectors.toList());
            Date endTime = null;
            if(!actionFinish.isEmpty()){
                endTime = actionFinish.get(0).getActionTime();
            }
            report.setId(job.getId());
            report.setJobType(job.getJobType());
            report.setEventId(job.getEventId());
            report.setStartTime(job.getStartTime() != null ? dateFormat.format(job.getStartTime()) : "");
            report.setEndTime(endTime != null ? dateFormat.format(endTime) : "");
            report.setEventStartTime(job.getEventStartTime() != null ? dateFormat.format(job.getEventStartTime()) : "");
            report.setGroupId(job.getGroupId());
            report.setStartSiteId(job.getStartSiteId());
            report.setEndSiteId(job.getEndSiteId() != null ? job.getEndSiteId() : "");
            report.setDescription(job.getDescription() != null ? job.getDescription() : "");
            report.setPlaceName(job.getPlaceName() != null ? job.getPlaceName() : "");
            if(job.getProcessResult() != null){
                ObjectMapper mapper = new ObjectMapper();
                BaseJobProcessingResult processingResult = mapper.convertValue(job.getProcessResult(), new TypeReference<BaseJobProcessingResult>() {
                });
                report.setProcessResult(processingResult.getContent());
            }
            else
                report.setProcessResult("");
            reportOnlineStatusDTOList.add(report);
        }
        setEventForReport(reportOnlineStatusDTOList);
        setGroupForReport(reportOnlineStatusDTOList);
        setSiteForReport(reportOnlineStatusDTOList);
        return reportOnlineStatusDTOList;
    }

    public List<ReportOnlineStatusDTO> setEventForReport(List<ReportOnlineStatusDTO> reportOnlineStatusDTOList){
        if(!reportOnlineStatusDTOList.isEmpty() && reportOnlineStatusDTOList != null){
            List<String> eventIdList = reportOnlineStatusDTOList.stream().map(event -> event.getEventId()).collect(Collectors.toList());
            List<EventDTO> listEvent = eventService.getListEventDTOByListParentIds(eventIdList);
            Map<String, EventDTO> eventDTOMap = new HashMap<>();
            listEvent.stream().forEach(e -> {
                eventDTOMap.put(e.getParentId(), e);
            });
            for (ReportOnlineStatusDTO report : reportOnlineStatusDTOList) {
                if (!StringUtil.isNullOrEmpty(report.getEventId())) {
                    EventDTO eventDTO = eventDTOMap.get(report.getEventId());
                    if (eventDTO != null) {
                        report.setEventCode(eventDTO.getEventCode());
                        report.setEventName(eventDTO.getEventName());
                    } else {
                        report.setEventCode("");
                        report.setEventName("");
                    }
                } else {
                    report.setEventCode("");
                    report.setEventName("");
                }
            }
            return reportOnlineStatusDTOList;
        }
        return null;
    }

    public List<ReportOnlineStatusDTO> setSiteForReport(List<ReportOnlineStatusDTO> reportOnlineStatusDTOList){
        if(!reportOnlineStatusDTOList.isEmpty() && reportOnlineStatusDTOList != null){
            List<String> startSiteIdList = reportOnlineStatusDTOList.stream().map(site -> site.getStartSiteId()).collect(Collectors.toList());
            List<String> endSiteIdList = reportOnlineStatusDTOList.stream().map(site -> site.getEndSiteId()).collect(Collectors.toList());
            List<String> siteIdList = Stream.concat(startSiteIdList.stream(), endSiteIdList.stream()).collect(Collectors.toList());
            siteIdList.add(0, "");
            siteIdList.add(siteIdList.size(), "");
            List<SiteDTO> listSite = siteService.findSiteByListSiteIds(siteIdList);
            Map<String, SiteDTO> siteMap = new HashMap<>();
            listSite.stream().forEach(s -> {
                siteMap.put(s.getSiteId(), s);
            });
            for (ReportOnlineStatusDTO report : reportOnlineStatusDTOList){
                if(!StringUtil.isNullOrEmpty(report.getPlaceName())){
                    report.setStartSite(report.getPlaceName());
                    report.setEndSite("");
                } else {
                    if(!StringUtil.isNullOrEmpty(report.getStartSiteId())){
                        SiteDTO siteDTO = siteMap.get(report.getStartSiteId());
                        if(siteDTO != null){
                            report.setStartSite(siteDTO.getSiteName());
                        } else {
                            report.setStartSite("");
                        }
                    } else {
                        report.setStartSite("");
                    }

                    if(!StringUtil.isNullOrEmpty(report.getEndSiteId())){
                        SiteDTO siteDTO1 = siteMap.get(report.getEndSiteId());
                        if(siteDTO1 != null){
                            report.setEndSite(siteDTO1.getSiteName());
                        } else {
                            report.setEndSite("");
                        }
                    } else {
                        report.setEndSite("");
                    }
                }
            }
            return reportOnlineStatusDTOList;
        }
        return null;
    }

    public List<ReportOnlineStatusDTO> setGroupForReport(List<ReportOnlineStatusDTO> reportOnlineStatusDTOList){
        if(!reportOnlineStatusDTOList.isEmpty() && reportOnlineStatusDTOList != null){
            List<String> groupIdList = reportOnlineStatusDTOList.stream().map(group -> group.getGroupId()).collect(Collectors.toList());
            List<Unit> listUnit = getAllGroup(groupIdList);
            Map<String, Unit> unitMap = new HashMap<>();
            listUnit.stream().forEach(s -> {
                unitMap.put(s.getUuid(), s);
            });
            for (ReportOnlineStatusDTO report : reportOnlineStatusDTOList){
                if(!StringUtil.isNullOrEmpty(report.getGroupId())){
                    Unit unit = unitMap.get(report.getGroupId());
                    if(unit != null){
                        report.setGroupCode(unit.getCode());
                        report.setGroupName(unit.getName());
                    } else {
                        report.setGroupCode("");
                        report.setGroupName("");
                    }
                } else {
                    report.setGroupCode("");
                    report.setGroupName("");
                }
            }
            return reportOnlineStatusDTOList;
        }
        return null;
    }

    public EventResponseDTO findMyEvent(String startTime, String endTime, String eventCodes, String parentIds, String key) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/event/job";
        String params = "startTime=" + startTime
                + "&endTime=" + endTime
                + "&eventCodes=" + eventCodes
                + "&parentIds=" + parentIds
                + "&key=" + key;
        urlRequest = urlRequest + "?" + params;
        EventResponseDTO dto = restTemplate.getForObject(urlRequest, EventResponseDTO.class);
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            return dto;
        }
        return dto;
    }

    private List<ReportStatisticResponse> processFilterJob(List<MapperJobStatusReport> result) {
        Map<String, List<MapperJobStatusReport>> groupBySource = result.parallelStream().collect(Collectors.groupingBy(MapperJobStatusReport::getObject));
        List<JobType> jobTypes = Arrays.asList(JobType.values());
        List<ReportStatisticResponse> reportResponses = new ArrayList<>();
        for (JobType jobType : jobTypes) {
            ReportStatisticResponse reportStatisticResponse = new ReportStatisticResponse();
            reportStatisticResponse.setSourceId(jobType.getCode());
            reportStatisticResponse.setSourceName(jobType.getDescription());
            List<Integer> listCheck = Arrays.asList(new Integer[]{3, 4});
            Map<Integer, ObjectTypeValue> objectTypeValueMap = defaultZeroStatus();
            if (groupBySource.get(jobType.getCode()) != null) {
                for (MapperJobStatusReport item : groupBySource.get(jobType.getCode())) {
                    if (listCheck.contains(item.getStatus())) {
                        ObjectTypeValue objectTypeValue = objectTypeValueMap.get(3);
                        long total = objectTypeValue.getValue() + item.getTotal();
                        objectTypeValue.setValue(total);
                        objectTypeValueMap.replace(3, objectTypeValue);
                    } else {
                        ObjectTypeValue objectTypeValue = objectTypeValueMap.get(item.getStatus());
                        long total = objectTypeValue.getValue() + item.getTotal();
                        objectTypeValue.setValue(total);
                        objectTypeValueMap.replace(item.getStatus(), objectTypeValue);
                    }
                }
            }
            List<ObjectTypeValue> data = new ArrayList<>();
            objectTypeValueMap.forEach((k, v) -> {
                data.add(v);
            });
            reportStatisticResponse.setObjectTypeValueList(data);
            reportResponses.add(reportStatisticResponse);
        }

        return reportResponses;
    }

    private List<ReportStatisticResponse> processFilterJobByGroup(List<Unit> units, List<MapperJobStatusReport> result) {
        Map<String, List<MapperJobStatusReport>> groupBySource = result.parallelStream().collect(Collectors.groupingBy(MapperJobStatusReport::getObject));

        List<ReportStatisticResponse> reportResponses = new ArrayList<>();
        for (Unit group : units) {
            ReportStatisticResponse reportStatisticResponse = new ReportStatisticResponse();
            reportStatisticResponse.setSourceId(group.getUuid());
            reportStatisticResponse.setSourceName(group.getName());
            reportStatisticResponse.setSiteAddress(group.getAddress());
            List<Integer> listCheck = Arrays.asList(new Integer[]{3, 4});
            Map<Integer, ObjectTypeValue> objectTypeValueMap = defaultZeroStatus();
            if (groupBySource.get(group.getUuid()) != null) {
                for (MapperJobStatusReport item : groupBySource.get(group.getUuid())) {
                    if (listCheck.contains(item.getStatus())) {
                        ObjectTypeValue objectTypeValue = objectTypeValueMap.get(3);
                        long total = objectTypeValue.getValue() + item.getTotal();
                        objectTypeValue.setValue(total);
                        objectTypeValueMap.replace(3, objectTypeValue);
                    } else {
                        ObjectTypeValue objectTypeValue = objectTypeValueMap.get(item.getStatus());
                        long total = objectTypeValue.getValue() + item.getTotal();
                        objectTypeValue.setValue(total);
                        objectTypeValueMap.replace(item.getStatus(), objectTypeValue);
                    }
                }
            }
            List<ObjectTypeValue> data = new ArrayList<>();
            objectTypeValueMap.forEach((k, v) -> {
                data.add(v);
            });
            reportStatisticResponse.setObjectTypeValueList(data);
            reportResponses.add(reportStatisticResponse);
        }

        return reportResponses;
    }

    private Map<Integer, ObjectTypeValue> defaultZeroStatus() {

        Map<Integer, ObjectTypeValue> mapObjectType = new HashMap<>();
        List<Integer> listCheck = Arrays.asList(new Integer[]{3, 4});
        for (int i = 0; i <= 5; i++) {
            if (!listCheck.contains(i)) {
                ObjectTypeValue objectTypeValue = new ObjectTypeValue();
                objectTypeValue.setObjectType(String.valueOf(JobStatus.of(i).code()));
                objectTypeValue.setObjectTypeName(JobStatus.of(i).description());
                objectTypeValue.setValue(0L);
                mapObjectType.put(i, objectTypeValue);
            }
        }
        ObjectTypeValue objectTypeValue = new ObjectTypeValue();
        objectTypeValue.setObjectType("3");
        objectTypeValue.setObjectTypeName("Kiểm tra");
        objectTypeValue.setValue(0L);
        mapObjectType.put(3, objectTypeValue);

        return mapObjectType;
    }

    private List<Unit> getAllGroup(List<String> ids) {
        //Set body param
        RequestMessage rbacRpcRequest = new RequestMessage();
        rbacRpcRequest.setRequestMethod("POST");
        rbacRpcRequest.setRequestPath("/v1.0/group/all/internal/report");
        Map<String, Object> body = new HashMap<>();
        body.put("group", ids);
        rbacRpcRequest.setBodyParam(body);
        rbacRpcRequest.setUrlParam(null);
        rbacRpcRequest.setHeaderParam(null);
        rbacRpcRequest.setPathParam(null);
        rbacRpcRequest.setVersion(ResourcePath.VERSION);
        String result = rabbitMQClient.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, rbacRpcRequest.toJsonString());
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage resultResponse = null;
            try {
                resultResponse = mapper.readValue(result, ResponseMessage.class);
                if (resultResponse != null && resultResponse.getStatus() == HttpStatus.OK.value() && resultResponse.getData() != null) {
//                    Object data = resultResponse.getData().getData();
//                    Map<String, User> mapUser = mapper.convertValue(data, new TypeReference<Map<String,User>>() {
//                    });
                    //OK
                    JsonNode jsonNode = mapper.readTree(result);
                    List<Unit> mapUser = mapper.readerFor(new TypeReference<List<Unit>>() {
                    }).readValue(jsonNode.get("data").get("data"));
                    return mapUser;
                }
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    @Override
    public List<AggJobByStatus> getAggJobByStatus(String groupId, Date startDate, Date endDate) {
        return reportJobCustomize.getAggJobByStatus(groupId, startDate, endDate);
    }
}
