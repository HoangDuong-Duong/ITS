/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service;

import com.elcom.its.shift.model.DailyLoginReport;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface DailyLoginReportService {

    List<DailyLoginReport> findByDay(Date day);

    void deleteByDay(Date day);

    DailyLoginReport findByUserIdAndByDay(String userId, Date day);

    void save(DailyLoginReport dailyLoginReport);

    List<DailyLoginReport> getDataReport(Date startTime, Date endTime);

    void deleteByDayAndUserIdNotIn(Date day, List<String> userIds);
}
