/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service.impl;

import com.elcom.its.shift.dto.GetUserShiftResponse;
import com.elcom.its.shift.dto.UserShiftGroupByStage;
import com.elcom.its.shift.dto.UserShiftGroupByUser;
import com.elcom.its.shift.model.UserShift;
import com.elcom.its.shift.model.Shift;
import com.elcom.its.shift.repository.ShiftRepository;
import com.elcom.its.shift.repository.UserShiftRepository;
import com.elcom.its.shift.service.StageService;
import com.elcom.its.shift.service.UserShiftService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.elcom.its.shift.dto.NumberOfShift;
import com.elcom.its.shift.dto.NumberOfShiftByDay;
import com.elcom.its.shift.dto.UserOnShiftByStage;
import com.elcom.its.shift.dto.WeeklyReportGroupByShift;
import com.elcom.its.shift.dto.WeeklyShiftReportContent;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Admin
 */
@Service
@Transactional
public class UserShiftServiceImpl implements UserShiftService {

    @Autowired
    private UserShiftRepository userShiftRepository;

    @Autowired
    private StageService stageService;

    @Autowired
    private ShiftRepository shiftRepository;

    @Override
    public UserShift save(UserShift userShift) {
        return userShiftRepository.save(userShift);
    }

    @Override
    public void save(List<UserShift> listUserShifts) {
        userShiftRepository.saveAll(listUserShifts);
    }

    @Override
    public void deleteByGroupCodeAndDayBetween(String groupCode, Date startDate, Date endDate) {
        userShiftRepository.deleteByGroupCodeAndDayBetween(groupCode, startDate, endDate);
    }

    @Override
    public List<UserShiftGroupByStage> getListUserShiftGroupByStages(Date startDate, Date endDate, String groupCode) {
        String month = getMonth(startDate);
        List<UserShift> listUserShifts = userShiftRepository.findByGroupCodeAndDayBetween(groupCode, startDate, endDate);
        return transformToUserShiftGroupByStage(listUserShifts, month);
    }

    List<UserShiftGroupByStage> transformToUserShiftGroupByStage(List<UserShift> listUserShifts, String month) {
        List<UserShiftGroupByStage> returnList = new ArrayList<>();
        Map<String, List<UserShift>> mapUserShiftsGroupByStage = listUserShifts.stream().collect(Collectors.groupingBy(UserShift::getStageCode));
        for (Map.Entry<String, List<UserShift>> entry : mapUserShiftsGroupByStage.entrySet()) {
            Map<String, List<UserShift>> mapUserShiftsGroupByUser = entry.getValue().stream().collect(Collectors.groupingBy(UserShift::getUserId));
            List<UserShiftGroupByUser> listUserShiftGroupByUser = new ArrayList<>();
            for (Map.Entry<String, List<UserShift>> entryUser : mapUserShiftsGroupByUser.entrySet()) {
                UserShiftGroupByUser userShiftGroupByUser = UserShiftGroupByUser.builder()
                        .userId(entryUser.getKey())
                        .username(entryUser.getValue().get(0).getUsername())
                        .listUserShift(entryUser.getValue())
                        .listNumberOfShiftByUsers(createNumberOfShiftByUsers(entryUser.getValue(), month))
                        .build();
                listUserShiftGroupByUser.add(userShiftGroupByUser);
            }
            UserShiftGroupByStage userShiftGroupByStage = UserShiftGroupByStage.builder()
                    .stageCode(entry.getKey())
                    .listUserShiftGroupByUser(listUserShiftGroupByUser)
                    .build();
            returnList.add(userShiftGroupByStage);

        }

        return returnList;
    }

    private List<NumberOfShift> createNumberOfShiftByUsers(List<UserShift> listUserShifts, String month) {
        List<Shift> listAllShifts = shiftRepository.findByMonthOrNumber(month, 0);
        Map<Integer, Set<Date>> mapNumberOfShift = new HashMap<>();
        for (UserShift userShift : listUserShifts) {
            for (Shift shift : listAllShifts) {
                if (userShift.getNumberOfShift().contains("" + shift.getNumber())) {
                    if (mapNumberOfShift.containsKey(shift.getNumber())) {
                        mapNumberOfShift.get(shift.getNumber()).add(userShift.getDay());
                    } else {
                        Set<Date> setDate = new HashSet<>();
                        setDate.add(userShift.getDay());
                        mapNumberOfShift.put(shift.getNumber(), setDate);
                    }
                }
            }
        }
        List<NumberOfShift> returnList = new ArrayList<>();
        for (Map.Entry<Integer, Set<Date>> entry : mapNumberOfShift.entrySet()) {
            returnList.add(NumberOfShift.builder()
                    .shiftNumber(entry.getKey())
                    .total(entry.getValue().size())
                    .build()
            );

        }
        return returnList;
    }

