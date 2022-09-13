/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service;

import com.elcom.its.config.model.Vds;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 *
 * @author Admin
 */
public interface VdsService {

    Vds save(Vds vds);
    
    boolean existsByLayoutTypeAndCameraIdAndProcessUnitId(Integer layoutType, String cameraId, String processUnitId);
    
    Page<Vds> findVds(String siteId, String cameraId, String processUnitId, String search, Integer page, Integer size);
    
    Vds findById(String id);
    
    void remove(Vds vds);
    
    boolean updateStatus(String id, Integer status, String modifiedBy);
    
    List<Vds> findByProcessUnitId(String idProcessUnit);
    
    List<String> findProcessUnitIdByLayoutId(Long layoutId);
    
    boolean updateCameraStatus(String cameraId, Integer status);
    
    boolean updateVdsSiteId(String cameraId, String siteId, String siteName);
}
