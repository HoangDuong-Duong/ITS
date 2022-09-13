package com.elcom.its.management.enums;

/**
 * @author hanh
 */
public enum DeviceStatus {

    SETUP(0, "Cài đặt"),
    STOP(1, "Hỏng"),
    RUNNING(2, "Đang sử dụng"),
    MAINTAIN(3, "Đang sửa chữa/bảo hành"),
    REPLACED(4,"Đã thay thế"),
    UNKNOWN(5, "Unknown");

    public static DeviceStatus valueOf(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int code;

    private String description;

    DeviceStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int code() {
        return code;
    }

    public String description() {
        return description;
    }

    public boolean isUnknown() {
        return code == UNKNOWN.code();
    }

    public static DeviceStatus parse(int code) {
        DeviceStatus[] validFlags = DeviceStatus.values();
        for (DeviceStatus validFlag : validFlags) {
            if (validFlag.code() == code) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }
}
