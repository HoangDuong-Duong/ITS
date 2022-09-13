/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.schedule;

import com.elcom.its.shift.controller.BaseController;
import com.elcom.its.shift.dto.UserDTO;
import com.elcom.its.shift.model.DailyLoginReport;
import com.elcom.its.shift.model.LoginHistory;
import com.elcom.its.shift.model.UserShift;
import com.elcom.its.shift.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class DailyReportSchedule {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DailyReportSchedule.class);

    @Value("${schedule.enabled:true}")
    private boolean isEnabled;

    @Autowired
    private DataService dataService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private DailyLoginReportService dailyLoginReportService;
    
    @Autowired
    private LoginHistoryService loginHistoryService;
    
    @Autowired
    private UserShiftService userShiftService;

    @Scheduled(cron = "0 0 0 01 * * ")
    public void addDataPartition() throws InterruptedException {
        if (isEnabled) {
            String[] tables = {"login_history"};
            dataService.addMonthlyPartition(tables);
        }
    }
    
    @Scheduled(fixedDelayString = "60000")
    public void createDailyLoginReport() throws InterruptedException, JsonProcessingException {
        LOGGER.info("Run schedule");
        List<UserDTO> listAllUser = userService.getAllUser();
        Date now = new Date();
        Date startOfDay = getStartOfDay(now);
        List<String> userIds = new ArrayList<>();
        for (UserDTO user : listAllUser) {
            userIds.add(user.getUuid());
            List<LoginHistory> listLoginHistoryByUser
                    = loginHistoryService.getLoginHistoryByUser(user.getUuid(), startOfDay, now);
            DailyLoginReport dailyLoginReportByUser = dailyLoginReportService.findByUserIdAndByDay(user.getUuid(), startOfDay);
            if (dailyLoginReportByUser != null) {
                updateDailyLoginReport(listLoginHistoryByUser, dailyLoginReportByUser, user, startOfDay);
                dailyLoginReportService.save(dailyLoginReportByUser);
            } else {
                dailyLoginReportByUser = createDailyLoginReport(listLoginHistoryByUser, user, startOfDay);
                dailyLoginReportService.save(dailyLoginReportByUser);
            }
        }
        dailyLoginReportService.deleteByDayAndUserIdNotIn(startOfDay, userIds);
    }
    
    private DailyLoginReport createDailyLoginReport(List<LoginHistory> listLoginHistory, UserDTO userDTO, Date day) {
        DailyLoginReport dailyLoginReport = new DailyLoginReport(UUID.randomUUID().toString());
        setUserData(dailyLoginReport, userDTO, day);
        updateDailyLoginReport(listLoginHistory, dailyLoginReport, userDTO, day);
        return dailyLoginReport;
    }
    
    private void updateDailyLoginReport(List<LoginHistory> listLoginHistory, DailyLoginReport dailyLoginReport, UserDTO userDTO, Date day) {
        setUserShiftData(dailyLoginReport, userDTO, day);
        dailyLoginReport.setUserFullName(userDTO.getFullName());
        setLoginHistoryData(dailyLoginReport, listLoginHistory);
    }
    
    private void setUserShiftData(DailyLoginReport dailyLoginReport, UserDTO userDTO, Date day) {
        UserShift userShift = userShiftService.findByUserIdAndDay(userDTO.getUuid(), day);
        dailyLoginReport.setShift(userShift != null ? userShift.getViewNumsOfShift() : null);
    }
    
    private void setUserData(DailyLoginReport dailyLoginReport, UserDTO userDTO, Date day) {
        dailyLoginReport.setDay(day);
        dailyLoginReport.setUserId(userDTO.getUuid());
        dailyLoginReport.setUserFullName(userDTO.getFullName());
        dailyLoginReport.setUsername(userDTO.getUserName());
    }
    
    private void setLoginHistoryData(DailyLoginReport dailyLoginReport, List<LoginHistory> listLoginHistory) {
        if (listLoginHistory != null && !listLoginHistory.isEmpty()) {
            List<String> listLoginHistoryString = listLoginHistory.stream()
                    .map(history -> history.getHistoryView()).collect(Collectors.toList());
            dailyLoginReport.setLoginHistory(String.join(", ", listLoginHistoryString));
            long totalTimeLogin = 0;
            for (LoginHistory loginHistory : listLoginHistory) {
                totalTimeLogin += loginHistory.getTotalTimeLogin();
            }
            dailyLoginReport.setTotalTime(totalTimeLogin);
            dailyLoginReport.setUsername(listLoginHistory.get(0).getUsername());
        }
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


}
