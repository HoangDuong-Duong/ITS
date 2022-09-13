/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service;

import com.elcom.its.vds.model.PuVideoThreads;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Admin
 */
public interface PuVideoThreadsService {

    public List<PuVideoThreads> findByPuVideoThreadsByProcessUnitId(String proceeUnitId);

    Page<PuVideoThreads> getAllByPage(Pageable pageable);

    List<PuVideoThreads> getAll();

    PuVideoThreads create(PuVideoThreads puVideoThreads);

    PuVideoThreads update(PuVideoThreads puVideoThreads);

    Optional<PuVideoThreads> findById(Long id);

    void delete(PuVideoThreads puVideoThreads);

    boolean existsByProcessUnitIdAndCameraId(String idProcessUnit, String idCamera);

    List<PuVideoThreads> getListPuVideoThreadsByIdProcessUnit(String idProcessUnit);

    boolean existsByLayoutId(Long idLayout);

    boolean existsByLayoutIdAndCameraIdAndProcessId(Long layoutId, String cameraId, String idProcess);

    boolean existsByCameraIdAndProcessId(String cameraId, String idProcess);
}
