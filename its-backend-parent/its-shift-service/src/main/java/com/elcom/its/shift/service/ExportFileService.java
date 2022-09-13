/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service;

import com.elcom.its.shift.dto.ExportShiftReportRequest;

/**
 *
 * @author Admin
 */
public interface ExportFileService {

    public void exportMonthlyShiftReport(ExportShiftReportRequest exportRequest);

    public void exportWeeklyShiftReport(ExportShiftReportRequest exportRequest);
}
