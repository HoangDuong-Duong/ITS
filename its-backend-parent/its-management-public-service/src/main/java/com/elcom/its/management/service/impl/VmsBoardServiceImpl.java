/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service.impl;

import com.elcom.its.management.config.ApplicationConfig;
import com.elcom.its.management.dto.Response;
import com.elcom.its.management.dto.VmsBoardDisplayNewResponse;
import com.elcom.its.management.dto.VmsBoardDisplayNewRequest;
import com.elcom.its.management.service.VmsBoardService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
public class VmsBoardServiceImpl implements VmsBoardService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public VmsBoardDisplayNewResponse callDisPlayNew(String vmsId, String name, String newsletterId, Object content, Object source,
            String preview, Date date,String userId) {
        if (date == null) {
            date = getNextTenDay();
        }
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        VmsBoardDisplayNewRequest displayRequest = VmsBoardDisplayNewRequest.builder()
                .boardId(vmsId)
                .name(name)
                .newsLetterId(newsletterId)
                .content(content)
                .source(source)
                .preview(preview)
                .endTime(dateFormat.format(date))
                .date(date)
                .createdBy(userId)
                .build();
        HttpEntity<VmsBoardDisplayNewRequest> request = new HttpEntity<VmsBoardDisplayNewRequest>(displayRequest);
        try {
            String requestUrl = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/display-script/top-priority";
            Response response = restTemplate.postForObject(requestUrl, request, Response.class);
            ObjectMapper mapper = new ObjectMapper();
            VmsBoardDisplayNewResponse displayResponse = mapper.convertValue(response.getData(), new TypeReference<VmsBoardDisplayNewResponse>() {
            });
            return displayResponse;
        } catch (Exception e) {
            return VmsBoardDisplayNewResponse.builder()
                    .success(false)
                    .parentId(null)
                    .build();
        }
    }

    private Date getNextTenDay() {
        Date date = new Date();
        long MILLIS_IN_10_DAY = 1000 * 60 * 60 * 24 * 10;
        return new Date(date.getTime() + MILLIS_IN_10_DAY);

    }

}
