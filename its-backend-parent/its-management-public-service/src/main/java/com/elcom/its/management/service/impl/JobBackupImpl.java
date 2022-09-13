package com.elcom.its.management.service.impl;

import com.elcom.its.management.model.Job;
import com.elcom.its.management.model.JobBackup;
import com.elcom.its.management.repository.JobBackupRepository;
import com.elcom.its.management.repository.JobRepository;
import com.elcom.its.management.service.JobBackupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobBackupImpl implements JobBackupService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobBackupRepository jobBackupRepository;
    @Override
    public void saveBackUp(String eventId) {
        List<Job> jobs = jobRepository.findByEventIdOrderByCreatedDateDesc(eventId);
        List<JobBackup> jobBackups = new ArrayList<>();
        for (Job jobBackup: jobs) {
            ModelMapper modelMapper = new ModelMapper();
            JobBackup tmp = modelMapper.map(jobBackup, JobBackup.class);
            jobBackups.add(tmp);
        }
        jobBackupRepository.saveAll(jobBackups);


    }
}
