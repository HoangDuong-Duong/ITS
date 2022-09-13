/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service.impl;

import com.elcom.its.vds.model.CameraLayouts;
import com.elcom.its.vds.repository.CameraLayoutsRepository;
import com.elcom.its.vds.service.CameraLayoutService;
import com.elcom.its.utils.StringUtil;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class CameraLayoutServiceImpl implements CameraLayoutService {

    @Autowired
    private CameraLayoutsRepository cameraLayoutsRepository;

    @Override
    public CameraLayouts create(CameraLayouts cameraLayouts) {
        return cameraLayoutsRepository.save(cameraLayouts);
    }

    @Override
    public Optional<CameraLayouts> findById(Long id) {
        return cameraLayoutsRepository.findById(id);
    }

    @Override
    public CameraLayouts update(CameraLayouts cameraLayouts) {
        return cameraLayoutsRepository.save(cameraLayouts);
    }

    @Override
    public void delete(CameraLayouts cameraLayouts) {
        cameraLayoutsRepository.delete(cameraLayouts);
    }

    @Override
    public Page<CameraLayouts> getAllByPage(Pageable pageable) {
        return cameraLayoutsRepository.findAll(pageable);
    }

    @Override
    public List<CameraLayouts> getAll() {
        return cameraLayoutsRepository.findAll();
    }

    @Override
    public boolean checkByName(String name) {
        return cameraLayoutsRepository.existsByName(name);
    }

    @Override
    public List<CameraLayouts> getLayoutsByCameraId(String cameraId, String search) {
        if (StringUtil.isNullOrEmpty(search)) {
            return cameraLayoutsRepository.findByCameraIdOrderByCreatedDateDesc(cameraId);
        } else {
            return cameraLayoutsRepository.findByCameraIdAndNameContainingIgnoreCaseOrderByCreatedDateDesc(cameraId, search);
        }
    }

    @Override
    public boolean checkExistByCameraId(String cameraId) {
        return cameraLayoutsRepository.existsByCameraId(cameraId);
    }

}
