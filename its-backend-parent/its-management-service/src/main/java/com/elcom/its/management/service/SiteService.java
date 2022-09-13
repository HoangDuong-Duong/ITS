/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service;

import com.elcom.its.management.dto.Point;
import com.elcom.its.management.dto.Response;
import com.elcom.its.management.dto.SiteDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 *
 * @author Admin
 */
public interface SiteService {

    SiteDTO findById(String siteId);

    List<SiteDTO> findByListId(List<String> siteIds);

    List<SiteDTO> findSiteByListSiteIds(List<String> siteIds);

    Point getPointByKmAndM(String urlParam);

    SiteDTO getSiteByPositionM(String urlParam);

    SiteDTO checkSiteExist(SiteDTO siteDTO);

}
