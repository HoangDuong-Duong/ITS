/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.elcom.its.management.service;

import com.elcom.its.management.dto.AggScheduledEvent;
import com.elcom.its.management.model.Job;
import com.elcom.its.management.model.ScheduledEvent;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Acer
 */
public interface ScheduledEventService {

    public ScheduledEvent save(ScheduledEvent scheduledEvent);

    public List<ScheduledEvent> getAllScheduledEvent();

    public List<ScheduledEvent> getScheduledEventByTime(Date startTime, Date endTime, String keyword);

    List<ScheduledEvent> getListScheduledEventForUser(Date startTime, Date endTime, String groupId, String userId, String keyword);

    ScheduledEvent findByIdAndStartTime(String id, Date startTime);

    boolean deleteScheduledEvent(ScheduledEvent scheduledEvent);

    List<ScheduledEvent> getListWaitingEvent(Date startTime, Date endTime);

    public ScheduledEvent findById(String id);
    
    public AggScheduledEvent getAggScheduledEvent(String userId, Date shiftStartDate, Date shiftEndDate, Date nextShiftStartDate, Date nextShiftEndDate);

}
