package com.elcom.its.config.service.impl;

import com.elcom.its.config.config.ApplicationConfig;
import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.Site;
import com.elcom.its.config.model.dto.*;
import com.elcom.its.config.service.DistanceService;
import com.elcom.its.config.service.SiteService;
import com.elcom.its.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistanceService.class);
    private static final String URI = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/site";
    private static final String URI_PROVINCE = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/";
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getAllSite(String siteList, Boolean isAdmin) {
        String uri = URI;
        String params = "page=" + 0 + "&size=" + 1000 + "&stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
        uri += "?" + params;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public List<SiteInfoDTO> getAllSiteUser(String siteList, Boolean isAdmin) {
        List<SiteInfoDTO> sitesList = null;
        String urlRequest = URI + "?page=" + 0 + "&size=" + 1000 + "&stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
        SiteResponseList dto = restTemplate.getForObject(urlRequest, SiteResponseList.class);
        LOGGER.info("Get camera list from ITS Core: {}", dto);
        if (dto != null && dto.getStatus() == HttpStatus.OK.value()) {
            sitesList = dto.getData();
        }
        return sitesList;
    }

    @Override
    public Response getAllSite(String siteList, Integer page, Integer size, String search, Boolean isAdmin) {
        String params = null;
        try {
            if (!StringUtil.isNullOrEmpty(search)) {
                params = "page=" + page + "&size=" + size + "&search=" + URLEncoder.encode(search, "UTF-8") + "&sort=id" + "&stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
            } else {
                params = "page=" + page + "&size=" + size + "&stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
            }

        } catch (UnsupportedEncodingException ex) {
            LOGGER.error(ex.toString());
        }
        String urlRequest = ApplicationConfig.ITS_ROOT_URL + "/v1.0/its/management/site?" + params;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response response = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find Category by catType from DBM: {}", response);
        return response;
    }

    @Override
    public Response getAllSiteHaveCam(String siteList, Boolean isAdmin) {
        String uri = URI + "/haveCamera?stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
        Response response = restTemplate.getForObject(uri, Response.class);
        LOGGER.info("find Category by catType from DBM: {}", response);
        return response;
    }

    @Override
    public Response getSiteById(String siteList, String id, Boolean isAdmin) {
        String uri = URI + "/" + id + "?stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response saveSite(String siteList, Site site, Boolean isAdmin) {
        String uri = URI + "?stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
        HttpEntity<Site> requestBody = new HttpEntity<>(site, null);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateSite(String siteList, Site site, Boolean isAdmin) {
        String uri = URI + "/" + site.getId() + "?stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
        HttpEntity<Site> requestBody = new HttpEntity<>(site, null);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteSite(String siteList, String site, Boolean isAdmin) {
        String uri = URI + "/" + site + "?stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.DELETE, null, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteMultiSite(String siteList, ListUuid site, Boolean isAdmin) {
        String uri = URI + "/multi-delete" + "?stageIdLs=" + siteList + "&isAdmin=" + isAdmin;
        HttpEntity<ListUuid> requestBody = new HttpEntity<>(site, null);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.DELETE, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getProvinces() {
        final String uri = URI_PROVINCE + "/province";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getDistricts(String provinceId) {
        final String uri = URI_PROVINCE + "/district?search=" + provinceId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getWards(String districtId) {
        final String uri = URI_PROVINCE + "/ward?search=" + districtId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response filter(String siteList, Integer page, Integer size, String id, String name, Long provinceId, Long districtId, Long wardId, Float km, Boolean isAdmin) {
        String param = "page=" + page + "&size=" + size;
        if (name != null) {
            param += "&name=" + name;
        }
        if (id != null) {
            param += "&id=" + id;
        }
        if (name != null) {
            param += "&name=" + name;
        }
        if (provinceId != null) {
            param += "&provinceId=" + provinceId;
        }
        if (districtId != null) {
            param += "&districtId=" + districtId;
        }
        if (wardId != null) {
            param += "&wardId=" + wardId;
        }
        if (km != null) {
            param += "&km=" + km;
        }

        String urlRequest = URI + "/filter?" + "?stageIdLs=" + siteList + "&isAdmin=" + isAdmin + param;
        LOGGER.info("urlRequest : {}", urlRequest);
        Response stretchPaginationDTO = restTemplate.getForObject(urlRequest, Response.class);
        LOGGER.info("find Category by catType from DBM: {}", stretchPaginationDTO);
        return stretchPaginationDTO;
    }

    @Override
    public Response getPointByPositionM(String urlparam) {
        final String uri = URI + "/point?" + urlparam;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getListSiteInListStage(String stageCodes) {
        String uri = URI + "/site-in-stage?stageCodes=" + stageCodes;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public SiteTrafficStatusDTO getSiteNearestPositionM(Long positionM) {
        return null;
    }
}
