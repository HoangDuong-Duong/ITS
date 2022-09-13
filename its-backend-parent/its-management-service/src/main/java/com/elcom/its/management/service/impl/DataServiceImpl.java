/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service.impl;

import com.elcom.its.management.repository.DataRepository;
import com.elcom.its.management.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class DataServiceImpl implements DataService {
    
    @Autowired
    private DataRepository dataRepository;
    
    @Override
    public void addDailyPartition(String[] tables) {
        dataRepository.addDailyPartition(tables);
    }

    @Override
    public void addMonthlyPartition(String[] tables) {
        dataRepository.addMonthlyPartition(tables);
    }

    @Override
    public boolean deleteTableData(String[] tables, String [] columnTables, String oldStartTime) {
        return dataRepository.deleteTableData(tables, columnTables, oldStartTime);
    }
}
