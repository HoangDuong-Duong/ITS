/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service.impl;

import com.elcom.its.vds.model.PuVideoThreads;
import com.elcom.its.vds.repository.PuVideoThreadsCustomizeRepository;
import com.elcom.its.vds.repository.PuVideoThreadsRepository;
import com.elcom.its.vds.service.PuVideoThreadsService;
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
public class PuVideoThreadsServiceImpl implements PuVideoThreadsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PuVideoThreadsServiceImpl.class);

    @Autowired
    private PuVideoThreadsRepository puVideoThreadsRepository;

    @Autowired
    private PuVideoThreadsCustomizeRepository puVideoThreadsCustomizeRepository;

    @Override
    public PuVideoThreads create(PuVideoThreads puVideoThreads) {
        puVideoThreads = puVideoThreadsRepository.save(puVideoThreads);
        return puVideoThreads;
    }

    @Override
    public Optional<PuVideoThreads> findById(Long id) {
        return puVideoThreadsRepository.findById(id);
    }

    @Override
    public PuVideoThreads update(PuVideoThreads puVideoThreads) {
        puVideoThreads = puVideoThreadsRepository.save(puVideoThreads);
        return puVideoThreads;
    }

    @Override
    public void delete(PuVideoThreads puVideoThreads) {
        puVideoThreadsRepository.delete(puVideoThreads);
    }

    @Override
    public Page<PuVideoThreads> getAllByPage(Pageable pageable) {
        return puVideoThreadsRepository.findAll(pageable);
    }

    @Override
    public List<PuVideoThreads> getAll() {
        return puVideoThreadsRepository.findAll();
    }

    @Override
    public boolean existsByProcessUnitIdAndCameraId(String idProcessUnit, String idCamera) {
        return puVideoThreadsRepository.existsByProcessUnitIdAndCameraId(idProcessUnit, idCamera);
    }

    @Override
    public List<PuVideoThreads> getListPuVideoThreadsByIdProcessUnit(String idProcessUnit) {
        List<PuVideoThreads> listPuVideoThreads = null;
        try {
            listPuVideoThreads = puVideoThreadsCustomizeRepository.findListPuVideoThreadsByIdProcessUnit(idProcessUnit);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            ex.printStackTrace();
        }
        return listPuVideoThreads;
    }

    @Override
    public List<PuVideoThreads> findByPuVideoThreadsByProcessUnitId(String proceeUnitId) {
        return puVideoThreadsRepository.findByProcessUnitId(proceeUnitId);
    }

    @Override
    public boolean existsByLayoutId(Long idLayout) {
        return puVideoThreadsRepository.existsByLayoutId(idLayout);
    }

    @Override
    public boolean existsByLayoutIdAndCameraIdAndProcessId(Long layoutId, String cameraId, String idProcess) {
        return puVideoThreadsRepository.existsByLayoutIdAndCameraIdAndProcessUnitId(layoutId, cameraId, idProcess);
    }

    @Override
    public boolean existsByCameraIdAndProcessId(String cameraId, String idProcess) {
        return puVideoThreadsRepository.existsByCameraIdAndProcessUnitId(cameraId, idProcess);
    }

}
