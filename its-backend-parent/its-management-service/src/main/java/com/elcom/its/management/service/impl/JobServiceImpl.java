/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service.impl;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.controller.BaseController;
import com.elcom.its.management.dto.*;
import com.elcom.its.management.enums.DeviceType;
import com.elcom.its.management.enums.JobStatus;
import com.elcom.its.management.enums.JobType;
import com.elcom.its.management.enums.Priority;
import com.elcom.its.management.model.ActionHistory;
import com.elcom.its.management.model.EventFile;
import com.elcom.its.management.model.File;
import com.elcom.its.management.model.Job;
import com.elcom.its.management.model.Device;
import com.elcom.its.management.repository.JobRepository;
import com.elcom.its.management.service.EventService;
import com.elcom.its.management.service.FmsService;
import com.elcom.its.management.service.JobService;
import com.elcom.its.management.service.PlaceService;
import com.elcom.its.management.service.SiteService;
import com.elcom.its.management.service.SystemDeviceService;
import com.elcom.its.management.service.UserService;
import com.elcom.its.management.service.VmsBoardService;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.groupdocs.conversion.internal.c.a.d.Act;
import org.springframework.data.domain.Pageable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
@Transactional
public class JobServiceImpl implements JobService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SystemDeviceService systemDeviceService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private EventService eventService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private UserService userService;

    @Autowired
    private VmsBoardService vmsBoardService;

    @Autowired
    private FmsService fmsService;

    @Override
    public void save(Job job) {
        jobRepository.save(job);
    }

    @Override
    public Job findById(String id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            return jobOptional.get();
        } else {
            return null;
        }
    }

    @Override
    public Job processVmsJob(List<VmsBoardJobProcessing> listVmsBoardJobProcessings, Job job, String userId) {
        Date now = new Date();
        listVmsBoardJobProcessings = handleVmsBoardJobProcessing(listVmsBoardJobProcessings, now, userId);
        job.setProcessResult(listVmsBoardJobProcessings);
        jobRepository.save(job);
        return job;
    }

    List<VmsBoardJobProcessing> handleVmsBoardJobProcessing(List<VmsBoardJobProcessing> listVmsBoardJobProcessings, Date now, String userId) {
        for (VmsBoardJobProcessing vmsBoardJobProcessing : listVmsBoardJobProcessings) {
            if (vmsBoardJobProcessing.getContent() == null) {
                continue;
            }
            VmsBoardDisplayNewResponse vmsBoardDisplayNewResponse = vmsBoardService.callDisPlayNew(
                    vmsBoardJobProcessing.getVmsBoardId(), null, vmsBoardJobProcessing.getTemplateId(), vmsBoardJobProcessing.getContent(), vmsBoardJobProcessing.getSource(), vmsBoardJobProcessing.getPreview(), vmsBoardJobProcessing.getEndTime(), userId);
            vmsBoardJobProcessing.setStatus(vmsBoardDisplayNewResponse.getSuccess());
            vmsBoardJobProcessing.setStartTime(now);
            vmsBoardJobProcessing.setParentId(vmsBoardDisplayNewResponse.getParentId());
        }
        return listVmsBoardJobProcessings;
    }

    boolean randomStatus() {
        Random rand = new Random();
        return rand.nextInt(2) == 1;
    }

    @Override
    public Job processBaseJob(Job job, BaseJobProcessingResult baseJobProcessing) {
        job.setProcessResult(baseJobProcessing);
        jobRepository.save(job);
        return job;
    }

    @Override
    public Job processRepairJob(List<RepairJobProcessing> listRepairJobProcessings, Job job) {
        Date now = new Date();
        listRepairJobProcessings.stream().forEach(process -> {
            process.setRepairedDate(now);
        }
        );
        Set<String> setFmsDeviceType = DeviceType.getListFmsDeviceType();
        job.setProcessResult(listRepairJobProcessings);
        List<RepairJobProcessing> listRepairFms = listRepairJobProcessings.stream().filter(x -> setFmsDeviceType.contains(x.getDeviceType())).collect(Collectors.toList());
        fmsService.updateFmsHistory(transform(listRepairFms, job));
        jobRepository.save(job);
        return job;
    }

    private List<UpdateFmsHistoryRequest> transform(List<RepairJobProcessing> listRepairJobProcessings, Job job) {
        return listRepairJobProcessings.stream()
                .map(rp -> UpdateFmsHistoryRequest.builder().fmsId(rp.getDeviceId()).content(job.getDescription() + " - " + rp.getRepairComment()).startTime(job.getStartTime()).endTime(rp.getRepairedDate()).build())
                .collect(Collectors.toList());

    }

    @Override
    public List<Job> findByEventId(String eventId) {
        List<Job> jobList = jobRepository.findByEventIdOrderByCreatedDateDesc(eventId);
        List<ActionHistory> actionHistories = new ArrayList<>();
        for (Job job : jobList) {
            actionHistories.addAll(job.getActionHistory());
        }
        getAvatarByListActorId(actionHistories);
        return jobList;
    }

    @Override
    public List<Job> findByEventIdReport(String eventId) {
        return jobRepository.findByEventIdOrderByCreatedDateAsc(eventId);
    }

    @Override
    public Job createNewJob(Map<String, Object> bodyParam) {
        Job newJob = new Job();
        String jobType = (String) bodyParam.get("jobType");
        String eventId = (String) bodyParam.get("eventId");
        String groupId = (String) bodyParam.get("groupId");
        String userId = (String) bodyParam.get("userId");
        String priority = (String) bodyParam.get("priority");
        String startSiteId = (String) bodyParam.get("startSiteId");
        String endSiteId = (String) bodyParam.get("endSiteId");
        String directionCode = (String) bodyParam.get("directionCode");
        String vehicleType = (String) bodyParam.get("vehicleType");
        String vehicleWeight = (String) bodyParam.get("vehicleWeight");
        String description = (String) bodyParam.get("description");
        String lane = (String) bodyParam.get("lane");
        String notifyMethod = (String) bodyParam.get("notifyMethod");
        int limitSpeed = bodyParam.get("limitSpeed") != null ? Integer.parseInt(bodyParam.get("limitSpeed").toString()) : 0;
        String placeId = (String) bodyParam.get("placeId");
        String placeName = (String) bodyParam.get("placeName");

        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        Date startTime = bodyParam.get("startTime") != null ? mapper.convertValue(
                bodyParam.get("startTime"), new TypeReference<Date>() {
        }) : new Date();
        Date endTime = bodyParam.get("endTime") != null ? mapper.convertValue(
                bodyParam.get("endTime"), new TypeReference<Date>() {
        }) : null;
        List<String> notifyOrganization = bodyParam.get("notifyOrganization") != null ? mapper.convertValue(
                bodyParam.get("notifyOrganization"), new TypeReference<List<String>>() {
        }) : null;
        Date eventStartTime = bodyParam.get("eventStartTime") != null ? mapper.convertValue(
                bodyParam.get("eventStartTime"), new TypeReference<Date>() {
        }) : null;

        List<Device> listDevices = bodyParam.get("listDevices") != null ? mapper.convertValue(
                bodyParam.get("listDevices"), new TypeReference<List<Device>>() {
        }) : null;

        newJob.setUserIds(userId);
        newJob.setEventId(eventId);
        newJob.setDescription(description);
        newJob.setDirection(directionCode);
        newJob.setStartSiteId(startSiteId);
        newJob.setEndSiteId(endSiteId);
        newJob.setGroupId(groupId);
        newJob.setId(UUID.randomUUID().toString());
        newJob.setJobType(jobType);
        newJob.setLane(lane);
        newJob.setNotifyMethod(notifyMethod);
        newJob.setVehicleType(vehicleType);
        newJob.setVehicleWeight(vehicleWeight);
        newJob.setStartTime(startTime);
        newJob.setEndTime(endTime);;
        newJob.setPriority(Priority.of(priority));
        newJob.setStatus(JobStatus.RECEIVE_PROCESSING);
        newJob.setNotifyOrganization(notifyOrganization);
        newJob.setCreatedDate(new Date());
        newJob.setModifiedTime(new Date());
        newJob.setEventStartTime(eventStartTime);
        newJob.setListDevices(listDevices);
        newJob.setLimitSpeed(limitSpeed);
        newJob.setPlaceId(placeId);
        newJob.setPlaceName(placeName);
        return newJob;
    }

    @Override
    public Job updateJob(Job job, Map<String, Object> bodyParam) {
        String jobType = (String) bodyParam.get("jobType");
        String eventId = (String) bodyParam.get("eventId");
        String groupId = (String) bodyParam.get("groupId");
        String userId = (String) bodyParam.get("userId");
        String priority = (String) bodyParam.get("priority");
        String startSiteId = (String) bodyParam.get("startSiteId");
        String endSiteId = (String) bodyParam.get("endSiteId");
        String directionCode = (String) bodyParam.get("directionCode");
        String vehicleType = (String) bodyParam.get("vehicleType");
        String vehicleWeight = (String) bodyParam.get("vehicleWeight");
        String description = (String) bodyParam.get("description");
        String lane = (String) bodyParam.get("lane");
        String notifyMethod = (String) bodyParam.get("notifyMethod");
        int limitSpeed = bodyParam.get("limitSpeed") != null ? Integer.parseInt(bodyParam.get("limitSpeed").toString()) : 0;
        String placeId = (String) bodyParam.get("placeId");
        String placeName = (String) bodyParam.get("placeName");
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        Date startTime = bodyParam.get("startTime") != null ? mapper.convertValue(
                bodyParam.get("startTime"), new TypeReference<Date>() {
        }) : new Date();
        Date endTime = bodyParam.get("endTime") != null ? mapper.convertValue(
                bodyParam.get("endTime"), new TypeReference<Date>() {
        }) : null;
        List<String> notifyOrganization = bodyParam.get("notifyOrganization") != null ? mapper.convertValue(
                bodyParam.get("notifyOrganization"), new TypeReference<List<String>>() {
        }) : null;
        Date eventStartTime = bodyParam.get("eventStartTime") != null ? mapper.convertValue(
                bodyParam.get("eventStartTime"), new TypeReference<Date>() {
        }) : null;

        List<Device> listDevices = bodyParam.get("listDevices") != null ? mapper.convertValue(
                bodyParam.get("listDevices"), new TypeReference<List<Device>>() {
        }) : null;

        job.setUserIds(userId);
        job.setEventId(eventId);
        job.setDescription(description);
        job.setDirection(directionCode);
        job.setStartSiteId(startSiteId);
        job.setEndSiteId(endSiteId);
        job.setGroupId(groupId);
        job.setJobType(jobType);
        job.setLane(lane);
        job.setNotifyMethod(notifyMethod);
        job.setVehicleType(vehicleType);
        job.setVehicleWeight(vehicleWeight);
        job.setStartTime(startTime);
        job.setEndTime(endTime);;
        job.setPriority(Priority.of(priority));
        job.setStatus(JobStatus.RECEIVE_PROCESSING);
        job.setNotifyOrganization(notifyOrganization);
        if (eventStartTime != null) {
            job.setEventStartTime(eventStartTime);
        }
        job.setListDevices(listDevices);
        job.setLimitSpeed(limitSpeed);
        job.setPlaceId(placeId);
        job.setPlaceName(placeName);
        return job;
    }

    @Override
    public List<String> getEvent(String groupId, Date startTime, Date endTime) {
        return jobRepository.findEventIdForGroup(groupId, startTime, endTime);
    }

    @Override
    public List<String> getEventAdmin(Date startTime, Date endTime) {
        return jobRepository.findByAdmin(startTime, endTime);
    }

    public List<EventFile> getListFileForEvent(String eventId) {
        List<Job> listJobByEvent = jobRepository.findByEventIdOrderByCreatedDateDesc(eventId);
        List<File> listJobFiles = new ArrayList<>();
        for (Job job : listJobByEvent) {
            List<ActionHistory> listActionHistorys = job.getActionHistory();
            listActionHistorys.forEach(action -> {
                if (action.getActionFiles() != null) {
                    listJobFiles.addAll(action.getActionFiles());
                }
            });
        }
        List<EventFile> listEventFiles = listJobFiles.stream()
                .map((file) -> transform(file))
                .collect(Collectors.toList());
        return listEventFiles;
    }

    EventFile transform(File file) {
        EventFile eventFile = new EventFile();
        eventFile.setFileName(file.getFileName());
        eventFile.setFileType(file.getFileType());
        eventFile.setFileUrl(file.getFileDownloadUri());
        eventFile.setSize((int) file.getSize());
        return eventFile;
    }

    @Override
    public Page<Job> getJobsForUser(String groupId, String userId, Pageable page) {
        return jobRepository.getJobsForUser(groupId, userId, page);
    }

    @Override
    public List<JobInMapNotification> getListJobNotificationInMap() {
        ArrayList<String> listNotiJobType = new ArrayList<String>(Arrays.asList(JobType.CLOSE_LANE.code(), JobType.FORBIDDEN_WAY.code(), JobType.CLOSE_OPEN_ENTRANCE_EXIT.code(), JobType.LIMIT_SPEED.code()));
        ArrayList<JobStatus> listJobStatus = new ArrayList<JobStatus>(Arrays.asList(JobStatus.CHECKING, JobStatus.PROCESSING, JobStatus.RECEIVE_PROCESSING, JobStatus.UPDATED_BOARD));
        List<Job> listNotiJob = jobRepository.findByStatusInAndJobTypeIn(listJobStatus, listNotiJobType);
        List<JobInMapNotification> listReturnJob = new ArrayList<>();
        List<Job> listNotiInSiteJob = listNotiJob.stream().
                filter(job -> (!job.getJobType().equalsIgnoreCase(JobType.CLOSE_OPEN_ENTRANCE_EXIT.code())))
                .collect(Collectors.toList());
        if (listNotiInSiteJob != null && !listNotiInSiteJob.isEmpty()) {
            List<PointRequestDTO> pointRequestList = new ArrayList<>();
            listNotiInSiteJob.stream().forEach(
                    job -> {
                        if (!StringUtil.isNullOrEmpty(job.getStartSiteId())) {
                            pointRequestList.add(PointRequestDTO.builder().siteId(job.getStartSiteId())
                                    .directionCode(job.getDirection()).build());
                        }
                        if (!StringUtil.isNullOrEmpty(job.getEndSiteId())) {
                            pointRequestList.add(PointRequestDTO.builder().siteId(job.getEndSiteId())
                                    .directionCode(job.getDirection()).build());
                        }
                    }
            );
            Map<String, PointSiteDirectionDTO> pointSiteDirectionMap = getPointMapInRoad(pointRequestList);
            listNotiInSiteJob.stream().forEach(
                    job -> {
                        listReturnJob.add(transformToJobInMapNotification(job, pointSiteDirectionMap));
                    }
            );
        }

        List<Job> listNotiInPlaceJob = listNotiJob.stream().
                filter(job -> (job.getJobType().equalsIgnoreCase(JobType.CLOSE_OPEN_ENTRANCE_EXIT.code())))
                .collect(Collectors.toList());
        if (listNotiInPlaceJob != null && !listNotiInPlaceJob.isEmpty()) {
            List<String> listPlaceIds = listNotiInPlaceJob.stream().map(job -> job.getPlaceId()).collect(Collectors.toList());
            List<PointPlaceResponse> listPointPlaceResponses = placeService.getListPointPlace(listPlaceIds);
            Map<String, PointPlaceResponse> mapPlace = new HashMap<>();
            listPointPlaceResponses.stream().forEach(point -> mapPlace.put(point.getPlaceId(), point));
            listNotiInPlaceJob.stream()
                    .forEach(job -> listReturnJob.add(transformToPlaceJobInMapNotification(job, mapPlace)));
        }

        return listReturnJob;
    }

    private JobInMapNotification transformToJobInMapNotification(Job job) {
        JobInMapNotification jobInMapNotification = new JobInMapNotification();
        jobInMapNotification.setJob(job);
        jobInMapNotification.setEndPoint(getPointInRoad(job.getDirection(), job.getEndSiteId()));
        jobInMapNotification.setStartPoint(getPointInRoad(job.getDirection(), job.getStartSiteId()));
        return jobInMapNotification;
    }

    private Point getPointInRoad(String directionCode, String siteId) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/site/point-in-road";
        String params = "directionCode=" + directionCode + "&siteId=" + siteId;
        urlRequest = urlRequest + "?" + params;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        HttpEntity<Response> response = restTemplate.exchange(urlRequest, HttpMethod.GET, requestBody, Response.class);
        Response dto = response != null ? response.getBody() : null;
        if (dto != null && (dto.getStatus() == HttpStatus.OK.value() || dto.getStatus() == HttpStatus.CREATED.value())) {
            ObjectMapper mapper = new ObjectMapper();
            Point point = mapper.convertValue(dto.getData(), new TypeReference<Point>() {
            });
            return point;
        }
        return null;
    }

    private Map<String, PointSiteDirectionDTO> getPointMapInRoad(List<PointRequestDTO> pointRequestList) {
        String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/site/point-in-road";
        PointSiteDirectionResponse response = restTemplate.postForObject(urlRequest, pointRequestList, PointSiteDirectionResponse.class);
        if (response != null && (response.getStatus() == HttpStatus.OK.value()
                || response.getStatus() == HttpStatus.CREATED.value())) {
            List<PointSiteDirectionDTO> dtoList = response.getData();
            if (dtoList != null && !dtoList.isEmpty()) {
                Map<String, PointSiteDirectionDTO> dtoMap = new HashMap<>();
                for (PointSiteDirectionDTO dto : dtoList) {
                    dtoMap.put(dto.getSiteId() + dto.getDirectionCode(), dto);
                }
                return dtoMap;
            }
        }
        return null;
    }

    private JobInMapNotification transformToJobInMapNotification(Job job, Map<String, PointSiteDirectionDTO> pointSiteDirectionMap) {
        JobInMapNotification jobInMapNotification = new JobInMapNotification();
        jobInMapNotification.setJob(job);
        if (pointSiteDirectionMap != null && pointSiteDirectionMap.containsKey(job.getEndSiteId() + job.getDirection())) {
            PointSiteDirectionDTO endSiteDirection = pointSiteDirectionMap.get(job.getEndSiteId() + job.getDirection());
            jobInMapNotification.setEndPoint(new Point(endSiteDirection.getLongitude(), endSiteDirection.getLatitude()));
        }
        if (pointSiteDirectionMap != null && pointSiteDirectionMap.containsKey(job.getStartSiteId() + job.getDirection())) {
            PointSiteDirectionDTO startSiteDirection = pointSiteDirectionMap.get(job.getStartSiteId() + job.getDirection());
            jobInMapNotification.setStartPoint(new Point(startSiteDirection.getLongitude(), startSiteDirection.getLatitude()));
        }
        return jobInMapNotification;
    }

    private JobInMapNotification transformToPlaceJobInMapNotification(Job job, Map<String, PointPlaceResponse> pointPlaceResponse) {
        JobInMapNotification jobInMapNotification = new JobInMapNotification();
        jobInMapNotification.setJob(job);
        LOGGER.info(job.getId());
        PointPlaceResponse placePoint = pointPlaceResponse.get(job.getPlaceId());
        jobInMapNotification.setStartPoint(new Point(placePoint.getLongitude(), placePoint.getLatitude()));
        return jobInMapNotification;
    }

    @Override
    public List<JobInMapNotification> getListJobNotificationInMap(String groupId, String userId) {
        ArrayList<String> listNotiJobType = new ArrayList<String>(Arrays.asList(JobType.CLOSE_LANE.code(), JobType.FORBIDDEN_WAY.code(), JobType.CLOSE_OPEN_ENTRANCE_EXIT.code(), JobType.LIMIT_SPEED.code()));
        ArrayList<JobStatus> listJobStatus = new ArrayList<JobStatus>(Arrays.asList(JobStatus.CHECKING, JobStatus.PROCESSING, JobStatus.RECEIVE_PROCESSING, JobStatus.UPDATED_BOARD));

        List<Job> listNotiJob = jobRepository.findListJobNotiForUser(groupId, userId, listJobStatus, listNotiJobType);
        List<JobInMapNotification> listReturnJob = new ArrayList<>();
        List<Job> listNotiInSiteJob = listNotiJob.stream().
                filter(job -> (!job.getJobType().equalsIgnoreCase(JobType.CLOSE_OPEN_ENTRANCE_EXIT.code())))
                .collect(Collectors.toList());
        if (listNotiInSiteJob != null && !listNotiInSiteJob.isEmpty()) {
            List<PointRequestDTO> pointRequestList = new ArrayList<>();
            listNotiInSiteJob.stream().forEach(
                    job -> {
                        if (!StringUtil.isNullOrEmpty(job.getStartSiteId())) {
                            pointRequestList.add(PointRequestDTO.builder().siteId(job.getStartSiteId())
                                    .directionCode(job.getDirection()).build());
                        }
                        if (!StringUtil.isNullOrEmpty(job.getEndSiteId())) {
                            pointRequestList.add(PointRequestDTO.builder().siteId(job.getEndSiteId())
                                    .directionCode(job.getDirection()).build());
                        }
                    }
            );
            Map<String, PointSiteDirectionDTO> pointSiteDirectionMap = getPointMapInRoad(pointRequestList);
            listNotiInSiteJob.stream().forEach(
                    job -> {
                        listReturnJob.add(transformToJobInMapNotification(job, pointSiteDirectionMap));
                    }
            );
        }

        List<Job> listNotiInPlaceJob = listNotiJob.stream().
                filter(job -> (job.getJobType().equalsIgnoreCase(JobType.CLOSE_OPEN_ENTRANCE_EXIT.code())))
                .collect(Collectors.toList());
        if (listNotiInPlaceJob != null && !listNotiInPlaceJob.isEmpty()) {
            List<String> listPlaceIds = listNotiInPlaceJob.stream().map(job -> job.getPlaceId()).collect(Collectors.toList());
            List<PointPlaceResponse> listPointPlaceResponses = placeService.getListPointPlace(listPlaceIds);
            Map<String, PointPlaceResponse> mapPlace = new HashMap<>();
            listPointPlaceResponses.stream().forEach(point -> mapPlace.put(point.getPlaceId(), point));
            listNotiInPlaceJob.stream()
                    .forEach(job -> listReturnJob.add(transformToPlaceJobInMapNotification(job, mapPlace)));
        }

        return listReturnJob;
    }

    @Override
    public DetailJob transform(Job job) {
        EventDTO eventDTO = eventService.getEventByParentId(job.getEventId());
        if (eventDTO == null) {
            return null;
        }
        List<ActionHistory> actionHistories = getAvatarByListActorId(job.getActionHistory());

        DetailJob detailJob = DetailJob.builder()
                .actionHistory(actionHistories)
                .createdBy(job.getCreatedBy())
                .createdDate(job.getCreatedDate())
                .description(job.getDescription())
                .endSiteId(job.getEndSiteId())
                .endTime(job.getEndTime())
                .eventCode(eventDTO.getEventCode())
                .eventId(job.getEventId())
                .eventName(eventDTO.getEventName())
                .eventStartTime(job.getEventStartTime())
                .groupId(job.getGroupId())
                .id(job.getId())
                .jobType(job.getJobType())
                .lane(job.getLane())
                .limitSpeed(job.getLimitSpeed())
                .listDevices(job.getListDevices())
                .notifyMethod(job.getNotifyMethod())
                .notifyOrganization(job.getNotifyOrganization())
                .placeId(job.getPlaceId())
                .placeName(job.getPlaceName())
                .priority(job.getPriority())
                .processResult(job.getProcessResult())
                .startSiteId(job.getStartSiteId())
                .startTime(job.getStartTime())
                .status(job.getStatus())
                .userIds(job.getUserIds())
                .vehicleType(job.getVehicleType())
                .vehicleWeight(job.getVehicleWeight())
                .sourceId(eventDTO.getSourceId())
                .direction(job.getDirection())
                .build();

        if (job.getStartSiteId() != null) {
            SiteDTO startSiteInfo = siteService.findById(job.getStartSiteId());
            detailJob.setStartSite(startSiteInfo);
        }
        if (job.getEndSiteId() != null) {
            SiteDTO endSiteInfo = siteService.findById(job.getEndSiteId());
            detailJob.setEndSite(endSiteInfo);
        }
        if (!StringUtil.isNullOrEmpty(job.getUserIds())) {
            detailJob.setListUser(userService.transform(userService.findByListId(job.getUserIds())));
        }
        return detailJob;
    }

    @Override
    public long deleteByEventIds(List<String> eventIds) {
        return jobRepository.deleteByEventIdIn(eventIds);
    }

    @Override
    public Page<Job> findByJobTypeAndStartTimeBetween(List<String> jobType, Date start, Date end, Pageable pageable) {
        return jobRepository.findByJobTypeInAndStartTimeBetween(jobType, start, end, pageable);
    }

    @Override
    public List<JobInStraightMap> getListJobInStraightMap() {
        ArrayList<String> listNotiJobType = new ArrayList<String>(Arrays.asList(JobType.CLOSE_LANE.code(), JobType.FORBIDDEN_WAY.code(), JobType.CLOSE_OPEN_ENTRANCE_EXIT.code(), JobType.LIMIT_SPEED.code()));
        ArrayList<JobStatus> listJobStatus = new ArrayList<JobStatus>(Arrays.asList(JobStatus.CHECKING, JobStatus.PROCESSING, JobStatus.RECEIVE_PROCESSING, JobStatus.UPDATED_BOARD));
        List<Job> listNotiJob = jobRepository.findByStatusInAndJobTypeIn(listJobStatus, listNotiJobType);
        return transformToListJobInStraightMap(listNotiJob);
    }

    @Override
    public List<JobInStraightMap> getListJobInStraightMap(String groupId, String userId) {
        ArrayList<String> listNotiJobType = new ArrayList<String>(Arrays.asList(JobType.CLOSE_LANE.code(), JobType.FORBIDDEN_WAY.code(), JobType.CLOSE_OPEN_ENTRANCE_EXIT.code(), JobType.LIMIT_SPEED.code()));
        ArrayList<JobStatus> listJobStatus = new ArrayList<JobStatus>(Arrays.asList(JobStatus.CHECKING, JobStatus.PROCESSING, JobStatus.RECEIVE_PROCESSING, JobStatus.UPDATED_BOARD));
        List<Job> listNotiJob = jobRepository.findListJobNotiForUser(groupId, userId, listJobStatus, listNotiJobType);
        return transformToListJobInStraightMap(listNotiJob);
    }

    private List<JobInStraightMap> transformToListJobInStraightMap(List<Job> listJob) {
        List<String> listSiteIds = new ArrayList<>();
        listJob.stream().forEach(job -> {
            if (job.getStartSiteId() != null) {
                listSiteIds.add(job.getStartSiteId());
            }
            if (job.getEndSiteId() != null) {
                listSiteIds.add(job.getEndSiteId());
            }
        });
        List<SiteDTO> listSiteInfo = siteService.findByListId(listSiteIds);
        Map<String, SiteDTO> siteMap = listSiteInfo.stream().collect(Collectors.toMap(SiteDTO::getSiteId, site -> site));
        return listJob.stream().map(job
                -> JobInStraightMap.builder()
                        .jobId(job.getId())
                        .startTime(job.getStartTime())
                        .status(job.getStatus())
                        .jobType(job.getJobType())
                        .eventId(job.getEventId())
                        .endTime(job.getEndTime())
                        .startSite(job.getStartSiteId() != null ? siteMap.get(job.getStartSiteId()) : null)
                        .endSite(job.getEndSiteId() != null ? siteMap.get(job.getEndSiteId()) : null)
                        .plateId(job.getPlaceId())
                        .plateName(job.getPlaceName())
                        .directionCode(job.getDirection())
                        .limitSpeed(job.getLimitSpeed() != null ? job.getLimitSpeed() : 0)
                        .build()
        ).collect(Collectors.toList());
    }

    private List<ActionHistory> getAvatarByListActorId(List<ActionHistory> actionHistories) {
        List<String> actorIdList = actionHistories.stream().map(actionHistory -> actionHistory.getActorId()).collect(Collectors.toList());
        String actorIds = String.join(",", actorIdList);
        List<User> userList = userService.findByListId(actorIds);
        Map<String, User> userMap = new HashMap<>();
        userList.stream().forEach(user -> {
            userMap.put(user.getUuid(), user);
        });
        for (ActionHistory actionHistory : actionHistories) {
            if (!StringUtil.isNullOrEmpty(actionHistory.getActorId())) {
                User user = userMap.get(actionHistory.getActorId());
                actionHistory.setAvatar(user.getAvatar());
            }
        }
        return actionHistories;
    }
}
