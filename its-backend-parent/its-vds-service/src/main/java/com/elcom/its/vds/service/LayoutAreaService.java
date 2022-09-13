/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service;

import com.elcom.its.vds.model.LayoutAreas;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Admin
 */
public interface LayoutAreaService {

    Page<LayoutAreas> getAllByPage(Pageable pageable);

    List<LayoutAreas> getAll();

    LayoutAreas create(LayoutAreas layoutAreas);

    LayoutAreas update(LayoutAreas layoutAreas);

    Optional<LayoutAreas> findById(Long id);

    void delete(LayoutAreas layoutAreas);

    List<LayoutAreas> findByCameraLayoutId(Long id);

    LayoutAreas findTop1ByOrderByIdDesc();
    
    List<LayoutAreas> getLayoutAreasList(String cameraId, long roiType);

    List<LayoutAreas> getListByLayoutId(Long id);
}
