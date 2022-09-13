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
public enum JobStatus {
    WAIT(0, "Chờ lên lịch"),
    RECEIVE_PROCESSING(1, "Nhận xử lý"),
    PROCESSING(2, "Đang xử lý"),
    CHECKING(3, "Kiểm tra"),
    UPDATED_BOARD(4, "Đã cập nhật bảng tin"),
    COMPLETE(5, "Hoàn thành"),
    UNKNOWN(6, "Không xác định");

    private int code;
    private String description;

    public int code() {
        return code;
    }
    public String description() {
        return description;
    }

    JobStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    
}
