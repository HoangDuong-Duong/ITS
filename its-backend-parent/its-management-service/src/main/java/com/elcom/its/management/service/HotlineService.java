package com.elcom.its.management.service;

import com.elcom.its.management.dto.*;

import java.util.List;

public interface HotlineService {
    Response saveHotline(List<HotlineDTO> HotlineDTO);
    Response updateHotline(HotlineDTO HotlineDTO, String id);

    Response delete(List<String> ids);
    HotlineResponseDTO getListReport(String startDate, String endDate);
    HotlineReportResponseDTO getReport(String startDate, String endDate);

}
