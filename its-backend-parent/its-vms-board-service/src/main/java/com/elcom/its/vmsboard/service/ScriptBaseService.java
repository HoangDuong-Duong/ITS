package com.elcom.its.vmsboard.service;

import com.elcom.its.vmsboard.dto.Response;
import com.elcom.its.vmsboard.model.ListUuid;
import com.elcom.its.vmsboard.model.ScriptBase;

public interface ScriptBaseService {
    Response getAllScriptBase(String startDate, String endDate, String name, Integer status, String vmsIds);

    Response activeScript(String id, Integer status);

    Response getDetailScriptBase(String id);

    Response save(ScriptBase scriptBase);

    Response update(ScriptBase scriptBase);

    Response delete(String sc);

    Response multiDelete(ListUuid ids);
}
