/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service.impl;

import com.elcom.its.vds.model.LayoutAreas;
import com.elcom.its.vds.repository.LayoutAreaRepository;
import com.elcom.its.vds.service.LayoutAreaService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class LayoutAreaServiceImpl implements LayoutAreaService {

    @Autowired
    private LayoutAreaRepository layoutAreaRepository;

    @Override
    public LayoutAreas create(LayoutAreas cameraLayouts) {
        cameraLayouts = layoutAreaRepository.save(cameraLayouts);
        return cameraLayouts;
    }

    @Override
    public Optional<LayoutAreas> findById(Long id) {
        return layoutAreaRepository.findById(id);
    }

    @Override
    public LayoutAreas update(LayoutAreas cameraLayouts) {
        cameraLayouts = layoutAreaRepository.save(cameraLayouts);
        return cameraLayouts;
    }

    @Override
    public void delete(LayoutAreas cameraLayouts) {
        layoutAreaRepository.delete(cameraLayouts);
    }

    @Override
    public Page<LayoutAreas> getAllByPage(Pageable pageable) {
        return layoutAreaRepository.findAll(pageable);
    }

    @Override
    public List<LayoutAreas> getAll() {
        return layoutAreaRepository.findAll();
    }

    @Override
    public List<LayoutAreas> findByCameraLayoutId(Long layoutId) {
        return layoutAreaRepository.findByLayoutIdOrderByIdDesc(layoutId);
    }

    @Override
    public LayoutAreas findTop1ByOrderByIdDesc() {
        return layoutAreaRepository.findTopByOrderByIdDesc();
    }

    @Override
    public List<LayoutAreas> getLayoutAreasList(String cameraId, long roiType) {
        return layoutAreaRepository.filterLayoutAreasList(cameraId, roiType);
    }

    @Override
    public List<LayoutAreas> getListByLayoutId(Long id) {
        List<LayoutAreas> layoutAreasList = new ArrayList<>();
        List<LayoutAreas> listAllLayoutAreas = layoutAreaRepository.findByLayoutId(id);

        listAllLayoutAreas.parallelStream()
                .collect(Collectors.groupingBy(LayoutAreas::getRoiType))
                .forEach((code, values) -> {
                    layoutAreasList.add(values.get(0));
                });
        return layoutAreasList;
    }
}
