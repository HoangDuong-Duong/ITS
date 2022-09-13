package com.elcom.its.vmsboard.service;

import com.elcom.its.vmsboard.dto.Response;
import com.elcom.its.vmsboard.model.ContentLayout;
import com.elcom.its.vmsboard.model.Layout;
import com.elcom.its.vmsboard.model.LayoutComponentAttribute;

import java.util.List;

public interface VmsLayoutService {
    Response getAllLayout(String id);
    Response createLayout(Layout layout);
    Response deleteLayout(String id);

    Response getAllContent(Integer siteLayout,Integer layoutId);
    Response createContent(ContentLayout contentLayout);
    Response deleteContent(String id);

    Response getAttributeInLayout(String layout);
    Response updateAttributeInLayout(String layout, List<LayoutComponentAttribute> list);
}
