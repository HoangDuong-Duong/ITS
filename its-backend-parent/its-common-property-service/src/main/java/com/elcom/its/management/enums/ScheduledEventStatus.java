/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.management.enums;

/**
 *
 * @author Admin
 */
public enum ScheduledEventStatus {
    WAIT(0, "Đang chờ"),
    UPCOMING(1,"Sắp diễn ra"),
    HAPPENED(2, "Đã xảy ra");

    private int code;
    private String description;

    public int code() {
        return code;
    }

    public String description() {
        return description;
    }

    ScheduledEventStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

}
