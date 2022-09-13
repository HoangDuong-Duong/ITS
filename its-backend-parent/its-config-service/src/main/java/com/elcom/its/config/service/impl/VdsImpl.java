/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service.impl;

import com.elcom.its.config.model.Vds;
import com.elcom.its.config.model.VdsSpec;
import com.elcom.its.config.repository.VdsRepository;
import com.elcom.its.config.service.VdsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class VdsImpl implements VdsService {

    @Autowired
    private VdsRepository vdsRepository;

    @Override
    public Vds save(Vds vds) {
        return vdsRepository.save(vds);
    }

    @Override
    public boolean existsByLayoutTypeAndCameraIdAndProcessUnitId(Integer layoutType,
            String cameraId, String processUnitId) {
        return vdsRepository.existsByLayoutTypeAndCameraIdAndProcessUnitId(layoutType, cameraId, processUnitId);
    }

    @Override
    public Page<Vds> findVds(String siteId, String cameraId, String processUnitId,
            String search, Integer page, Integer size) {
        if (page != null && page > 0) {
            page--;
        } else {
            page = 0;
        }
        if (size == null) {
            size = 20;
        }
        String sort = "createdDate";
        Pageable paging = PageRequest.of(page, size, Sort.by(sort).descending());
        return vdsRepository.findAll(Specification.where(VdsSpec.findVds(siteId, cameraId,
                processUnitId, search)), paging);
    }

    @Override
    public Vds findById(String id) {
        return vdsRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(Vds vds) {
        vdsRepository.delete(vds);
    }

    @Override
    public boolean updateStatus(String id, Integer status, String modifiedBy) {
        return vdsRepository.updateStatus(id, status, modifiedBy) > 0;
    }

    @Override
    public List<Vds> findByProcessUnitId(String idProcessUnit) {
        return vdsRepository.findByProcessUnitIdAndStatus(idProcessUnit, 1);
    }

    @Override
    public List<String> findProcessUnitIdByLayoutId(Long layoutId) {
        return vdsRepository.findAllProcessUnitIdByLayoutId(layoutId);
    }

    @Override
    public boolean updateCameraStatus(String cameraId, Integer status) {
        return vdsRepository.updateCameraStatus(status, cameraId) > 0;
    }

    @Override
    public boolean updateVdsSiteId(String cameraId, String siteId, String siteName) {
        return vdsRepository.updateVdsSiteId(cameraId, siteId,siteName) > 0;
    }
}
