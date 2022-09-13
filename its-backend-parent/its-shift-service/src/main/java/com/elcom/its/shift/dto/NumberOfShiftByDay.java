/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Admin
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NumberOfShiftByDay implements Comparable<NumberOfShiftByDay> {

    @Override
    public int compareTo(NumberOfShiftByDay e) {
        return this.getDay().compareTo(e.getDay());
    }
    private Date day;
    private List<NumberOfShift> listNumberOfShifts;
}
