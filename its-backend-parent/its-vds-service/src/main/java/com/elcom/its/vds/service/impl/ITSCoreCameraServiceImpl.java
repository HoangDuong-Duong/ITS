/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service.impl;

import com.elcom.its.vds.config.ApplicationConfig;
import com.elcom.its.vds.model.CameraDTO;
import com.elcom.its.vds.service.ITSCoreCameraService;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;
import com.elcom.its.vds.tools.Utils;
import com.elcom.its.vds.model.dto.CameraCreateUpdateDTO;
import com.elcom.its.vds.model.dto.CameraDTOMessage;
import com.elcom.its.vds.model.dto.CameraDetailDTOMessage;
import com.elcom.its.vds.model.dto.CameraPaginationDTO;
import com.elcom.its.vds.model.dto.CameraResponseList;
import com.elcom.its.vds.model.dto.CamerasResponse;
import com.elcom.its.vds.model.dto.CategoryResponseList;
import com.elcom.its.vds.model.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
public class ITSCoreCameraServiceImpl implements ITSCoreCameraService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ITSCoreCameraServiceImpl.class);
    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/camera";

    private final String CATE_URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/category";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<CameraDTO> getAll() {
        List<CameraDTO> cameraDTOList = null;
        String urlRequest = URI;

        CameraDTOMessage cameraDTOMessage = restTemplate.getForObject(urlRequest, CameraDTOMessage.class);

        if (cameraDTOMessage != null && cameraDTOMessage.getStatus() == HttpStatus.OK.value()) {
            cameraDTOList = cameraDTOMessage.getData();
        }
        return cameraDTOList;
    }

    public CameraPaginationDTO getAll(Integer page, Integer size, String search) {
        String params = null;
        try {
            params = Utils.getParams(page, size, search);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }

        String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/camera?" + params;
        LOGGER.info("urlRequest: {}", urlRequest);
        CameraPaginationDTO cameraPaginationDTO = restTemplate.getForObject(urlRequest, CameraPaginationDTO.class);
        LOGGER.info("find Camera by query from DBM: {}", cameraPaginationDTO);
        return cameraPaginationDTO;
    }

    @Override
    public CameraResponseList getCamerasListFromDBM(String urlParam) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CategoryResponseList getCameraTypeFromDBM() {
        String requestURI = CATE_URI + "/filter?catType=CAMERA";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);

        LOGGER.info("Request URI: " + requestURI);
        ResponseEntity<CategoryResponseList> result = restTemplate.exchange(requestURI, HttpMethod.GET, requestBody, CategoryResponseList.class);

        return result.getBody();
    }

    @Override
    public CameraDetailDTOMessage getCamerasByIdFromDBM(String id) {
        final String uri = URI + "/";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<CameraDetailDTOMessage> result = restTemplate.exchange(uri + id, HttpMethod.GET, requestBody, CameraDetailDTOMessage.class);

        LOGGER.info("Camera: " + result.getBody());

        return result.getBody();
    }

    @Override
    public CameraDetailDTOMessage createCamera(CameraCreateUpdateDTO cameraDTO) {
        LOGGER.info("Create camera: " + cameraDTO.getName());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CameraCreateUpdateDTO> requestBody = new HttpEntity<>(cameraDTO, headers);
        ResponseEntity<CameraDetailDTOMessage> result = restTemplate.exchange(URI, HttpMethod.POST, requestBody, CameraDetailDTOMessage.class);
        CameraDetailDTOMessage response = result.getBody();
        return response;
    }

    @Override
    public CameraDetailDTOMessage updateCamera(CameraCreateUpdateDTO cameraDTO, String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CameraCreateUpdateDTO> requestBody = new HttpEntity<>(cameraDTO, headers);
        ResponseEntity<CameraDetailDTOMessage> result
                = restTemplate.exchange(URI + "/" + id, HttpMethod.PUT, requestBody, CameraDetailDTOMessage.class);

        CameraDetailDTOMessage response = result.getBody();
        return response;
    }

    @Override
    public CameraResponseList getCameraBySiteIds(List<String> siteIds) {
        return null;
    }

    @Override
    public Response deleteCamerasFromDBM(String id) {
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + id, HttpMethod.DELETE, null, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteMultipleCameras(List<String> cameraIdsDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<String>> requestBody = new HttpEntity<>(cameraIdsDTO, headers);

        LOGGER.info("cameraids: " + requestBody);

        ResponseEntity<Response> result = restTemplate.exchange(URI + "/multi-delete", HttpMethod.DELETE, requestBody, Response.class);

        return result.getBody();
    }

    @Override
    public CamerasResponse updateImageUrlCamerasFromDBM(String id, String imageUrl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CameraResponseList getCamerasBySiteIdFromDBM(String siteId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getSiteIdListFromDBM(Set<String> cameraIdSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getIdListFromDBM(Set<String> cameraIdSet, List<String> siteIdList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> getCamneraIdListBySiteIdListStringFromDBM(String urlParam) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getLiveImageCamera(String cameraId) {
        String uri = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/vms/live-image?cameraId=" + cameraId;
        return restTemplate.getForObject(uri, Response.class);
    }

    @Override
    public Response updateCameraThumnail(String cameraId, String thumnail) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        CameraCreateUpdateDTO cameraDTO = new CameraCreateUpdateDTO();
        cameraDTO.setImageUrl(thumnail);
        HttpEntity<CameraCreateUpdateDTO> requestBody = new HttpEntity<>(cameraDTO, headers);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/camera-image/" + cameraId,
                HttpMethod.PUT, requestBody, Response.class);
        return result != null ? result.getBody() : null;
    }
}
