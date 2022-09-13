package com.elcom.its.vmsboard.service;

import com.elcom.its.vmsboard.model.HistoryDisplayNews;

public interface HistoryDisplayScriptService {
    HistoryDisplayNews createHistoryReportFile(String startDate, String endDate, String uuid);
}
