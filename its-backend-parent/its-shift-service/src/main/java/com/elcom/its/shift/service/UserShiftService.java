/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service;

import com.elcom.its.shift.dto.GetUserShiftResponse;
import com.elcom.its.shift.dto.NumberOfShiftByDay;
import com.elcom.its.shift.dto.UserShiftGroupByStage;
import com.elcom.its.shift.dto.UserShiftGroupByUser;
import com.elcom.its.shift.dto.WeeklyShiftReportContent;
import com.elcom.its.shift.model.UserShift;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface UserShiftService {

    UserShift save(UserShift userShift);

    void save(List<UserShift> listUserShifts);

    void deleteByGroupCodeAndDayBetween(String groupCode, Date startDate, Date endDate);

    List<UserShiftGroupByStage> getListUserShiftGroupByStages(Date startDate, Date endDate, String groupCode);

    List<String> getListUserIdOnShift(Date date, String siteId);

    List<UserShiftGroupByUser> transformToListUserShiftGroupByUser(List<UserShift> listUserShifts, String month);

    List<NumberOfShiftByDay> getNumberOfShiftByDay(List<UserShift> listUserShifts, String month);

    GetUserShiftResponse getUserShiftResponse(Date startDate, Date endDate, String groupCode);

    List<UserShift> findByGroupCodeAndDayBetween(Date startDate, Date endDate, String groupCode);

    List<WeeklyShiftReportContent> getWeeklyShiftReportContents(Date startDate, Date endDate, String groupCode);

    UserShift findByUserIdAndDay(String userId, Date day);

    List<UserShift> findByUserIdAndDayAndShift(String userId, Date day, String shift);
}