    @Override
    public List<String> getListUserIdOnShift(Date date, String siteId) {
        List<String> stageCodes = stageService.getListStageCodeByListSite(siteId.split(","));
        Shift shift = getShift(date);
        if (shift == null) {
            return new ArrayList<>();
        }
        Date paramDate = getStartOfDay(date);
        List<UserShift> listUserShifts = userShiftRepository.findByStageCodeInAndDayAndNumberOfShiftContainingIgnoreCase(stageCodes, paramDate, shift.getNumber() + "");
        if (listUserShifts == null || listUserShifts.isEmpty()) {
            return new ArrayList<>();
        }
        return listUserShifts.stream().map(u -> u.getUserId()).collect(Collectors.toList());
    }

    private Shift getShift(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyy");
        int hour = date.getHours();
        int minute = date.getMinutes();
        float floatTime = hour + ((float) minute) / 60.0f;
        return shiftRepository.findShift(floatTime, simpleDateFormat.format(date));
    }

    private Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    private Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    private LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    private Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
    }

    @Override
    public List<UserShiftGroupByUser> transformToListUserShiftGroupByUser(List<UserShift> listUserShifts, String month) {
        Map<String, List<UserShift>> mapUserShiftsGroupByUser = listUserShifts.stream().collect(Collectors.groupingBy(UserShift::getUserId));
        List<UserShiftGroupByUser> listUserShiftGroupByUser = new ArrayList<>();

        for (Map.Entry<String, List<UserShift>> entry : mapUserShiftsGroupByUser.entrySet()) {
            UserShiftGroupByUser userShiftGroupByUser = UserShiftGroupByUser.builder()
                    .userId(entry.getKey())
                    .username(entry.getValue().get(0).getUsername())
                    .listUserShift(createUserShiftDataForUser(entry.getValue()))
                    .listNumberOfShiftByUsers(createNumberOfShiftByUsers(entry.getValue(), month))
                    .build();
            listUserShiftGroupByUser.add(userShiftGroupByUser);
        }
        return listUserShiftGroupByUser;
    }

    private List<UserShift> createUserShiftDataForUser(List<UserShift> listUserShifts) {
        Map<Date, List<UserShift>> mapUserShiftsGroupByUser = listUserShifts.stream().collect(Collectors.groupingBy(UserShift::getDay));
        List<UserShift> returnList = new ArrayList<>();
        for (Map.Entry<Date, List<UserShift>> entry : mapUserShiftsGroupByUser.entrySet()) {
            UserShift userShift = new UserShift();
            userShift.setId(UUID.randomUUID().toString());
            userShift.setDay(entry.getValue().get(0).getDay());
            userShift.setUserId(entry.getValue().get(0).getUserId());
            userShift.setUsername(entry.getValue().get(0).getUsername());
            userShift.setNumberOfShift(connectNumsOfShift(entry.getValue()));
            userShift.setStageCode(entry.getValue().get(0).getStageCode());
            userShift.setGroupCode(entry.getValue().get(0).getGroupCode());
            returnList.add(userShift);
        }
        return returnList;
    }

    private String connectNumsOfShift(List<UserShift> listUserShifts) {
        Set<String> totalNumsOfShift = new HashSet<>();
        for (UserShift userShift : listUserShifts) {
            String[] ShiftStringArr = userShift.getNumberOfShift().split("");
            totalNumsOfShift.addAll(Arrays.asList(ShiftStringArr));
        }
        return String.join("", totalNumsOfShift);
    }

    @Override
    public List<NumberOfShiftByDay> getNumberOfShiftByDay(List<UserShift> listUserShifts, String month) {
        Map<Date, List<UserShift>> userShiftsGroupByDay = listUserShifts.stream().collect(Collectors.groupingBy(UserShift::getDay));
        List<NumberOfShiftByDay> returnList = new ArrayList<>();
        for (Map.Entry<Date, List<UserShift>> entry : userShiftsGroupByDay.entrySet()) {
            List<Shift> listAllShift = shiftRepository.findByMonthOrNumber(month, 0);
            List<NumberOfShift> listNumberOfShifts = new ArrayList<>();
            for (Shift shift : listAllShift) {
                Set<String> setUserIds = new HashSet<>();
                int numberOfUser = 0;
                for (UserShift userShift : entry.getValue()) {
                    if (userShift.getNumberOfShift().contains(shift.getNumber() + "") && !setUserIds.contains(userShift.getUserId())) {
                        numberOfUser++;
                        setUserIds.add(userShift.getUserId());
                    }
                }
                listNumberOfShifts.add(
                        NumberOfShift.builder()
                                .shiftNumber(shift.getNumber())
                                .total(numberOfUser)
                                .build()
                );
            }
            returnList.add(
                    NumberOfShiftByDay.builder()
                            .day(entry.getKey())
                            .listNumberOfShifts(listNumberOfShifts)
                            .build()
            );
        }
        Collections.sort(returnList);
        return returnList;
    }

    @Override
    public GetUserShiftResponse getUserShiftResponse(Date startDate, Date endDate, String groupCode) {
        List<UserShift> listUserShifts = userShiftRepository.findByGroupCodeAndDayBetween(groupCode, startDate, endDate);
        String month = getMonth(startDate);
        List<UserShiftGroupByStage> listUserShiftGroupByStages = transformToUserShiftGroupByStage(listUserShifts, month);
        List<NumberOfShiftByDay> listNumberOfShiftByDays = getNumberOfShiftByDay(listUserShifts, month);

        return GetUserShiftResponse.builder()
                .numberOfShiftByDay(listNumberOfShiftByDays)
                .userShiftGroupByStage(listUserShiftGroupByStages)
                .build();
    }

    @Override
    public List<UserShift> findByGroupCodeAndDayBetween(Date startDate, Date endDate, String groupCode) {
        return userShiftRepository.findByGroupCodeAndDayBetween(groupCode, startDate, endDate);
    }

    @Override
    public List<WeeklyShiftReportContent> getWeeklyShiftReportContents(Date startDate, Date endDate, String groupCode) {
        List<WeeklyShiftReportContent> returnList = new ArrayList<>();
        List<UserShift> listUserShifts = userShiftRepository.findByGroupCodeAndDayBetween(groupCode, startDate, endDate);
        Map<Date, List<UserShift>> mapUserShiftsGroupByDate = listUserShifts.stream().collect(Collectors.groupingBy(UserShift::getDay));
        for (Map.Entry<Date, List<UserShift>> entry : mapUserShiftsGroupByDate.entrySet()) {
            Map<Integer, List<UserShift>> mapShiftUser = new HashMap<>();
            for (UserShift userShift : entry.getValue()) {
                for (int i = 0; i < userShift.getNumberOfShift().length(); i++) {
                    int shiftKey = Integer.parseInt(userShift.getNumberOfShift().charAt(i) + "");
                    if (mapShiftUser.containsKey(shiftKey)) {
                        mapShiftUser.get(shiftKey).add(userShift);
                    } else {
                        List<UserShift> listUserByShift = new ArrayList<>(Arrays.asList(userShift));
                        mapShiftUser.put(shiftKey, listUserByShift);
                    }
                }
            }
            List<WeeklyReportGroupByShift> listWeeklyReportGroupByShifts = new ArrayList<>();
            for (Map.Entry<Integer, List<UserShift>> shiftUserEntry : mapShiftUser.entrySet()) {
                List<UserOnShiftByStage> listUserOnShiftByStages = new ArrayList<>();
                Map<String, List<UserShift>> mapUserShiftsGroupByStage = shiftUserEntry.getValue().stream().collect(Collectors.groupingBy(UserShift::getStageCode));
                for (Map.Entry<String, List<UserShift>> userStageEntry : mapUserShiftsGroupByStage.entrySet()) {
                    List<String> listUser = userStageEntry.getValue().stream().map(us -> us.getUsername()).collect(Collectors.toList());
                    listUserOnShiftByStages.add(
                            UserOnShiftByStage.builder()
                                    .stageCode(userStageEntry.getKey())
                                    .listUser(String.join(",", listUser))
                                    .build()
                    );
                }
                listWeeklyReportGroupByShifts.add(
                        WeeklyReportGroupByShift.builder()
                                .shiftNumber(shiftUserEntry.getKey())
                                .listUserOnShiftByStages(listUserOnShiftByStages)
                                .build()
                );
            }
            returnList.add(WeeklyShiftReportContent.builder()
                    .day(entry.getKey())
                    .listWeeklyReportGroupByShifts(listWeeklyReportGroupByShifts)
                    .build()
            );
        }
        return returnList;
    }

    private String getMonth(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-yyyy");
        return simpleDateFormat.format(date);
    }

    @Override
    public UserShift findByUserIdAndDay(String userId, Date day) {
        return userShiftRepository.findFirstByUserIdAndDay(userId, day);
    }

    @Override
    public List<UserShift> findByUserIdAndDayAndShift(String userId, Date day, String shift) {
        return userShiftRepository.findByUserIdAndDayAndNumberOfShiftContainingIgnoreCase(userId, day, shift);
    }
}
