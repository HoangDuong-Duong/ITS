/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.controller;

import com.elcom.its.management.dto.ABACResponseDTO;
import com.elcom.its.management.dto.AuthorizationResponseDTO;
import com.elcom.its.management.dto.BaseJobProcessingResult;
import com.elcom.its.management.dto.EventDTO;
import com.elcom.its.management.dto.RepairJobProcessing;
import com.elcom.its.management.dto.VmsBoardJobProcessing;
import com.elcom.its.management.dto.VmsBoardSuggestion;
import com.elcom.its.management.enums.EventStatus;
import com.elcom.its.management.enums.JobStatus;
import com.elcom.its.management.enums.JobType;
import com.elcom.its.management.enums.Priority;
import com.elcom.its.management.model.Device;
import com.elcom.its.management.model.File;
import com.elcom.its.management.model.Job;
import com.elcom.its.management.service.EventService;
import com.elcom.its.management.service.JobService;
import com.elcom.its.management.service.StageService;
import com.elcom.its.management.validation.JobValidation;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.elcom.its.utils.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
public class JobController extends BaseController {

    static final Logger LOGGER = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService jobService;

    @Autowired
    private StageService stageService;

    @Autowired
    private EventService eventService;

    public ResponseMessage createJob(Map<String, String> headerParam, String requestPath,
            String requestMethod, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            if (!checkCenterPermission(dto, requestPath, "POST", bodyParam, "CREATE", null)) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n t???o c??ng vi???c", null));
            } else {

                Job newJob = jobService.createNewJob(bodyParam);
                EventDTO event = eventService.getEventByParentId(newJob.getEventId());
                if (event != null && event.getEventStatus().equals(EventStatus.PROCESSED)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "S??? ki???n ???? k???t th??c", null));
                } else {
                    String invalid = new JobValidation().validateJob("CREATE", newJob);
                    if (invalid != null) {
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalid, null));
                    } else {
                        addActionHistory(newJob, "CREATE", "T???o c??ng vi???c", null, null, dto);
                        newJob.setCreatedBy(dto.getUuid());
                        jobService.save(newJob);
                        eventService.updateEventStatus(newJob.getEventId(), EventStatus.PROCESSING, dto.getUuid());
                        notifyJobAction("CREATE", "T???o c??ng vi???c", newJob, dto);
                        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), newJob));
                    }
                }

            }
        }

        return response;
    }

    public ResponseMessage suggestVmsJob(Map<String, String> headerParam, String requestPath,
            String requestMethod, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            if (!checkCenterPermission(dto, requestPath, "POST", bodyParam, "CREATE", null)) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n x??? l?? c??ng vi???c", null));
            } else {
                VmsBoardSuggestion vmsBoardSuggestion = tranform(bodyParam);
                Job job = createJobFromVmsBoardSuggestion(vmsBoardSuggestion, dto);
                VmsBoardJobProcessing vmsBoardJobProcessing = createBoardJobProcessing(vmsBoardSuggestion);
                jobService.processVmsJob(new ArrayList<VmsBoardJobProcessing>(Arrays.asList(vmsBoardJobProcessing)), job, dto.getUuid());
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), job));

            }
        }

        return response;
    }

    public ResponseMessage updateJob(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMethod, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            Job jobById = jobService.findById(pathParam);
            if (jobById == null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Kh??ng t??m th???y c??ng vi???c", null));
            } else if (!jobById.getStatus().equals(JobStatus.RECEIVE_PROCESSING)) {
                response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "C??ng vi???c ???? chuy???n tr???ng th??i", null));
            } else {
                if (!checkCenterPermission(dto, requestPath, "PUT", bodyParam, "UPDATE", jobById)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n ch???nh s???a c??ng vi???c", null));
                } else {
                    jobById = jobService.updateJob(jobById, bodyParam);
                    String invalid = new JobValidation().validateJob("UPDATE", jobById);
                    if (invalid != null) {
                        response = new ResponseMessage(new MessageContent(HttpStatus.BAD_REQUEST.value(), invalid, null));
                    } else {
                        addActionHistory(jobById, "UPDATE", "Ch???nh s???a c??ng vi???c", null, null, dto);
                        jobById.setId(pathParam);
                        jobService.save(jobById);
                        notifyJobAction("UPDATE", "Ch???nh s???a c??ng vi???c", jobById, dto);
                        response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobById));
                    }
                }
            }
        }

        return response;
    }

    public ResponseMessage confirmJob(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMethod, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {

            Job jobById = jobService.findById(pathParam);
            if (jobById == null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Kh??ng t??m th???y c??ng vi???c", null));
            } else {
                if (!jobById.getStatus().equals(JobStatus.RECEIVE_PROCESSING)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "C??ng vi???c ???? chuy???n tr???ng th??i", null));
                } else if (checkBasePermission(dto, requestPath, "POST", jobById)) {
                    jobById.setStatus(JobStatus.PROCESSING);
                    addActionHistory(jobById, "CONFIRM", "X??c nh???n c??ng vi???c", null, null, dto);
                    jobService.save(jobById);
                    notifyJobAction("CONFIRM", "X??c nh???n c??ng vi???c", jobById, dto);
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobById));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n x??c nh???n c??ng vi???c", null));
                }

            }
        }
        return response;
    }

    public ResponseMessage getJobByEvent(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMethod, String urlParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n xem danh s??ch c??ng vi???c", null));
            } else {
                Map<String, String> urlMap = StringUtil.getUrlParamValues(urlParam);
                String eventId = urlMap.get("eventId");
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobService.findByEventId(eventId)));
            }
        }

        return response;
    }

    public ResponseMessage processJob(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMethod, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            Job jobById = jobService.findById(pathParam);
            if (jobById == null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Kh??ng t??m th???y c??ng vi???c", null));
            } else {
                if (jobById.getStatus().equals(JobStatus.RECEIVE_PROCESSING)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "C??ng vi???c ch??a ???????c x??c nh???n", null));
                } else if (jobById.getStatus().equals(JobStatus.COMPLETE) && !jobById.getJobType().equalsIgnoreCase(JobType.NOTIFY_INFORMATION.code())
                        && !jobById.getJobType().equalsIgnoreCase(JobType.SUPERVISE_SCENE.code())) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "C??ng vi???c ???? ho??n th??nh", null));
                } else if (checkBasePermission(dto, requestPath, "POST", jobById)) {
                    String content = (String) bodyParam.get("content");
                    ObjectMapper mapper = new ObjectMapper();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    mapper.setDateFormat(df);
                    List<File> listFile = bodyParam.get("listFile") != null ? mapper.convertValue(
                            bodyParam.get("listFile"), new TypeReference<List<File>>() {
                    }) : null;
                    if (jobById.getJobType().equalsIgnoreCase(JobType.NOTIFY_INFORMATION.code()) || jobById.getJobType().equalsIgnoreCase(JobType.SUPERVISE_SCENE.code())) {
                        jobById.setStatus(JobStatus.COMPLETE);
                    } else {
                        jobById.setStatus(JobStatus.CHECKING);
                    }
                    addActionHistory(jobById, "PROCESS", "G???i k???t qu??? x??? l??", content, listFile, dto);
                    BaseJobProcessingResult baseJobProcessingResult = BaseJobProcessingResult.builder()
                            .content(content)
                            .listFile(listFile)
                            .build();
                    jobService.processBaseJob(jobById, baseJobProcessingResult);
                    notifyJobAction("PROCESS", "G???i k???t qu??? x??? l??", jobById, dto);
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobById));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n x??c nh???n c??ng vi???c", null));
                }
            }
        }
        return response;
    }

    public ResponseMessage processVmsJob(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMethod, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            String jobId = (String) bodyParam.get("jobId");
            Job jobById = jobService.findById(jobId);
            if (jobById == null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Kh??ng t??m th???y c??ng vi???c", null));
            } else {
                if (jobById.getStatus().equals(JobStatus.RECEIVE_PROCESSING)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "C??ng vi???c ch??a ???????c x??c nh???n", null));
                } else if (jobById.getStatus().equals(JobStatus.COMPLETE)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "C??ng vi???c ???? ho??n th??nh", null));
                } else if (checkBasePermission(dto, requestPath, "POST", jobById)) {
                    ObjectMapper mapper = new ObjectMapper();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    mapper.setDateFormat(df);
                    List<VmsBoardJobProcessing> listVmsBoardJobProcessing = bodyParam.get("listVmsBoardJobProcessing")
                            != null ? mapper.convertValue(bodyParam.get("listVmsBoardJobProcessing"), new TypeReference<List<VmsBoardJobProcessing>>() {
                                    }) : null;
                    jobById.setStatus(JobStatus.PROCESSING);
                    addActionHistory(jobById, "PROCESS", "C???p nh???t bi???n b??o VMS", null, null, dto);
                    jobService.processVmsJob(listVmsBoardJobProcessing, jobById, dto.getUuid());
                    notifyJobAction("PROCESS", "C???p nh???t bi???n b??o VMS", jobById, dto);
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobById));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n x??c nh???n c??ng vi???c", null));
                }
            }
        }
        return response;
    }

    public ResponseMessage processRepairJob(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMethod, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            String jobId = (String) bodyParam.get("jobId");
            Job jobById = jobService.findById(jobId);
            if (jobById == null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Kh??ng t??m th???y c??ng vi???c", null));
            } else {
                if (jobById.getStatus().equals(JobStatus.RECEIVE_PROCESSING)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "C??ng vi???c ch??a ???????c x??c nh???n", null));
                } else if (jobById.getStatus().equals(JobStatus.COMPLETE)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "C??ng vi???c ???? ho??n th??nh", null));
                } else if (checkBasePermission(dto, requestPath, "POST", jobById)) {
                    ObjectMapper mapper = new ObjectMapper();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    mapper.setDateFormat(df);
                    List<RepairJobProcessing> listRepairJobProcessing = bodyParam.get("listRepairJobProcessing") != null
                            ? mapper.convertValue(bodyParam.get("listRepairJobProcessing"), new TypeReference<List<RepairJobProcessing>>() {
                            }) : null;
                    List<File> listFile = bodyParam.get("listFile") != null ? mapper.convertValue(
                            bodyParam.get("listFile"), new TypeReference<List<File>>() {
                    }) : null;
                    String content = (String) bodyParam.get("content");
                    jobById.setStatus(JobStatus.CHECKING);
                    addActionHistory(jobById, "PROCESS", "G???i k???t qu??? x??? l??", null, null, dto);
                    if (jobById.getListDevices() == null || jobById.getListDevices().isEmpty()) {
                        BaseJobProcessingResult baseJobProcessingResult = BaseJobProcessingResult.builder()
                                .content(content)
                                .listFile(listFile)
                                .build();
                        jobService.processBaseJob(jobById, baseJobProcessingResult);
                    } else {
                        jobService.processRepairJob(listRepairJobProcessing, jobById);
                    }
                    notifyJobAction("PROCESS", "G???i k???t qu??? x??? l??", jobById, dto);
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobById));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n x??? l?? c??ng vi???c", null));
                }
            }
        }
        return response;
    }

    public ResponseMessage finishJob(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMethod) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            Job jobById = jobService.findById(pathParam);
            if (jobById == null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Kh??ng t??m th???y c??ng vi???c", null));
            } else {
                if (!checkCenterPermission(dto, requestPath, "POST", null, "FINISH", jobById)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n k???t th??c c??ng vi???c", null));
                } else {
                    if (jobById.getJobType().equalsIgnoreCase(JobType.UPDATE_VMS_BOARD.code())) {
                        jobById.setStatus(JobStatus.UPDATED_BOARD);
                        addActionHistory(jobById, "FINISH", "Ho??n th??nh c???p nh???t", null, null, dto);
                        jobService.save(jobById);
                        notifyJobAction("FINISH", "Ho??n th??nh c???p nh???t", jobById, dto);
                    } else {
                        jobById.setStatus(JobStatus.COMPLETE);
                        addActionHistory(jobById, "FINISH", "K???t th??c c??ng vi???c", null, null, dto);
                        jobService.save(jobById);
                        notifyJobAction("FINISH", "K???t th??c c??ng vi???c", jobById, dto);
                    }
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobById));
                }
            }
        }
        return response;
    }

    public ResponseMessage requestReverification(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMethod, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            Job jobById = jobService.findById(pathParam);
            if (jobById == null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Kh??ng t??m th???y c??ng vi???c", null));
            } else {
                if (!checkCenterPermission(dto, requestPath, "POST", null, "REQUEST_REVERIFYCATION", jobById)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n y??u c???u x??c minh l???i", null));
                } else if (jobById.getStatus().equals(JobStatus.COMPLETE)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "C??ng vi???c ???? ho??n th??nh", null));
                } else if (!jobById.getStatus().equals(JobStatus.CHECKING)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "C??ng vi???c ch??a ???????c x??c minh", null));
                } else {
                    String content = (String) bodyParam.get("content");
                    ObjectMapper mapper = new ObjectMapper();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    mapper.setDateFormat(df);
                    List<File> listFile = bodyParam.get("listFile") != null ? mapper.convertValue(
                            bodyParam.get("listFile"), new TypeReference<List<File>>() {
                    }) : null;
                    jobById.setStatus(JobStatus.PROCESSING);
                    addActionHistory(jobById, "REQUEST_REVERIFYCATION", "Y??u c???u x??c minh l???i", content, listFile, dto);
                    jobService.save(jobById);
                    notifyJobAction("REQUEST_REVERIFYCATION", "Y??u c???u x??c minh l???i", jobById, dto);
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobById));
                }
            }
        }
        return response;
    }

    public ResponseMessage cancelJob(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMethod) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            Job jobById = jobService.findById(pathParam);

            if (jobById == null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Kh??ng t??m th???y c??ng vi???c", null));
            } else {
                if (!checkCenterPermission(dto, requestPath, "POST", null, "CANCEL", jobById)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n h???y s???a c??ng vi???c", null));
                } else {
                    jobById.setStatus(JobStatus.COMPLETE);
                    addActionHistory(jobById, "CANCEL", "H???y b??? c??ng vi???c", null, null, dto);
                    jobService.save(jobById);
                    notifyJobAction("CANCEL", "H???y b??? c??ng vi???c", jobById, dto);
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobById));
                }
            }
        }
        return response;
    }

    public ResponseMessage commentInJob(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMethod, Map<String, Object> bodyParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "POST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n b??nh lu???n c??ng vi???c", null));
            } else {
                Job jobById = jobService.findById(pathParam);
                if (jobById == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Kh??ng t??m th???y c??ng vi???c", null));
                } else {
                    String comment = (String) bodyParam.get("comment");
                    ObjectMapper mapper = new ObjectMapper();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    mapper.setDateFormat(df);
                    List<File> listFile = bodyParam.get("listFile") != null ? mapper.convertValue(
                            bodyParam.get("listFile"), new TypeReference<List<File>>() {
                    }) : null;
                    addActionHistory(jobById, "COMMENT", "B??nh lu???n c??ng vi???c", comment, listFile, dto);
                    jobService.save(jobById);
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobById));
                }
            }
        }
        return response;
    }

    public ResponseMessage findJobById(Map<String, String> headerParam, String requestPath, String pathParam) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            Job jobById = jobService.findById(pathParam);
            if (jobById == null) {
                response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Kh??ng t??m th???y c??ng vi???c", null));
            } else {
                if (!checkBasePermission(dto, requestPath, "LIST", jobById)) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng ???????c x??? l?? c??ng vi???c n??y", null));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobService.transform(jobById)));
                }
            }
        }
        return response;
    }

    public ResponseMessage getListJobNotifyInMap(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMetho) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n l???y danh s??ch c??ng vi???c tr??n b??nh ?????", null));
            } else {
                if (resultCheckABAC.getAdmin() != null && resultCheckABAC.getAdmin()) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobService.getListJobNotificationInMap()));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobService.getListJobNotificationInMap(dto.getUnit().getUuid(), dto.getUuid())));
                }
            }
        }
        return response;
    }

    public ResponseMessage getListJobInStraightMap(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMetho) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n l???y danh s??ch c??ng vi???c tr??n b??nh ?????", null));
            } else {
                if (resultCheckABAC.getAdmin() != null && resultCheckABAC.getAdmin()) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobService.getListJobInStraightMap()));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobService.getListJobInStraightMap(dto.getUnit().getUuid(), dto.getUuid())));
                }
            }
        }
        return response;
    }

    public ResponseMessage getProcessResult(Map<String, String> headerParam, String requestPath, String pathParam,
            String requestMethod) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "LIST", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n l???y k???t qu??? x??? l?? c??ng vi???c", null));
            } else {
                Job jobById = jobService.findById(pathParam);
                if (jobById == null) {
                    response = new ResponseMessage(new MessageContent(HttpStatus.NOT_FOUND.value(), "Kh??ng t??m th???y c??ng vi???c", null));
                } else {
                    response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobById.getProcessResult()));
                }

            }

        }
        return response;
    }

    public ResponseMessage getJobsForUser(Map<String, String> headerParam, String requestPath, String urlParam,
            String requestMethod) throws ExecutionException, InterruptedException {
        ResponseMessage response = null;
        AuthorizationResponseDTO dto = authenToken(headerParam);
        if (dto == null) {
            response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n ch??a ????ng nh???p", null));
        } else {
            ABACResponseDTO resultCheckABAC = authorizeABAC(null, "DETAIL", dto.getUuid(), requestPath);
            if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
                response = new ResponseMessage(new MessageContent(HttpStatus.FORBIDDEN.value(), "B???n kh??ng c?? quy???n l???y danh s??ch c??ng vi???c", null));
            } else {
                Map<String, String> mapParam = StringUtil.getUrlParamValues(urlParam);
                String page = mapParam.get("page") != null ? mapParam.get("page") : "0";
                String size = mapParam.get("size") != null ? mapParam.get("size") : "20";
                Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size), Sort.by("createdDate").descending());
                Page<Job> jobPage = jobService.getJobsForUser(dto.getUuid(), dto.getUuid(), pageable);
                response = new ResponseMessage(new MessageContent(HttpStatus.OK.value(), HttpStatus.OK.toString(), jobPage.getContent(), jobPage.getTotalElements()));
            }
        }
        return response;
    }

    private boolean checkBasePermission(AuthorizationResponseDTO user, String requestPath,
            String requestMethod, Job job) throws ExecutionException, InterruptedException {
        ABACResponseDTO resultCheckABAC = authorizeABAC(null, requestMethod, user.getUuid(), requestPath);
        if (resultCheckABAC == null || resultCheckABAC.getStatus() == false) {
            return false;
        } else if (resultCheckABAC.getAdmin() != null && resultCheckABAC.getAdmin()) {
            return true;
        } else {
            if (requestMethod.equals("LIST")) {
                return job.getGroupId().equalsIgnoreCase(user.getUnit().getUuid());
            }
            if (StringUtil.isNullOrEmpty(job.getUserIds())) {
                if (job.getGroupId().equalsIgnoreCase(user.getUnit().getUuid())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                String[] userIdsInJob = job.getUserIds().split(",");
                for (String userInJob : userIdsInJob) {
                    if (userInJob.equalsIgnoreCase(user.getUuid())) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    private boolean checkCenterPermission(AuthorizationResponseDTO user, String requestPath,
            String requestMethod, Map<String, Object> bodyParam, String action, Job job) {
        try {
            String eventId;
            if (job != null) {
                eventId = job.getEventId();
            } else {
                eventId = (String) bodyParam.get("eventId");
            }
            String stageEventId = stageService.getStageHaveEvent(eventId);
            Map<String, Object> bodyCheckABAC = createBodyCheckABAC(user, stageEventId);
            ABACResponseDTO resultCheckABAC = authorizeABAC(bodyCheckABAC, requestMethod, user.getUuid(), requestPath);
            if (resultCheckABAC == null || !resultCheckABAC.getStatus()) {
                return false;
            } else {
                switch (action) {
                    case "CREATE":
                        return true;
                    case "REQUEST_REVERIFYCATION":
                        return true;
                    case "UPDATE":
                        return true;
                    case "FINISH":
                        return true;
                    case "CANCEL":
                        return true;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }

        return false;
    }

    private VmsBoardSuggestion tranform(Map<String, Object> bodymap) {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        VmsBoardSuggestion vmsBoardSuggestion = mapper.convertValue(bodymap, new TypeReference<VmsBoardSuggestion>() {
        });
        return vmsBoardSuggestion;
    }

    private VmsBoardJobProcessing createBoardJobProcessing(VmsBoardSuggestion suggestion) {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        VmsBoardJobProcessing vmsBoardProcessing = mapper.convertValue(suggestion, new TypeReference<VmsBoardJobProcessing>() {
        });
        return vmsBoardProcessing;
    }

    private Job createJobFromVmsBoardSuggestion(VmsBoardSuggestion suggestion, AuthorizationResponseDTO userDTO) {
        Date now = new Date();
        Device device = new Device();
        device.setDeviceId(suggestion.getVmsBoardId());
        device.setDeviceType("VMSBOARD");
        device.setDeviceName(suggestion.getVmsBoardName());
        Job job = new Job();
        job.setId(UUID.randomUUID().toString());
        job.setCreatedBy(userDTO.getUuid());
        job.setCreatedDate(now);
        job.setUserIds(userDTO.getUuid());
        job.setStatus(JobStatus.UPDATED_BOARD);
        job.setEndSiteId(suggestion.getSiteId());
        job.setStartSiteId(suggestion.getSiteId());
        job.setEndTime(suggestion.getEndTime());
        job.setEventId(suggestion.getEventId());
        job.setEventStartTime(suggestion.getEventStartTime());
        job.setGroupId(userDTO.getUnit().getUuid());
        job.setJobType(JobType.UPDATE_VMS_BOARD.code());
        job.setStartTime(now);
        job.setListDevices(new ArrayList<>(Arrays.asList(device)));
        addActionHistory(job, "CREATE", "T???o c??ng vi???c", null, null, userDTO);
        addActionHistory(job, "PROCESS", "C???p nh???t bi???n b??o VMS", null, null, userDTO);
        job.setPriority(Priority.AVERAGE_IMPORTANCE);
        job.setModifiedTime(new Date());
        return job;
    }
}
