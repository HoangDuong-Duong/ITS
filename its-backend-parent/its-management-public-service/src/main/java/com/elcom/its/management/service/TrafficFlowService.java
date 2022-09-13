/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service;

import com.elcom.its.management.dto.Response;

/**
 *
 * @author Admin
 */
public interface TrafficFlowService {

    Response getDetailTrafficBySite(String startTime, String endTime, String siteId, String directionCode);
}
