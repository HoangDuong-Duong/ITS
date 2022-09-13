/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.repository;

import com.elcom.its.management.enums.JobStatus;
import com.elcom.its.management.model.Job;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface JobRepository extends PagingAndSortingRepository<Job, String> {

    List<Job> findByEventIdOrderByCreatedDateDesc(String eventId);

    @Query("select j.eventId  from Job j WHERE j.groupId = :groupId and j.eventStartTime between :startTime and :endTime")
    List<String> findEventIdForGroup(@Param("groupId") String groupId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Query("select j.eventId  from Job j WHERE  j.eventStartTime between :startTime and :endTime")
    List<String> findByAdmin(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Query("select j from Job j WHERE (j.groupId = :groupId AND j.userIds = null) OR (j.userIds LIKE %:userId%) ")
    Page<Job> getJobsForUser(String groupId, String userId, Pageable pageable);

    List<Job> findByStatusInAndJobTypeIn(List<JobStatus> jobStatus, List<String> jobType);
    
    @Query("select j from Job j WHERE (j.groupId = :groupId or j.createdBy = :userId ) AND j.status in (:jobStatus) AND j.jobType in (:jobType) ")
    List<Job> findListJobNotiForUser(String groupId,String userId, List<JobStatus> jobStatus, List<String> jobType);

    List<Job> findByEventIdOrderByCreatedDateAsc(String eventId);
    
    long deleteByEventIdIn(List<String> eventIds);
}
