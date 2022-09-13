/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service;

import com.elcom.its.shift.dto.AggJobByStatus;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface JobService {

    List<AggJobByStatus> getAggJobByStatus(String groupId, Date startDate, Date endDate);
}
