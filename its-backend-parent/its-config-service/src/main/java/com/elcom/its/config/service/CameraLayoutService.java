/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service;

import com.elcom.its.config.model.CameraLayouts;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Admin
 */
public interface CameraLayoutService {

    Page<CameraLayouts> getAllByPage(Pageable pageable);

    List<CameraLayouts> getAll();

    CameraLayouts create(CameraLayouts cameraLayouts);

    CameraLayouts update(CameraLayouts cameraLayouts);

    Optional<CameraLayouts> findById(Long id);

    void delete(CameraLayouts cameraLayouts);

    public boolean checkByName(String name);

    List<CameraLayouts> getLayoutsByCameraId(String cameraId, String search);

    boolean checkExistByCameraId(String cameraId);
}
