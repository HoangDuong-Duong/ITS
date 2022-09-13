/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service;

import com.elcom.its.management.dto.BaseJobProcessingResult;
import com.elcom.its.management.dto.DetailJob;
import com.elcom.its.management.dto.JobInMapNotification;
import com.elcom.its.management.dto.JobInStraightMap;
import com.elcom.its.management.dto.RepairJobProcessing;
import com.elcom.its.management.dto.VmsBoardJobProcessing;
import com.elcom.its.management.model.ActionHistory;
import com.elcom.its.management.model.EventFile;
import com.elcom.its.management.model.Job;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;

/**
 *
 * @author Admin
 */
public interface JobService {

    public void save(Job job);

    public Job findById(String id);

    public List<Job> findByEventId(String eventId);

    public List<Job> findByEventIdReport(String eventId);

    public Job processVmsJob(List<VmsBoardJobProcessing> listVmsBoardJobProcessings, Job job,String userId);

    public Job processBaseJob(Job job, BaseJobProcessingResult baseJobProcessing);

    public Job processRepairJob(List<RepairJobProcessing> listRepairJobProcessings, Job job);

    public Job createNewJob(Map<String, Object> bodyMap);

    public Job updateJob(Job job, Map<String, Object> bodyMap);

    public List<String> getEvent(String groupId, Date startTime, Date endTime);

    public List<String> getEventAdmin(Date startTime, Date endTime);

    public List<EventFile> getListFileForEvent(String eventId);

    public Page<Job> getJobsForUser(String groupId, String userId, Pageable page);

    public List<JobInMapNotification> getListJobNotificationInMap();

    public List<JobInMapNotification> getListJobNotificationInMap(String groupId, String userId);

    public List<JobInStraightMap> getListJobInStraightMap();

    public List<JobInStraightMap> getListJobInStraightMap(String groupId, String userId);

    public DetailJob transform(Job job);

    public long deleteByEventIds(List<String> eventIds);

    public Page<Job> findByJobTypeAndStartTimeBetween(List<String> jobType, Date start, Date end, Pageable pageable);

}
