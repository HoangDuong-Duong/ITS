/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.report.enums;

/**
 *
 * @author Admin
 */
public enum ObjectTrackProcessStatus {
    NOPROCESS(0, "Chưa xử lý"),
    PROCESSING(1, "Đang xử lý"),
    PROCESSED(2, "Đã xác định"),
    UNKNOWN(3, "Không xác định");

    private final int code;
    private final String description;

    ObjectTrackProcessStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ObjectTrackProcessStatus of(int code) {
        ObjectTrackProcessStatus[] validFlags = ObjectTrackProcessStatus.values();
        for (ObjectTrackProcessStatus validFlag : validFlags) {
            if (validFlag.code() == code) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }

    public int code() {
        return code;
    }

    public String description() {
        return description;
    }

}
