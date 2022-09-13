package com.elcom.its.config.service;

import com.elcom.its.config.model.DisplayScript;
import com.elcom.its.config.model.Route;
import com.elcom.its.config.model.dto.Response;

public interface DisplayScriptService {
    Response getDisplayScript();
    Response getDisplayScriptPlan();
    Response getScriptIsPlaying();
    Response getAllDisplayScript(Integer page, Integer size, String search);
    Response getDisplayScriptById(String id);
    Response saveDisplayScript(DisplayScript displayScript);
    Response updateDisplayScript(DisplayScript displayScript);
    Response deleteDisplayScript(String route);
}
