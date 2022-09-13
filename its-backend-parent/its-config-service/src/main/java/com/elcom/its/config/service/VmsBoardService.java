package com.elcom.its.config.service;

import com.elcom.its.config.model.*;
import com.elcom.its.config.model.dto.Response;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface VmsBoardService {
    Response getAllVmsBoard();
    Response getAllVmsBoard(Integer page, Integer size, String search);
    Response getDisplayScript(Integer page, Integer size, String keyword, String deviceId, String fromDate, String toDate) throws UnsupportedEncodingException;
    Response getVmsBoardById(String id);
    Response saveVmsBoard(VmsBoard vmsBoard);
    Response updateVmsBoard(VmsBoard vmsBoard);
    Response deleteVmsBoard(String vms);
    Response multiDelete(ListUuid ids);
}
