/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.repository;

import com.elcom.its.config.model.CameraLayouts;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface CameraLayoutsRepository extends JpaRepository<CameraLayouts, Long> {

    boolean existsByName(String name);

    List<CameraLayouts> findByCameraIdOrderByCreatedDateDesc(String cameraId);

    List<CameraLayouts> findByCameraIdAndNameContainingIgnoreCaseOrderByCreatedDateDesc(String cameraId, String search);

    boolean existsByCameraId(String cameraId);
}
