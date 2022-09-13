/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.service;

import com.elcom.its.management.dto.*;

import java.util.List;

/**
 *
 * @author Admin
 */
public interface ITSRecognitionService {
    RecognitionPlateResponseDTO findRecognition(List<String> stages, String fromDate, String toDate,
                                                String plate, String vehicleType, String filterObjectType, List<String> filterObjectIds,
                                                String brand, String color, Integer page, Integer size, Boolean isAdmin, Boolean distinctPlate);

    RecognitionPlateResponseDTO findHistory(List<String> stages, String fromDate, String toDate,
                                            String plate, String vehicleType, String filterObjectType, List<String> filterObjectIds,
                                            String brand, String color, Integer page, Integer size, Boolean isAdmin, Boolean extract);

    RecognitionStatisticResponseDTO findRecognitionStatistic(List<String> stages, String fromDate, String toDate,
                                                             String plate, String vehicleType, String filterObjectType, List<String> filterObjectIds,
                                                             String brand, String color, Integer page, Integer size, Boolean isAdmin);

    RecognitionPlateCorrectDTO correctData(String id, CorrectRecognitionInfoRequest correctRecognitionInfoRequest);

    Response deleteRecognitionFromDBM(String id);
    Response detailRecognition(String id);

    Response deleteMultiRecognition(List<String> recognitionIdList);

    Response getStageMultiEvent(List<String> recognitionIds);
}
