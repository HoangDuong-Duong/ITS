/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class WeeklyReportGroupByShift {

    int shiftNumber;
    List<UserOnShiftByStage> listUserOnShiftByStages;

    public int getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(int shiftNumber) {
        this.shiftNumber = shiftNumber;
    }

    public List<UserOnShiftByStage> getListUserOnShiftByStages() {
        return listUserOnShiftByStages;
    }

    public void setListUserOnShiftByStages(List<UserOnShiftByStage> listUserOnShiftByStages) {
        this.listUserOnShiftByStages = listUserOnShiftByStages;
    }

}
