/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.elcom.its.management.service.impl;

import com.elcom.its.management.dto.AggScheduledEvent;
import com.elcom.its.management.enums.ScheduledEventStatus;
import com.elcom.its.management.model.ScheduledEvent;
import com.elcom.its.management.repository.JobRepository;
import com.elcom.its.management.repository.ReportScheduledEventCustomize;
import com.elcom.its.management.repository.ScheduledEventRepository;
import com.elcom.its.management.service.ScheduledEventService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Acer
 */
@Service
@Transactional
public class ScheduledEventServiceImpl implements ScheduledEventService {

    @Autowired
    private ScheduledEventRepository scheduledEventRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ReportScheduledEventCustomize reportScheduledEventCustomize;

    @Override
    public ScheduledEvent save(ScheduledEvent scheduledEvent) {
        jobRepository.save(scheduledEvent.getJob());
        scheduledEventRepository.save(scheduledEvent);
        return scheduledEvent;
    }

    @Override
    public List<ScheduledEvent> getAllScheduledEvent() {
        return scheduledEventRepository.findAll();
    }

    @Override
    public List<ScheduledEvent> getScheduledEventByTime(Date startTime, Date endTime, String keyword) {
        String search = "%" + keyword + "%";
        List<ScheduledEvent> rs = scheduledEventRepository.findByStartTimeBetween(startTime, endTime, search);
        return rs;
    }

    @Override
    public List<ScheduledEvent> getListScheduledEventForUser(Date startTime, Date endTime, String groupId, String userId, String keyword) {
        keyword = "%" + keyword + "%";
        userId = "%" + userId + "%";
        return scheduledEventRepository.getListScheduledEventForUser(startTime, endTime, groupId, userId, keyword);
    }

    @Override
    public ScheduledEvent findByIdAndStartTime(String id, Date startTime) {
        return scheduledEventRepository.findByIdAndStartTime(id, startTime);
    }

    @Override
    public boolean deleteScheduledEvent(ScheduledEvent scheduledEvent) {
        try {
            jobRepository.delete(scheduledEvent.getJob());
            scheduledEventRepository.delete(scheduledEvent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<ScheduledEvent> getListWaitingEvent(Date startTime, Date endTime) {
        List<ScheduledEventStatus> status = new ArrayList<ScheduledEventStatus>(Arrays.asList(ScheduledEventStatus.WAIT, ScheduledEventStatus.UPCOMING));
        return scheduledEventRepository.findByStatusInAndStartTimeBetween(status, startTime, endTime);
    }

    @Override
    public ScheduledEvent findById(String id) {
        Optional<ScheduledEvent> scheduledEventOptional = scheduledEventRepository.findById(id);
        if (scheduledEventOptional.isPresent()) {
            return scheduledEventOptional.get();
        } else {
            return null;
        }
    }

    @Override
    public AggScheduledEvent getAggScheduledEvent(String userId, Date shiftStartDate, Date shiftEndDate, Date nextShiftStartDate, Date nextShiftEndDate) {
        return reportScheduledEventCustomize.getAggScheduledEvent(userId, shiftStartDate, shiftEndDate, nextShiftStartDate, nextShiftEndDate);
    }

}
