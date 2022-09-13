package com.elcom.its.config.service;

import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.Stretch;
import com.elcom.its.config.model.dto.Response;

import java.util.List;

public interface StretchService {
    Response getAllStretch(String siteList, Boolean isAdmin);

    Response getAllStretch(String siteList, Integer page, Integer size, String search, Boolean isAdmin);

    Response getStretchById(String siteList, String id, Boolean isAdmin);

    Response saveStretch(Stretch stretch);

    Response updateStretch(Stretch stretch);

    Response deleteStretch(String siteList, String stretch, Boolean isAdmin);

    Response deleteMultiStage(String siteList, ListUuid stretch, Boolean isAdmin);

    Response filterStretch(String siteList, Integer page, Integer size, String id, String name, String siteStart, String siteEnd, Boolean isAdmin);

    public String findBySite(String siteId);

    Response getStretchByListCodes(String listCode);
}
