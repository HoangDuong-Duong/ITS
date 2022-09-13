/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service;

import com.elcom.its.shift.dto.AggScheduledEvent;
import java.util.Date;

/**
 *
 * @author Admin
 */
public interface ScheduledEventService {
    AggScheduledEvent getAggScheduledEvent(String userId, Date shiftStartDate, Date shiftEndDate, Date nextShiftStartDate, Date nextShiftEndDate);
}
