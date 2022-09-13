/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service.impl;

import com.elcom.its.shift.model.LoginHistory;
import com.elcom.its.shift.repository.LoginHistoryRepository;
import com.elcom.its.shift.service.LoginHistoryService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class LoginHistoryServiceImpl implements LoginHistoryService {

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    @Override
    public void save(LoginHistory loginHistory) {
        loginHistoryRepository.save(loginHistory);
    }

    @Override
    public LoginHistory findLastLoginHistory(String userId) {
        return loginHistoryRepository.findFirstByUserIdOrderByLoginHistoryPK_LoginTimeDesc(userId);
    }

    @Override
    public List<LoginHistory> getLoginHistoryByUser(String userId, Date startDate, Date endDate) {
        return loginHistoryRepository.findByUserIdAndLoginHistoryPK_LoginTimeBetween(userId, startDate, endDate);
    }

}
