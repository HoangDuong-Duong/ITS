/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service.impl;

import com.elcom.its.shift.dto.AggEventByStatus;
import com.elcom.its.shift.dto.AggJobByStatus;
import com.elcom.its.shift.dto.AggScheduledEvent;
import com.elcom.its.shift.dto.DailyEventReport;
import com.elcom.its.shift.dto.DailyEventReportOnShift;
import com.elcom.its.shift.dto.ShiftDate;
import com.elcom.its.shift.dto.Stage;
import com.elcom.its.shift.model.Shift;
import com.elcom.its.shift.model.UserShift;
import com.elcom.its.shift.service.EventService;
import com.elcom.its.shift.service.JobService;
import com.elcom.its.shift.service.ScheduledEventService;
import com.elcom.its.shift.service.ShiftNotificationService;
import com.elcom.its.shift.service.ShiftService;
import com.elcom.its.shift.service.StageService;
import com.elcom.its.shift.service.UserShiftService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class ShiftNotificationServiceImpl implements ShiftNotificationService {

    @Autowired
    private UserShiftService userShiftService;

    @Autowired
    private EventService eventService;

    @Autowired
    private JobService jobService;

    @Autowired
    private StageService stageService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private ScheduledEventService scheduledEventService;

    @Override
    public DailyEventReport getDailyEventReport(String userId, String groupId) throws ParseException {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
        Shift shift = shiftService.getShift(date);
        if (shift == null) {
            return null;
        }
        List<Shift> allShifts = shiftService.findAll(simpleDateFormat.format(date));
        int numsOfShift = allShifts.size();
        Shift previousShift = allShifts.get((shift.getNumber() - 2 + numsOfShift) % numsOfShift);
        Shift nextShift = allShifts.get((shift.getNumber()) % numsOfShift);
        List<String> listStageCodes = getListStageCode(userId, date, shift);
        DailyEventReportOnShift shiftReport = getDailyEventReportOnShift(userId, shift, groupId, date, listStageCodes);
        if (previousShift.getStartTimeFloat() > previousShift.getEndTimeFloat()) {
            date = getPrevDay(date);
        }
        DailyEventReportOnShift previousShiftReport = getDailyEventReportOnShift(userId, previousShift, groupId, getDate(shift.getStartTime(), date), listStageCodes);
        return DailyEventReport.builder()
                .shiftReport(shiftReport)
                .previousShiftReport(previousShiftReport)
                .aggScheduledEvent(getAggScheduledEvent(userId, shift, nextShift))
                .build();
    }

    private List<String> getListStageCode(String userId, Date date, Shift shift) {
        List<UserShift> listUserShifts = userShiftService.findByUserIdAndDayAndShift(userId, getStartOfDay(date), "" + shift.getNumber());
        if (listUserShifts == null) {
            return null;
        }
        return listUserShifts.stream().map(userShift -> userShift.getStageCode()).collect(Collectors.toList());

    }

    private AggScheduledEvent getAggScheduledEvent(String userId, Shift shift, Shift nextShift) throws ParseException {
        Date date = new Date();
        ShiftDate shiftDate = getShiftDate(shift, date);
        if (shift.getStartTimeFloat() > shift.getEndTimeFloat()) {
            date = getNextDay(date);
        }
        ShiftDate nextShiftDate = getShiftDate(nextShift, getDate(shift.getEndTime(), date));
        return scheduledEventService.getAggScheduledEvent(userId, shiftDate.getShiftStartDate(), shiftDate.getShiftEndDate(),
                nextShiftDate.getShiftStartDate(), nextShiftDate.getShiftEndDate());
    }

    private DailyEventReportOnShift getDailyEventReportOnShift(String userId, Shift shift, String groupId, Date date, List<String> stageCodes) throws ParseException {

        ShiftDate shiftDate = getShiftDate(shift, date);
        List<AggEventByStatus> listAggEventByStatus = eventService.getAggEventByStatus(String.join(",", stageCodes), shiftDate.getShiftStartDate(), shiftDate.getShiftEndDate());
        List<AggJobByStatus> listAggJobByStatus = jobService.getAggJobByStatus(groupId, shiftDate.getShiftStartDate(), shiftDate.getShiftEndDate());
        List<Stage> listStages = stageService.findByStageCodes(String.join(",", stageCodes));
        removeDuplicateStage(listStages);
        List<String> listStageName = listStages.stream().map(stage -> stage.getStageName()).collect(Collectors.toList());
        return DailyEventReportOnShift.builder()
                .startTime(shiftDate.getShiftStartDate())
                .endTime(shiftDate.getShiftEndDate())
                .listAggEventByStatus(listAggEventByStatus)
                .listAggJobByStatus(listAggJobByStatus)
                .listStage(String.join(",", listStageName))
                .build();
    }

    private ShiftDate getShiftDate(Shift shift, Date date) throws ParseException {
        Date shiftStartTime;
        Date shiftEndTime;
        if (shift.getStartTimeFloat() < shift.getEndTimeFloat()) {
            shiftStartTime = getDate(shift.getStartTime(), date);
            shiftEndTime = getDate(shift.getEndTime(), date);
        } else {
            if (getTimeFloat(date) <= shift.getEndTimeFloat()) {
                shiftStartTime = getDate(shift.getStartTime(), getPrevDay(date));
                shiftEndTime = getDate(shift.getEndTime(), date);
            } else {
                shiftStartTime = getDate(shift.getStartTime(), date);
                shiftEndTime = getDate(shift.getEndTime(), getNextDay(date));

            }
        }
        return ShiftDate.builder()
                .shiftEndDate(shiftEndTime)
                .shiftStartDate(shiftStartTime)
                .build();
    }

    private Date getDate(String hourMinTime, Date day) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateInput = dateFormat.format(day) + " " + hourMinTime + ":00";
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(dateInput);
    }

    private float getTimeFloat(Date date) {
        int min = date.getMinutes();
        int hour = date.getHours();
        return hour + ((float) min) / 60f;
    }

    private static Date getPrevDay(Date date) {
        long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
        return new Date(date.getTime() - MILLIS_IN_A_DAY);
    }

    private static Date getNextDay(Date date) {
        long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
        return new Date(date.getTime() + MILLIS_IN_A_DAY);
    }

    private Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    private Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
    }

    private LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    void removeDuplicate(List<String> list) {
        List<String> deledeList = new ArrayList<>();
        Set<String> setString = new HashSet<>();
        for (String st : list) {
            if (setString.contains(st)) {
                deledeList.add(st);
            } else {
                setString.add(st);
            }
        }
        list.removeAll(deledeList);
    }

    void removeDuplicateStage(List<Stage> list) {
        List<Stage> deledeList = new ArrayList<>();
        Set<String> setString = new HashSet<>();
        for (Stage st : list) {
            if (setString.contains(st.getCode())) {
                deledeList.add(st);
            } else {
                setString.add(st.getCode());
            }
        }
        list.removeAll(deledeList);
    }

}
