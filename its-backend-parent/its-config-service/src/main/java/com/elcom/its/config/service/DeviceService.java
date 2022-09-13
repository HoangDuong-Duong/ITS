/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service;

import com.elcom.its.config.model.dto.Response;

/**
 *
 * @author Admin
 */
public interface DeviceService {

    public Response getSystemDevicesInStages(String stageIds);

    public Response getAllSystemDevices();
}
