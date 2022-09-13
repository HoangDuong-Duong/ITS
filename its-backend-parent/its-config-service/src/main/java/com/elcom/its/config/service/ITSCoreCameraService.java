/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.config.service;

import com.elcom.its.config.model.CameraDTO;
import com.elcom.its.config.model.dto.*;

import java.util.List;
import java.util.Set;

/**
 *
 * @author Admin
 */
public interface ITSCoreCameraService {

    List<CameraDTO> getAll();

    CameraPaginationDTO getAll(Integer page, Integer size, String search);

    CameraPaginationDTO getAll(String stages, Integer page, Integer size, String search);

    CameraResponseList getCamerasListFromDBM(String urlParam);

    CategoryResponseList getCameraTypeFromDBM();

    CameraDetailDTOMessage getCamerasByIdFromDBM(String id);

    CameraDetailDTOMessage createCamera(CameraCreateUpdateDTO cameraDTO);

    CameraDetailDTOMessage updateCamera(CameraCreateUpdateDTO cameraDTO, String id);

    Response getCameraBySiteIds(List <String> siteIds);

    Response getCameraByStageIds(List<String> stageIds);

    CamerasResponse updateImageUrlCamerasFromDBM(String id, String imageUrl);

    Response deleteCamerasFromDBM(String id);

    Response deleteMultipleCameras(List<String> cameraIdsDTO);

    CameraResponseList getCamerasBySiteIdFromDBM(String siteId);

    List<String> getSiteIdListFromDBM(Set<String> cameraIdSet);

    List<String> getIdListFromDBM(Set<String> cameraIdSet, List<String> siteIdList);

    List<String> getCamneraIdListBySiteIdListStringFromDBM(String urlParam);
    
    Response getLiveImageCamera(String cameraId);
}
