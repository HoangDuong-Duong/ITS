/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.notify.service;

import com.elcom.its.notify.model.DeviceMap;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface DeviceMapService {

    List<DeviceMap> findAll();

    void saveAll(List<DeviceMap> deviceMaps);
}
