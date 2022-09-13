/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.notify.service.impl;

import com.elcom.its.notify.model.DeviceMap;
import com.elcom.its.notify.repository.DeviceMapRepository;
import com.elcom.its.notify.service.DeviceMapService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class DeviceMapServiceImpl implements DeviceMapService {

    @Autowired
    private DeviceMapRepository deviceMapRepository;

    @Override
    public List<DeviceMap> findAll() {
        return deviceMapRepository.findAll();
    }

    @Override
    public void saveAll(List<DeviceMap> deviceMaps) {
        deviceMapRepository.saveAll(deviceMaps);
    }
}
