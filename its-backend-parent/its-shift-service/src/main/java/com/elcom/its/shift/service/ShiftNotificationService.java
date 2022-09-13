/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service;

import com.elcom.its.shift.dto.DailyEventReport;
import java.text.ParseException;

/**
 *
 * @author Admin
 */
public interface ShiftNotificationService {

    DailyEventReport getDailyEventReport(String userId, String groupId) throws ParseException ;

}
