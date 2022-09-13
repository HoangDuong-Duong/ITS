/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service.impl;

import com.elcom.its.shift.model.DailyLoginReport;
import com.elcom.its.shift.repository.DailyLoginReportRepository;
import com.elcom.its.shift.service.DailyLoginReportService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Admin
 */
@Service
@Transactional
public class DailyLoginReportServiceImpl implements DailyLoginReportService {

    @Autowired
    private DailyLoginReportRepository dailyLoginReportRepository;

    @Override
    public List<DailyLoginReport> findByDay(Date day) {
        return dailyLoginReportRepository.findByDay(day);
    }

    @Override
    public DailyLoginReport findByUserIdAndByDay(String userId, Date day) {
        return dailyLoginReportRepository.findFirstByUserIdAndDay(userId, day);
    }

    @Override
    public void save(DailyLoginReport dailyLoginReport) {
        dailyLoginReportRepository.save(dailyLoginReport);
    }

    @Override
    public List<DailyLoginReport> getDataReport(Date startTime, Date endTime) {
        return dailyLoginReportRepository.findDailyReport(startTime, endTime);
    }

    @Override
    public void deleteByDay(Date day) {
        dailyLoginReportRepository.deleteByDay(day);
    }

    @Override
    public void deleteByDayAndUserIdNotIn(Date day, List<String> userIds) {
        dailyLoginReportRepository.deleteByDayAndUserIdNotIn(day, userIds);
    }

}
