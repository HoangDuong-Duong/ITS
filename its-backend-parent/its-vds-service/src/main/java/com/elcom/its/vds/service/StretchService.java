package com.elcom.its.vds.service;

import com.elcom.its.vds.model.ListUuid;
import com.elcom.its.vds.model.Stretch;
import com.elcom.its.vds.model.dto.Response;

public interface StretchService {
    Response getAllStretch();

    Response getAllStretch(Integer page, Integer size, String search);

    Response getStretchById(String id);

    Response saveStretch(Stretch stretch);

    Response updateStretch(Stretch stretch);

    Response deleteStretch(String stretch);

    Response deleteMultiStage(ListUuid stretch);

    Response filterStretch(Integer page, Integer size, String id, String name, String siteStart, String siteEnd);
}
