/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service;

import com.elcom.its.vds.model.ModelProfiles;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Admin
 */
public interface ModelProfileService {

    Page<ModelProfiles> getAllByPage(Pageable pageable);

    List<ModelProfiles> getAll();

    ModelProfiles create(ModelProfiles modelProfile);

    ModelProfiles update(ModelProfiles modelProfile);

    Optional<ModelProfiles> findById(Long id);

    void delete(ModelProfiles modelProfile);

    public List<ModelProfiles> getAIModelProfiles(List<Long> profileIds);
}
