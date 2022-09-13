/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vmsboard.service;

import com.elcom.its.vmsboard.dto.Response;
import com.elcom.its.vmsboard.model.HistoryDisplayNews;
import com.elcom.its.vmsboard.model.ListUuid;
import com.elcom.its.vmsboard.model.VmsBoard;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface VmsBoardService {
//    Response getAllVmsBoard();
//    Response getAllVmsBoard(Integer page, Integer size, String search);
    Response getAllVmsBoard(String name, String siteId, String typeBoard, Integer status,boolean isAdmin, String stages);
    Response getDisplayScript(Integer page, Integer size, String keyword, String deviceId, String fromDate, String toDate) throws UnsupportedEncodingException;
    Response getVmsBoardById(String id);
    Response saveVmsBoard(VmsBoard vmsBoard);
    Response updateVmsBoard(VmsBoard vmsBoard);
    Response deleteVmsBoard(String vms);
    Response multiDelete(ListUuid ids);
    Response getAllVmsByGroup(ListUuid ids);
    Response getCurrentDisplay(String urlParam);
    Response getVmsBoardByDirection(String direction);
    Response getHistoryDisplayScript(String startTime, String endTime, String vmsId);
}
