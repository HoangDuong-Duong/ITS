/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service;

import com.elcom.its.shift.model.Shift;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface ShiftService {

    public Shift save(Shift shift);

    public List<Shift> findAll(String month);

    public Shift findById(String id);

    public void delete(Shift shift);

    public void deleteAllShift(String month);

    public void save(List<Shift> listShift);
    
    Shift getShift(Date date);

}
