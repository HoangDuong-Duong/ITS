/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.repository;

import com.elcom.its.shift.model.UserShift;
import java.util.Date;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface UserShiftRepository extends CrudRepository<UserShift, String> {

    long deleteByGroupCodeAndDayBetween(String groupCode, Date startDate, Date endDate);

    List<UserShift> findByGroupCodeAndDayBetween(String groupCode, Date startDate, Date endDate);

    List<UserShift> findByStageCodeInAndDayAndNumberOfShiftContainingIgnoreCase(List<String> stageCode, Date date, String shift);

    UserShift findFirstByUserIdAndDay(String userId, Date day);

    List<UserShift> findByUserIdAndDayAndNumberOfShiftContainingIgnoreCase(String userId, Date date, String shift);
}
