/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service;

import com.elcom.its.shift.model.LoginHistory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
@Service
public interface LoginHistoryService {

    void save(LoginHistory loginHistory);

    LoginHistory findLastLoginHistory(String userId);

    List<LoginHistory> getLoginHistoryByUser(String userId, Date startDate, Date endDate);

}
