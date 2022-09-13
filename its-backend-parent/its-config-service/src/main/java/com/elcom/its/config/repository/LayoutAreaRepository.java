/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.repository;

import com.elcom.its.config.model.LayoutAreas;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface LayoutAreaRepository extends JpaRepository<LayoutAreas, Long> {

    List<LayoutAreas> findByLayoutIdOrderByIdDesc(Long layoutId);

    LayoutAreas findTopByOrderByIdDesc();

    @Query("SELECT l FROM CameraLayouts c INNER JOIN LayoutAreas l ON c.id = l.layoutId WHERE c.cameraId = :cameraId and l.roiType = :roiType ORDER BY l.id DESC")
    List<LayoutAreas> filterLayoutAreasList(@Param("cameraId") String cameraId, @Param("roiType") long roiType);
}
