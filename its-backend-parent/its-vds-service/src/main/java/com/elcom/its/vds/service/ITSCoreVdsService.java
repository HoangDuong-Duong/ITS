/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.vds.service;

import com.elcom.its.vds.model.Vds;
import com.elcom.its.vds.model.dto.EventResponseDTO;
import com.elcom.its.vds.model.dto.Response;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface ITSCoreVdsService {

    Response getListEventByVds(Vds vds, int page, int size, String filterTimeLevel);

    Response getAggEventTypeByVds(Vds vds, String filterTimeLevel);

    Response getTrafficflowDataByVds(Vds vds, String filterTimeLevel);

    EventResponseDTO findEventPage(String stages, String fromDate, String toDate,
            String filterObjectType, String filterObjectIds,
            String objectName, String eventCode, Integer eventStatus,
            String directionCode, String plate,Boolean reportStatus, Integer page, Integer size, Boolean isAdmin);

    String downloadZipImageAndVideoViolation(String plate, String startTime, List<String> listOfFileNames) throws IOException;
}
