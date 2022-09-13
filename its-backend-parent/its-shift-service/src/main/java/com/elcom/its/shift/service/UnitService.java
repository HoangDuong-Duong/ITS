/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service;

import com.elcom.its.shift.dto.Unit;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;

/**
 *
 * @author Admin
 */
public interface UnitService {

    public Unit findById(String id) throws JsonProcessingException;
}
