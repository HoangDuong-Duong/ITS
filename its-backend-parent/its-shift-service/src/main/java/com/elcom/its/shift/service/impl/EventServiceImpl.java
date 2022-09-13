/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service.impl;

import com.elcom.its.shift.config.ApplicationConfig;
import com.elcom.its.shift.dto.AggEventByStatus;
import com.elcom.its.shift.dto.Response;
import com.elcom.its.shift.dto.Stage;
import com.elcom.its.shift.service.EventService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Admin
 */
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<AggEventByStatus> getAggEventByStatus(String stageCodes, Date startDate, Date endDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String urlParam = "endTime=" + dateFormat.format(endDate) + "&stageCodes=" + stageCodes + "&startTime=" + dateFormat.format(startDate);
            String urlRequest = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/report/event/event-by-status?" + urlParam;
            Response response = restTemplate.getForObject(urlRequest, Response.class);
            ObjectMapper mapper = new ObjectMapper();

            List<AggEventByStatus> listAggEventByStatus = mapper.convertValue(response.getData(), new TypeReference<List<AggEventByStatus>>() {
            });
            return listAggEventByStatus;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
