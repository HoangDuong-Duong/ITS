/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service;

import com.elcom.its.vds.model.Vds;
import java.util.List;
import com.elcom.its.vds.model.dto.Response;
import com.elcom.its.vds.model.dto.SiteVdsDTO;
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

    List<String> findAllCameraIds();

    List<String> findAllSiteIds();

    void remove(Vds vds);

    boolean updateStatus(String id, Integer status, String modifiedBy);

    List<Vds> findByProcessUnitId(String idProcessUnit);

    List<String> findProcessUnitIdByLayoutId(Long layoutId);

    Response getTrafficFlowBySite(String urlParam);
    Response getTrafficFlowBySiteReport(String urlParam);

    List<Vds> getListVdsByCamera(String cameraId);

    boolean updateCameraStatus(String cameraId, Integer status);

    void updateStatus(List<String> idList, Integer status, String modifiedBy);

    List<String> findProcessUnitIdByVdsIdList(List<String> idList);

    List<SiteVdsDTO> getSiteList();
    
    boolean existsByLayoutTypeAndCameraId(Integer layoutType, String cameraId);
    
    Page<Vds> findVds(List<String> siteIdList, String cameraId, String processUnitId, String search, Integer page, Integer size);
    
    void updateRenderStatus(List<String> idList, Integer status, String modifiedBy);
}
