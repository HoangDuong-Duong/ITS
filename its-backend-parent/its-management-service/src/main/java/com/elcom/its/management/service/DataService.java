/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service;

/**
 *
 * @author Admin
 */
public interface DataService {

    void addDailyPartition(String[] tables);
    
    void addMonthlyPartition(String[] tables);
    
    boolean deleteTableData(String[] tables, String [] columnTables, String oldStartTime);
}
