/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service;

import com.elcom.its.management.dto.UpdateFmsHistoryRequest;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface FmsService {

    public void updateFmsHistory(List<UpdateFmsHistoryRequest> listUpdateFmsHistoryRequests);
}
