package com.elcom.its.shift.service.impl;

import com.elcom.its.shift.repository.DataRepository;
import com.elcom.its.shift.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataServiceImpl implements DataService {
    @Autowired
    private DataRepository dataRepository;

    public DataServiceImpl() {
    }

    public void addDailyPartition(String[] tables) {
        this.dataRepository.addDailyPartition(tables);
    }

    public void addMonthlyPartition(String[] tables) {
        this.dataRepository.addMonthlyPartition(tables);
    }

    public boolean deleteTableData(String[] tables, String[] columnTables, String oldStartTime) {
        return this.dataRepository.deleteTableData(tables, columnTables, oldStartTime);
    }
}
