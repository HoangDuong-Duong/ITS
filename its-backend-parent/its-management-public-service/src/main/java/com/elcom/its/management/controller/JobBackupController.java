package com.elcom.its.management.controller;

import com.elcom.its.management.model.EventFile;
import com.elcom.its.management.model.Job;
import com.elcom.its.management.service.JobBackupService;
import com.elcom.its.management.service.JobService;
import com.elcom.its.message.MessageContent;
import com.elcom.its.message.ResponseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class JobBackupController {
    @Autowired
    private JobBackupService jobBackupService;
    @Autowired
    private JobService jobService;

    public ResponseMessage jobBackup(String pathParam) {
        jobBackupService.saveBackUp(pathParam);
        List<EventFile> fileJob = jobService.getListFileForEvent(pathParam);
        return new ResponseMessage(new MessageContent(HttpStatus.OK.value(), "ok", fileJob));
    }
}
