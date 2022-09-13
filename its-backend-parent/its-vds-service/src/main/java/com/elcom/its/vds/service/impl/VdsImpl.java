/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service.impl;

import com.elcom.its.vds.config.ApplicationConfig;
import com.elcom.its.vds.model.Vds;
import com.elcom.its.vds.model.VdsSpec;
import com.elcom.its.vds.model.dto.Response;
import com.elcom.its.vds.model.dto.SiteVdsDTO;
import com.elcom.its.vds.repository.VdsRepository;
import com.elcom.its.vds.service.VdsService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
public class VdsImpl implements VdsService {

    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/traffic-flow/filter";
    private static final String URI_REPORT = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/traffic-flow/report-day";

    @Autowired
    private VdsRepository vdsRepository;

    @Override
    public Vds save(Vds vds) {
        return vdsRepository.save(vds);
    }

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean existsByLayoutTypeAndCameraIdAndProcessUnitId(Integer layoutType,
            String cameraId, String processUnitId) {
        return vdsRepository.existsByLayoutTypeAndCameraIdAndProcessUnitId(layoutType, cameraId, processUnitId);
    }

    @Override
    public Page<Vds> findVds(String siteId, String cameraId, String processUnitId,
            String search, Integer page, Integer size) {
        if (page != null && page > 0) {
            page--;
        } else {
            page = 0;
        }
        if (size == null) {
            size = 20;
        }
        String sort = "createdDate";
        Pageable paging = PageRequest.of(page, size, Sort.by(sort).descending());
        return vdsRepository.findAll(Specification.where(VdsSpec.findVds(siteId, cameraId,
                processUnitId, search)), paging);
    }

    @Override
    public Vds findById(String id) {
        return vdsRepository.findById(id).orElse(null);
    }

    @Override
    public List<String> findAllCameraIds() {
        List<String> cameraIds = vdsRepository.findAllCameraId();

        if (cameraIds != null) {
            return cameraIds;
        }
        return new ArrayList<>();
    }

    @Override
    public List<String> findAllSiteIds() {
        List<String> cameraIds = vdsRepository.findAllSiteId();

        if (cameraIds != null) {
            return cameraIds;
        }
        return new ArrayList<>();
    }

    @Override
    public void remove(Vds vds) {
        vdsRepository.delete(vds);
    }

    @Override
    public boolean updateStatus(String id, Integer status, String modifiedBy) {
        return vdsRepository.updateStatus(id, status, modifiedBy) > 0;
    }

    @Override
    public List<Vds> findByProcessUnitId(String idProcessUnit) {
        return vdsRepository.findByProcessUnitIdAndStatus(idProcessUnit, 1);
    }

    @Override
    public List<String> findProcessUnitIdByLayoutId(Long layoutId) {
        return vdsRepository.findAllProcessUnitIdByLayoutId(layoutId);
    }

    @Override
    public Response getTrafficFlowBySite(String urlParam) {
        final String uri = URI + "?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getTrafficFlowBySiteReport(String urlParam) {
        final String uri = URI_REPORT + "?" + urlParam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public List<Vds> getListVdsByCamera(String cameraId) {
        return vdsRepository.findByCameraIdOrderByCreatedDateCDesc(cameraId);
    }

    @Override
    public boolean updateCameraStatus(String cameraId, Integer status) {
        return vdsRepository.updateCameraStatus(status, cameraId) > 0;
    }

    @Override
    public void updateStatus(List<String> idList, Integer status, String modifiedBy) {
        vdsRepository.updateStatus(idList, status, modifiedBy);
    }

    @Override
    public List<String> findProcessUnitIdByVdsIdList(List<String> idList) {
        return vdsRepository.findAllProcessUnitIdByVdsIdList(idList);
    }

    @Override
    public List<SiteVdsDTO> getSiteList() {
        return vdsRepository.getSiteList();
    }

    @Override
    public boolean existsByLayoutTypeAndCameraId(Integer layoutType, String cameraId) {
        return vdsRepository.existsByLayoutTypeAndCameraId(layoutType, cameraId);
    }

    @Override
    public Page<Vds> findVds(List<String> siteIdList, String cameraId, String processUnitId,
            String search, Integer page, Integer size) {
        if (page != null && page > 0) {
            page--;
        } else {
            page = 0;
        }
        if (size == null) {
            size = 20;
        }
        String sort = "createdDate";
        Pageable paging = PageRequest.of(page, size, Sort.by(sort).descending());
        return vdsRepository.findAll(Specification.where(VdsSpec.findVds(siteIdList, cameraId,
                processUnitId, search)), paging);
    }

    @Override
    public void updateRenderStatus(List<String> idList, Integer status, String modifiedBy) {
        vdsRepository.updateRenderStatus(idList, status, modifiedBy);
    }

}
