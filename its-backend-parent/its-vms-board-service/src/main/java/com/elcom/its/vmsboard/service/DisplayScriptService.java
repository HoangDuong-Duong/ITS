package com.elcom.its.vmsboard.service;

import com.elcom.its.vmsboard.dto.Response;
import com.elcom.its.vmsboard.model.DisplayDetailDTO;
import com.elcom.its.vmsboard.model.DisplayScript;
import com.elcom.its.vmsboard.model.ScriptMaxPriority;

public interface DisplayScriptService {
    Response getDisplayScript();
    Response getDisplayScriptPlan(String scriptBaseId);
    Response getScriptIsPlaying();
    Response getAllDisplayScript(String baseId);
    Response getDisplayScriptByParentId(String id);
    Response getDisplayScriptDefault(String id);
    Response getDisplayScriptByBoardId(String boardId);
    Response saveDisplayScript(DisplayScript displayScript);
    Response updateDisplayScript(DisplayScript displayScript, String pathParam);
    Response deleteDisplayScript(String route);
    Response getTimeByBoardId(String boardId);
    Response getTimeByBaseAndBoardId(String baseId, String boardId);
    Response getDetailScript(String id);
    Response updateDetailScript(DisplayDetailDTO displayDetailDTO, String id);
    Response deleteDetailScript(String id);
    Response getScriptRunningInBoard(String boardId);
    Response getNewsLetterRunningInBoard(String boardId);
    Response getNewsLetterByEventType(String eventType);
    Response saveScriptMaxPriority(ScriptMaxPriority scriptMaxPriority);
    Response saveScriptTopPriority(ScriptMaxPriority scriptMaxPriority);
}
