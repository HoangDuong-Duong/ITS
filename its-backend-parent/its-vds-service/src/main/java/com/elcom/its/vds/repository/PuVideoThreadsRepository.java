/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.repository;

import com.elcom.its.vds.model.PuVideoThreads;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface PuVideoThreadsRepository extends JpaRepository<PuVideoThreads, Long> {

    boolean existsByProcessUnitIdAndCameraId(String processUnitId, String cameraId);

    List<PuVideoThreads> findByProcessUnitId(String processUnitId);

    boolean existsByLayoutId(Long layoutId);

    boolean existsByLayoutIdAndCameraIdAndProcessUnitId(Long layoutId, String cameraId, String idProcess);

    boolean existsByCameraIdAndProcessUnitId(String cameraId, String idProcess);
}
