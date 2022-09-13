package com.elcom.its.config.service;

import com.elcom.its.config.model.ListUuid;
import com.elcom.its.config.model.NewsLetterTemplate;
import com.elcom.its.config.model.Route;
import com.elcom.its.config.model.dto.Response;

public interface NewsLetterTemplateService {
    Response getAllNewsLetterTemplate();
    Response getAllNewsLetterTemplate(Integer page, Integer size, String search);
    Response getNewsLetterTemplateById(String id);
    Response saveNewsLetterTemplate(NewsLetterTemplate newsLetterTemplate);
    Response updateNewsLetterTemplate(NewsLetterTemplate newsLetterTemplate);
    Response deleteNewsLetterTemplate(String route);
    Response multiDelete(ListUuid uuid);
}
