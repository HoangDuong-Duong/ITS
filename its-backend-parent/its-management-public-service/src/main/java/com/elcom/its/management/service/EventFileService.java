/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service;

import com.elcom.its.management.dto.EventDTO;
import com.elcom.its.management.dto.Response;
import com.elcom.its.management.model.EventFile;

import java.text.ParseException;

/**
 *
 * @author Admin
 */
public interface EventFileService {

    EventFile createFileEventHistory(String eventId,String uuid) throws Exception;

    EventFile createFileEventInfo(String eventId,String uuid) throws Exception;

    EventFile createFileEventInfoUpdate(String eventId, String uuid) throws Exception;

    EventFile createFileAccident(String eventId, String uuid) throws Exception;

    public EventFile createReportEvent(String eventId,String uuid) throws Exception;

    public EventFile createReportEventDaily(String fromDate,String toDate, String uuid) throws Exception;

    EventFile createFileDayReport(String uuid,String startTime, String endTime) throws ParseException;
}
