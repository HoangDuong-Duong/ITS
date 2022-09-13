/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service.impl;

import com.elcom.its.config.model.BaseModels;
import com.elcom.its.config.repository.BaseModelsRepository;
import com.elcom.its.config.service.BaseModelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class BaseModelsServiceImpl implements BaseModelsService {

    @Autowired
    private BaseModelsRepository baseModelsRepository;

    @Override
    public BaseModels getbaseModels(Long baseModelId) {
        return baseModelsRepository.findById(baseModelId).orElse(null);
    }
}
