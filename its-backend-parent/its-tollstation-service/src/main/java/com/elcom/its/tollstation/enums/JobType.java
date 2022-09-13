/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.elcom.its.tollstation.enums;

/**
 *
 * @author Admin
 */
public enum JobType {

    VEHICLE_RESCUE("VEHICLE_RESCUE", "Cứu hộ phương tiện"),
    UPDATE_VMS_BOARD("UPDATE_VMS_BOARD", "Cập nhật biển báo VMS"),
    CONTACT_RELEVANT_AUTHORITIES("CONTACT_RELEVANT_AUTHORITIES", "Liên hệ cơ quan hữu quan"),
    CHECK_SCENE("CHECK_SCENE", "Kiểm tra hiện trường"),
    TROUBLESHOOT("TROUBLESHOOT", "Sửa chữa, khắc phục sự cố"),
    CLOSE_LANE("CLOSE_LANE", "Đóng làn đường"),
    FORBIDDEN_WAY("FORBIDDEN_WAY", "Cấm đường"),
    NOTIFY_INFORMATION("NOTIFY_INFORMATION", "Gửi thông điệp VOV"),
    SUPERVISE_SCENE("SUPERVISE_SCENE", "Giám sát hiện trường"),
    LIMIT_SPEED("LIMIT_SPEED", "Hạn chế tốc độ"),
    CLOSE_OPEN_ENTRANCE_EXIT("CLOSE_OPEN_ENTRANCE_EXIT", "Đóng lối ra/vào"),
    OTHER("OTHER", "Khác");

    private String code;
    private String description;

    public static JobType parse(String code) {
        JobType[] validFlags = JobType.values();
        for (JobType validFlag : validFlags) {
            if (validFlag.code().equals(code)) {
                return validFlag;
            }
        }
        return OTHER;
    }

    public String code() {
        return code;
    }

    public String description() {
        return description;
    }

    JobType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
