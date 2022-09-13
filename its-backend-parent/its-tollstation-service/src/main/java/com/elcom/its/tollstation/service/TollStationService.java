package com.elcom.its.tollstation.service;

import com.elcom.its.tollstation.dto.*;

import java.util.List;

public interface TollStationService {

    TollStationResponseDTO getTollStation(String stage, Boolean isAdmin);
    LanesResponseDTO reportLaneStatus(String tollStationId,String stage, Boolean isAdmin);
    Response reportLine(String tollStationId, String fromDate, String toDate, String levelFilterByTime,String stage, Boolean isAdmin);
    Response report(String tollStationId, String fromDate, String toDate,String stage, Boolean isAdmin);
    Response reportLane(String tollStationId, String fromDate, String toDate,String stage, Boolean isAdmin);
    LanesHistoryResponseDTO getHistoryCloseLane(String tollStationId, String fromDate, String toDate,String stage, Boolean isAdmin);

    List<TollStationDTO> getTollStationByDirectionCode(String directionCode, String stage, Boolean isAdmin);
    Response saveBot(BotDTO botDTO);
    Response updateBot(BotDTO botDTO, String id);
    Response deleteBot(String id);
}
