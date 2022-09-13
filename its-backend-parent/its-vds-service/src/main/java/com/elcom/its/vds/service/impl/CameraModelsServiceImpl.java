/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service.impl;

import com.elcom.its.vds.model.CameraModels;
import com.elcom.its.vds.repository.CameraModelsRepository;
import com.elcom.its.vds.service.CameraModelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class CameraModelsServiceImpl implements CameraModelsService {

    @Autowired
    private CameraModelsRepository cameraModelsRepository;

    @Override
    public CameraModels getCameraModels(Long cameraModelId) {
        return cameraModelsRepository.findById(cameraModelId).orElse(null);
    }
}
