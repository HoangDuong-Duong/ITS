/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.cabinet.service;

import com.elcom.its.cabinet.dto.Response;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public interface ElectricCabinetService {

    public Response getListElectricCabinet(String urlParam, String listStageCodes, boolean adminBackend);

    public Response createElectricCabinet(Map<String, Object> bodyMap);

    public Response deleteElectricCabinet(List<String> electricCabinetIds);

    public Response updateElectricCabinet(Map<String, Object> bodyMap);

    Response processTurnOnFan(List<String> listCabinetIds);

    Response processTurnOffFan(List<String> listCabinetIds);

    Response processTurnOnFireAlarm(List<String> listCabinetIds);

    Response processTurnOffFireAlarm(List<String> listCabinetIds);

    Response processOpenDoor(List<String> listCabinetIds);

    Response processCloseDoor(List<String> listCabinetIds);

}
