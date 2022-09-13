package com.elcom.its.config.service;

import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.Site;
import com.elcom.its.config.model.dto.Province;
import com.elcom.its.config.model.dto.Response;
import com.elcom.its.config.model.dto.SiteInfoDTO;
import com.elcom.its.config.model.dto.SiteTrafficStatusDTO;

import java.util.List;

public interface SiteService {

    Response getAllSite(String siteList, Boolean isAdmin);
    List<SiteInfoDTO> getAllSiteUser(String siteList, Boolean isAdmin);

    Response getAllSite(String siteList, Integer page, Integer size, String search, Boolean isAdmin);

    Response getAllSiteHaveCam(String siteList, Boolean isAdmin);

    Response getSiteById(String siteList, String id, Boolean isAdmin);

    Response saveSite(String siteList, Site site, Boolean isAdmin);

    Response updateSite(String siteList, Site site, Boolean isAdmin);

    Response deleteSite(String siteList, String site, Boolean isAdmin);

    Response deleteMultiSite(String siteList, ListUuid site, Boolean isAdmin);

    Response getProvinces();

    Response getDistricts(String provinceId);

    Response getWards(String districtId);
    
    Response getPointByPositionM(String urlparam);

    Response filter(String siteList, Integer page, Integer size,String id, String name, Long provinceId, Long districtId, Long wardId, Float km, Boolean isAdmin);
    
    Response getListSiteInListStage(String stageCodes);

    SiteTrafficStatusDTO getSiteNearestPositionM(Long positionM);

}
