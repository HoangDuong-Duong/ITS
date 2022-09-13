package com.elcom.its.management.service;

import com.elcom.its.management.dto.*;

import java.util.List;

public interface ReportDeviceService {
    Response saveReport(List<ReportDeviceDTO> reportDeviceDTO);
    Response updateHotline(ReportDeviceDTO reportDeviceDTO, String id);

    Response delete(List<String> ids);
    DeviceResponseDTO getReport(String startDate, String endDate);

}
