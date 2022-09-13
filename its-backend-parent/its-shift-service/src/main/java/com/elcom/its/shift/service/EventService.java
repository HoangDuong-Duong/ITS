/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service;

import java.util.Date;
import java.util.List;
import com.elcom.its.shift.dto.AggEventByStatus;

/**
 *
 * @author Admin
 */
public interface EventService {

    List<AggEventByStatus> getAggEventByStatus(String stageCodes, Date startDate, Date endDate);
}
