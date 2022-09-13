package com.elcom.its.shift.service;

public interface DataService {
    void addDailyPartition(String[] tables);

    void addMonthlyPartition(String[] tables);

    boolean deleteTableData(String[] tables, String[] columnTables, String oldStartTime);
}
