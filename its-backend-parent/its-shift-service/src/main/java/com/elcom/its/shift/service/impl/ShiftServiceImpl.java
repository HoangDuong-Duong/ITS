/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.service.impl;

import com.elcom.its.shift.model.Shift;
import com.elcom.its.shift.repository.ShiftRepository;
import com.elcom.its.shift.service.ShiftService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Admin
 */
@Service
@Transactional
public class ShiftServiceImpl implements ShiftService {
    
    @Autowired
    private ShiftRepository shiftRepository;
    
    @Override
    public Shift save(Shift shift) {
        return shiftRepository.save(shift);
    }
    
    @Override
    public List<Shift> findAll(String month) {
        return shiftRepository.findByMonthAndNumberGreaterThanOrderByNumberAsc(month, 0);
    }
    
    @Override
    public Shift findById(String id) {
        Optional<Shift> shiftOptional = shiftRepository.findById(id);
        if (shiftOptional.isPresent()) {
            return shiftOptional.get();
        } else {
            return null;
        }
    }
    
    @Override
    public void delete(Shift shift) {
        shiftRepository.delete(shift);
    }
    
    @Override
    public void deleteAllShift(String month) {
        shiftRepository.deleteByMonthAndNumberGreaterThan(month, 0);
    }
    
    @Override
    public void save(List<Shift> listShift) {
        shiftRepository.saveAll(listShift);
    }
    
    @Override
    public Shift getShift(Date date) {
        int hour = date.getHours();
        int minute = date.getMinutes();
        float floatTime = hour + ((float) minute) / 60.0f;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
        return shiftRepository.findShift(floatTime, dateFormat.format(date));
    }
    
}
