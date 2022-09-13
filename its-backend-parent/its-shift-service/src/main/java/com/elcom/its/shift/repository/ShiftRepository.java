/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.repository;

import com.elcom.its.shift.model.Shift;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository
public interface ShiftRepository extends CrudRepository<Shift, String> {

    List<Shift> findByMonthOrNumber(String month, int number);

    List<Shift> findByMonthAndNumberGreaterThanOrderByNumberAsc(String month, int number);

    long deleteByMonthAndNumberGreaterThan(String month, int number);

    Shift findFirstByStartTimeFloatLessThanEqualAndEndTimeFloatGreaterThanEqualOrderByStartTimeFloatDesc(float startTimeFloat, float endTimeFloat);

    @Query("SELECT s FROM Shift s"
            + " WHERE s.month = :month and (s.startTimeFloat < s.endTimeFloat and s.startTimeFloat<=:timeFloat and s.endTimeFloat>=:timeFloat)"
            + "or (s.startTimeFloat > s.endTimeFloat and ((s.startTimeFloat<=:timeFloat and :timeFloat<24)or(0<:timeFloat and :timeFloat<= s.endTimeFloat)))")
    Shift findShift(float timeFloat,String month);
}
