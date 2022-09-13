/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.shift.dto;

import com.elcom.its.shift.model.UserShift;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 *
 * @author Admin
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserShiftGroupByUser {

    String userId;
    String username;
    List<UserShift> listUserShift;
    List<NumberOfShift> listNumberOfShiftByUsers;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<UserShift> getListUserShift() {
        return listUserShift;
    }

    public void setListUserShift(List<UserShift> listUserShift) {
        this.listUserShift = listUserShift;
    }

    public List<NumberOfShift> getListNumberOfShiftByUsers() {
        return listNumberOfShiftByUsers;
    }

    public void setListNumberOfShiftByUsers(List<NumberOfShift> listNumberOfShiftByUsers) {
        this.listNumberOfShiftByUsers = listNumberOfShiftByUsers;
    }

    @JsonIgnore
    public String getStageCode() {
        if (this.listUserShift == null || this.listUserShift.isEmpty()) {
            return null;
        }
        return this.listUserShift.get(0).getStageCode();
    }

}
