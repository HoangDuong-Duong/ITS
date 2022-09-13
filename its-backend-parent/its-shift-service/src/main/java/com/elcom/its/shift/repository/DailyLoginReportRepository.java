/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.repository;

import com.elcom.its.shift.model.DailyLoginReport;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface DailyLoginReportRepository extends CrudRepository<DailyLoginReport, String> {

    List<DailyLoginReport> findByDay(Date day);

    DailyLoginReport findFirstByUserIdAndDay(String userId, Date day);

    @Query("SELECT d from DailyLoginReport d WHERE d.day >= :startTime and d.day <= :endTime and (d.shift is not null or d.totalTime is not null)")
    List<DailyLoginReport> findDailyReport(Date startTime, Date endTime);

    List<DailyLoginReport> findByDayBetweenAndTotalTimeIsNotNull(Date startTime, Date endTime);

    long deleteByDay(Date day);

    long deleteByDayAndUserIdNotIn(Date day, List<String> userIds);
}
