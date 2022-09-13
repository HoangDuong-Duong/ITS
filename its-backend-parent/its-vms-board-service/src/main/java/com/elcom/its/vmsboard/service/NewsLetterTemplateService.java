package com.elcom.its.vmsboard.service;

import com.elcom.its.vmsboard.dto.Response;
import com.elcom.its.vmsboard.model.ListUuid;
import com.elcom.its.vmsboard.model.NewsLetterTemplate;

public interface NewsLetterTemplateService {
    Response getAllNewsLetterTemplate();
    Response getAllNewsLetterTemplate(Integer page, Integer size, String name, String typeBoard, String typeEvent, String search);
    Response getNewsLetterTemplateById(String id);
    Response saveNewsLetterTemplate(NewsLetterTemplate newsLetterTemplate);
    Response updateNewsLetterTemplate(NewsLetterTemplate newsLetterTemplate);
    Response deleteNewsLetterTemplate(String route);
    Response multiDelete(ListUuid uuid);
}
