package com.elcom.its.management.enums;

/**
 * @author hanh
 */
public enum ReportDeviceType {

    CAMERA_PTZ(0, "Camera quan sát"),
    CAMERA(1, "Camera dò xe"),
    VMS_BOARD(2, "Biển báo điện tử"),
    TEC(3, "Cadpro TEC"),
    SCREEN(4, "Màn hình tường"),
    UNKNOWN(5, "Unknown");

    public static ReportDeviceType valueOf(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int code;

    private String description;

    ReportDeviceType(int code, String description) {
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

    public static ReportDeviceType parse(int code) {
        ReportDeviceType[] validFlags = ReportDeviceType.values();
        for (ReportDeviceType validFlag : validFlags) {
            if (validFlag.code() == code) {
                return validFlag;
            }
        }
        return UNKNOWN;
    }
}
