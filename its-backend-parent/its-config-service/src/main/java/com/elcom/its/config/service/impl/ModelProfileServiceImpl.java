/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service.impl;

import com.elcom.its.config.model.ModelProfiles;
import com.elcom.its.config.repository.ModelProfilesRepository;
import com.elcom.its.config.service.ModelProfileService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class ModelProfileServiceImpl implements ModelProfileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelProfileServiceImpl.class);
    
    @Autowired
    private ModelProfilesRepository modelProfileRepository;

    @Override
    public ModelProfiles create(ModelProfiles modelProfile) {
        modelProfile = modelProfileRepository.save(modelProfile);
        return modelProfile;
    }

    @Override
    public Optional<ModelProfiles> findById(Long id) {
        return modelProfileRepository.findById(id);
    }

    @Override
    public ModelProfiles update(ModelProfiles modelProfile) {
        modelProfile = modelProfileRepository.save(modelProfile);
        return modelProfile;
    }

    @Override
    public void delete(ModelProfiles modelProfile) {
        modelProfileRepository.delete(modelProfile);
    }

    @Override
    public Page<ModelProfiles> getAllByPage(Pageable pageable) {
        return modelProfileRepository.findAll(pageable);
    }

    @Override
    public List<ModelProfiles> getAll() {
        return modelProfileRepository.findAll();
    }

    @Override
    public List<ModelProfiles> getAIModelProfiles(List<Long> profileIds) {
        List<ModelProfiles> listModelProfiles = null;
        try {
            listModelProfiles = modelProfileRepository.findByIdIn(profileIds);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
        return listModelProfiles;
    }

}
